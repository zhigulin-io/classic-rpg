package ru.drifles.crpg.common;

import org.joml.Matrix4f;

public final class Camera {
    private static int tilesNumber = 16;
    private static final float CAMERA_OFFSET = -0.5f;
    private static final float LEFT_POSITION = CAMERA_OFFSET;
    private static final float TOP_POSITION = CAMERA_OFFSET;
    private static final float RIGHT_POSITION = tilesNumber + CAMERA_OFFSET;
    private static final float BOTTOM_POSITION = tilesNumber + CAMERA_OFFSET;
    private static final float NEAR_POSITION = -1;
    private static final float FAR_POSITION = 1;

    public static final Matrix4f PROJECTION = new Matrix4f().ortho(
            LEFT_POSITION,
            RIGHT_POSITION,
            BOTTOM_POSITION,
            TOP_POSITION,
            NEAR_POSITION,
            FAR_POSITION
    );

    public static void setTilesNumber(int number) {
        tilesNumber = number;
        PROJECTION.setOrtho(
                LEFT_POSITION,
                number + CAMERA_OFFSET,
                number + CAMERA_OFFSET,
                TOP_POSITION,
                NEAR_POSITION,
                FAR_POSITION
        );
    }

    public static int getTilesNumber() {
        return tilesNumber;
    }

    private Camera() {  }
}
