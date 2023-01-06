package team.lodestar.sage.notification;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import team.lodestar.sage.capability.SageLevelChunkCapability;
import team.lodestar.sage.network.PacketHandler;
import team.lodestar.sage.network.packet.SpawnNotificationPacket;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class NotificationManager {
    private static final Set<Vec3> QUEUED_DELETE_NOTIFICATIONS = new HashSet<>(); // Only used client-side

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

    public static void removeNotificationAt(Level level, Vec3 notifPos) {
        //if (level.isClientSide)
    }

    public static void tick(Level level) {
        if (!(level instanceof ServerLevel serverLevel))
            return;

        serverLevel.getChunkSource().chunkMap.getChunks().forEach(holder -> {
            LevelChunk chunk = holder.getTickingChunk();
            if (chunk == null)
                return;

            chunk.getCapability(SageLevelChunkCapability.CAPABILITY).ifPresent(capability -> {
                for (Iterator<Notification> i = capability.CHUNK_NOTIFICATIONS.iterator(); i.hasNext();) {
                    Notification notification = i.next();
                    notification.tick(level);

                    if (notification.shouldDisappear())
                        i.remove(); // TODO: sync
                }
            });
        });
    }
}
