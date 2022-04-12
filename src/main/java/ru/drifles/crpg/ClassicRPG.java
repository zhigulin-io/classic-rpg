package ru.drifles.crpg;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWWindowCloseCallback;
import ru.drifles.crpg.callback.ErrorCallback;
import ru.drifles.crpg.callback.KeyCallback;
import ru.drifles.crpg.callback.WindowCloseCallback;
import ru.drifles.crpg.object.Triangle;

import java.util.logging.Logger;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.*;

public class ClassicRPG {
    private static final Logger LOG = Logger.getLogger(ClassicRPG.class.getName());
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    private static final String TITLE = "Classic RPG";
    private static final GLFWErrorCallback ERROR_CALLBACK = new ErrorCallback();
    private static final GLFWWindowCloseCallback WINDOW_CLOSE_CALLBACK = new WindowCloseCallback();
    private static final GLFWKeyCallback KEY_CALLBACK = new KeyCallback();

    private long window;

    public static void main(String[] args) {
        var game = new ClassicRPG();
        game.launch();
    }

    private ClassicRPG() {
        if (!glfwInit()) {
            LOG.severe("Could not initialize GLFW");
            return;
        }

        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

        window = glfwCreateWindow(WIDTH, HEIGHT, TITLE, 0, 0);
        if (window == 0) {
            LOG.severe("Could not create GLFW window");
            glfwTerminate();
            return;
        }

        glfwSetErrorCallback(ERROR_CALLBACK);
        glfwSetWindowCloseCallback(window, WINDOW_CLOSE_CALLBACK);
        glfwSetKeyCallback(window, KEY_CALLBACK);

        glfwMakeContextCurrent(window);

        glfwSwapInterval(1);

        createCapabilities();
    }

    private void launch() {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        Triangle t = new Triangle();

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT);

            t.draw();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
    }
}
