package ru.drifles.crpg.common;

public enum Color {
    BLUE(0.3f, 0.3f, 7.0f),
    RED(7.0f, 0.3f, 0.3f),
    WHITE(7.0f, 7.0f, 7.0f),
    BLACK(0.3f, 0.3f, 0.3f),
    GREEN(0.3f, 7.0f, 0.3f);

    private final float r;
    private final float g;
    private final float b;

    Color(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public float getR() {
        return r;
    }

    public float getG() {
        return g;
    }

    public float getB() {
        return b;
    }
}
