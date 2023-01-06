package team.lodestar.sage.notification.behavior;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import team.lodestar.sage.SageMod;

import java.util.function.Supplier;

public class NotificationBehaviorRegistry {
    public static final ResourceKey<Registry<NotificationBehaviorType<?>>> NOTIFICATION_BEHAVIORS_KEY = ResourceKey.createRegistryKey(SageMod.in("notification_behaviors"));
    public static final DeferredRegister<NotificationBehaviorType<?>> NOTIFICATION_BEHAVIORS = DeferredRegister.create(NOTIFICATION_BEHAVIORS_KEY, SageMod.MODID);
    public static final Supplier<IForgeRegistry<NotificationBehaviorType<?>>> NOTIFICATION_BEHAVIORS_REGISTRY = NOTIFICATION_BEHAVIORS.makeRegistry(() -> new RegistryBuilder<NotificationBehaviorType<?>>().setMaxID(Integer.MAX_VALUE - 1).disableSaving().disableSync());

    public static final RegistryObject<NotificationBehaviorType<LifetimeNotificationBehavior>> LIFETIME_BEHAVIOR = NOTIFICATION_BEHAVIORS.register("lifetime", () -> new NotificationBehaviorType<>(LifetimeNotificationBehavior.class));

    public static void registerNotifications() {
        NOTIFICATION_BEHAVIORS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
