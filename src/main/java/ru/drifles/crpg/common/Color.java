package ru.drifles.crpg.common;

public enum Color {
    RED(1.0f, 0.0f, 0.0f),
    GREEN(0.0f, 1.0f, 0.0f),
    BLUE(0.0f, 0.0f, 1.0f),
    WHITE(1.0f, 1.0f, 1.0f),
    BLACK(0.0f, 0.0f, 0.0f);

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
