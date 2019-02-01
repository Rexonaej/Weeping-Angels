package me.sub.angels.common.events;

public class EventHandler {

//    @SubscribeEvent
//    public static void angelTeleported(EventAngelTeleport e) {
//        EntityPlayer player = e.getEntityPlayer();
//        if (player.world.rand.nextInt(2) == 2) {
//            player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 600, 3));
//            player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 600, 1));
//            player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 600, 3));
//            player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 600, 3));
//        }
//    }

    /**
     * Update checker thing, tells the player that the mods out of date if they're on a old build
     */
//    @SubscribeEvent
//    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent e) {
//        EntityPlayer player = e.player;
//        if (!player.world.isRemote) {
//            ForgeVersion.CheckResult version = ForgeVersion.getResult(Loader.instance().activeModContainer());
//            if (version.status == ForgeVersion.Status.OUTDATED) {
//                TextComponentString url = new TextComponentString(TextFormatting.AQUA + TextFormatting.BOLD.toString() + "UPDATE");
//                url.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://minecraft.curseforge.com/projects/weeping-angels-mod"));
//                url.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Open URL")));
//
//                player.sendMessage(new TextComponentString(TextFormatting.GOLD + "[Weeping Angels] : ").appendSibling(url));
//                String changes = String.valueOf(version.changes).replace("{" + version.target + "=", "").replace("}", "");
//                player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Changes: " + TextFormatting.BLUE + changes));
//            }
//        }
//    }

    /*
     * Spawning arms in snow biomes
     */
    //TODO fabric
//    @SubscribeEvent
//    public static void decorateBiomeEvent(DecorateBiomeEvent e) {
//        if (e.getWorld().getBiome(e.getPos()).isSnowyBiome()) {
//            if (e.getRand().nextInt(30) <= 10) {
//                generate(e.getWorld(), e.getPos());
//            }
//        }
//    }
//
//    private static void generate(World world, BlockPos position) {
//        BlockPos pos = new BlockPos(position.add(new BlockPos(8, 0, 8)));
//        if ((!world.provider.isNether() || pos.getY() < 255) && world.getBiome(position).isSnowyBiome()) {
//            if (world.getBlockState(pos).getBlock() == Blocks.SNOW || world.getBlockState(pos).getBlock() == Blocks.SNOW_LAYER)
//                world.setBlockState(pos, WAObjects.Blocks.ARM.getDefaultState(), 1);
//        }
//    }

    //TODO move to entity
//    @SubscribeEvent
//    public static void cancelDamage(LivingAttackEvent e) {
//        Entity source = e.getSource().getTrueSource();
//        if (source instanceof EntityLivingBase) {
//            EntityLivingBase attacker = (EntityLivingBase) source;
//            EntityLivingBase victim = e.getEntityLiving();
//
//            if (victim instanceof EntityWeepingAngel) {
//                ItemStack item = attacker.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
//                boolean isPic = item.getItem() instanceof ItemPickaxe;
//                e.setCanceled(!isPic);
//
//                if (!isPic) {
//                    attacker.attackEntityFrom(WAObjects.STONE, 2.5F);
//                } else {
//                    ItemPickaxe pick = (ItemPickaxe) item.getItem();
//
//                    if (pick != Items.DIAMOND_PICKAXE && victim.world.getDifficulty() == EnumDifficulty.HARD) {
//                        e.setCanceled(true);
//                    }
//
//                    victim.playSound(SoundEvents.BLOCK_STONE_BREAK, 1.0F, 1.0F);
//                    pick.setDamage(item, pick.getDamage(item) - 1);
//                }
//
//                if (!(source instanceof Entity)) {
//                    e.setCanceled(true);
//                }
//            }
//        }
//    }

    //TODO with config
//    @SubscribeEvent
//    public static void onSpawn(LivingSpawnEvent.CheckSpawn e) {
//        if(e.getEntity() instanceof EntityWeepingAngel){
//            e.setResult(Event.Result.DENY);
//            for (int i : WAConfig.spawn.dimensionWhitelist)
//            {
//                if(i == e.getWorld().provider.getDimension()){
//                    e.setResult(Event.Result.DEFAULT);
//                }
//            }
//        }
//    }
	
}
