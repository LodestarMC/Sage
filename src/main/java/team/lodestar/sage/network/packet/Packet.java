package team.lodestar.sage.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.unsafe.UnsafeHacks;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public abstract class Packet {
    public abstract void encode(FriendlyByteBuf buf);

    public abstract Packet decode(FriendlyByteBuf buf);

    public abstract void handle(NetworkEvent.Context context);

    private void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> handle(context.get()));
    }

    public boolean isClientRecipient(NetworkEvent.Context context) {
        return context.getDirection().getReceptionSide().isClient();
    }

    public boolean isServerRecipient(NetworkEvent.Context context) {
        return context.getDirection().getReceptionSide().isServer();
    }

    public static <T extends Packet> void register(Class<T> packetClass, SimpleChannel instance, int index) {
        if (packetClass == null || packetClass == Packet.class)
            throw new IllegalArgumentException("Packet class cannot be null or Packet.class");

        T packet = UnsafeHacks.newInstance(packetClass); // this is all your fault, sammy
        instance.registerMessage(index, packetClass, Packet::encode, buf -> (T) packet.decode(buf), Packet::handle);
    }
}
