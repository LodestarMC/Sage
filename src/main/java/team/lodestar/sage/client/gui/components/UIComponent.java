package team.lodestar.sage.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import team.lodestar.sage.client.gui.PositionInfo;
import team.lodestar.sage.client.gui.events.ComponentEventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class UIComponent {
    protected PositionInfo positionInfo = new PositionInfo();
    protected List<UIComponent> children = new ArrayList<>();
    protected UIComponent parent; // can be null, if this is the root parent
    protected List<ComponentEventHandler> eventHandlers = new ArrayList<>();

    private float cachedX;
    private float cachedY;
    private float partialTicks;

    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTicks) {
        partialTicks = pPartialTicks;

        if (containsPoint(pMouseX, pMouseY))
            eventHandlers.forEach(handler -> handler.invokeOnHover());
        else
            eventHandlers.forEach(handler -> handler.invokeOnNotHover());

        renderComponent(pPoseStack, pMouseX, pMouseY, pPartialTicks);

        for (UIComponent child : children)
            child.render(pPoseStack, pMouseX, pMouseY, pPartialTicks);
    }

    // Override this to render your component
    protected void renderComponent(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) { }

    public UIComponent withChild(UIComponent component) {
        component.parent = this;
        component.positionInfo.relative = true;
        children.add(component);
        return this;
    }

    public UIComponent at(float x, float y) {
        positionInfo.x = x;
        positionInfo.y = y;
        positionInfo.relative = true;

        return this;
    }

    public UIComponent atDirect(float x, float y) {
        positionInfo.x = x;
        positionInfo.y = y;
        positionInfo.relative = false;

        return this;
    }

    public UIComponent width(float width) {
        positionInfo.width = width;
        return this;
    }

    public UIComponent height(float height) {
        positionInfo.height = height;
        return this;
    }

    public UIComponent dimensions(float width, float height) {
        positionInfo.width = width;
        positionInfo.height = height;
        return this;
    }

    public UIComponent paddingLeft(float x) {
        positionInfo.paddingX += x;
        return this;
    }

    public UIComponent paddingRight(float x) {
        positionInfo.paddingX -= x;
        return this;
    }

    public UIComponent paddingUp(float y) {
        positionInfo.paddingY += y;
        return this;
    }

    public UIComponent paddingDown(float y) {
        positionInfo.paddingY -= y;
        return this;
    }

    public UIComponent paddingTopLeft(float p) {
        paddingLeft(p);
        paddingUp(p);
        return this;
    }

    public UIComponent onClick(Consumer<UIComponent> handler) {
        eventHandlers.add(new ComponentEventHandler().setComponent(this).onClick(handler));
        return this;
    }

    public UIComponent onHover(Consumer<UIComponent> handler) {
        eventHandlers.add(new ComponentEventHandler().setComponent(this).onHover(handler));
        return this;
    }

    public UIComponent onNotHover(Consumer<UIComponent> handler) {
        eventHandlers.add(new ComponentEventHandler().setComponent(this).onNotHover(handler));
        return this;
    }

    public UIComponent addHandler(ComponentEventHandler handler) {
        handler.setComponent(this);
        eventHandlers.add(handler);
        return this;
    }

    public void receiveMouseRelease(double mouseX, double mouseY) {
        if (containsPoint(mouseX, mouseY))
            eventHandlers.forEach(handler -> handler.invokeOnClick());

        for (UIComponent component : children)
            component.receiveMouseRelease(mouseX, mouseY);
    }

    private boolean containsPoint(double x, double y) {
        return x > getAbsoluteX() && x < getAbsoluteX() + getWidth() && y > getAbsoluteY() && y < getAbsoluteY() + getHeight();
    }

    private float calculateX() {
        float x = positionInfo.x;

        if (positionInfo.relative && parent != null)
            x += parent.getAbsoluteX() + positionInfo.paddingX;

        cachedX = x;
        return x;
    }

    private float calculateY() {
        float y = positionInfo.y;

        if (positionInfo.relative && parent != null)
            y += parent.getAbsoluteY() + positionInfo.paddingY;

        cachedY = y;
        return y;
    }

    public float getAbsoluteX() {
        return cachedX;
    }

    public float getAbsoluteY() {
        return cachedY;
    }

    public float getX() {
        return positionInfo.x;
    }

    public float getY() {
        return positionInfo.y;
    }

    public float getWidth() {
        return positionInfo.width;
    }

    public float getHeight() {
        return positionInfo.height;
    }

    public double mouseX() {
        Minecraft mc = Minecraft.getInstance();
        return mc.mouseHandler.xpos() * (double)mc.getWindow().getGuiScaledWidth() / mc.getWindow().getScreenWidth();
    }

    public double mouseY() {
        Minecraft mc = Minecraft.getInstance();
        return mc.mouseHandler.ypos() * (double)mc.getWindow().getGuiScaledHeight() / mc.getWindow().getScreenHeight();
    }

    public float partialTicks() {
        return partialTicks;
    }

    // NOTE: if for some reason you are dynamically changing the positions of a component,
    // you can call this method to make it recalculate its position relative to its parent (unless thats not needed)
    // this also tells all children of this component to recalculate as well, so on massive UI trees, it is expensive
    public void recalculatePosition() {
        cachedX = calculateX();
        cachedY = calculateY();

        for (UIComponent child : children)
            child.recalculatePosition();
    }
}
