package net.artur.avtomat.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.artur.avtomat.block.custom.AvtomatBlock;
import net.artur.avtomat.block.entity.RazlivVodiBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class RazlivVodiBlockEntityRenderer implements BlockEntityRenderer<RazlivVodiBlockEntity> {
    public RazlivVodiBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(RazlivVodiBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack,
                       MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            ItemStack itemStack = pBlockEntity.getRenderStack();
            pPoseStack.pushPose();
            pPoseStack.translate(0.4f, 1.35f, 0.8f);
            pPoseStack.scale(1f, 1f, 1f);


            switch (pBlockEntity.getBlockState().getValue(AvtomatBlock.FACING)) {
                case NORTH, WEST, SOUTH, EAST -> pPoseStack.mulPose(Vector3f.ZP.rotationDegrees(0));
            }
        itemRenderer.renderStatic(itemStack, ItemTransforms.TransformType.GUI, getLightLevel(pBlockEntity.getLevel(), pBlockEntity.getBlockPos()),
                OverlayTexture.NO_OVERLAY, pPoseStack, pBufferSource, 1);
        pPoseStack.popPose();
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
