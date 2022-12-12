package team.lodestar.sage.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import team.lodestar.sage.client.gui.PositionInfo;
import team.lodestar.sage.client.gui.events.OnComponentClick;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class UIComponent {
    protected PositionInfo positionInfo = new PositionInfo();
    protected List<UIComponent> children = new ArrayList<>();
    protected UIComponent parent; // can be null, if this is the root parent
    protected OnComponentClick onClick;
    protected Consumer<UIComponent> onHover;

    private int cachedX;
    private int cachedY;

    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTicks) {
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

    public UIComponent at(int x, int y) {
        positionInfo.x = x;
        positionInfo.y = y;
        positionInfo.relative = true;

        return this;
    }

    public UIComponent atDirect(int x, int y) {
        positionInfo.x = x;
        positionInfo.y = y;
        positionInfo.relative = false;

        return this;
    }

    public UIComponent width(int width) {
        positionInfo.width = width;
        return this;
    }

    public UIComponent height(int height) {
        positionInfo.height = height;
        return this;
    }

    public UIComponent dimensions(int width, int height) {
        positionInfo.width = width;
        positionInfo.height = height;
        return this;
    }

    public UIComponent paddingLeft(int x) {
        positionInfo.paddingX += x;
        return this;
    }

    public UIComponent paddingRight(int x) {
        positionInfo.paddingX -= x;
        return this;
    }

    public UIComponent paddingUp(int y) {
        positionInfo.paddingY += y;
        return this;
    }

    public UIComponent paddingDown(int y) {
        positionInfo.paddingY -= y;
        return this;
    }

    public UIComponent paddingTopLeft(int p) {
        paddingLeft(p);
        paddingUp(p);
        return this;
    }

    public UIComponent onClick(OnComponentClick handler) {
        onClick = handler;
        return this;
    }

    public UIComponent onHover(Consumer<UIComponent> handler) {
        onHover = handler;
        return this;
    }

    public void receiveMouseRelease(double mouseX, double mouseY) {
        if (containsPoint(mouseX, mouseY) && onClick != null)
            onClick.onClick(this);

        for (UIComponent component : children)
            component.receiveMouseRelease(mouseX, mouseY);
    }

    private boolean containsPoint(double x, double y) {
        return x > getX() && x < getX() + getWidth() && y > getY() && y < getY() + getHeight();
    }

    private int calculateX() {
        int x = positionInfo.x;

        if (positionInfo.relative && parent != null)
            x += parent.getX() + positionInfo.paddingX;

        cachedX = x;
        return x;
    }

    private int calculateY() {
        int y = positionInfo.y;

        if (positionInfo.relative && parent != null)
            y += parent.getY() + positionInfo.paddingY;

        cachedY = y;
        return y;
    }

    public int getX() {
        return cachedX;
    }

    public int getY() {
        return cachedY;
    }

    public int getWidth() {
        return positionInfo.width;
    }

    public int getHeight() {
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
