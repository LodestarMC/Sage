package team.lodestar.sage.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import team.lodestar.sage.client.gui.components.UIComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SageGui {

    // i could use a registry, but for now this will do fine
    private static List<SageGui> guis = new ArrayList<>();

    private SageDisplay display;
    private boolean visible = true;

    public SageGui(Supplier<UIComponent> componentSupplier) {
        display = new SageDisplay(componentSupplier);
    }

    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        display.render(poseStack, mouseX, mouseY, partialTicks);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public static void renderAll(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        for (SageGui gui : guis) {
            if (gui.visible)
                gui.render(poseStack, mouseX, mouseY, partialTicks);
        }
    }

    public static void registerGui(SageGui gui) {
        if (!guis.contains(gui))
            guis.add(gui);
    }
}
