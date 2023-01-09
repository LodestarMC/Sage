package team.lodestar.sage.client.gui.events;

import team.lodestar.sage.client.gui.components.UIComponent;

import java.util.function.Consumer;

public class ComponentEventHandler {
    protected UIComponent component;
    private Consumer<UIComponent> onClick;
    private Consumer<UIComponent> onHover;
    private Consumer<UIComponent> onNotHover;
    private Consumer<UIComponent> onGuiScaleChange;
    private Consumer<UIComponent> onShow;
    private OnRender onRender;
    private boolean initialized = false;

    private void initialize() {
        if (!initialized) {
            initialized = true;
            init();
        }
    }

    // If you need to access the component when the event handler is initialized, override this method
    // (It is not safe to access the component in the constructor, because the component may not be initialized yet)
    protected void init() { }

    public ComponentEventHandler setComponent(UIComponent component) {
        this.component = component;
        initialize();
        return this;
    }

    public ComponentEventHandler onClick(Consumer<UIComponent> onClick) {
        this.onClick = onClick;
        return this;
    }

    public ComponentEventHandler onHover(Consumer<UIComponent> onHover) {
        this.onHover = onHover;
        return this;
    }

    public ComponentEventHandler onNotHover(Consumer<UIComponent> onNotHover) {
        this.onNotHover = onNotHover;
        return this;
    }

    public ComponentEventHandler onGuiScaleChange(Consumer<UIComponent> onGuiScaleChange) {
        this.onGuiScaleChange = onGuiScaleChange;
        return this;
    }

    public ComponentEventHandler onRender(OnRender onRender) {
        this.onRender = onRender;
        return this;
    }

    public ComponentEventHandler onShow(Consumer<UIComponent> onShow) {
        this.onShow = onShow;
        return this;
    }

    public void invokeOnClick() {
        if (onClick != null)
            onClick.accept(component);
    }

    public void invokeOnHover() {
        if (onHover != null)
            onHover.accept(component);
    }

    public void invokeOnNotHover() {
        if (onNotHover != null)
            onNotHover.accept(component);
    }

    public void invokeOnGuiScaleChange() {
        if (onGuiScaleChange != null)
            onGuiScaleChange.accept(component);
    }

    public void invokeOnRender(float partialTicks) {
        if (onRender != null)
            onRender.onRender(component, partialTicks);
    }

    public void invokeOnShow() {
        if (onShow != null)
            onShow.accept(component);
    }

    public interface OnRender {
        void onRender(UIComponent component, float partialTicks);
    }
}
