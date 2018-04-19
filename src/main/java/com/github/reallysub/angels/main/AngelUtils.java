package com.github.reallysub.angels.main;

import java.util.List;

import com.github.reallysub.angels.common.WAObjects;
import com.github.reallysub.angels.common.WAObjects.WAItems;
import com.github.reallysub.angels.common.entities.EntityAngel;
import com.github.reallysub.angels.events.EventAngelTeleport;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;

public class AngelUtils {
	
	public static void getForTorch(EntityPlayer p, EnumHand hand) {
		ItemStack torch = p.getHeldItem(hand);
		if (torch.getItem() == Item.getItemFromBlock(Blocks.TORCH) || torch.getItem() == Item.getItemFromBlock(Blocks.REDSTONE_TORCH)) {
			String itemName = torch.getDisplayName();
			ItemStack newStack = new ItemStack(WAItems.unLitTorch);
			newStack.setStackDisplayName(itemName);
			torch.setCount(0);
			if (hand == EnumHand.MAIN_HAND) {
				p.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, newStack);
			} else {
				p.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, newStack);
			}
		}
	}
	
	public static boolean teleportDimEntity(Entity entity, BlockPos pos, int targetDim) {
		if (entity.getEntityWorld().isRemote || entity.isRiding() || entity.isBeingRidden() || !entity.isEntityAlive()) {
			return false;
		}
		
		EntityPlayerMP player = null;
		
		if (entity instanceof EntityPlayerMP) {
			player = (EntityPlayerMP) entity;
			
			if (player.world.rand.nextInt(2) == 2) {
				player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 600, 3));
				player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 600, 1));
				player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 600, 3));
				player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 600, 3));
			}
			
			MinecraftForge.EVENT_BUS.post(new EventAngelTeleport(player));
		}
		
		int from = entity.dimension;
		if (from != targetDim) {
			MinecraftServer server = player == null ? entity.getServer() : player.mcServer;
			WorldServer fromDim = server.getWorld(from);
			WorldServer toDim = server.getWorld(targetDim);
			Teleporter teleporter = new WAMTeleporter(toDim);
			
			if (player != null) {
				server.getPlayerList().transferPlayerToDimension(player, targetDim, teleporter);
				if (from == 1 && entity.isEntityAlive()) {
					toDim.spawnEntity(entity);
					toDim.updateEntityWithOptionalForce(entity, false);
				}
			} else {
				NBTTagCompound tagCompound = entity.serializeNBT();
				float rotationYaw = entity.rotationYaw;
				float rotationPitch = entity.rotationPitch;
				fromDim.removeEntity(entity);
				Entity newEntity = EntityList.createEntityFromNBT(tagCompound, toDim);
				
				if (newEntity != null) {
					newEntity.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), rotationYaw, rotationPitch);
					newEntity.forceSpawn = true;
					toDim.spawnEntity(newEntity);
					newEntity.forceSpawn = false;
				} else {
					return false;
				}
			}
		}
		
		if (!entity.world.isBlockLoaded(pos)) {
			entity.world.getChunkFromBlockCoords(pos);
		}
		
		if (player != null && player.connection != null) {
			player.connection.setPlayerLocation(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D, player.rotationYaw, player.rotationPitch);
			player.addExperienceLevel(0);
		} else {
			entity.setPositionAndUpdate(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D);
		}
		
		entity.fallDistance = 0;
		
		return true;
	}
	
	public static void getAllAngels(EntityPlayer player, int distance, double radius) {
		if (distance < 0 || distance > 256) {
			distance = 256;
		}
		Vec3d vec3 = player.getLookVec();
		double targetX = player.posX;
		double targetY = player.posY + player.getEyeHeight() - 0.10000000149011612D;
		double targetZ = player.posZ;
		double distanceTraveled = 0;
		
		while ((int) distanceTraveled < distance) {
			targetX += vec3.x;
			targetY += vec3.y;
			targetZ += vec3.z;
			distanceTraveled += vec3.lengthVector();
			AxisAlignedBB bb = new AxisAlignedBB(targetX - radius, targetY - radius, targetZ - radius, targetX + radius, targetY + radius, targetZ + radius);
			
			for (EntityAngel angel : player.world.getEntitiesWithinAABB(EntityAngel.class, bb)) {
				if (angel.canBeCollidedWith() && isTargetInSight(player, angel) && !player.isPotionActive(MobEffects.BLINDNESS) && !player.isSpectator()) {
					angel.setSeen(true);
					if (angel.getSeenTime() != 1) {
						// angel.setSeenTime(15);
					}
					if (angel.getAttackTarget() == player && angel.getSeenTime() == 1) {
						SoundEvent sound = angel.getSeenSound();
						if (sound != null) {
							angel.playSound(sound, 1.0F, 1.0F);
						}
					}
				}
			}
		}
	}
	
	private static boolean isTargetInSight(EntityLivingBase player, Entity angel) {
		return player.canEntityBeSeen(angel) && isInEyeLine(player, angel, 60);
	}
	
	private static boolean isInEyeLine(Entity player, Entity angel, float fov) {
		double dx = angel.posX - player.posX;
		double dz;
		for (dz = angel.posZ - player.posZ; dx * dx + dz * dz < 1.0E-4D; dz = (Math.random() - Math.random()) * 0.01D) {
			dx = (Math.random() - Math.random()) * 0.01D;
		}
		while (player.rotationYaw > 360) {
			player.rotationYaw -= 360;
		}
		while (player.rotationYaw < -360) {
			player.rotationYaw += 360;
		}
		float yaw = (float) (Math.atan2(dz, dx) * 180.0D / Math.PI) - player.rotationYaw;
		yaw = yaw - 90;
		while (yaw < -180) {
			yaw += 360;
		}
		while (yaw >= 180) {
			yaw -= 360;
		}
		return yaw < fov && yaw > -fov;
	}
	
	public static void getAllAngels(EntityAngel angel) {
		for (EntityAngel angel2 : angel.world.getEntitiesWithinAABB(EntityAngel.class, angel.getEntityBoundingBox().grow(20, 20, 20))) {
			if (angel.canEntityBeSeen(angel2) && angel != angel2 && isTargetInSight(angel, angel2)) {
				angel2.setSeen(true);
			}
		}
	}
	
	// Teleporter
	public static class WAMTeleporter extends Teleporter {
		
		public WAMTeleporter(WorldServer worldIn) {
			super(worldIn);
		}
		
		@Override
		public void placeInPortal(Entity entity, float rotationYaw) {}
		
		@Override
		public boolean placeInExistingPortal(Entity entity, float float_for_something) {
			return true;
		}
		
		@Override
		public boolean makePortal(Entity entity) {
			return true;
		}
		
		@Override
		public void removeStalePortalLocations(long stale_long) {
			
		}
	}
	
}
