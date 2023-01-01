package team.lodestar.sage.notification;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import team.lodestar.sage.SageMod;
import team.lodestar.sage.capability.SageLevelChunkCapability;
import team.lodestar.sage.network.PacketHandler;
import team.lodestar.sage.network.packet.SpawnNotificationPacket;
import team.lodestar.sage.notification.behavior.NotificationBehavior;

import java.util.List;
import java.util.function.Supplier;

public class NotificationManager {
    public static final ResourceKey<Registry<NotificationBehavior>> NOTIFICATION_BEHAVIORS_KEY = ResourceKey.createRegistryKey(SageMod.in("notification_behaviors"));
    public static final DeferredRegister<NotificationBehavior> NOTIFICATION_BEHAVIORS = DeferredRegister.create(NOTIFICATION_BEHAVIORS_KEY, SageMod.MODID);
    public static final Supplier<IForgeRegistry<NotificationBehavior>> NOTIFICATION_BEHAVIORS_REGISTRY = NOTIFICATION_BEHAVIORS.makeRegistry(() -> new RegistryBuilder<NotificationBehavior>().setMaxID(Integer.MAX_VALUE - 1).disableSaving().disableSync());

    public static void addNotification(Level level, Notification notification, List<ServerPlayer> targets) {
        if (level.isClientSide)
            return;

        addNotificationDirect(level, notification);

        for (ServerPlayer player : targets)
            PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SpawnNotificationPacket(notification));
    }

    public static void addNotificationDirect(Level level, Notification notification) {
        Vec3 position = notification.getPosition();
        LevelChunk chunk = level.getChunkAt(new BlockPos(position.x, position.y, position.z));

        chunk.getCapability(SageLevelChunkCapability.CAPABILITY).ifPresent(capability -> {
            capability.CHUNK_NOTIFICATIONS.add(notification);
            chunk.setUnsaved(true);
        });
    }
}
