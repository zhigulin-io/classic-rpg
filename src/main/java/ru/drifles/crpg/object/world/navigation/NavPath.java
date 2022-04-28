package ru.drifles.crpg.object.world.navigation;

import org.joml.Matrix4f;
import ru.drifles.crpg.common.BufferObject;
import ru.drifles.crpg.common.ShaderProgram;
import ru.drifles.crpg.common.VertexArrayObject;
import ru.drifles.crpg.common.VertexAttribPointer;
import ru.drifles.crpg.object.Drawable;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;

public class NavPath implements Drawable {

    private static final ShaderProgram shaderProgram = new ShaderProgram(
            "/world/navigation/navPathVS.glsl",
            "/world/navigation/navPathFS.glsl"
    );
    private static final int positionLocation = shaderProgram.getAttribLocation("position");

    private static final float tileSize = 16.0f;

    private static final float[] vertices = {
            1.0f, 0.0f,
            0.0f, 0.0f,
    };

    private static final float[] indices = {
            0, 1
    };

    private static final VertexArrayObject vao = new VertexArrayObject(
            new BufferObject[] {
                    new BufferObject(BufferObject.BufferType.VERTEX, vertices, BufferObject.DrawType.STATIC),
                    new BufferObject(BufferObject.BufferType.ELEMENT, indices, BufferObject.DrawType.STATIC)
            },
            new VertexAttribPointer[] {
                    new VertexAttribPointer(positionLocation, 2, GL_FLOAT, false, 0, 0)
            }
    );

    private final Matrix4f viewMatrix;
    private final Matrix4f modelMatrix;

    public NavPath(NavNode from, NavNode to) {

        float angle = 0;

        if (from.getTile().getPosition().getY() > to.getTile().getPosition().getY()) {
            angle = (float) (Math.PI / -2);
        } else if (from.getTile().getPosition().getY() < to.getTile().getPosition().getY()) {
            angle = (float) (Math.PI / 2);
        } else if (from.getTile().getPosition().getX() > to.getTile().getPosition().getX()) {
            angle = (float) (Math.PI);
        }

        this.modelMatrix = new Matrix4f()
                .scale(tileSize)
                .translate(0.5f, 0.5f, 0.0f)
                .rotateZ(angle);

        this.viewMatrix = new Matrix4f()
                .translate(
                        from.getTile().getPosition().getX() * tileSize,
                        from.getTile().getPosition().getY() * tileSize,
                        0.0f
                );

    }

    @Override
    public void draw() {
        shaderProgram.use();
        shaderProgram.setUniformMatrix("model", modelMatrix);
        shaderProgram.setUniformMatrix("view", viewMatrix);

        vao.bind();
        glDrawElements(GL_LINES, 2, GL_UNSIGNED_INT, 0);
        vao.unbind();
    }
}
