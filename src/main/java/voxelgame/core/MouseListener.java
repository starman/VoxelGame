package voxelgame.core;

import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener {
    private static final double CENTER_X = Window.get().getWidth() / 2.0;
    private static final double CENTER_Y = Window.get().getHeight() / 2.0;

    private static MouseListener instance;
    private Vector2f currentPos;
    private Vector2f displVec;
    private boolean inWindow;
    private boolean leftButtonPressed;
    private Vector2f previousPos;
    private boolean rightButtonPressed;

    private MouseListener() {
        previousPos = new Vector2f(-1, -1);
        currentPos = new Vector2f();
        displVec = new Vector2f();
        leftButtonPressed = false;
        rightButtonPressed = false;
        inWindow = false;
    }

    public static MouseListener get() {
        if (MouseListener.instance == null) {
            MouseListener.instance = new MouseListener();
        }

        return MouseListener.instance;
    }

    public static void mousePosCallback(long window, double xPos, double yPos) {
        get().currentPos.x = (float) xPos;
        get().currentPos.y = (float) yPos;
    }

    public static void mouseEnterCallback(long window, boolean entered) {
       get().inWindow = entered;
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        get().leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
        get().rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
    }

    public static Vector2f getCurrentPos() {
        return get().currentPos;
    }

    public static Vector2f getDisplVec() {
        return get().displVec;
    }

    public static void input() {
        get().displVec.x = 0;
        get().displVec.y = 0;

        if (get().previousPos.x > 0 && get().previousPos.y > 0 && get().inWindow) {
            double deltaX = get().currentPos.x - get().previousPos.x;
            double deltaY = get().currentPos.y - get().previousPos.y;

            if (deltaX != 0 || deltaY != 0) {
                double accumulatedX = get().currentPos.x - CENTER_X;
                double accumulatedY = get().currentPos.y - CENTER_Y;

                get().displVec.y = (float) -accumulatedX;
                get().displVec.x = (float) -accumulatedY;

                glfwSetCursorPos(Window.get().getWindowGLFW(), CENTER_X, CENTER_Y);
            }
        }

        get().previousPos.x = get().currentPos.x;
        get().previousPos.y = get().currentPos.y;
    }

    public static boolean isLeftButtonPressed() {
        return get().leftButtonPressed;
    }

    public static boolean isRightButtonPressed() {
        return get().rightButtonPressed;
    }
}
