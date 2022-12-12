package team.lodestar.sage.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.GameRenderer;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;

import java.awt.*;

public class PanelComponent extends UIComponent {

    private Color fillColor;

    public PanelComponent(int width, int height, Color color) {
        dimensions(width, height);
        fillColor = color;
    }

    @Override
    protected void renderComponent(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        poseStack.pushPose();

        float x = getX();
        float y = getY();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableTexture();

        VFXBuilders.createScreen()
                .setPosColorTexDefaultFormat()
                .setShader(GameRenderer::getPositionColorShader)
                .setPositionWithWidth(x, y, positionInfo.width, positionInfo.height)
                .setColor(fillColor)
                .draw(poseStack);

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();

        poseStack.popPose();
    }
}
