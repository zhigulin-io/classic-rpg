package ru.drifles.crpg.renderer;

import org.joml.Matrix4f;
import ru.drifles.crpg.accessory.BufferObject;
import ru.drifles.crpg.accessory.ShaderProgram;
import ru.drifles.crpg.accessory.VertexArrayObject;
import ru.drifles.crpg.accessory.VertexAttribPointer;
import ru.drifles.crpg.common.Color;
import ru.drifles.crpg.gameobject.walker.Walker;

import static org.lwjgl.opengl.GL11.*;

public class WalkerRenderer {

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

    private WalkerRenderer() {  }

    public static void render(Walker walker) {
        var viewMatrix = new Matrix4f().setTranslation(walker.getPosition().x(), walker.getPosition().y(), 0);

        if (walker.getRoute() != null) {
            var step = walker.getRoute().peek();
            float stepSize = 1.0f / step.cost();

            var stepNumber = walker.getStepNumber();
            var dX = step.destination().x() - step.source().x();
            var dY = step.destination().y() - step.source().y();

            viewMatrix.setTranslation(
                    walker.getPosition().x() + stepNumber * stepSize * dX,
                    walker.getPosition().y() + stepNumber * stepSize * dY,
                    0.0f
            );
            walker.move();
        }

        SHADER_PROGRAM.use();

        SHADER_PROGRAM.setUniform("Color", Color.GREEN.getR(), Color.GREEN.getG(), Color.GREEN.getB());
        SHADER_PROGRAM.setUniformMatrix("model", MODEL_MATRIX);
        SHADER_PROGRAM.setUniformMatrix("view", viewMatrix);

        VAO.bind();
        glDrawArrays(GL_TRIANGLE_FAN, 0, VERTICES.length);
        VAO.unbind();
    }
}
