package net.artur.avtomat.item.custom;

import net.artur.avtomat.item.ModItems;
import net.minecraft.world.Containers;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

public class SmallBottleFullItem extends Item {

    public SmallBottleFullItem(Properties properties) {
        super(properties);
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack emptyBottle = new ItemStack(ModItems.SMALLBOTTLERAW.get());

        // Добавляем эффект насыщения (можно изменить)
        entity.addEffect(new MobEffectInstance(MobEffects.SATURATION, 2 * 2, 0));

        // Уменьшаем количество бутылок на 1
        if (entity instanceof Player player) {
            if (!player.isCreative()) { // Проверяем, не находится ли сущность в творческом режиме
                stack.shrink(1);
            }
        }

        // Добавляем пустую бутылку в инвентарь игрока (если это игрок)
        if (entity instanceof Player player) {
            if (!player.getInventory().add(emptyBottle)) {
                // Если в инвентаре нет места, выкидываем пустую бутылку на землю
                Containers.dropItemStack(level, player.getX(), player.getY(), player.getZ(), emptyBottle);
            }
        }

        // Возвращаем текущую бутылку, так как ее количество уже уменьшено
        return stack;
    }


}