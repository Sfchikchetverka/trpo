package net.artur.avtomat.block.entity;

import net.artur.avtomat.Avtomat;
import net.artur.avtomat.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Avtomat.MOD_ID);

    public static final RegistryObject<BlockEntityType<RazlivVodiBlockEntity>> RAZLIV_STATION =
            BLOCK_ENTITIES.register("razliv_station", () ->
                    BlockEntityType.Builder.of(RazlivVodiBlockEntity::new,
                            ModBlocks.AVTOMAT_BLOCK.get()).build(null));
    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }
}
