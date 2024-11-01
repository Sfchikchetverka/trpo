package net.artur.avtomat.recipe;

import net.artur.avtomat.Avtomat;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Avtomat.MOD_ID);

    public static final RegistryObject<RecipeSerializer<RazlivStationRecipe>> AVTOMAT_RAZLIV_SERIALIZER =
            SERIALIZERS.register("razliv_vodi", () -> RazlivStationRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
