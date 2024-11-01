package net.artur.avtomat.screen;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.artur.avtomat.Avtomat;
import net.artur.avtomat.block.entity.RazlivVodiBlockEntity;
import net.artur.avtomat.item.ModItems;
import net.artur.avtomat.network.ModMessages;
import net.artur.avtomat.network.packet.RunBlockEntityProcessC2SPacket;
import net.artur.avtomat.network.packet.StartBlockEntityProcessC2SPacket;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import java.lang.management.MonitorInfo;
import java.util.Collections;

public class RazlivVodiBlockScreen extends AbstractContainerScreen<RazlivVodiBlockMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Avtomat.MOD_ID,"textures/gui/razliv_vodi_gui.png");
    public RazlivVodiBlockScreen(RazlivVodiBlockMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);

    }
    public static Button button;
    private Button button2;
    public float mouseX;
    public float mouseY;
    public int x;
    public int y;
    public boolean isHovered = false;
    public boolean isHovered2 = false;
    public boolean toolTipHover = false;
    @Override
    protected void init() {
        super.init();
        x = (width - imageWidth) / 2;
        y = (height - imageHeight) / 2;
        button = new Button(x+48, y+12, 46, 14, Component.literal("Открыть сундук"), this::buttonClick);
        button2 = new Button(x+48, y+58, 46, 14, Component.literal("Открыть сундук"), this::buttonClick1);
        this.addWidget(button);
        this.addWidget(button2);
    }
    private void buttonClick1(Button button2) {
        BlockPos blockPos = this.getMenu().blockEntity.getBlockPos();
        RunBlockEntityProcessC2SPacket packet = new RunBlockEntityProcessC2SPacket(blockPos);
        ModMessages.sendToServer(packet);
        this.onClose();
    }

    private void buttonClick(Button button) {
        BlockPos blockPos = this.getMenu().blockEntity.getBlockPos();
        StartBlockEntityProcessC2SPacket packet = new StartBlockEntityProcessC2SPacket(blockPos);
        ModMessages.sendToServer(packet);
    }

    @Override
    protected void renderBg(PoseStack stack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        mouseX = pMouseX;
        mouseY = pMouseY;
        this.blit(stack, x, y, 0, 0, imageWidth, imageHeight);
        renderButtonHover(stack, x, y);
        renderButtonClick(stack, x, y);
        renderProgressArrow(stack, x, y);
        ButtonNotPressedAndHovered();
        if(toolTipHover){
            this.renderTooltip(stack, Component.literal("Баланс: " + menu.getMoney() + "₽"),pMouseX+10,pMouseY+10);
        }
    }


    private void renderButtonHover(PoseStack pPoseStack, int x, int y) {
        if(isHovered){
            blit(pPoseStack, x+48, y+12, 177, 28,45, 14);
        }
        if(isHovered2){
            blit(pPoseStack, x+48, y+58, 177, 64,45, 14);
        }
    }

    private void renderButtonClick(PoseStack pPoseStack, int x, int y) {
        if(this.menu.blockEntity.isClicked){
            button.blit(pPoseStack, x+47, y+11, 176, 45,48, 17);
        }
    }

    private void ButtonNotPressedAndHovered(){
        isHovered = mouseX >= x + 48 && mouseX <= x + 48 + button.getWidth() && mouseY >= y + 12 && mouseY <= y + 12 + button.getHeight() && !this.menu.blockEntity.isClicked;
        isHovered2 = mouseX >= x + 48 && mouseX <= x + 48 + button.getWidth() && mouseY >= y + 58 && mouseY <= y + 58 + button.getHeight();
        toolTipHover = mouseX >= x + 79 && mouseX <= x + 79 + 14 && mouseY >= y + 35 && mouseY <= y + 35 + 14;
    }
    private void renderProgressArrow(PoseStack pPoseStack, int x, int y) {
        if(menu.isCrafting()) {
            blit(pPoseStack, x + 15, y + 30, 176, 0, 8, menu.getScaledProgress());
        }
    }


    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);

        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }


}
