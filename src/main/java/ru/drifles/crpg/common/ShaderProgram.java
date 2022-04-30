package ru.drifles.crpg.common;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.FloatBuffer;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL20.*;

public final class ShaderProgram {

    private final int program;

    public ShaderProgram(String vertexPath, String fragmentPath) {
        this.program = glCreateProgram();

        var vertexShader = glCreateShader(GL_VERTEX_SHADER);
        var fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);

        var vertexURL = getClass().getResource(vertexPath);
        var fragmentURL = getClass().getResource(fragmentPath);

        try (var file = new BufferedReader(new FileReader(new File(Objects.requireNonNull(vertexURL).toURI())))) {
            var source = file.lines().collect(Collectors.joining("\n"));
            glShaderSource(vertexShader, source);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }

        try (var file = new BufferedReader(new FileReader(new File(Objects.requireNonNull(fragmentURL).toURI())))) {
            var source = file.lines().collect(Collectors.joining("\n"));
            glShaderSource(fragmentShader, source);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }

        glCompileShader(vertexShader);
        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Could not compile vertex shader");
        }

        glCompileShader(fragmentShader);
        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Could not compile fragment shader");
        }

        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        glLinkProgram(program);
        if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException("Could not link shader program");
        }

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    public void use() {
        glUseProgram(program);
        setUniformMatrix("projection", Camera.PROJECTION);
    }

    public void setUniform(String uniformName, float... values) {
        int location = getUniformLocation(uniformName);

        switch (values.length) {
            case 1 -> glUniform1f(location, values[0]);
            case 2 -> glUniform2fv(location, values);
            case 3 -> glUniform3fv(location, values);
            case 4 -> glUniform4fv(location, values);
            default -> throw new RuntimeException("Too many values for uniform variable");
        }
    }

    public void setUniformMatrix(String matrixName, Matrix4f matrix) {
        int location = getUniformLocation(matrixName);

        FloatBuffer matrixValues = BufferUtils.createFloatBuffer(16);
        matrix.get(matrixValues);

        glUniformMatrix4fv(location, false, matrixValues);
        matrixValues.clear();
    }

    public int getAttribLocation(String paramName) {
        return glGetAttribLocation(program, paramName);
    }

    public int getUniformLocation(String uniformName) {
        return glGetUniformLocation(program, uniformName);
    }
}
