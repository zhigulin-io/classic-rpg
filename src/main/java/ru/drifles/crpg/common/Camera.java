package ru.drifles.crpg.common;

import org.joml.Matrix4f;

public class Camera {
    private static final float LEFT_POSITION = 0;
    private static final float TOP_POSITION = 0;
    private static final float RIGHT_POSITION = 160;
    private static final float BOTTOM_POSITION = 160;
    private static final float NEAR_POSITION = -1;
    private static final float FAR_POSITION = 1;

    public static final float WIDTH = RIGHT_POSITION - LEFT_POSITION;
    public static final float HEIGHT = BOTTOM_POSITION - TOP_POSITION;

    public static final Matrix4f PROJECTION = new Matrix4f().ortho(
            LEFT_POSITION,
            RIGHT_POSITION,
            BOTTOM_POSITION,
            TOP_POSITION,
            NEAR_POSITION,
            FAR_POSITION
    );
}
