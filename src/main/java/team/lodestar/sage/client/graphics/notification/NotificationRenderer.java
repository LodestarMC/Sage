package team.lodestar.sage.client.graphics.notification;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import team.lodestar.sage.SageMod;
import team.lodestar.sage.capability.SageLevelChunkCapability;
import team.lodestar.sage.notification.Notification;

import java.util.function.Supplier;

public abstract class NotificationRenderer {

    public static final int NOTIFICATION_RENDER_DISTANCE = 5;

    public static final ResourceKey<Registry<NotificationRenderer>> NOTIFICATION_RENDERER_KEY = ResourceKey.createRegistryKey(SageMod.in("notification_renderer"));
    public static final DeferredRegister<NotificationRenderer> NOTIFICATION_RENDERERS = DeferredRegister.create(NOTIFICATION_RENDERER_KEY, SageMod.MODID);
    public static final Supplier<IForgeRegistry<NotificationRenderer>> NOTIFICATION_RENDERERS_REGISTRY = NOTIFICATION_RENDERERS.makeRegistry(() -> new RegistryBuilder<NotificationRenderer>().setMaxID(Integer.MAX_VALUE - 1).disableSaving().disableSync());

    public static final RegistryObject<NotificationRenderer> DEFAULT_RENDERER = NOTIFICATION_RENDERERS.register("default", () -> new SimpleNotificationRenderer(SageMod.in("default"), SageMod.in("textures/notif/notification.png"), 0.2f));

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
        NOTIFICATION_RENDERERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
