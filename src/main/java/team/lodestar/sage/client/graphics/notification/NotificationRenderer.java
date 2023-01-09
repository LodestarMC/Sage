package team.lodestar.sage.client.graphics.notification;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import team.lodestar.sage.SageMod;
import team.lodestar.sage.capability.SageLevelChunkCapability;
import team.lodestar.sage.notification.Notification;

public abstract class NotificationRenderer {

    public static final int NOTIFICATION_RENDER_DISTANCE = 5;

    private static BiMap<ResourceLocation, NotificationRenderer> RENDERERS = HashBiMap.create();

    public static final NotificationRenderer DEFAULT_RENDERER = new SimpleNotificationRenderer(SageMod.in("default"), SageMod.in("textures/notif/notification.png"), 0.2f);

    public static void renderNotifications(PoseStack poseStack, Camera camera, float partialTicks) {
        ChunkPos playerChunkPos = Minecraft.getInstance().player.chunkPosition();
        int r = NOTIFICATION_RENDER_DISTANCE;

        for (int x = -r; x <= r; x++) {
            for (int z = -r; z <= r; z++) {
                LevelChunk chunk = Minecraft.getInstance().level.getChunk(playerChunkPos.x + x, playerChunkPos.z + z);

                if (chunk == null)
                    continue;

                chunk.getCapability(SageLevelChunkCapability.CAPABILITY).ifPresent(capability -> {
                    for (Notification notification : capability.CHUNK_NOTIFICATIONS) {
                        notification.getRenderer().renderNotification(notification, poseStack, camera, partialTicks);
                    }
                });
            }
        }
    }

    public abstract void renderNotification(Notification notification, PoseStack poseStack, Camera camera, float partialTicks);

    public static void registerSageNotificationRenderers() {
        RENDERERS.put(SageMod.in("default"), DEFAULT_RENDERER);
    }

    public static NotificationRenderer getRenderer(ResourceLocation id) {
        return RENDERERS.get(id);
    }

    public static ResourceLocation getKey(NotificationRenderer renderer) {
        return RENDERERS.inverse().get(renderer);
    }
}
