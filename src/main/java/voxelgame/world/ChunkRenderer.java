package voxelgame.world;

import org.lwjgl.BufferUtils;
import voxelgame.core.Window;
import voxelgame.core.Shader;
import voxelgame.core.Texture;
import voxelgame.core.TextureAtlas;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL33.*;

public class ChunkRenderer {
    private int vaoID;
    private int vboID;
    private int eboID;
    private int vertexCount;
    private Shader shaderProgram;
    private Texture texture;
    private TextureAtlas textureAtlas;

    private static final float[] CUBE_POS_FRONT = {
            -0.5f, 0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
    };

    private static final float[] CUBE_POS_BACK = {
            -0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
    };

    private static final float[] CUBE_POS_RIGHT = {
            0.5f, 0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
    };

    private static final float[] CUBE_POS_LEFT = {
            -0.5f, 0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,
    };

    private static final float[] CUBE_POS_TOP = {
            -0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, -0.5f,
    };

    private static final float[] CUBE_POS_BOTTOM = {
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, -0.5f,
    };

    private static final int[] CUBE_INDICES = {
            // Front face
            0, 1, 2,
            2, 3, 0,
            // Back face
            4, 5, 6,
            6, 7, 4,
            // Right face
            8, 9, 10,
            10, 11, 8,
            // Left face
            12, 13, 14,
            14, 15, 12,
            // Top face
            16, 17, 18,
            18, 19, 16,
            // Bottom face
            20, 21, 22,
            22, 23, 20
    };

    public ChunkRenderer() {
        initShaders();
        initBuffers();
        initTextureAtlas();
    }

    private void initShaders() {
        List<Shader.ShaderModuleData> shaderModuleDataList = new ArrayList<>();
        shaderModuleDataList.add(new Shader.ShaderModuleData("resources/shaders/default.vert", GL_VERTEX_SHADER));
        shaderModuleDataList.add(new Shader.ShaderModuleData("resources/shaders/default.frag", GL_FRAGMENT_SHADER));
        shaderProgram = new Shader(shaderModuleDataList);
    }

    private void initTextureAtlas() {
        texture = new Texture("resources/textures/terrain.png");
        textureAtlas = new TextureAtlas(256, 256, 16);
    }

    private void initBuffers() {
        vaoID = glGenVertexArrays();
        vboID = glGenBuffers();
        eboID = glGenBuffers();
        glBindVertexArray(vaoID);

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);

        int positionSize = 3;
        int texCoordSize = 2;
        int vertexSizeBytes = (positionSize + texCoordSize) * Float.BYTES;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, texCoordSize, GL_FLOAT, false, vertexSizeBytes, positionSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindVertexArray(0);
    }

    private void generateMesh(Chunk chunk) {
        List<Float> vertices = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        int vertexOffset = 0; // Track the offset for each face - needed for indices

        for (int x = 0; x < chunk.getSize(); x++) {
            for (int y = 0; y < chunk.getSize(); y++) {
                for (int z = 0; z < chunk.getSize(); z++) {
                    Block block = chunk.getBlock(x, y, z);
                    if (block.getType() != BlockType.AIR) {
                        float xPos = (float) x;
                        float yPos = (float) y;
                        float zPos = (float) z;

                        float[] sideTextureCoords = textureAtlas.getTextureCoordinates(block.getType().getSideTextureIndex());
                        float[] topTextureCoords = textureAtlas.getTextureCoordinates(block.getType().getTopTextureIndex());
                        float[] bottomTextureCoords = textureAtlas.getTextureCoordinates(block.getType().getBottomTextureIndex());

                        int texCoordIndex = 0;

                        // Generate vertices for the front face
                        for (int i = 0; i < CUBE_POS_FRONT.length; i += 3) {
                            vertices.add(CUBE_POS_FRONT[i] + xPos);
                            vertices.add(CUBE_POS_FRONT[i + 1] + yPos);
                            vertices.add(CUBE_POS_FRONT[i + 2] + zPos);

                            vertices.add(sideTextureCoords[texCoordIndex]);
                            vertices.add(sideTextureCoords[texCoordIndex + 1]);

                            texCoordIndex = (texCoordIndex + 2) % sideTextureCoords.length;
                        }

                        for (int index : CUBE_INDICES) {
                            indices.add(index + vertexOffset);
                        }

                        vertexOffset += CUBE_POS_FRONT.length / 3;

                        texCoordIndex = 0;

                        // Generate vertices for the back face
                        for (int i = 0; i < CUBE_POS_BACK.length; i += 3) {
                            vertices.add(CUBE_POS_BACK[i] + xPos);
                            vertices.add(CUBE_POS_BACK[i + 1] + yPos);
                            vertices.add(CUBE_POS_BACK[i + 2] + zPos);

                            vertices.add(sideTextureCoords[texCoordIndex]);
                            vertices.add(sideTextureCoords[texCoordIndex + 1]);

                            texCoordIndex = (texCoordIndex + 2) % sideTextureCoords.length;
                        }

                        for (int index : CUBE_INDICES) {
                            indices.add(index + vertexOffset);
                        }

                        vertexOffset += CUBE_POS_BACK.length / 3;

                        texCoordIndex = 0;

                        // Generate vertices for the right face
                        for (int i = 0; i < CUBE_POS_RIGHT.length; i += 3) {
                            vertices.add(CUBE_POS_RIGHT[i] + xPos);
                            vertices.add(CUBE_POS_RIGHT[i + 1] + yPos);
                            vertices.add(CUBE_POS_RIGHT[i + 2] + zPos);

                            vertices.add(sideTextureCoords[texCoordIndex]);
                            vertices.add(sideTextureCoords[texCoordIndex + 1]);

                            texCoordIndex = (texCoordIndex + 2) % sideTextureCoords.length;
                        }

                        for (int index : CUBE_INDICES) {
                            indices.add(index + vertexOffset);
                        }

                        vertexOffset += CUBE_POS_RIGHT.length / 3;

                        texCoordIndex = 0;

                        // Generate vertices for the left face
                        for (int i = 0; i < CUBE_POS_LEFT.length; i += 3) {
                            vertices.add(CUBE_POS_LEFT[i] + xPos);
                            vertices.add(CUBE_POS_LEFT[i + 1] + yPos);
                            vertices.add(CUBE_POS_LEFT[i + 2] + zPos);

                            vertices.add(sideTextureCoords[texCoordIndex]);
                            vertices.add(sideTextureCoords[texCoordIndex + 1]);

                            texCoordIndex = (texCoordIndex + 2) % sideTextureCoords.length;
                        }

                        for (int index : CUBE_INDICES) {
                            indices.add(index + vertexOffset);
                        }

                        vertexOffset += CUBE_POS_LEFT.length / 3;

                        texCoordIndex = 0;

                        // Generate vertices for the top face
                        for (int i = 0; i < CUBE_POS_TOP.length; i += 3) {
                            vertices.add(CUBE_POS_TOP[i] + xPos);
                            vertices.add(CUBE_POS_TOP[i + 1] + yPos);
                            vertices.add(CUBE_POS_TOP[i + 2] + zPos);

                            vertices.add(topTextureCoords[texCoordIndex]);
                            vertices.add(topTextureCoords[texCoordIndex + 1]);

                            texCoordIndex = (texCoordIndex + 2) % topTextureCoords.length;
                        }

                        for (int index : CUBE_INDICES) {
                            indices.add(index + vertexOffset);
                        }

                        vertexOffset += CUBE_POS_TOP.length / 3;

                        texCoordIndex = 0;

                        // Generate vertices for the bottom face
                        for (int i = 0; i < CUBE_POS_BOTTOM.length; i += 3) {
                            vertices.add(CUBE_POS_BOTTOM[i] + xPos);
                            vertices.add(CUBE_POS_BOTTOM[i + 1] + yPos);
                            vertices.add(CUBE_POS_BOTTOM[i + 2] + zPos);

                            vertices.add(bottomTextureCoords[texCoordIndex]);
                            vertices.add(bottomTextureCoords[texCoordIndex + 1]);

                            texCoordIndex = (texCoordIndex + 2) % bottomTextureCoords.length;
                        }

                        for (int index : CUBE_INDICES) {
                            indices.add(index + vertexOffset);
                        }

                        vertexOffset += CUBE_POS_BOTTOM.length / 3;
                    }
                }
            }
        }

        vertexCount = indices.size();

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, createFloatBuffer(vertices), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, createIntBuffer(indices), GL_STATIC_DRAW);
    }

    private FloatBuffer createFloatBuffer(List<Float> data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.size());
        for (float value : data) {
            buffer.put(value);
        }
        buffer.flip();
        return buffer;
    }

    private IntBuffer createIntBuffer(List<Integer> data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.size());
        for (int value : data) {
            buffer.put(value);
        }
        buffer.flip();
        return buffer;
    }

    public void renderChunk(Chunk chunk) {
        generateMesh(chunk);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        shaderProgram.use();

        shaderProgram.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        texture.bind();

        shaderProgram.uploadUniformMat4f("uProjMatrix", Window.getCurrentScene().getCamera().getProjMatrix());
        shaderProgram.uploadUniformMat4f("uViewMatrix", Window.getCurrentScene().getCamera().getViewMatrix());

        glBindVertexArray(vaoID);

        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);

        glBindVertexArray(0);
        shaderProgram.detach();
    }

    public void cleanup() {
        shaderProgram.detach();

        glDeleteBuffers(vboID);
        glDeleteBuffers(eboID);
        glDeleteVertexArrays(vaoID);
    }
}
