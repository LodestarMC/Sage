package team.lodestar.sage.client.gui;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import team.lodestar.sage.client.gui.components.UIComponent;

import java.util.function.Supplier;

public class SageDisplay {

    private Supplier<UIComponent> componentSupplier; // Only called once, to set rootComponent
    private UIComponent rootComponent;
    private int lastScreenWidth;
    private int lastScreenHeight;
    private boolean centered = false;

    public SageDisplay(Supplier<UIComponent> componentSupplier) {
        this.componentSupplier = componentSupplier;
    }

    public UIComponent rootComponent() {
        initializeRootIfNeeded();
        return rootComponent;
    }

    public void setCentered(boolean centered) {
        this.centered = centered;
    }

    private void initializeRootIfNeeded() {
        if (rootComponent == null) {
            rootComponent = componentSupplier.get();
            rootComponent.recalculatePosition();
        }
    }

    public void render(PoseStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        Window window = Minecraft.getInstance().getWindow();
        int screenWidth = window.getGuiScaledWidth();
        int screenHeight = window.getGuiScaledHeight();

        if (screenWidth != lastScreenWidth || screenHeight != lastScreenHeight) {
            if (centered)
                centerRootComponent();

            receiveGuiScaleChange();
        }

        lastScreenWidth = screenWidth;
        lastScreenHeight = screenHeight;

        rootComponent().render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
    }

    private void centerRootComponent() {
        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        rootComponent().at((screenWidth - rootComponent().getWidth()) / 2, (screenHeight - rootComponent().getHeight()) / 2);
        rootComponent().recalculatePosition();
    }

    public void receiveOnShow() {
        rootComponent().receiveOnShow();
    }

    public void receiveMouseReleased(double mouseX, double mouseY) {
        rootComponent().receiveMouseRelease(mouseX, mouseY);
    }

    public void receiveGuiScaleChange() {
        rootComponent().receiveGuiScaleChange();
    }
}
