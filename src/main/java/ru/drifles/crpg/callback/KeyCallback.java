package ru.drifles.crpg.callback;

import org.lwjgl.glfw.GLFWKeyCallback;
import ru.drifles.crpg.ClassicRPG;
import ru.drifles.crpg.gameobject.walker.Walker;

import static org.lwjgl.glfw.GLFW.*;

public class KeyCallback extends GLFWKeyCallback {
    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_ESCAPE) {
            glfwSetWindowShouldClose(window, true);
        } else if (key == GLFW_KEY_1) {
            ClassicRPG.getInstance().getWorld().getWalker().setRoutingType(Walker.RoutingType.A_STAR);
        } else if (key == GLFW_KEY_2) {
            ClassicRPG.getInstance().getWorld().getWalker().setRoutingType(Walker.RoutingType.BFS);
        } else if (key == GLFW_KEY_3) {
            ClassicRPG.getInstance().getWorld().getWalker().setRoutingType(Walker.RoutingType.DFS);
        } else if (key == GLFW_KEY_4) {
            ClassicRPG.getInstance().getWorld().getWalker().setRoutingType(Walker.RoutingType.DIJKSTRA);
        } else if (key == GLFW_KEY_0) {
            ClassicRPG.getInstance().getWorld().getWalker().setRoutingType(Walker.RoutingType.CUSTOM);
        }
    }
}
