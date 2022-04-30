package ru.drifles.crpg.renderer;

import org.joml.Matrix4f;
import ru.drifles.crpg.common.*;
import ru.drifles.crpg.object.world.Tile;

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
            0.5f,  0.5f
    };

    private static final int[] INDICES = {
            0, 1, 2,
            3, 2, 0
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

    private static final Matrix4f MODEL_MATRIX = new Matrix4f()
            .scale(Properties.tileSize)
            .translate(0.5f, 0.5f, 0.0f);

    private final Matrix4f viewMatrix;
    private final Color color;
    private final Color borderColor;

    public TileRenderer(Tile tile) {
        this.viewMatrix  = new Matrix4f()
                .translate(
                        tile.getPosition().getX() * Properties.tileSize,
                        tile.getPosition().getY() * Properties.tileSize,
                        0.0f
                );
        this.color = tile.isPassable() ? Color.WHITE : Color.RED;
        this.borderColor = Color.BLACK;
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
            SHADER_PROGRAM.setUniform("Color", borderColor.getR(), borderColor.getG(), borderColor.getG());
            glDrawElements(GL_LINE_LOOP, 4, GL_UNSIGNED_INT, 0);
        }
        VAO.unbind();
    }
}
