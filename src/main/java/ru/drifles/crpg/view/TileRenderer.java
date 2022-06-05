package ru.drifles.crpg.view;

import org.joml.Matrix4f;
import ru.drifles.crpg.model.Tile;
import ru.drifles.crpg.model.Way;

import static org.lwjgl.opengl.GL11.*;

public class TileRenderer {

    private static final ShaderProgram SHADER_PROGRAM = new ShaderProgram(
            "/world/tileVS.glsl",
            "/world/tileFS.glsl"
    );

    private static final int POSITION_LOCATION = SHADER_PROGRAM.getAttribLocation("position");

    private static final float[] VERTICES = {
            -0.5f, 0.5f,
            -0.5f, -0.5f,
            0.5f, -0.5f,
            0.5f, 0.5f,
            0.0f, 0.0f,
            0.5f, 0.0f,
            0.0f, 0.5f,
            0.0f, -0.5f,
            -0.5f, 0.0f
    };

    private static final int[] INDICES = {
            0, 1, 2,
            3, 2, 0,
            4, 5,
            4, 6,
            4, 7,
            4, 8,
            4, 0,
            4, 1,
            4, 2,
            4, 3
    };

    private static final VertexArrayObject VAO = new VertexArrayObject(
            new BufferObject[]{
                    new BufferObject(BufferObject.BufferType.VERTEX, VERTICES, BufferObject.DrawType.STATIC),
                    new BufferObject(BufferObject.BufferType.ELEMENT, INDICES, BufferObject.DrawType.STATIC)
            },
            new VertexAttribPointer[]{
                    new VertexAttribPointer(POSITION_LOCATION, 2, GL_FLOAT, false, 0, 0)
            }
    );

    private static final Matrix4f MODEL_MATRIX = new Matrix4f().identity();

    private TileRenderer() {  }

    public static void render(Tile tile) {
        SHADER_PROGRAM.use();

        var color = tile.passable() ? Color.WHITE : Color.RED;
        SHADER_PROGRAM.setUniform("Color", color.getR(), color.getG(), color.getB());
        SHADER_PROGRAM.setUniformMatrix("model", MODEL_MATRIX);

        var viewMatrix = new Matrix4f().translate(tile.position().x(), tile.position().y(), 0.0f);
        SHADER_PROGRAM.setUniformMatrix("view", viewMatrix);

        VAO.bind();
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

        SHADER_PROGRAM.setUniform("Color", Color.BLACK.getR(), Color.BLACK.getG(), Color.BLACK.getB());
        glDrawElements(GL_LINE_LOOP, 4, GL_UNSIGNED_INT, 0);

        if (tile.passable()) {
            SHADER_PROGRAM.setUniform("Color", Color.BLUE.getR(), Color.BLUE.getG(), Color.BLUE.getB());
            glDrawElements(GL_POINTS, 1, GL_UNSIGNED_INT, Integer.BYTES * 6);

            for (Way way : tile.ways()) {
                var offset = 0;

                if (way.destination().x() > tile.position().x()) {
                    if (way.destination().y() > tile.position().y()) {
                        offset = 20;
                    } else if (way.destination().y() < tile.position().y()) {
                        offset = 18;
                    } else {
                        offset = 6;
                    }
                } else if (way.destination().x() < tile.position().x()) {
                    if (way.destination().y() > tile.position().y()) {
                        offset = 14;
                    } else if (way.destination().y() < tile.position().y()) {
                        offset = 16;
                    } else {
                        offset = 12;
                    }
                } else {
                    if (way.destination().y() > tile.position().y()) {
                        offset = 8;
                    } else if (way.destination().y() < tile.position().y()) {
                        offset = 10;
                    }
                }

                glDrawElements(GL_LINES, 2, GL_UNSIGNED_INT, offset * Integer.BYTES);
            }
        }

        VAO.unbind();
    }
}
