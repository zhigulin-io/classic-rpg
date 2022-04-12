package ru.drifles.crpg.callback;

import org.lwjgl.glfw.GLFWErrorCallback;

public class ErrorCallback extends GLFWErrorCallback {
    @Override
    public void invoke(int error, long description) {
        throw new RuntimeException("GLFW Error(" + error + ") occurred: " + getDescription(description));
    }
}
