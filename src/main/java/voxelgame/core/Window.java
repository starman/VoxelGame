package voxelgame.core;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import voxelgame.scenes.GameScene;
import voxelgame.scenes.MainMenuScene;
import voxelgame.utils.Time;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width;
    private int height;
    private String title;
    private long glfwWindow;

    private static Window window = null;

    private static Scene currentScene;

    private Window () {
        this.width = 1920;
        this.height = 1080;
        this.title = "VOXEL GAME";
    }

    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new MainMenuScene();
                currentScene.init();
                break;
            case 1:
                currentScene = new GameScene();
                currentScene.init();
                break;
            default:
                assert false : "Unknown scene '" + newScene + "'";
                break;
        }
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }

        return Window.window;
    }

    public static Scene getCurrentScene() {
        return get().currentScene;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        cleanup();
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create GLFW window");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetCursorEnterCallback(glfwWindow, MouseListener::mouseEnterCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        glfwSetInputMode(glfwWindow, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1); // enable v-sync

        glfwShowWindow(glfwWindow);

        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);

        Window.changeScene(1); // game scene
    }

    public void loop() {
        float beginTime = Time.getTime();
        float endTime;
        float deltaTime = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();

            glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);
            glClear(GL_DEPTH_BUFFER_BIT);

            if (deltaTime >= 0) {
                currentScene.update(deltaTime);
            }

            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();
            deltaTime = endTime - beginTime;
            beginTime = endTime;
        }
    }

    public void cleanup() {
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
