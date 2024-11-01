package net.artur.avtomat.event;

import net.artur.avtomat.Avtomat;
import net.artur.avtomat.block.entity.ModBlockEntities;
import net.artur.avtomat.block.entity.renderer.RazlivVodiBlockEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
@Mod.EventBusSubscriber(modid = Avtomat.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {
    @Mod.EventBusSubscriber(modid = Avtomat.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

    }

    @Mod.EventBusSubscriber(modid = Avtomat.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.RAZLIV_STATION.get(),
                    RazlivVodiBlockEntityRenderer::new);
        }
    }
}