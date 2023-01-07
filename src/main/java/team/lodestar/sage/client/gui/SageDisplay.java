package team.lodestar.sage.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import team.lodestar.sage.client.gui.components.UIComponent;

import java.util.function.Supplier;

public class SageDisplay {

    private Supplier<UIComponent> componentSupplier; // Only called once, to set rootComponent
    private UIComponent rootComponent;

    public SageDisplay(Supplier<UIComponent> componentSupplier) {
        this.componentSupplier = componentSupplier;
    }

    public UIComponent getRootComponent() {
        if (rootComponent == null) {
            rootComponent = componentSupplier.get();
            rootComponent.recalculatePosition();
        }

        return rootComponent;
    }

    public void render(PoseStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        rootComponent.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
    }
}
