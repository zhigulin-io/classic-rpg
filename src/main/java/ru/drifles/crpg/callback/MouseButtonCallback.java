package ru.drifles.crpg.callback;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import ru.drifles.crpg.ClassicRPG;
import ru.drifles.crpg.common.Camera;
import ru.drifles.crpg.config.Properties;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class MouseButtonCallback extends GLFWMouseButtonCallback {

    private static final double TILES_WIDTH = Camera.WIDTH;
    private static final double TILES_HEIGHT = Camera.HEIGHT;

    private final DoubleBuffer xPositionBuffer = BufferUtils.createDoubleBuffer(1);
    private final DoubleBuffer yPositionBuffer = BufferUtils.createDoubleBuffer(1);

    @Override
    public void invoke(long window, int button, int action, int mods) {
        var walker = ClassicRPG.getInstance().getWorld().getWalker();
        var graph = ClassicRPG.getInstance().getWorld().getLand().getGraph();

        glfwGetCursorPos(window, xPositionBuffer, yPositionBuffer);

        if (action == GLFW_RELEASE) {
            var tileX = (int) (xPositionBuffer.get(0) / Properties.windowWidth * TILES_WIDTH);
            var tileY = (int) (yPositionBuffer.get(0) / Properties.windowHeight * TILES_HEIGHT);
            var tile = graph[tileY][tileX];

            if (tile.passable()) {
                switch (button) {
                    case GLFW_MOUSE_BUTTON_LEFT -> walker.setPosition(tile.position());
                    case GLFW_MOUSE_BUTTON_RIGHT -> walker.setTarget(tile.position());
                }
            }
        }
    }
}
