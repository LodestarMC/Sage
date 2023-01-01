package team.lodestar.sage.notification.behavior;

import net.minecraft.nbt.CompoundTag;

public class NotificationBehavior {
    public boolean shouldNotificationDisappear() { return false; }

    public boolean needsSaving() { return false;}

    public CompoundTag serializeNbt() {
        return null;
    }

    public NotificationBehavior deserializeNbt(CompoundTag nbt) { return null; }
}
