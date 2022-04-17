package ru.drifles.crpg.object.test;

import ru.drifles.crpg.common.ShaderProgram;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Triangle {

    private static final float[] vertices = {
            -1.0f, -1.0f,
            1.0f, -1.0f,
            0.0f, 1.0f
    };

    private static final ShaderProgram program = new ShaderProgram("/triangleVS.glsl", "/triangleFS.glsl");

    private final int vao = glGenVertexArrays();

    public Triangle() {
        glBindVertexArray(vao);
        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        var attribPosition = program.getAttribLocation("position");
        glVertexAttribPointer(attribPosition, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(attribPosition);
        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void draw() {
        program.use();
        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, 3);
    }
}
