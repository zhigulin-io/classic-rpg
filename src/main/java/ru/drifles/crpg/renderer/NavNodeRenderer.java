package ru.drifles.crpg.renderer;

import org.joml.Matrix4f;
import ru.drifles.crpg.common.*;
import ru.drifles.crpg.object.world.navigation.NavNode;

import static org.lwjgl.opengl.GL11.*;

public class NavNodeRenderer implements Renderer {
    private static final ShaderProgram SHADER_PROGRAM = new ShaderProgram(
            "/world/navigation/navNodeVS.glsl",
            "/world/navigation/navNodeFS.glsl"
    );
    private static final int POSITION_LOCATION = SHADER_PROGRAM.getAttribLocation("position");

    private static final float[] VERTICES = {
            0.5f, 0.5f
    };

    private static final VertexArrayObject VAO = new VertexArrayObject(
            new BufferObject[] {
                    new BufferObject(BufferObject.BufferType.VERTEX, VERTICES, BufferObject.DrawType.STATIC)
            },
            new VertexAttribPointer[] {
                    new VertexAttribPointer(POSITION_LOCATION, 2, GL_FLOAT, false, 0, 0)
            }
    );

    private static final Matrix4f MODEL_MATRIX = new Matrix4f().scale(Properties.tileSize);

    private final Matrix4f VIEW_MATRIX;

    public NavNodeRenderer(NavNode navNode) {
        this.VIEW_MATRIX = new Matrix4f().translate(
                navNode.getTile().getPosition().getX() * Properties.tileSize,
                navNode.getTile().getPosition().getY() * Properties.tileSize,
                0.0f
        );
    }

    @Override
    public void render() {
        SHADER_PROGRAM.use();
        SHADER_PROGRAM.setUniformMatrix("model", MODEL_MATRIX);
        SHADER_PROGRAM.setUniformMatrix("view", VIEW_MATRIX);

        VAO.bind();
        glDrawArrays(GL_POINTS, 0, 1);
        VAO.unbind();
    }
}
