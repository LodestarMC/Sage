package team.lodestar.sage;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import team.lodestar.sage.client.graphics.notification.NotificationRenderer;

@Mod.EventBusSubscriber(modid = SageMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER)
            NotificationRenderer.renderNotifications(event.getPoseStack(), event.getCamera(), event.getPartialTick());
    }
}
