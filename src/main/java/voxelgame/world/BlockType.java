package voxelgame.world;

public enum BlockType {
    AIR,
    GRASS(1, 2, 3),
    DIRT(3, 3, 3),
    STONE(0, 0, 0),
    WOOD;

    private int sideTextureIndex;
    private int topTextureIndex;
    private int bottomTextureIndex;

    BlockType() {
    }

    BlockType(int sideTextureIndex, int topTextureIndex, int bottomTextureIndex) {
        this.sideTextureIndex = sideTextureIndex;
        this.topTextureIndex = topTextureIndex;
        this.bottomTextureIndex = bottomTextureIndex;
    }

    public int getTopTextureIndex() {
        return topTextureIndex;
    }

    public int getBottomTextureIndex() {
        return bottomTextureIndex;
    }

    public int getSideTextureIndex() {
        return sideTextureIndex;
    }
}
