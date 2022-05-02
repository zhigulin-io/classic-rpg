package ru.drifles.crpg;

import ru.drifles.crpg.callback.ErrorCallback;
import ru.drifles.crpg.callback.KeyCallback;
import ru.drifles.crpg.callback.MouseButtonCallback;
import ru.drifles.crpg.callback.WindowCloseCallback;
import ru.drifles.crpg.common.Properties;
import ru.drifles.crpg.object.world.World;

import java.util.logging.Logger;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.GL_PROGRAM_POINT_SIZE;

public class ClassicRPG {
    private static final Logger LOG = Logger.getLogger(ClassicRPG.class.getName());

    private static final ClassicRPG instance = new ClassicRPG();

    public static void main(String[] args) {
        instance.launch();
    }

    public static ClassicRPG getInstance() {
        return instance;
    }

    private final World world;
    private final long window;

    public World getWorld() {
        return world;
    }

    private ClassicRPG() {
        this.window = createWindow();
        configOpenGLFeatures();

        this.world = new World("/level1.world");
        registerCallbacks();
    }

    private void launch() {
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT);

            world.draw();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    private long createWindow() {
        if (!glfwInit()) {
            LOG.severe("Could not initialize GLFW");
            System.exit(-1);
        }

        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

        long window = glfwCreateWindow(Properties.windowWidth, Properties.windowHeight, Properties.windowTitle, 0, 0);
        if (window == 0) {
            LOG.severe("Could not create GLFW window");
            glfwTerminate();
            System.exit(-1);
        }

        glfwMakeContextCurrent(window);

        if (Properties.vsync)
            glfwSwapInterval(1);

        return window;
    }

    private void configOpenGLFeatures() {
        createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_PROGRAM_POINT_SIZE);

        glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
    }

    private void registerCallbacks() {
        glfwSetErrorCallback(new ErrorCallback());
        glfwSetWindowCloseCallback(window, new WindowCloseCallback());
        glfwSetKeyCallback(window, new KeyCallback());
        glfwSetMouseButtonCallback(window, new MouseButtonCallback());
    }
}
