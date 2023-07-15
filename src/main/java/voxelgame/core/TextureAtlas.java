package voxelgame.core;

public class TextureAtlas {
    private int width, height;
    private int textureSize;

    public TextureAtlas(int width, int height, int textureSize) {
        this.width = width;
        this.height = height;
        this.textureSize = textureSize;
    }

    public float[] getTextureCoordinates(int textureIndex) {
        int numTexturesX = width / textureSize;
        int textureRow = textureIndex / numTexturesX;
        int textureColumn = textureIndex % numTexturesX;

        float left = (float) (textureColumn * textureSize) / width;
        float right = (float) ((textureColumn + 1) * textureSize) / width;
        float top = (float) (textureRow * textureSize) / height;
        float bottom = (float) ((textureRow + 1) * textureSize) / height;

        float[] textureCoordinates = {
                left, top,        // left top
                left, bottom,     // left bottom
                right, bottom,    // right bottom
                right, top        // right top
        };

        return textureCoordinates;
    }
}
