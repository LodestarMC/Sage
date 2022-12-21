package team.lodestar.sage.client.gui.effect;

import net.minecraft.client.Minecraft;
import team.lodestar.sage.client.gui.components.UIComponent;
import team.lodestar.sage.client.gui.events.ComponentEventHandler;

public class StretchOnHoverEffect extends ComponentEventHandler {
    private float originalWidth;
    private float originalHeight;
    private float deltaWidth;
    private float deltaHeight;
    private float speed;
    private float t = 0;
    private EasingFunc easingFunction;

    public StretchOnHoverEffect(float deltaWidth, float deltaHeight, float speed, EasingFunc easingFunction) {
        this.deltaWidth = deltaWidth;
        this.deltaHeight = deltaHeight;
        this.speed = speed;
        this.easingFunction = easingFunction;

        onHover(this::onHover);
        onNotHover(this::onNotHover);
    }

    @Override
    public void init() {
        if (originalWidth == 0)
            originalWidth = component.getWidth();

        if (originalHeight == 0)
            originalHeight = component.getHeight();
    }

    public void onHover(UIComponent component) {
        t += Minecraft.getInstance().getDeltaFrameTime() * speed;
        if (t > 1)
            t = 1;

        float width = easingFunction.apply(0, deltaWidth, t);
        float height = easingFunction.apply(0, deltaHeight, t);
        component.dimensions(originalWidth + width, originalHeight + height);
    }

    public void onNotHover(UIComponent component) {
        t -= Minecraft.getInstance().getDeltaFrameTime() * speed;
        if (t < 0)
            t = 0;

        float width = easingFunction.apply(0, deltaWidth, t);
        float height = easingFunction.apply(0, deltaHeight, t);
        component.dimensions(originalWidth + width, originalHeight + height);
    }
}
