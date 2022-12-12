package team.lodestar.sage.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import team.lodestar.sage.client.gui.components.UIComponent;

import java.util.function.Supplier;

public class SageScreen extends Screen {

    private Supplier<UIComponent> componentSupplier; // Only called once, to set rootComponent
    private UIComponent rootComponent;

    public SageScreen(Component pTitle) {
        super(pTitle);
    }

    public SageScreen set(Supplier<UIComponent> componentSupplier) {
        this.componentSupplier = componentSupplier;
        return this;
    }

    public void show() {
        if (componentSupplier != null && rootComponent == null) {
            rootComponent = componentSupplier.get();
            rootComponent.recalculatePosition();
        }

        Minecraft.getInstance().setScreen(this);
    }

    @Override
    public void render(PoseStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        rootComponent.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        rootComponent.receiveMouseRelease(pMouseX, pMouseY);
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }
}
