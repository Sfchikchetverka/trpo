package net.artur.avtomat.network;

import net.artur.avtomat.Avtomat;
import net.artur.avtomat.network.packet.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Avtomat.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(ExampleC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ExampleC2SPacket::new)
                .encoder(ExampleC2SPacket::toBytes)
                .consumerMainThread(ExampleC2SPacket::handle)
                .add();

        net.messageBuilder(ItemStackSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ItemStackSyncS2CPacket::new)
                .encoder(ItemStackSyncS2CPacket::toBytes)
                .consumerMainThread(ItemStackSyncS2CPacket::handle)
                .add();

        net.messageBuilder(RunBlockEntityProcessC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(RunBlockEntityProcessC2SPacket::new)
                .encoder(RunBlockEntityProcessC2SPacket::encode)
                .consumerMainThread(RunBlockEntityProcessC2SPacket::handle)
                .add();

        net.messageBuilder(StartBlockEntityProcessC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(StartBlockEntityProcessC2SPacket::new)
                .encoder(StartBlockEntityProcessC2SPacket::encode)
                .consumerMainThread(StartBlockEntityProcessC2SPacket::handle)
                .add();

        net.messageBuilder(MoneyC2SPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(MoneyC2SPacket::new)
                .encoder(MoneyC2SPacket::toBytes)
                .consumerMainThread(MoneyC2SPacket::handle)
                .add();

        net.messageBuilder(SpawnParticleC2SPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SpawnParticleC2SPacket::new)
                .encoder(SpawnParticleC2SPacket::encode)
                .consumerMainThread(SpawnParticleC2SPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}