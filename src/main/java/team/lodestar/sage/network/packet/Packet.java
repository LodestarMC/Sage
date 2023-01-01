package team.lodestar.sage.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.unsafe.UnsafeHacks;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class Packet {
    public Packet(FriendlyByteBuf buf) {
        decode(buf);
    }

    public Packet() {

    }

    public void encode(FriendlyByteBuf buf) { }

    public Packet decode(FriendlyByteBuf buf) { return null; }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> handle(context.get()));
    }

    public void handle(NetworkEvent.Context context) { }

    public boolean isClientRecipient(NetworkEvent.Context context) {
        return context.getDirection().getReceptionSide().isClient();
    }

    public boolean isServerRecipient(NetworkEvent.Context context) {
        return context.getDirection().getReceptionSide().isServer();
    }

    public static <T extends Packet> void register(Class<T> packetClass, SimpleChannel instance, int index) {
        T packet = UnsafeHacks.newInstance(packetClass); // this is all your fault, sammy
        instance.registerMessage(index, packetClass, Packet::encode, buf -> (T) packet.decode(buf), Packet::handle);
    }
}
