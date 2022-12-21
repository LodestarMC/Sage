package team.lodestar.sage.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;

import java.awt.*;

// TODO: TextComponents should be dynamic by default. Instead of storing a string directly, we need a supplier
public class TextComponent extends UIComponent {
    private String text;
    private Color color;
    private boolean shadow;
    private boolean needsSizeCalculated = true;

    public TextComponent(String text, Color color) {
        this.text = text;
        this.color = color;
        this.shadow = true;

        if (Minecraft.getInstance().font != null) {
            calculateSize();
            needsSizeCalculated = false;
        }
    }

    public TextComponent(String text, Color color, boolean shadow) {
        this(text, color);
        this.shadow = shadow;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void shadow(boolean shadow) { this.shadow = shadow; }

    @Override
    protected void renderComponent(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        if (needsSizeCalculated) {
            calculateSize();
            recalculatePosition(); // to update any children
            needsSizeCalculated = false;
        }

        if (shadow)
            Minecraft.getInstance().font.drawShadow(poseStack, text, getAbsoluteX(), getAbsoluteY(), color.getRGB());
        else
            Minecraft.getInstance().font.draw(poseStack, text, getAbsoluteX(), getAbsoluteY(), color.getRGB());
    }

    private void calculateSize() {
        width(Minecraft.getInstance().font.width(text));
        height(Minecraft.getInstance().font.lineHeight);
    }
}
