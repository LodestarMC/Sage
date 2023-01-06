package team.lodestar.sage.notification.behavior;

import net.minecraftforge.fml.unsafe.UnsafeHacks;

public final class NotificationBehaviorType<T extends NotificationBehavior> {
    private T instance;

    public NotificationBehaviorType(Class<T> clazz) {
        if (clazz == null || clazz == NotificationBehavior.class)
            throw new IllegalArgumentException("Class cannot be null or equal to NotificationBehavior");

        instance = UnsafeHacks.newInstance(clazz);
    }

    public Class<T> getNotificationClass() {
        return (Class<T>) instance.getClass();
    }

    public T getInstance() {
        return instance;
    }
}
