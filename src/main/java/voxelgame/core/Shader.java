package voxelgame.core;

import org.joml.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;
import voxelgame.utils.Utils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {
    private int shaderProgramID;
    private boolean beingUsed = false;

    public Shader(List<ShaderModuleData> shaderModuleDataList) {
        shaderProgramID = glCreateProgram();
        if (shaderProgramID == 0) {
            assert false : "Error: Could not create shader program";
        }

        List<Integer> shaderModules = new ArrayList<>();
        shaderModuleDataList.forEach(s -> shaderModules.add(compile(Utils.readFile(s.shaderFile), s.shaderType)));

        link(shaderModules);
    }

    protected int compile(String shaderSource, int shaderType) {
        int shaderID = glCreateShader(shaderType);
        glShaderSource(shaderID, shaderSource);
        glCompileShader(shaderID);

        int success = glGetShaderi(shaderID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(shaderID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: Shader compilation failed. '" + shaderType + "'");
            System.out.println(glGetShaderInfoLog(shaderID, len));
            assert false : "";
        }

        glAttachShader(shaderProgramID, shaderID);

        return shaderID;
    }

    private void link(List<Integer> shaderModules) {
        glLinkProgram(shaderProgramID);

        int success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: Linking of shaders failed. '" + shaderProgramID + "'");
            System.out.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false : "";
        }

        shaderModules.forEach(s -> glDetachShader(shaderProgramID, s));
        shaderModules.forEach(GL30::glDeleteShader);
    }

    public void use() {
        if (!beingUsed) {
            glUseProgram(shaderProgramID);
            beingUsed = true;
        }
    }

    public void detach() {
        glUseProgram(0);
        beingUsed = false;
    }

    public void uploadUniformMat4f(String uniformName, Matrix4f mat4) {
        int uniformLocation = glGetUniformLocation(shaderProgramID, uniformName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(uniformLocation, false, matBuffer);
    }

    public void uploadUniformMat3f(String uniformName, Matrix3f mat3) {
        int uniformLocation = glGetUniformLocation(shaderProgramID, uniformName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        glUniformMatrix3fv(uniformLocation, false, matBuffer);
    }

    public void uploadUniformVec4f(String uniformName, Vector4f vec) {
        int uniformLocation = glGetUniformLocation(shaderProgramID, uniformName);
        use();
        glUniform4f(uniformLocation, vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadUniformVec3f(String uniformName, Vector3f vec) {
        int uniformLocation = glGetUniformLocation(shaderProgramID, uniformName);
        use();
        glUniform3f(uniformLocation, vec.x, vec.y, vec.z);
    }

    public void uploadUniformVec2f(String uniformName, Vector2f vec) {
        int uniformLocation = glGetUniformLocation(shaderProgramID, uniformName);
        use();
        glUniform2f(uniformLocation, vec.x, vec.y);
    }

    public void uploadUniformFloat(String uniformName, float value) {
        int uniformLocation = glGetUniformLocation(shaderProgramID, uniformName);
        use();
        glUniform1f(uniformLocation, value);
    }

    public void uploadUniformInt(String uniformName, int value) {
        int uniformLocation = glGetUniformLocation(shaderProgramID, uniformName);
        use();
        glUniform1i(uniformLocation, value);
    }

    public void uploadTexture(String textureName, int slot) {
        int uniformLocation = glGetUniformLocation(shaderProgramID, textureName);
        use();
        glUniform1i(uniformLocation, slot);
    }

    public int getShaderProgramID() {
        return shaderProgramID;
    }

    public record ShaderModuleData(String shaderFile, int shaderType) {

    }
}
