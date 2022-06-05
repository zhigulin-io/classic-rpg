package ru.drifles.crpg.controller;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import ru.drifles.crpg.ClassicRPG;
import ru.drifles.crpg.view.Camera;
import ru.drifles.crpg.view.Properties;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class MouseButtonCallback extends GLFWMouseButtonCallback {
    private final DoubleBuffer xPositionBuffer = BufferUtils.createDoubleBuffer(1);
    private final DoubleBuffer yPositionBuffer = BufferUtils.createDoubleBuffer(1);

    @Override
    public void invoke(long window, int button, int action, int mods) {
        var walker = ClassicRPG.getInstance().getWorld().getWalker();
        var graph = ClassicRPG.getInstance().getWorld().getLand().getGraph();

        glfwGetCursorPos(window, xPositionBuffer, yPositionBuffer);

        if (action == GLFW_RELEASE) {
            var tileX = (int) (xPositionBuffer.get(0) / Properties.windowWidth * Camera.getTilesNumber());
            var tileY = (int) (yPositionBuffer.get(0) / Properties.windowHeight * Camera.getTilesNumber());

            if (tileY < graph.length && tileX < graph[tileY].length) {
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
}
