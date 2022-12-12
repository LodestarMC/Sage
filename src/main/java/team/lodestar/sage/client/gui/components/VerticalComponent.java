package team.lodestar.sage.client.gui.components;

public class VerticalComponent extends UIComponent {
    private int spacing;

    public VerticalComponent(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public UIComponent withChild(UIComponent component) {
        for (int i = 0; i < children.size(); i++)
            component.positionInfo.y += spacing + children.get(i).positionInfo.height;

        return super.withChild(component);
    }
}
