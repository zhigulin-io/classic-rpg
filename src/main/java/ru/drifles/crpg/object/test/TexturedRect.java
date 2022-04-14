package ru.drifles.crpg.object.test;

import org.lwjgl.system.MemoryStack;
import ru.drifles.crpg.common.ShaderProgram;
import ru.drifles.crpg.object.GameObject;

import java.net.URISyntaxException;
import java.util.Objects;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

public class TexturedRect extends GameObject {

    private static final ShaderProgram shaderProgram = new ShaderProgram(
            "/texturedRectVS.glsl",
            "/texturedRectFS.glsl"
    );

    private static final int vbo = glGenBuffers();
    private static final int ebo = glGenBuffers();
    private static final int vao = glGenVertexArrays();
    private static final int texture = glGenTextures();

    private static final float[] vertices = {
            // Позиции     // Цвета            // Текстурные координаты
             0.5f,  0.5f,   1.0f, 0.0f, 0.0f,   0.0f, 0.0f,   // Верхний правый
             0.5f, -0.5f,   0.0f, 1.0f, 0.0f,   0.0f, 1.0f,   // Нижний правый
            -0.5f, -0.5f,   0.0f, 0.0f, 1.0f,   1.0f, 1.0f,   // Нижний левый
            -0.5f,  0.5f,   1.0f, 1.0f, 0.0f,   1.0f, 0.0f    // Верхний левый
    };

    private static final int[] indices = {
            0, 2, 1,
            0, 3, 2
    };

    static {
        try (var stack = MemoryStack.stackPush()) {
            var texturePath = TexturedRect.class.getResource("/texture.png");
            var width = stack.mallocInt(1);
            var height = stack.mallocInt(1);
            var channels = stack.mallocInt(1);

            var imgPath = Objects.requireNonNull(texturePath).toURI().getPath();

            var image = stbi_load(
                    imgPath,
                    width,
                    height,
                    channels,
                    4
            );

            glBindTexture(GL_TEXTURE_2D, texture);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexImage2D(
                    GL_TEXTURE_2D,
                    0,
                    GL_RGBA,
                    width.get(0),
                    height.get(0),
                    0,
                    GL_RGBA,
                    GL_UNSIGNED_BYTE,
                    image
            );
            glGenerateMipmap(GL_TEXTURE_2D);
            glBindTexture(GL_TEXTURE_2D, 0);
            stbi_image_free(Objects.requireNonNull(image));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        int positionAttribLocation = shaderProgram.getAttribLocation("position");
        int colorAttribLocation = shaderProgram.getAttribLocation("color");
        int textureAttribLocation = shaderProgram.getAttribLocation("textureCoordinate");

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBindTexture(GL_TEXTURE_2D, texture);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        glVertexAttribPointer(positionAttribLocation, 2, GL_FLOAT, false, 7 * Float.BYTES, 0);
        glVertexAttribPointer(colorAttribLocation, 3, GL_FLOAT, false, 7 * Float.BYTES, 2 * Float.BYTES);
        glVertexAttribPointer(textureAttribLocation, 2, GL_FLOAT, false, 7 * Float.BYTES, 5 * Float.BYTES);
        glEnableVertexAttribArray(positionAttribLocation);
        glEnableVertexAttribArray(colorAttribLocation);
        glEnableVertexAttribArray(textureAttribLocation);
        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    @Override
    public void draw() {
        glBindTexture(GL_TEXTURE_2D, texture);
        shaderProgram.use();
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }
}
