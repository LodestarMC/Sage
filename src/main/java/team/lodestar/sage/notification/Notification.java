package team.lodestar.sage.notification;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.INBTSerializable;
import team.lodestar.sage.notification.behavior.NotificationBehavior;

import java.util.ArrayList;
import java.util.List;

public class Notification implements INBTSerializable<CompoundTag> {
    private Vec3 position;
    private List<NotificationBehavior> behaviors = new ArrayList<>();

    private Notification() { }

    public Notification(Vec3 position) {
        this.position = position;
    }

    public Notification(BlockPos position) {
        this.position = new Vec3(position.getX(), position.getY(), position.getZ());
    }

    public Notification withBehavior(NotificationBehavior behavior) {
        behaviors.add(behavior);
        return this;
    }

    public Vec3 getPosition() {
        return position;
    }

    public List<NotificationBehavior> getBehaviors() {
        return behaviors;
    }

    public void tick(Level level) {
        behaviors.forEach(behavior -> behavior.tick(level));
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

        ListTag behaviorList = new ListTag();

        for (NotificationBehavior behavior : behaviors) {
            if (!behavior.needsSaving())
                continue;

            CompoundTag behaviorTag = new CompoundTag();
            tag.putString("name", NotificationManager.NOTIFICATION_BEHAVIORS_REGISTRY.get().getKey(behavior).toString());
            tag.put("data", behavior.serializeNbt());
            behaviorList.add(behaviorTag);
        }

        tag.put("behaviors", behaviorList);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        position = new Vec3(nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z"));

        ListTag behaviorList = nbt.getList("behaviors", Tag.TAG_COMPOUND);

        for (Tag tag : behaviorList) {
            CompoundTag behaviorTag = (CompoundTag) tag;
            String name = behaviorTag.getString("name");
            CompoundTag data = behaviorTag.getCompound("data");
            NotificationBehavior behavior = NotificationManager.NOTIFICATION_BEHAVIORS_REGISTRY.get().getValue(new ResourceLocation(name)).deserializeNbt(data);
            behaviors.add(behavior);
        }
    }

    public static Notification fromNbt(CompoundTag nbt) {
        Notification notification = new Notification();
        notification.deserializeNBT(nbt);
        return notification;
    }
}