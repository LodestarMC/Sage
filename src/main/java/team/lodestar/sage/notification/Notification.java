package team.lodestar.sage.notification;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.INBTSerializable;
import team.lodestar.sage.client.graphics.notification.NotificationRenderer;
import team.lodestar.sage.notification.behavior.NotificationBehavior;
import team.lodestar.sage.notification.behavior.NotificationBehaviorRegistry;

import java.util.ArrayList;
import java.util.List;

public class Notification implements INBTSerializable<CompoundTag> {
    private Vec3 position;
    private List<NotificationBehavior> behaviors = new ArrayList<>();
    private boolean needsSaving = false;
    private NotificationRenderer renderer;

    private Notification() { }

    public Notification(Vec3 position, NotificationRenderer renderer) {
        this.position = position;
        this.renderer = renderer;
    }

    public Notification(BlockPos position, NotificationRenderer renderer) {
        this(new Vec3(position.getX(), position.getY(), position.getZ()), renderer);
    }

    public Notification setRenderer(NotificationRenderer renderer) {
        this.renderer = renderer;
        return this;
    }

    public NotificationRenderer getRenderer() {
        return renderer;
    }

    public Notification withBehavior(NotificationBehavior behavior) {
        behaviors.add(behavior);
        return this;
    }

    public boolean needsSaving() {
        return needsSaving;
    }

    public void setNeedsSaving(boolean needsSaving) {
        this.needsSaving = needsSaving;
    }

    public Vec3 getPosition() {
        return position;
    }

    public List<NotificationBehavior> getBehaviors() {
        return behaviors;
    }

    public void tick(Level level) {
        behaviors.forEach(behavior -> behavior.tick(this, level));
    }

    public boolean shouldDisappear() {
        return behaviors.stream().anyMatch(NotificationBehavior::shouldNotificationDisappear);
    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("x", position.x);
        tag.putDouble("y", position.y);
        tag.putDouble("z", position.z);

        tag.putString("renderer", NotificationRenderer.NOTIFICATION_RENDERERS_REGISTRY.get().getKey(renderer).toString());

        ListTag behaviorList = new ListTag();

        for (NotificationBehavior behavior : behaviors) {
            CompoundTag behaviorTag = new CompoundTag();
            behaviorTag.putString("name", NotificationBehaviorRegistry.NOTIFICATION_BEHAVIORS_REGISTRY.get().getKey(behavior.getType()).toString());
            behaviorTag.put("data", behavior.serializeNbt());
            behaviorList.add(behaviorTag);
        }

        tag.put("behaviors", behaviorList);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        position = new Vec3(nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z"));

        renderer = NotificationRenderer.NOTIFICATION_RENDERERS_REGISTRY.get().getValue(new ResourceLocation(nbt.getString("renderer")));
        if (renderer == null) // fall back to default renderer
            renderer = NotificationRenderer.DEFAULT_RENDERER.get();

        ListTag behaviorList = nbt.getList("behaviors", Tag.TAG_COMPOUND);

        for (Tag tag : behaviorList) {
            CompoundTag behaviorTag = (CompoundTag) tag;
            String name = behaviorTag.getString("name");
            CompoundTag data = behaviorTag.getCompound("data");
            NotificationBehavior behavior = NotificationBehaviorRegistry.NOTIFICATION_BEHAVIORS_REGISTRY.get().getValue(new ResourceLocation(name)).getInstance().deserializeNbt(data);
            behaviors.add(behavior);
        }
    }

    public static Notification fromNbt(CompoundTag nbt) {
        Notification notification = new Notification();
        notification.deserializeNBT(nbt);
        return notification;
    }
}
