package team.lodestar.sage.client.gui.effect;

public interface ComposableEffect {
    default boolean isAnimationDone() {
        return getAnimationProgress() >= 1f;
    }

    float getAnimationProgress();
}
