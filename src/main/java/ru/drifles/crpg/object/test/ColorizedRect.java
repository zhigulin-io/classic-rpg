package ru.drifles.crpg.object.test;

import ru.drifles.crpg.common.ShaderProgram;
import ru.drifles.crpg.object.GameObject;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class ColorizedRect extends GameObject {

    private static final float[] vertices = {
            // position    color
            -1.0f, -1.0f,  1.0f, 0.0f, 0.0f,
             1.0f, -1.0f,  0.0f, 1.0f, 0.0f,
             1.0f,  1.0f,  0.0f, 0.0f, 1.0f,
            -1.0f,  1.0f,  1.0f, 1.0f, 1.0f
    };

    private static final int[] indices = {
            0, 1, 2,
            0, 2, 3
    };

    private static final ShaderProgram shaderProgram = new ShaderProgram(
            "/colorizedRectVS.glsl",
            "/colorizedRectFS.glsl"
    );

    private final int vao;

    public ColorizedRect() {
        int vbo = glGenBuffers();
        vao = glGenVertexArrays();
        int ebo = glGenBuffers();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        int positionAttribLocation = shaderProgram.getAttribLocation("position");
        int colorAttribLocation = shaderProgram.getAttribLocation("color");

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glVertexAttribPointer(positionAttribLocation, 2, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glVertexAttribPointer(colorAttribLocation, 3, GL_FLOAT, false, 5 * Float.BYTES, 2 * Float.BYTES);
        glEnableVertexAttribArray(positionAttribLocation);
        glEnableVertexAttribArray(colorAttribLocation);
        glBindVertexArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    @Override
    public void draw() {
        shaderProgram.use();
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }
}
