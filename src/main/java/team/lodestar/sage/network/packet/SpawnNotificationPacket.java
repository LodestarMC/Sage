package team.lodestar.sage.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import team.lodestar.sage.notification.Notification;
import team.lodestar.sage.notification.NotificationManager;

public class SpawnNotificationPacket extends Packet {
    public Notification notification;

    public SpawnNotificationPacket(Notification notification) {
        this.notification = notification;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(notification.serializeNBT());
    }

    @Override
    public SpawnNotificationPacket decode(FriendlyByteBuf buf) {
        return new SpawnNotificationPacket(Notification.fromNbt(buf.readNbt()));
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        context.setPacketHandled(true);

        if (!isClientRecipient(context))
            return;

        NotificationManager.addNotificationDirect(Minecraft.getInstance().level, notification);
    }
}
