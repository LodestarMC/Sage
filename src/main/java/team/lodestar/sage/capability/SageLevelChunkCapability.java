package team.lodestar.sage.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import team.lodestar.lodestone.systems.capability.LodestoneCapabilityProvider;
import team.lodestar.sage.SageMod;
import team.lodestar.sage.notification.Notification;

import java.util.HashSet;
import java.util.Set;

public class SageLevelChunkCapability implements INBTSerializable<CompoundTag> {
    public static Capability<SageLevelChunkCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
    public Set<Notification> CHUNK_NOTIFICATIONS = new HashSet<>();

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(SageLevelChunkCapability.class);
    }

    public static void attachToChunk(AttachCapabilitiesEvent<LevelChunk> event) {
        event.addCapability(SageMod.in("chunk_data"), new LodestoneCapabilityProvider(CAPABILITY, () -> new SageLevelChunkCapability()));
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag notifications = new ListTag();

        for (Notification notification : CHUNK_NOTIFICATIONS) {
            notifications.add(notification.serializeNBT());
        }

        tag.put("notifications", notifications);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        ListTag notifications = nbt.getList("notifications", Tag.TAG_COMPOUND);

        for (int i = 0; i < notifications.size(); i++) {
            CHUNK_NOTIFICATIONS.add(Notification.fromNbt(notifications.getCompound(i)));
        }
    }
}
