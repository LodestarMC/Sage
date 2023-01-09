package team.lodestar.sage.client.gui.effect;

import team.lodestar.sage.client.gui.components.UIComponent;
import team.lodestar.sage.client.gui.events.ComponentEventHandler;

public class ChangePosOnHoverEffect extends ComponentEventHandler implements ComposableEffect {
    private float originalX;
    private float originalY;
    private float deltaX;
    private float deltaY;
    private float speed;
    private float t = 0;
    private EasingFunc easingFunction;

    public ChangePosOnHoverEffect(float deltaX, float deltaY, float speed, EasingFunc easingFunction) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.speed = speed;
        this.easingFunction = easingFunction;

        onHover(this::onHover);
        onNotHover(this::onNotHover);
    }

    @Override
    public void init() {
        if (originalX == 0)
            originalX = component.getX();
        if (originalY == 0)
            originalY = component.getY();
    }

    @Override
    public float getAnimationProgress() {
        return t;
    }

    public void onHover(UIComponent component) {
        t += component.partialTicks() * speed;
        if (t > 1)
            t = 1;

        float x = easingFunction.apply(0, deltaX, t);
        float y = easingFunction.apply(0, deltaY, t);
        component.at(originalX + x, originalY + y);
        component.recalculatePosition();
    }

    public void onNotHover(UIComponent component) {
        t -= component.partialTicks() * speed;
        if (t < 0)
            t = 0;

        float x = easingFunction.apply(0, deltaX, t);
        float y = easingFunction.apply(0, deltaY, t);
        component.at(originalX + x, originalY + y);
        component.recalculatePosition();
    }
}
