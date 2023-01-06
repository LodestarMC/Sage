package team.lodestar.sage.notification.behavior;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import team.lodestar.sage.notification.Notification;

public class LifetimeNotificationBehavior extends NotificationBehavior {
    private int ticksLeft;

    @Override
    public boolean shouldNotificationDisappear() {
        return ticksLeft <= 0;
    }

    public LifetimeNotificationBehavior(int timeLeft) {
        super(NotificationBehaviorRegistry.LIFETIME_BEHAVIOR.get());
        this.ticksLeft = timeLeft;
    }

    @Override
    public void tick(Notification notification, Level level) {
        ticksLeft--;
    }

    @Override
    public CompoundTag serializeNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("ticksLeft", ticksLeft);
        return tag;
    }

    @Override
    public NotificationBehavior deserializeNbt(CompoundTag nbt) {
        return new LifetimeNotificationBehavior(nbt.getInt("ticksLeft"));
    }
}
