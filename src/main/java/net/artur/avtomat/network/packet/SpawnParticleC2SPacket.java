package net.artur.avtomat.network.packet;

import net.artur.avtomat.block.entity.RazlivVodiBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SpawnParticleC2SPacket {

    private final BlockPos pos;

    public SpawnParticleC2SPacket(BlockPos pos) {
        this.pos = pos;
    }

    public SpawnParticleC2SPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public static void handle(SpawnParticleC2SPacket message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Level world = context.get().getSender().getLevel();
            BlockEntity blockEntity = world.getBlockEntity(message.pos);
            double x = 10.0 + (Math.random() - 0.5) * 2.0;
            double y = 64.0 + (Math.random() - 0.5) * 2.0;
            double z = 10.0 + (Math.random() - 0.5) * 2.0;
            world.addParticle(ParticleTypes.EXPLOSION,blockEntity.getBlockPos().getX(),blockEntity.getBlockPos().getY()+1,blockEntity.getBlockPos().getX(),
                    x,y,z);
        });
        context.get().setPacketHandled(true);
    }
}
