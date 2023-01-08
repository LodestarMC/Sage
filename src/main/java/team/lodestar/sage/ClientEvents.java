package team.lodestar.sage;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import team.lodestar.sage.client.graphics.notification.NotificationRenderer;
import team.lodestar.sage.client.gui.SageGui;
import team.lodestar.sage.client.gui.components.TextComponent;

import java.awt.*;

@Mod.EventBusSubscriber(modid = SageMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {
    private static SageGui TEST_GUI = new SageGui(() -> new TextComponent("Hello World!", Color.WHITE));

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER)
            NotificationRenderer.renderNotifications(event.getPoseStack(), event.getCamera(), event.getPartialTick());
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        SageGui.registerGui(TEST_GUI);
        SageGui.renderAll(event.getPoseStack(), -1, -1, event.getPartialTick());
    }
}
