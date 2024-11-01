package net.artur.avtomat;

import com.mojang.logging.LogUtils;
import net.artur.avtomat.block.ModBlocks;
import net.artur.avtomat.block.entity.ModBlockEntities;
import net.artur.avtomat.event.ClientEvents;
import net.artur.avtomat.item.ModItems;
import net.artur.avtomat.network.ModMessages;
import net.artur.avtomat.recipe.ModRecipes;
import net.artur.avtomat.screen.ModMenuTypes;
import net.artur.avtomat.screen.RazlivVodiBlockScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Avtomat.MOD_ID)
public class Avtomat
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "avtomat";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public Avtomat()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        modEventBus.addListener(this::commonSetup);


        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        ModRecipes.register(FMLJavaModLoadingContext.get().getModEventBus());
        event.enqueueWork(ModMessages::register);
    }


    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            MinecraftForge.EVENT_BUS.register(ClientEvents.class);
            MenuScreens.register(ModMenuTypes.RAZLIV_VODI_BLOCK_MENU.get(), RazlivVodiBlockScreen::new);
        }
    }
}
