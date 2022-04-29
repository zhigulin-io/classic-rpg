package ru.drifles.crpg.object.world.navigation;

import org.joml.Matrix4f;
import ru.drifles.crpg.common.BufferObject;
import ru.drifles.crpg.common.ShaderProgram;
import ru.drifles.crpg.common.VertexArrayObject;
import ru.drifles.crpg.common.VertexAttribPointer;
import ru.drifles.crpg.object.Drawable;
import ru.drifles.crpg.object.world.Tile;

import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;

public class NavNode implements Drawable {

    private static final float[] vertices = {
            0.0f, 0.0f
    };

    private static final float[] indices = {
            0
    };

    private static final ShaderProgram shaderProgram = new ShaderProgram(
            "/world/navigation/navNodeVS.glsl",
            "/world/navigation/navNodeFS.glsl"
    );
    private static final int positionLocation = shaderProgram.getAttribLocation("position");

    private static final VertexArrayObject vao = new VertexArrayObject(
            new BufferObject[] {
                    new BufferObject(BufferObject.BufferType.VERTEX, vertices, BufferObject.DrawType.STATIC),
                    new BufferObject(BufferObject.BufferType.ELEMENT, indices, BufferObject.DrawType.STATIC)
            },
            new VertexAttribPointer[] {
                    new VertexAttribPointer(positionLocation, 2, GL_FLOAT, false, 0, 0)
            }
    );

    private static final float tileSize = 16.0f;
    private static final Matrix4f modelMatrix = new Matrix4f().scale(tileSize).translate(0.5f, 0.5f, 0.0f);

    private final Matrix4f viewMatrix;
    private final Tile tile;

    public NavNode(Tile tile) {
        this.tile = tile;
        this.viewMatrix = new Matrix4f().translate(
                tile.getPosition().getX() * tileSize,
                tile.getPosition().getY() * tileSize,
                0.0f
        );
    }

    @Override
    public void draw() {
        shaderProgram.use();
        shaderProgram.setUniformMatrix("model", modelMatrix);
        shaderProgram.setUniformMatrix("view", viewMatrix);

        vao.bind();
        glDrawElements(GL_POINTS, 1, GL_UNSIGNED_INT, 0);
        vao.unbind();
    }

    public Tile getTile() {
        return tile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NavNode navNode = (NavNode) o;
        return tile.equals(navNode.tile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tile);
    }

    @Override
    public String toString() {
        return "NavNode{" +
                "tile=" + tile +
                '}';
    }
}
