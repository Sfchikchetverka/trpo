package net.artur.avtomat.network.packet;

import net.artur.avtomat.block.entity.RazlivVodiBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class RunBlockEntityProcessC2SPacket {

    private final BlockPos pos;

    public RunBlockEntityProcessC2SPacket(BlockPos pos) {
        this.pos = pos;
    }

    public RunBlockEntityProcessC2SPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public static void handle(RunBlockEntityProcessC2SPacket message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Level world = context.get().getSender().getLevel();
            BlockEntity blockEntity = world.getBlockEntity(message.pos);
            if (blockEntity instanceof RazlivVodiBlockEntity) {
                ((RazlivVodiBlockEntity) blockEntity).onClick((RazlivVodiBlockEntity) blockEntity);
            }
        });
        context.get().setPacketHandled(true);
    }
}
