package ru.drifles.crpg;

import ru.drifles.crpg.callback.ErrorCallback;
import ru.drifles.crpg.callback.KeyCallback;
import ru.drifles.crpg.callback.MouseButtonCallback;
import ru.drifles.crpg.callback.WindowCloseCallback;
import ru.drifles.crpg.common.Time;
import ru.drifles.crpg.config.Properties;
import ru.drifles.crpg.gameobject.World;
import ru.drifles.crpg.renderer.TileRenderer;
import ru.drifles.crpg.renderer.WalkerRenderer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.GL_PROGRAM_POINT_SIZE;

public class ClassicRPG {
    private static final Logger LOG = Logger.getLogger(ClassicRPG.class.getName());

    private static final ClassicRPG instance;

    static {
        try {
            instance = new ClassicRPG();
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        instance.launch();
    }

    public static ClassicRPG getInstance() {
        return instance;
    }

    private final long window;
    private final World world;

    public World getWorld() {
        return world;
    }

    private ClassicRPG() throws URISyntaxException, IOException {
        this.window = createWindow();
        configOpenGLFeatures();

        this.world = new World();
        registerCallbacks();
    }

    private void launch() {
        while (!glfwWindowShouldClose(window)) {
            var beginTime = glfwGetTime();
            glClear(GL_COLOR_BUFFER_BIT);

            for (int y = 0; y < world.getLand().getGraph().length; y++) {
                for (int x = 0; x < world.getLand().getGraph()[y].length; x++) {
                    TileRenderer.render(world.getLand().getGraph()[y][x]);
                }
            }

            WalkerRenderer.render(world.getWalker());

            glfwSwapBuffers(window);
            var endTime = glfwGetTime();
            Time.delta = endTime - beginTime;
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
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

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

        glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
    }

    private void registerCallbacks() {
        glfwSetErrorCallback(new ErrorCallback());
        glfwSetWindowCloseCallback(window, new WindowCloseCallback());
        glfwSetKeyCallback(window, new KeyCallback());
        glfwSetMouseButtonCallback(window, new MouseButtonCallback());
    }
}
