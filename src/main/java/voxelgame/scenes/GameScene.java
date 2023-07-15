package voxelgame.scenes;

import org.joml.Vector2f;
import voxelgame.core.Camera;
import voxelgame.core.KeyListener;
import voxelgame.core.MouseListener;
import voxelgame.core.Window;
import voxelgame.core.Scene;
import voxelgame.world.Chunk;
import voxelgame.world.ChunkRenderer;

import static org.lwjgl.glfw.GLFW.*;

public class GameScene extends Scene {
    private ChunkRenderer chunkRenderer;
    private Chunk chunk;

    public GameScene() {

    }

    @Override
    public void init() {
        this.camera = new Camera(Window.get().getWidth(), Window.get().getHeight());

        chunkRenderer = new ChunkRenderer();
        chunk = new Chunk(16);
    }

    @Override
    public void update(float deltaTime) {
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

        if (MouseListener.isDragging()) {
            Vector2f displayVec = MouseListener.getDisplayVec();
            camera.addRotation((float) Math.toRadians(-displayVec.x * 0.1f),
                    (float) Math.toRadians(-displayVec.y * 0.1f));
        }

        chunkRenderer.renderChunk(chunk);
    }
}
