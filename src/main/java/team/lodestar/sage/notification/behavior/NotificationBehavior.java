package team.lodestar.sage.notification.behavior;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import team.lodestar.sage.notification.Notification;

public class NotificationBehavior {
    protected Notification notification;

    public NotificationBehavior(Notification notification) {
        this.notification = notification;
    }

    public boolean shouldNotificationDisappear() { return false; }

    public boolean needsSaving() { return false;}

    public void tick(Level level) { }

    public CompoundTag serializeNbt() {
        return null;
    }

    public NotificationBehavior deserializeNbt(CompoundTag nbt) { return null; }
}
