package net.artur.avtomat.network.packet;

import net.artur.avtomat.block.entity.RazlivVodiBlockEntity;
import net.artur.avtomat.screen.RazlivVodiBlockMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MoneyC2SPacket {

    private final BlockPos pos;
    private final int money;

    public MoneyC2SPacket(BlockPos pos, int money) {
        this.pos = pos;
        this.money = money;
    }

    public MoneyC2SPacket(FriendlyByteBuf buf) {
        this.money = buf.readInt();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(money);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof RazlivVodiBlockEntity blockEntity) {
                blockEntity.Money = money;

                if(Minecraft.getInstance().player.containerMenu instanceof RazlivVodiBlockMenu menu &&
                        menu.getBlockEntity().getBlockPos().equals(pos)) {
                    menu.setMoney(this.money);
                }
            }
        });
        return true;
    }
}
