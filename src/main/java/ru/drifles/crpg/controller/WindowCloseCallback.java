package ru.drifles.crpg.controller;

import org.lwjgl.glfw.GLFWWindowCloseCallback;

public class WindowCloseCallback extends GLFWWindowCloseCallback {
    @Override
    public void invoke(long window) {
        System.out.println("WINDOW SHOULD BE CLOSED");
    }
}
