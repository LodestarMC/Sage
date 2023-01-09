package team.lodestar.sage.client.gui.effect;

import net.minecraft.client.Minecraft;
import team.lodestar.sage.client.gui.components.UIComponent;
import team.lodestar.sage.client.gui.events.ComponentEventHandler;

public class StretchEffect extends ComponentEventHandler {
    private float originalWidth;
    private float originalHeight;
    private float deltaWidth;
    private float deltaHeight;
    private float speed;
    private float t = 0;
    private EasingFunc easingFunction;

    public StretchEffect(float deltaWidth, float deltaHeight, float speed, EasingFunc easingFunction) {
        this.deltaWidth = deltaWidth;
        this.deltaHeight = deltaHeight;
        this.speed = speed;
        this.easingFunction = easingFunction;
    }

    @Override
    public void init() {
        if (originalWidth == 0)
            originalWidth = component.getWidth();

        if (originalHeight == 0)
            originalHeight = component.getHeight();
    }

    public void stretch(UIComponent component) {
        t += Minecraft.getInstance().getDeltaFrameTime() * speed;
        if (t > 1)
            t = 1;

        float width = easingFunction.apply(0, deltaWidth, t);
        float height = easingFunction.apply(0, deltaHeight, t);
        component.dimensions(originalWidth + width, originalHeight + height);
    }

    public void shrink(UIComponent component) {
        t -= Minecraft.getInstance().getDeltaFrameTime() * speed;
        if (t < 0)
            t = 0;

        float width = easingFunction.apply(0, deltaWidth, t);
        float height = easingFunction.apply(0, deltaHeight, t);
        component.dimensions(originalWidth + width, originalHeight + height);
    }

    public static class OnHover extends StretchEffect {

        public OnHover(float deltaWidth, float deltaHeight, float speed, EasingFunc easingFunction) {
            super(deltaWidth, deltaHeight, speed, easingFunction);
            onHover(this::stretch);
            onNotHover(this::shrink);
        }
    }

    public static class OnShow extends StretchEffect {

        public OnShow(float deltaWidth, float deltaHeight, float speed, EasingFunc easingFunction) {
            super(deltaWidth, deltaHeight, speed, easingFunction);
            onRender((c, f) -> stretch(component));
        }
    }
}
