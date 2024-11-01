package net.artur.avtomat.block.entity;
import net.artur.avtomat.item.ModItems;
import net.artur.avtomat.network.ModMessages;
import net.artur.avtomat.network.packet.ItemStackSyncS2CPacket;
import net.artur.avtomat.network.packet.MoneyC2SPacket;
import net.artur.avtomat.network.packet.SpawnParticleC2SPacket;
import net.artur.avtomat.recipe.RazlivStationRecipe;
import net.artur.avtomat.screen.RazlivVodiBlockMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class RazlivVodiBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                ModMessages.sendToClients(new ItemStackSyncS2CPacket(this, worldPosition));
            }
        }
    };
    public int Money = 0;
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;
    public boolean isClicked = false;
    public RazlivVodiBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RAZLIV_STATION.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> RazlivVodiBlockEntity.this.progress;
                    case 1 -> RazlivVodiBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> RazlivVodiBlockEntity.this.progress = value;
                    case 1 -> RazlivVodiBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Разлив воды");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new RazlivVodiBlockMenu(id, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.putInt("gem_infusing_station.progress", this.progress);
        nbt.putInt("money", this.Money);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("gem_infusing_station.progress");
        Money = nbt.getInt("money");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, RazlivVodiBlockEntity pEntity) {
        if(level.isClientSide()) {
            return;
        }
        hasItemInThirdSlot(pEntity);
        if(hasRecipe(pEntity) && pEntity.isClicked) {
            pEntity.progress++;
            setChanged(level, pos, state);
            if(pEntity.progress >= pEntity.maxProgress) {
                pEntity.isClicked = false;
                craftItem(pEntity);
            }
        } else {
            pEntity.resetProgress();
            setChanged(level, pos, state);
        }
        pEntity.sendPacket(pEntity.getBlockPos());

    }

    private void resetProgress() {
        this.progress = 0;
    }
    private void sendPacket(BlockPos pos){
        if(!level.isClientSide()) ModMessages.sendToClients(new MoneyC2SPacket(pos, this.Money));
    }

    private void spawnParticle(BlockPos pos){
        if(!level.isClientSide()) ModMessages.sendToClients(new SpawnParticleC2SPacket(pos));
    }

    private static void craftItem(RazlivVodiBlockEntity pEntity) {
        Level level = pEntity.level;
        SimpleContainer inventory = new SimpleContainer(pEntity.itemHandler.getSlots());
        for (int i = 0; i < pEntity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, pEntity.itemHandler.getStackInSlot(i));
        }

        Optional<RazlivStationRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(RazlivStationRecipe.Type.INSTANCE, inventory, level);
        if(hasRecipe(pEntity) ) {
            pEntity.Money-= (recipe.get().getMoney());
            pEntity.itemHandler.extractItem(1, 1, false);
            pEntity.itemHandler.setStackInSlot(2, new ItemStack(recipe.get().getResultItem().getItem(),
                    pEntity.itemHandler.getStackInSlot(2).getCount() + 1));
            pEntity.resetProgress();
        }
    }

    private static void hasItemInThirdSlot(@NotNull RazlivVodiBlockEntity entity){
        ItemStack slot3Item = entity.itemHandler.getStackInSlot(0);
        if(slot3Item.getItem() == ModItems.COIN.get()){
            entity.itemHandler.extractItem(0, 1, false);
            entity.Money+=1;
        }
        if(slot3Item.getItem() == ModItems.COIN1.get()){
            entity.itemHandler.extractItem(0, 1, false);
            entity.Money+=2;
        }
        if(slot3Item.getItem() == ModItems.COIN2.get()){
            entity.itemHandler.extractItem(0, 1, false);
            entity.Money+=5;
        }
        if(slot3Item.getItem() == ModItems.COIN3.get()){
            entity.itemHandler.extractItem(0, 1, false);
            entity.Money+=10;
        }
        if(slot3Item.getItem() == ModItems.BILL.get()){
            entity.itemHandler.extractItem(0, 1, false);
            entity.Money+=10;
        }
        if(slot3Item.getItem() == ModItems.BILL2.get()){
            entity.itemHandler.extractItem(0, 1, false);
            entity.Money+=50;
        }
        if(slot3Item.getItem() == ModItems.BILL3.get()){
            entity.itemHandler.extractItem(0, 1, false);
            entity.Money+=100;
        }
        if(slot3Item.getItem() == ModItems.BILL4.get()){
            entity.itemHandler.extractItem(0, 1, false);
            entity.Money+=500;
        }
        if(slot3Item.getItem() == ModItems.BILL5.get()){
            entity.itemHandler.extractItem(0, 1, false);
            entity.Money+=1000;
        }
        if(slot3Item.getItem() == ModItems.BILL6.get()){
            entity.itemHandler.extractItem(0, 1, false);
            entity.Money+=5000;
        }
    }

    private static boolean hasRecipe(RazlivVodiBlockEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }
        Optional<RazlivStationRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(RazlivStationRecipe.Type.INSTANCE, inventory, level);
        return recipe.isPresent() && canInsertAmountIntoOutputSlot(inventory) &&
                canInsertItemIntoOutputSlot(inventory, recipe.get().getResultItem()) && hasCorrectMoneyAmount(entity, recipe);
    }

    private static boolean hasCorrectMoneyAmount(RazlivVodiBlockEntity entity, Optional<RazlivStationRecipe> recipe) {
        return entity.Money >= recipe.get().getMoney();
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack stack) {
        return inventory.getItem(2).getItem() == stack.getItem() || inventory.getItem(2).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return inventory.getItem(2).getMaxStackSize() > inventory.getItem(2).getCount();
    }

    public void GiveBack(RazlivVodiBlockEntity entity){
            while (entity.Money != 0) {
                if (entity.Money - 10 >= 0) {
                    entity.Money -= 10;
                    ItemStack itemStack = new ItemStack(ModItems.COIN3.get());
                    entity.getLevel().addFreshEntity(new ItemEntity(entity.getLevel(), entity.getBlockPos().getX() + 0.5,
                            entity.getBlockPos().getY() + 0.5, entity.getBlockPos().getZ() + 0.5, itemStack));
                } else if (entity.Money - 5 >= 0) {
                    entity.Money -= 5;
                    ItemStack itemStack = new ItemStack(ModItems.COIN3.get());
                    entity.getLevel().addFreshEntity(new ItemEntity(entity.getLevel(), entity.getBlockPos().getX() + 0.5,
                            entity.getBlockPos().getY() + 0.5, entity.getBlockPos().getZ() + 0.5, itemStack));
                } else if (entity.Money - 2 >= 0) {
                    entity.Money -= 2;
                    ItemStack itemStack = new ItemStack(ModItems.COIN3.get());
                    entity.getLevel().addFreshEntity(new ItemEntity(entity.getLevel(), entity.getBlockPos().getX() + 0.5,
                            entity.getBlockPos().getY() + 0.5, entity.getBlockPos().getZ() + 0.5, itemStack));
                } else if (entity.Money - 1 >= 0) {
                    entity.Money -= 1;
                    ItemStack itemStack = new ItemStack(ModItems.COIN3.get());
                    entity.getLevel().addFreshEntity(new ItemEntity(entity.getLevel(), entity.getBlockPos().getX() + 0.5,
                            entity.getBlockPos().getY() + 0.5, entity.getBlockPos().getZ() + 0.5, itemStack));
                }
        }
    }


    public ItemStack getRenderStack() {
        ItemStack stack;

        if(!itemHandler.getStackInSlot(2).isEmpty()) {
            stack = itemHandler.getStackInSlot(2);
        } else {
            stack = itemHandler.getStackInSlot(1);
        }

        return stack;
    }

    public void setHandler(ItemStackHandler itemStackHandler) {
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            itemHandler.setStackInSlot(i, itemStackHandler.getStackInSlot(i));
        }
    }

    public void onClick(RazlivVodiBlockEntity entity){
        GiveBack(entity);
        entity.spawnParticle(entity.getBlockPos());
    }
}
