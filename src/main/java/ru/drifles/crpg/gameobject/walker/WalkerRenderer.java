package ru.drifles.crpg.gameobject.walker;

import org.joml.Matrix4f;
import ru.drifles.crpg.accessory.BufferObject;
import ru.drifles.crpg.accessory.ShaderProgram;
import ru.drifles.crpg.accessory.VertexArrayObject;
import ru.drifles.crpg.accessory.VertexAttribPointer;
import ru.drifles.crpg.common.Color;
import ru.drifles.crpg.common.Renderer;

import static org.lwjgl.opengl.GL11.*;

public class WalkerRenderer implements Renderer {

    private static final ShaderProgram SHADER_PROGRAM = new ShaderProgram(
            "/world/tileVS.glsl",
            "/world/tileFS.glsl"
    );

    private static final int POSITION_LOCATION = SHADER_PROGRAM.getAttribLocation("position");

    private static final float[] VERTICES = {
            0.0f, 0.0f,
            0.3f, 0.0f,
            0.2f, 0.2f,
            0.0f, 0.3f,
            -0.2f, 0.2f,
            -0.3f, 0.0f,
            -0.2f, -0.2f,
            0.0f, -0.3f,
            0.2f, -0.2f,
            0.3f, 0.0f
    };

    private static final VertexArrayObject VAO = new VertexArrayObject(
            new BufferObject[] {
                    new BufferObject(BufferObject.BufferType.VERTEX, VERTICES, BufferObject.DrawType.STATIC),
            },
            new VertexAttribPointer[] {
                    new VertexAttribPointer(POSITION_LOCATION, 2, GL_FLOAT, false, 0, 0)
            }
    );

    private static final Matrix4f MODEL_MATRIX = new Matrix4f().identity();

    private final Matrix4f viewMatrix;
    private final Color color = Color.GREEN;
    private final Walker walker;

    public WalkerRenderer(Walker walker) {
        this.walker = walker;
        this.viewMatrix = new Matrix4f().translate(walker.getPosition().getX(), walker.getPosition().getY(), 0.0f);
    }

    @Override
    public void render() {
        this.viewMatrix.setTranslation(walker.getPosition().getX(), walker.getPosition().getY(), 0.0f);

        SHADER_PROGRAM.use();

        SHADER_PROGRAM.setUniform("Color", color.getR(), color.getG(), color.getB());
        SHADER_PROGRAM.setUniformMatrix("model", MODEL_MATRIX);
        SHADER_PROGRAM.setUniformMatrix("view", viewMatrix);

        VAO.bind();
        glDrawArrays(GL_TRIANGLE_FAN, 0, VERTICES.length);
        VAO.unbind();
    }
}
