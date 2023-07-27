package voxelgame.scenes;

import org.joml.Vector2f;
import voxelgame.core.Camera;
import voxelgame.core.KeyListener;
import voxelgame.core.MouseListener;
import voxelgame.core.Window;
import voxelgame.core.Scene;
import voxelgame.world.*;

import static org.lwjgl.glfw.GLFW.*;

public class GameScene extends Scene {
    private static final float MOUSE_SENSITIVITY = 0.1f;

    private World world;

    public GameScene() {

    }

    @Override
    public void init() {
        this.camera = new Camera(Window.get().getWidth(), Window.get().getHeight());

        world = new World(16, 64, 16);
    }

    @Override
    public void update(float deltaTime) {
        MouseListener.input();

        // Mouse/camera movement
        Vector2f displVec = MouseListener.getDisplVec();
        camera.addRotation((float) Math.toRadians(-displVec.x * MOUSE_SENSITIVITY),
                (float) Math.toRadians(-displVec.y * MOUSE_SENSITIVITY));

        if (KeyListener.isKeyPressed(GLFW_KEY_W)) {
            camera.moveForward(deltaTime * 2.0f);
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_S)) {
            camera.moveBackwards(deltaTime * 2.0f);
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_A)) {
            camera.moveLeft(deltaTime * 2.0f);
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_D)) {
            camera.moveRight(deltaTime * 2.0f);
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
            camera.moveUp(deltaTime * 2.0f);
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            camera.moveDown(deltaTime * 2.0f);
        }

        world.update(deltaTime);
    }
}
