package voxelgame.world;

import voxelgame.core.Window;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class World {
    private static final int RENDER_DISTANCE = 2;

    private int chunkSizeX;
    private int chunkSizeY;
    private int chunkSizeZ;

    private ChunkRenderer chunkRenderer;

    private Map<Long, Chunk> loadedChunks;
    private Map<Long, Chunk> unloadedChunks;

    public World(int chunkSizeX, int chunkSizeY, int chunkSizeZ) {
        this.chunkSizeX = chunkSizeX;
        this.chunkSizeY = chunkSizeY;
        this.chunkSizeZ = chunkSizeZ;
        this.loadedChunks = new HashMap<>();
        this.unloadedChunks = new HashMap<>();
        this.chunkRenderer = new ChunkRenderer();
    }

    public void update(float deltaTime) {
        loadChunksInPlayerRange();
        unloadChunksOutsideOfPlayerRange();

        List<Chunk> chunksToLoad = new ArrayList<>();
        for (Chunk chunk : loadedChunks.values()) {
            chunksToLoad.add(chunk);
        }

        chunkRenderer.renderChunks(chunksToLoad);
    }

    public Chunk loadChunk(int chunkX, int chunkZ) {
        long chunkKey = getChunkKey(chunkX, chunkZ);

        if (loadedChunks.containsKey(chunkKey)) {
            return loadedChunks.get(chunkKey);
        }

        if (unloadedChunks.containsKey(chunkKey)) {
            Chunk chunk = unloadedChunks.get(chunkKey);
            unloadedChunks.remove(chunkKey);
            loadedChunks.put(chunkKey, chunk);
            return chunk;
        }

        Chunk chunk = generateChunk(chunkX, chunkZ);
        loadedChunks.put(chunkKey, chunk);
        return chunk;
    }

    public void unloadChunk(int chunkX, int chunkZ) {
        long chunkKey = getChunkKey(chunkX, chunkZ);
        Chunk chunk = loadedChunks.get(chunkKey);
        if (chunk != null) {
            saveChunk(chunk);
            loadedChunks.remove(chunkKey);
            unloadedChunks.put(chunkKey, chunk);
        }
    }

    public void loadChunksInPlayerRange() {
        int playerChunkX = (int) Window.getCurrentScene().getCamera().getPosition().x / chunkSizeX;
        int playerChunkZ = (int) Window.getCurrentScene().getCamera().getPosition().z / chunkSizeZ;

        for (int x = playerChunkX - RENDER_DISTANCE; x <= playerChunkX + RENDER_DISTANCE; x++) {
            for (int z = playerChunkZ - RENDER_DISTANCE; z <= playerChunkZ + RENDER_DISTANCE; z++) {
                if (!loadedChunks.containsKey(getChunkKey(x, z))) {
                    loadChunk(x, z);
                }
            }
        }
    }

    public void unloadChunksOutsideOfPlayerRange() {
        int playerChunkX = (int) Window.getCurrentScene().getCamera().getPosition().x / chunkSizeX;
        int playerChunkZ = (int) Window.getCurrentScene().getCamera().getPosition().z / chunkSizeZ;

        List<Chunk> chunksOutsideRenderDistance = new ArrayList<>();

        for (Chunk chunk : loadedChunks.values()) {
            int chunkX = chunk.getPositionX();
            int chunkZ = chunk.getPositionZ();

            if (Math.abs(chunkX - playerChunkX) > RENDER_DISTANCE || Math.abs(chunkZ - playerChunkZ) > RENDER_DISTANCE) {
                chunksOutsideRenderDistance.add(chunk);
            }
        }

        for (Chunk chunk : chunksOutsideRenderDistance) {
            int chunkX = chunk.getPositionX();
            int chunkZ = chunk.getPositionZ();
            unloadChunk(chunkX, chunkZ);
        }
    }

    public void saveChunk(Chunk chunk) {
        // TODO: Implement chunk saving
    }

    public Block getBlock(int blockX, int blockY, int blockZ) {
        int chunkX = blockX / chunkSizeX;
        int chunkZ = blockZ / chunkSizeZ;
        Chunk chunk = getChunk(chunkX, chunkZ);

        if (chunk != null) {
            int localX = blockX % chunkSizeX;
            int localY = blockY;
            int localZ = blockZ % chunkSizeZ;
            return chunk.getBlock(localX, localY, localZ);
        }

        // Return a default block if the chunk is not loaded
        return new Block(BlockType.AIR);
    }

    public void setBlock(int blockX, int blockY, int blockZ, Block block) {
        int chunkX = blockX / chunkSizeX;
        int chunkZ = blockZ / chunkSizeZ;
        Chunk chunk = getChunk(chunkX, chunkZ);

        if (chunk != null) {
            int localX = blockX % chunkSizeX;
            int localY = blockY;
            int localZ = blockZ % chunkSizeZ;
            chunk.setBlock(localX, localY, localZ, block);
        }
    }

    private Chunk getChunk(int chunkX, int chunkZ) {
        // Returns null if chunk is unloaded
        long chunkKey = getChunkKey(chunkX, chunkZ);
        return loadedChunks.get(chunkKey);
    }

    private long getChunkKey(int chunkX, int chunkZ) {
        return ((long) chunkX << 32) + chunkZ;
    }

    private Chunk generateChunk(int chunkX, int chunkZ) {
        Chunk chunk = new Chunk(chunkX, chunkZ, chunkSizeX, chunkSizeY, chunkSizeZ);

        // TODO: Implement world gen

        return chunk;
    }
}