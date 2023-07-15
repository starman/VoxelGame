package voxelgame.world;

public class Chunk {
    private Block[][][] blocks;
    private int sizeX;
    private int sizeY;
    private int sizeZ;

    public Chunk(int sizeX, int sizeY, int sizeZ) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        blocks = new Block[sizeX][sizeY][sizeZ];
        generateBlocks();
    }

    private void generateBlocks() {
        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                for (int z = 0; z < blocks[x][y].length; z++) {
                    BlockType type = BlockType.GRASS; // world gen
                    blocks[x][y][z] = new Block(type);
                }
            }
        }
    }

    public Block getBlock(int x, int y, int z) {
        return blocks[x][y][z];
    }

    public int getSizeX() {
        return this.sizeX;
    }

    public int getSizeY() {
        return this.sizeY;
    }

    public int getSizeZ() {
        return this.sizeZ;
    }
}
