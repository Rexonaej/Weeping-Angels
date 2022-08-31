package mc.craig.software.angels.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import mc.craig.software.angels.common.CatacombTracker;
import mc.craig.software.angels.common.WAEntities;
import mc.craig.software.angels.common.WASounds;
import mc.craig.software.angels.common.entity.angel.AngelTextureVariant;
import mc.craig.software.angels.common.entity.angel.WeepingAngel;
import mc.craig.software.angels.donators.DonationChecker;
import mc.craig.software.angels.donators.Donator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class WAHelper {

    public static Predicate<? super Entity> ANOMALY_ENTITIES = input -> input.getType().is(WATags.ANOMALYS);

    public static List<Entity> getAnomaliesAroundEntity(Entity entity, int radius) {
        return entity.level.getEntities((Entity) null, entity.getBoundingBox().inflate(radius, radius, radius), ANOMALY_ENTITIES);
    }

    @ExpectPlatform
    public static Packet<?> spawnPacket(LivingEntity livingEntity) {
        throw new RuntimeException("This isn't where you get the packet! tut tut!");
    }

    public static void onPlayerTick(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            if (!player.level.isClientSide) {
                boolean isInCatacomb = CatacombTracker.isInCatacomb(player);
                RandomSource randomSource = serverPlayer.level.getRandom();

                if (player.tickCount % 40 == 0) {
                    CatacombTracker.tellClient(serverPlayer, isInCatacomb);
                }

                if (isInCatacomb && serverPlayer.tickCount % 200 == 0) {
                    serverPlayer.connection.send(new ClientboundSoundPacket(WAHelper.getRandomSounds(randomSource), SoundSource.AMBIENT, player.getX() + randomSource.nextInt(18), player.getY() + randomSource.nextInt(18), player.getZ() + randomSource.nextInt(18), 0.25F, 1F, serverPlayer.level.random.nextLong()));
                }
            }

            // Update Donators
            if (player.level.isClientSide()) {
                for (Donator donator : DonationChecker.getModDonators()) {
                    if (player.getStringUUID().equals(donator.getUuid())) {
                        donator.tick(player);
                    }
                }
            }
        }
    }

    public static boolean spawnWeepingAngel(ServerLevel serverLevel, BlockPos blockPos, AngelTextureVariant angelTextureVariant, boolean dropsLoot, float rotation) {
        WeepingAngel weepingAngel = WAEntities.WEEPING_ANGEL.get().create(serverLevel);
        weepingAngel.setVariant(angelTextureVariant);
        weepingAngel.setDrops(dropsLoot);
        weepingAngel.absMoveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, rotation, 0);
        return serverLevel.addFreshEntity(weepingAngel);
    }

    public static Vec3 fogColor() {
        return new Vec3(0.14F, 0.15F, 0.22F);
    }


    public static boolean intersects(AABB bb, Vec3 min, Vec3 max) {
        return bb.intersects(Math.min(min.x, max.x), Math.min(min.y, max.y), Math.min(min.z, max.z), Math.max(min.x, max.x), Math.max(min.y, max.y), Math.max(min.z, max.z));
    }

    public static Structure getConfigured(ServerLevel level, ResourceLocation resourceLocation) {
        Registry<Structure> registry = level.registryAccess().registryOrThrow(Registry.STRUCTURE_REGISTRY);
        return registry.get(resourceLocation);
    }

    public static SoundEvent getRandomSounds(RandomSource randomSource) {
        SoundEvent[] soundEvents = new SoundEvent[]{SoundEvents.AMBIENT_CAVE, SoundEvents.MUSIC_DISC_11, SoundEvents.SCULK_SHRIEKER_SHRIEK};
        return soundEvents[randomSource.nextInt(soundEvents.length)];
    }
}
