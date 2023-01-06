package team.lodestar.sage.notification.behavior;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import team.lodestar.sage.notification.Notification;

public abstract class NotificationBehavior {
    public abstract boolean shouldNotificationDisappear();

    public abstract void tick(Notification notification, Level level);

    public abstract CompoundTag serializeNbt();

    public abstract NotificationBehavior deserializeNbt(CompoundTag nbt);
}
