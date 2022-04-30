package ru.drifles.crpg.renderer;

import org.joml.Matrix4f;
import ru.drifles.crpg.common.BufferObject;
import ru.drifles.crpg.common.ShaderProgram;
import ru.drifles.crpg.common.VertexArrayObject;
import ru.drifles.crpg.common.VertexAttribPointer;
import ru.drifles.crpg.object.world.navigation.NavPath;

import static org.lwjgl.opengl.GL11.*;
import static ru.drifles.crpg.common.Properties.tileSize;

public class NavPathRenderer implements Renderer {

    private static final ShaderProgram SHADER_PROGRAM = new ShaderProgram(
            "/world/navigation/navPathVS.glsl",
            "/world/navigation/navPathFS.glsl"
    );
    private static final int POSITION_LOCATION = SHADER_PROGRAM.getAttribLocation("position");

    private static final float[] VERTICES = {
            1.0f, 0.0f,
            0.0f, 0.0f,
    };

    private static final VertexArrayObject VAO = new VertexArrayObject(
            new BufferObject[] {
                    new BufferObject(BufferObject.BufferType.VERTEX, VERTICES, BufferObject.DrawType.STATIC),
            },
            new VertexAttribPointer[] {
                    new VertexAttribPointer(POSITION_LOCATION, 2, GL_FLOAT, false, 0, 0)
            }
    );

    private final Matrix4f viewMatrix;
    private final Matrix4f modelMatrix;

    public NavPathRenderer(NavPath navPath) {
        float angle = 0;

        if (navPath.getFrom().getTile().getPosition().getY() > navPath.getTo().getTile().getPosition().getY()) {
            angle = (float) (Math.PI / -2);
        } else if (navPath.getFrom().getTile().getPosition().getY() < navPath.getTo().getTile().getPosition().getY()) {
            angle = (float) (Math.PI / 2);
        } else if (navPath.getFrom().getTile().getPosition().getX() > navPath.getTo().getTile().getPosition().getX()) {
            angle = (float) (Math.PI);
        }

        this.modelMatrix = new Matrix4f()
                .scale(tileSize)
                .translate(0.5f, 0.5f, 0.0f)
                .rotateZ(angle);

        this.viewMatrix = new Matrix4f()
                .translate(
                        navPath.getFrom().getTile().getPosition().getX() * tileSize,
                        navPath.getFrom().getTile().getPosition().getY() * tileSize,
                        0.0f
                );
    }

    @Override
    public void render() {
        SHADER_PROGRAM.use();
        SHADER_PROGRAM.setUniformMatrix("model", modelMatrix);
        SHADER_PROGRAM.setUniformMatrix("view", viewMatrix);

        VAO.bind();
        glDrawArrays(GL_LINES, 0, 2);
        VAO.unbind();
    }
}
