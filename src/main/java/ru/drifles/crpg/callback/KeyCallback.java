package ru.drifles.crpg.callback;

import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;

public class KeyCallback extends GLFWKeyCallback {
    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_ESCAPE) {
            glfwSetWindowShouldClose(window, true);
        }
    }
}
