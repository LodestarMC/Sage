package team.lodestar.sage.client.graphics.notification;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.systems.rendering.StateShards;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.sage.notification.Notification;

import java.awt.*;

import static team.lodestar.lodestone.helpers.RenderHelper.FULL_BRIGHT;

// TODO: Can we include more fields for customization?
public class SimpleNotificationRenderer extends NotificationRenderer {

    private ResourceLocation texture;
    private RenderType renderType;
    private float scale;

    public SimpleNotificationRenderer(ResourceLocation name, ResourceLocation texture, float scale) {
        this.texture = texture;
        renderType = RenderType.create(
                name.toString(),
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
                        .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                        .setCullState(new RenderStateShard.CullStateShard(false))
                        .setDepthTestState(new RenderStateShard.DepthTestStateShard("always", 519))
                        .createCompositeState(true)
        );
        this.scale = scale;
    }

    @Override
    public void renderNotification(Notification notification, PoseStack poseStack, Camera camera, float partialTicks) {
        Vec3 cameraPos = camera.getPosition();
        Vec3 notifPos = notification.getPosition();

        RenderSystem.disableDepthTest();
        poseStack.pushPose();
        poseStack.translate(notifPos.x, notifPos.y, notifPos.z);
        poseStack.translate(-cameraPos.x(), -cameraPos.y(), -cameraPos.z());
        poseStack.mulPose(camera.rotation());
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180));

        VertexConsumer consumer = RenderHandler.DELAYED_RENDER.getBuffer(renderType);
        VFXBuilders.createWorld()
                .setPosColorTexLightmapDefaultFormat()
                .setColor(Color.WHITE)
                .setAlpha(1f)
                .setLight(FULL_BRIGHT)
                .renderQuad(consumer, poseStack, scale);

        poseStack.popPose();
    }
}
