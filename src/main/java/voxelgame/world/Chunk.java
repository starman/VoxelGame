package voxelgame.world;

public class Chunk {
    private Block[][][] blocks;
    private int sizeX;
    private int sizeY;
    private int sizeZ;
    private int positionX;
    private int positionZ;

    public Chunk(int positionX, int positionZ, int sizeX, int sizeY, int sizeZ) {
        this.positionX = positionX;
        this.positionZ = positionZ;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        blocks = new Block[sizeX][sizeY][sizeZ];
        // generateBlocks();
    }

    private void generateBlocks() {
        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                for (int z = 0; z < blocks[x][y].length; z++) {
                    BlockType type = BlockType.AIR;
                    blocks[x][y][z] = new Block(type);
                }
            }
        }
    }

    public Block getBlock(int x, int y, int z) {
        return blocks[x][y][z];
    }

    public void setBlock(int x, int y, int z, Block block) {
        if (x >= 0 && x < sizeX && y >= 0 && y < sizeY && z >= 0 && z < sizeZ) {
            blocks[x][y][z] = block;
        }
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionZ() {
        return positionZ;
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
