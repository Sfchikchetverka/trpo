package net.artur.avtomat.network.packet;

import net.artur.avtomat.block.entity.RazlivVodiBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class StartBlockEntityProcessC2SPacket {

    private final BlockPos pos;

    public StartBlockEntityProcessC2SPacket(BlockPos pos) {
        this.pos = pos;
    }

    public StartBlockEntityProcessC2SPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public static void handle(StartBlockEntityProcessC2SPacket message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Level world = context.get().getSender().getLevel();
            BlockEntity blockEntity = world.getBlockEntity(message.pos);
            if (blockEntity instanceof RazlivVodiBlockEntity) {
                ((RazlivVodiBlockEntity) blockEntity).isClicked = true;
            }
        });
        context.get().setPacketHandled(true);
    }
}
