package voxelgame.core;

public abstract class Scene {
    protected Camera camera;

    public Scene() {

    }

    public void init() {

    }

    public abstract void update(float deltaTime);

    public Camera getCamera() {
        return this.camera;
    }
}
