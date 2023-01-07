package team.lodestar.sage.client.gui;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import team.lodestar.sage.client.gui.components.UIComponent;

import java.util.function.Supplier;

public class SageScreen extends Screen {

    private SageDisplay display;
    private int lastScreenWidth;
    private int lastScreenHeight;
    private boolean centered;

    public SageScreen(Component pTitle, Supplier<UIComponent> componentSupplier) {
        super(pTitle);
        display = new SageDisplay(componentSupplier);
    }

    public SageScreen centered() {
        centered = true;
        return this;
    }

    public void show() {
        Window window = Minecraft.getInstance().getWindow();
        int screenWidth = window.getGuiScaledWidth();
        int screenHeight = window.getGuiScaledHeight();

        if (screenWidth != lastScreenWidth || screenHeight != lastScreenHeight) {
            if (centered)
                centerRootComponent();

            rootComponent().receiveGuiScaleChange();
        }

        lastScreenWidth = screenWidth;
        lastScreenHeight = screenHeight;

        Minecraft.getInstance().setScreen(this);
    }

    private void centerRootComponent() {
        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        rootComponent().at((screenWidth - rootComponent().getWidth()) / 2, (screenHeight - rootComponent().getHeight()) / 2);
        rootComponent().recalculatePosition();
    }

    private UIComponent rootComponent() {
        return display.getRootComponent();
    }

    @Override
    public void render(PoseStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        display.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        rootComponent().receiveMouseRelease(pMouseX, pMouseY);
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }
}
