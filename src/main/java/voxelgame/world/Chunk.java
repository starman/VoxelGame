package voxelgame.world;

public class Chunk {
    private Block[][][] blocks;
    private int size;

    public Chunk(int size) {
        this.size = size;
        blocks = new Block[size][size][size];
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

    public int getSize() {
        return this.size;
    }
}
