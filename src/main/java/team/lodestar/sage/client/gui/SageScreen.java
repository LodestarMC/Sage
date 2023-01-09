package team.lodestar.sage.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import team.lodestar.sage.client.gui.components.UIComponent;

import java.util.function.Supplier;

public class SageScreen extends Screen {

    private SageDisplay display;

    public SageScreen(Component pTitle, Supplier<UIComponent> componentSupplier) {
        super(pTitle);
        display = new SageDisplay(componentSupplier);
    }

    public SageScreen centered() {
        display.setCentered(true);
        return this;
    }

    public void show() {
        Minecraft.getInstance().setScreen(this);
        display.receiveOnShow();
    }

    private UIComponent rootComponent() {
        return display.rootComponent();
    }

    @Override
    public void render(PoseStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        display.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        display.receiveMouseReleased(pMouseX, pMouseY);
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }
}
