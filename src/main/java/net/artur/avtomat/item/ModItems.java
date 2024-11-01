package net.artur.avtomat.item;

import net.artur.avtomat.Avtomat;
import net.artur.avtomat.item.custom.BigBottleFullItem;
import net.artur.avtomat.item.custom.MediumBottleFullItem;
import net.artur.avtomat.item.custom.SmallBottleFullItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Avtomat.MOD_ID);
    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }

    public static final RegistryObject<Item> COIN = ITEMS.register("coin",
            ()-> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> COIN1 = ITEMS.register("coin1",
            ()-> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> COIN2 = ITEMS.register("coin2",
            ()-> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> COIN3 = ITEMS.register("coin3",
            ()-> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> BILL = ITEMS.register("bill",
            ()-> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> BILL2 = ITEMS.register("bill2",
            ()-> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> BILL3 = ITEMS.register("bill3",
            ()-> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> BILL4 = ITEMS.register("bill4",
            ()-> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> BILL5 = ITEMS.register("bill5",
            ()-> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> BILL6 = ITEMS.register("bill6",
            ()-> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> SMALLBOTTLERAW = ITEMS.register("smallbottleraw",
            ()-> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> SMALLBOTTLEFULL = ITEMS.register("smallbottlefull",
            ()-> new SmallBottleFullItem(new Item.Properties().food(new FoodProperties.Builder().
                    nutrition(2).saturationMod(0.01f).build()).stacksTo(16).tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> BIGBOTTLERAW = ITEMS.register("bigbottleraw",
            ()-> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> BIGBOTTLEFULL = ITEMS.register("bigbottlefull",
            ()-> new BigBottleFullItem(new Item.Properties().food(new FoodProperties.Builder().
                    nutrition(6).saturationMod(4f).build()).stacksTo(16).tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> MEDIUMBOTTLERAW = ITEMS.register("mediumbottleraw",
            ()-> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> MEDIUMBOTTLEFULL = ITEMS.register("mediumbottlefull",
            ()-> new MediumBottleFullItem(new Item.Properties().food(new FoodProperties.Builder().
                    nutrition(4).saturationMod(2f).build()).stacksTo(16).tab(CreativeModeTab.TAB_MISC)));
}

