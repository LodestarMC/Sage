package team.lodestar.sage.client.graphics.notification;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.systems.rendering.StateShards;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.sage.SageMod;
import team.lodestar.sage.capability.SageLevelChunkCapability;
import team.lodestar.sage.notification.Notification;

import java.awt.*;

import static team.lodestar.lodestone.helpers.RenderHelper.FULL_BRIGHT;

public class NotificationRenderer {

    public static ResourceLocation NOTIFICATION_TEXTURE = SageMod.in("textures/notif/notification.png");
    public static RenderType RENDER_TYPE = RenderType.create(
            SageMod.in("notification").toString(),
            DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
            VertexFormat.Mode.QUADS,
            256,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionColorTexLightmapShader))
                    .setTransparencyState(StateShards.NORMAL_TRANSPARENCY)
                    .setWriteMaskState(new RenderStateShard.WriteMaskStateShard(true, false))
                    .setLightmapState(new RenderStateShard.LightmapStateShard(false))
                    .setTextureState(new RenderStateShard.TextureStateShard(NOTIFICATION_TEXTURE, false, false))
                    .setCullState(new RenderStateShard.CullStateShard(false))
                    .setDepthTestState(new RenderStateShard.DepthTestStateShard("always", 519))
                    .createCompositeState(true)
            );

    public static final int NOTIFICATION_RENDER_DISTANCE = 5;

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
                        renderNotification(poseStack, camera, notification, partialTicks);
                    }
                });
            }
        }
    }

    public static void renderNotification(PoseStack poseStack, Camera camera, Notification notification, float partialTicks) {
        Vec3 cameraPos = camera.getPosition();
        Vec3 notifPos = notification.getPosition();

        RenderSystem.disableDepthTest();
        poseStack.pushPose();
        poseStack.translate(notifPos.x, notifPos.y, notifPos.z);
        poseStack.translate(-cameraPos.x(), -cameraPos.y(), -cameraPos.z());
        //poseStack.mulPose(camera.rotation());
        poseStack.mulPose(Vector3f.YP.rotationDegrees((Minecraft.getInstance().level.getGameTime() + partialTicks) * 2));

        VertexConsumer consumer = RenderHandler.DELAYED_RENDER.getBuffer(RENDER_TYPE);
        VFXBuilders.createWorld()
                .setPosColorTexLightmapDefaultFormat()
                .setColor(Color.WHITE)
                .setAlpha(1f)
                .setLight(FULL_BRIGHT)
                .renderQuad(consumer, poseStack, 0.2f);

        poseStack.popPose();
    }
}
