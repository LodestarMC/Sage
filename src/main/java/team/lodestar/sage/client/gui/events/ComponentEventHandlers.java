package team.lodestar.sage.client.gui.events;

import team.lodestar.sage.client.gui.components.UIComponent;

import java.util.ArrayList;
import java.util.List;

public class ComponentEventHandlers {
    private List<OnComponentClick> onClickHandlers = new ArrayList<>();
    private List<OnComponentHover> onHoverHandlers = new ArrayList<>();

    public void addOnClickHandler(OnComponentClick click) {
        onClickHandlers.add(click);
    }

    public void invokeOnClickHandlers(UIComponent component) {
        onClickHandlers.forEach(handler -> handler.onClick(component));
    }

    public void addOnHoverHandler(OnComponentHover hover) {
        onHoverHandlers.add(hover);
    }

    public void invokeOnHoverHandlers(UIComponent component) {
        onHoverHandlers.forEach(handler -> handler.onHover(component));
    }

    public interface OnComponentClick {
        void onClick(UIComponent component);
    }

    public interface OnComponentHover {
        void onHover(UIComponent component);
    }
}