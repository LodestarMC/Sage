package team.lodestar.sage.client.gui.components;

public class HorizontalComponent extends UIComponent {
    private int spacing = 0;

    public HorizontalComponent(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public UIComponent withChild(UIComponent component) {
        for (int i = 0; i < children.size(); i++)
            component.positionInfo.x += spacing + children.get(i).positionInfo.width + children.get(i).positionInfo.paddingX;

        return super.withChild(component);
    }
}
