package team.lodestar.sage.network;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import team.lodestar.sage.SageMod;
import team.lodestar.sage.network.packet.Packet;
import team.lodestar.sage.network.packet.SpawnNotificationPacket;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            SageMod.in("main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    private static int nextPacketId = 0;

    public static void registerPackets(FMLCommonSetupEvent event) {
        Packet.register(SpawnNotificationPacket.class, CHANNEL, nextPacketId++);
    }
}
