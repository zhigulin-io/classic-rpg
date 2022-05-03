package ru.drifles.crpg.gameobject.tile;

import org.joml.Matrix4f;
import ru.drifles.crpg.accessory.BufferObject;
import ru.drifles.crpg.accessory.ShaderProgram;
import ru.drifles.crpg.accessory.VertexArrayObject;
import ru.drifles.crpg.accessory.VertexAttribPointer;
import ru.drifles.crpg.common.Color;
import ru.drifles.crpg.common.Renderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;

public class TileRenderer implements Renderer {

    private static final ShaderProgram SHADER_PROGRAM = new ShaderProgram(
            "/world/tileVS.glsl",
            "/world/tileFS.glsl"
    );

    private static final int POSITION_LOCATION = SHADER_PROGRAM.getAttribLocation("position");

    private static final float[] VERTICES = {
            -0.5f,  0.5f,
            -0.5f, -0.5f,
            0.5f, -0.5f,
            0.5f,  0.5f,
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
            4, 8
    };

    private static final VertexArrayObject VAO = new VertexArrayObject(
            new BufferObject[] {
                    new BufferObject(BufferObject.BufferType.VERTEX, VERTICES, BufferObject.DrawType.STATIC),
                    new BufferObject(BufferObject.BufferType.ELEMENT, INDICES, BufferObject.DrawType.STATIC)
            },
            new VertexAttribPointer[] {
                    new VertexAttribPointer(POSITION_LOCATION, 2, GL_FLOAT, false, 0, 0)
            }
    );

    private static final Matrix4f MODEL_MATRIX = new Matrix4f().identity();

    private final Matrix4f viewMatrix;
    private final Color color;
    private final Color borderColor;
    private final Color positionColor;
    private final Color wayColor;
    private final Tile tile;

    public TileRenderer(Tile tile) {
        this.tile = tile;
        this.viewMatrix  = new Matrix4f().translate(tile.getPosition().getX(), tile.getPosition().getY(), 0.0f);
        this.borderColor = Color.BLACK;
        this.color = tile.isPassable() ? Color.WHITE : Color.RED;
        this.positionColor = tile.isPassable() ? Color.BLUE : null;
        this.wayColor = tile.isPassable() ? Color.BLUE : null;
    }

    @Override
    public void render() {
        SHADER_PROGRAM.use();

        SHADER_PROGRAM.setUniform("Color", color.getR(), color.getG(), color.getB());
        SHADER_PROGRAM.setUniformMatrix("model", MODEL_MATRIX);
        SHADER_PROGRAM.setUniformMatrix("view", viewMatrix);

        VAO.bind();
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

        if (borderColor != null) {
            SHADER_PROGRAM.setUniform("Color", borderColor.getR(), borderColor.getG(), borderColor.getB());
            glDrawElements(GL_LINE_LOOP, 4, GL_UNSIGNED_INT, 0);
        }

        if (positionColor != null) {
            SHADER_PROGRAM.setUniform("Color", positionColor.getR(), positionColor.getG(), positionColor.getB());
            glDrawElements(GL_POINTS,1, GL_UNSIGNED_INT, Integer.BYTES * 6);
        }

        if (wayColor != null) {
            SHADER_PROGRAM.setUniform("Color", wayColor.getR(), wayColor.getG(), wayColor.getB());
            for (Way way : tile.getWays()) {
                if (way.to().isPassable()) {
                    var offset = Integer.BYTES;

                    if (way.to().getPosition().getX() > tile.getPosition().getX())
                        offset *= 6;
                    else if (way.to().getPosition().getY() > tile.getPosition().getY())
                        offset *= 8;
                    else if (way.to().getPosition().getY() < tile.getPosition().getY())
                        offset *= 10;
                    else if (way.to().getPosition().getX() < tile.getPosition().getX())
                        offset *= 12;

                    glDrawElements(GL_LINES, 2, GL_UNSIGNED_INT, offset);
                }
            }
        }

        VAO.unbind();
    }
}
