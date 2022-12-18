package team.lodestar.sage.client.gui.effect;

public class Easings {
    public static float linear(float a, float b, float t) {
        return a + (b - a) * t;
    }

    public static float easeOutQuad(float a, float b, float t) {
        return a + (b - a) * (1 - (float)Math.pow(1 - t, 2));
    }

    public static float easeOutCubic(float a, float b, float t) {
        return a + (b - a) * (1 - (float)Math.pow(1 - t, 3));
    }

    public static float easeOutQuart(float a, float b, float t) {
        return a + (b - a) * (1 - (float)Math.pow(1 - t, 4));
    }

    public static float easeOutQuint(float a, float b, float t) {
        return a + (b - a) * (1 - (float)Math.pow(1 - t, 5));
    }
}
