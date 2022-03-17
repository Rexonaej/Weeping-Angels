package me.suff.mc.angels.data;

import com.google.gson.JsonObject;
import me.suff.mc.angels.WeepingAngels;
import me.suff.mc.angels.common.WAObjects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Random;

import static me.suff.mc.angels.WeepingAngels.MODID;

/* Created by Craig on 10/03/2021 */
public class WAGlobalLoot {
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> GLM = DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, MODID);

    public static final RegistryObject<DiscLoot.Serializer> ANGEL_LOOT = GLM.register("loot", DiscLoot.Serializer::new);

    public static ItemStack genMusicDisc(Random random) {
        return new ItemStack(random.nextBoolean()  ? WAObjects.Items.SALLY.get() : WAObjects.Items.TIME_PREVAILS.get());
    }

    public static class DiscLoot extends LootModifier {

        private final int chance;

        public DiscLoot(final LootItemCondition[] conditionsIn, int chance) {
            super(conditionsIn);
            this.chance = chance;
        }

        @Override
        protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {

            if (context.getRandom().nextInt(100) <= chance) {
                generatedLoot.add(genMusicDisc(context.getRandom()));
            }

            return generatedLoot;
        }

        private static class Serializer extends GlobalLootModifierSerializer<DiscLoot> {
            @Override
            public DiscLoot read(ResourceLocation location, JsonObject object, LootItemCondition[] conditions) {
                final int multiplicationFactor = GsonHelper.getAsInt(object, "chance", 2);
                return new DiscLoot(conditions, multiplicationFactor);
            }

            @Override
            public JsonObject write(DiscLoot instance) {
                final JsonObject obj = this.makeConditions(instance.conditions);
                obj.addProperty("chance", instance.chance);
                return obj;
            }
        }
    }

}