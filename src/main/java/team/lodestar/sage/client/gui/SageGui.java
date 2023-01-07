package team.lodestar.sage.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import team.lodestar.sage.client.gui.components.UIComponent;

import java.util.function.Supplier;

public class SageGui {
    private SageDisplay display;

    public SageGui(Supplier<UIComponent> componentSupplier) {
        display = new SageDisplay(componentSupplier);
    }

    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        display.render(poseStack, mouseX, mouseY, partialTicks);
    }
}
