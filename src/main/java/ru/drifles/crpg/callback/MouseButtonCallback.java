package ru.drifles.crpg.callback;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import ru.drifles.crpg.ClassicRPG;
import ru.drifles.crpg.common.Camera;
import ru.drifles.crpg.common.Position;
import ru.drifles.crpg.common.Properties;
import ru.drifles.crpg.object.world.Tile;
import ru.drifles.crpg.object.world.Walker;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class MouseButtonCallback extends GLFWMouseButtonCallback {

    private static final double TILES_WIDTH = Camera.WIDTH / Properties.tileSize;
    private static final double TILES_HEIGHT = Camera.HEIGHT / Properties.tileSize;

    private final DoubleBuffer xPositionBuffer = BufferUtils.createDoubleBuffer(1);
    private final DoubleBuffer yPositionBuffer = BufferUtils.createDoubleBuffer(1);

    //private final Walker walker = ClassicRPG.getInstance().getWorld().getWalker();

    @Override
    public void invoke(long window, int button, int action, int mods) {
        var walker = ClassicRPG.getInstance().getWorld().getWalker();
        var tiles = ClassicRPG.getInstance().getWorld().getTiles();

        glfwGetCursorPos(window, xPositionBuffer, yPositionBuffer);

        if (action == GLFW_RELEASE) {
            var tileX = (int) (xPositionBuffer.get(0) / Properties.windowWidth * TILES_WIDTH);
            var tileY = (int) (yPositionBuffer.get(0) / Properties.windowHeight * TILES_HEIGHT);

            if (tiles[tileY][tileX].isPassable()) {

                switch (button) {
                    case GLFW_MOUSE_BUTTON_LEFT -> walker.setPosition(tileX, tileY);
                    case GLFW_MOUSE_BUTTON_RIGHT -> walker.setTarget(new Position(tileX, tileY));
                }

            }
        }
    }
}
