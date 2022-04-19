package ru.drifles.crpg.object;

import org.joml.Matrix4f;
import ru.drifles.crpg.common.*;

import static org.lwjgl.opengl.GL11.*;

public class GridCell extends GameObject {

    protected static final float[] vertices = {
            -0.5f,  0.5f,
            -0.5f, -0.5f,
            0.5f, -0.5f,
            0.5f,  0.5f
    };

    protected static final int[] indices = { 0, 1, 2, 3 };

    protected static final ShaderProgram shaderProgram = new ShaderProgram("/world/tileVS.glsl", "/world/tileFS.glsl");
    protected static final int positionLocation = shaderProgram.getAttribLocation("position");

    protected static final VertexArrayObject vao = new VertexArrayObject(
            new BufferObject[] {
                    new BufferObject(BufferObject.BufferType.VERTEX, vertices, BufferObject.DrawType.STATIC),
                    new BufferObject(BufferObject.BufferType.ELEMENT, indices, BufferObject.DrawType.STATIC)
            },
            new VertexAttribPointer[] {
                    new VertexAttribPointer(positionLocation, 2, GL_FLOAT, false, 0, 0)
            }
    );

    protected static final float tileSize = 16.0f;
    protected static final Matrix4f modelMatrix = new Matrix4f().scale(tileSize);

    protected final Matrix4f viewMatrix = new Matrix4f().translate(
            position.getX() * tileSize,
            position.getY() * tileSize,
            0.0f
    );

    protected Color color;

    public GridCell(int x, int y) {
        super(x, y);
        this.color = Color.BLACK;
    }

    @Override
    public void draw() {
        shaderProgram.use();
        shaderProgram.setUniform("Color", color.getR(), color.getG(), color.getB());
        shaderProgram.setUniformMatrix("model", modelMatrix);
        shaderProgram.setUniformMatrix("view", viewMatrix);

        vao.bind();
        glDrawElements(GL_LINE_LOOP, 6, GL_UNSIGNED_INT, 0);
        vao.unbind();
    }
}
