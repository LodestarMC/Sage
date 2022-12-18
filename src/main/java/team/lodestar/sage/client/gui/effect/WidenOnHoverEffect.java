package team.lodestar.sage.client.gui.effect;

import net.minecraft.client.Minecraft;
import team.lodestar.sage.client.gui.components.UIComponent;
import team.lodestar.sage.client.gui.events.ComponentEventHandler;

public class WidenOnHoverEffect extends ComponentEventHandler {
    private float originalWidth;
    private float targetWidth;
    private float speed;
    private float t = 0;
    private EasingFunc easingFunction;

    public WidenOnHoverEffect(float targetWidth, float speed, EasingFunc easingFunction) {
        this.targetWidth = targetWidth;
        this.speed = speed;
        this.easingFunction = easingFunction;

        onHover(this::onHover);
        onNotHover(this::onNotHover);
    }

    @Override
    public void init() {
        if (originalWidth == 0)
            originalWidth = component.getWidth();
    }

    public void onHover(UIComponent component) {
        t += Minecraft.getInstance().getDeltaFrameTime() * speed;
        if (t > 1)
            t = 1;

        float width = easingFunction.apply(originalWidth, targetWidth, t);
        component.width(width);
    }

    public void onNotHover(UIComponent component) {
        t -= Minecraft.getInstance().getDeltaFrameTime() * speed;
        if (t < 0)
            t = 0;

        float width = easingFunction.apply(originalWidth, targetWidth, t);
        component.width(width);
    }
}
