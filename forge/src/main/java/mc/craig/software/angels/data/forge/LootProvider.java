package mc.craig.software.angels.data.forge;

import mc.craig.software.angels.WeepingAngels;
import mc.craig.software.angels.common.blocks.WABlocks;
import mc.craig.software.angels.common.items.WAItems;
import mc.craig.software.angels.registry.RegistrySupplier;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class extends the `LootTableProvider` class from the Minecraft game engine and is responsible for
 * generating loot tables for various blocks and items in the game.
 */
public class LootProvider extends LootTableProvider {

    /**
     * Constructs a new `LootProvider` instance with the specified output directory, set of resource locations,
     * and list of sub-providers.
     *
     * @param arg the `PackOutput` object representing the output directory for the generated loot tables
     * @param set the set of resource locations representing the names of all the loot tables generated by this provider
     * @param list the list of `SubProviderEntry` objects representing the loot table sub-providers used by this provider
     */
    public LootProvider(PackOutput arg, Set<ResourceLocation> set, List<SubProviderEntry> list) {
        super(arg, set, list);
    }

    /**
     * Validates the specified map of resource locations and loot tables. This method is called by the Minecraft
     * game engine to validate all loot tables before they are used in the game.
     *
     * @param map the map of resource locations and loot tables to validate
     * @param validationContext the validation context to use for validation
     */
    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationContext) {
        map.forEach((arg, arg2) -> {
            arg2.validate(validationContext.setParams(arg2.getParamSet()).enterElement("{" + arg + "}", new LootDataId(LootDataType.TABLE, arg)));
        });
    }

    /**
     * This nested class extends the `BlockLootSubProvider` class from the Minecraft game engine and is
     * responsible for generating loot tables for blocks in the game.
     */
    public static class ModBlockLoot extends BlockLootSubProvider {
        public ModBlockLoot() {
            super(Stream.of(Blocks.DRAGON_EGG, Blocks.BEACON, Blocks.CONDUIT, Blocks.SKELETON_SKULL, Blocks.WITHER_SKELETON_SKULL, Blocks.PLAYER_HEAD, Blocks.ZOMBIE_HEAD, Blocks.CREEPER_HEAD, Blocks.DRAGON_HEAD, Blocks.PIGLIN_HEAD, Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX).map(ItemLike::asItem).collect(Collectors.toSet()), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected void generate() {
            this.add(WABlocks.KONTRON_ORE.get(), (block) -> createOreDrop(block, WAItems.KONTRON_INGOT.get()));
            this.add(WABlocks.KONTRON_ORE_DEEPSLATE.get(), (block) -> createOreDrop(block, WAItems.KONTRON_INGOT.get()));
            dropSelf(WABlocks.CHRONODYNE_GENERATOR.get());
            dropSelf(WABlocks.COFFIN.get());
            dropSelf(WABlocks.STATUE.get());
            dropSelf(WABlocks.SNOW_ANGEL.get());
            dropSelf(WABlocks.PLINTH.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            ArrayList<Block> blocks = new ArrayList<>();
            for (RegistrySupplier<Block> entry : WABlocks.BLOCKS.getEntries()) {
                blocks.add(entry.get());
            }
            return blocks;
        }
    }

    public static class ModChestLoot implements LootTableSubProvider {

        @Override
        public void generate(BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {
            biConsumer.accept(WeepingAngels.CRYPT_LOOT, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(20)).add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE)).add(LootItem.lootTableItem(Items.NAME_TAG).setWeight(30)).add(LootItem.lootTableItem(Items.BOOK).setWeight(10).apply(EnchantRandomlyFunction.randomApplicableEnchantment())).add(LootItem.lootTableItem(Items.IRON_PICKAXE).setWeight(5)).add(EmptyLootItem.emptyItem().setWeight(5))).withPool(LootPool.lootPool().setRolls(UniformGenerator.between(2.0F, 4.0F)).add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F)))).add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))).add(LootItem.lootTableItem(Items.REDSTONE).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F)))).add(LootItem.lootTableItem(Items.LAPIS_LAZULI).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F)))).add(LootItem.lootTableItem(Items.DIAMOND).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))).add(LootItem.lootTableItem(Items.COAL).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 8.0F)))).add(LootItem.lootTableItem(Items.BREAD).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))).add(LootItem.lootTableItem(Items.GLOW_BERRIES).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 6.0F)))).add(LootItem.lootTableItem(Items.MELON_SEEDS).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))).add(LootItem.lootTableItem(Items.PUMPKIN_SEEDS).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))).add(LootItem.lootTableItem(Items.BEETROOT_SEEDS).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(3.0F)).add(LootItem.lootTableItem(Blocks.RAIL).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 8.0F)))).add(LootItem.lootTableItem(Blocks.POWERED_RAIL).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F)))).add(LootItem.lootTableItem(Blocks.DETECTOR_RAIL).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F)))).add(LootItem.lootTableItem(Blocks.ACTIVATOR_RAIL).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F)))).add(LootItem.lootTableItem(Blocks.TORCH).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 16.0F))))));
        }
    }
}
