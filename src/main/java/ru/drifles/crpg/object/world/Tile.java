package ru.drifles.crpg.object.world;

import org.joml.Matrix4f;
import ru.drifles.crpg.common.*;
import ru.drifles.crpg.object.GameObject;

import static org.lwjgl.opengl.GL20.*;

public class Tile extends GameObject {

	private static final float[] vertices = {
			-0.5f,  0.5f,
			-0.5f, -0.5f,
			0.5f, -0.5f,
			0.5f,  0.5f
	};

	private static final int[] indices = {
			0, 1, 2,
			0, 2, 3
	};

	private static final ShaderProgram shaderProgram = new ShaderProgram("/world/tileVS.glsl", "/world/tileFS.glsl");
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
	private static final Matrix4f modelMatrix = new Matrix4f().scale(tileSize);

	private final Matrix4f viewMatrix = new Matrix4f().translate(
			position.getX() * tileSize,
			position.getY() * tileSize,
			0.0f
	);

	private Color color;

	public Tile(int x, int y) {
		super(x, y);
		this.color = Color.WHITE;
	}
	
	@Override
	public void draw() {
		shaderProgram.use();
		shaderProgram.setUniform("Color", color.getR(), color.getG(), color.getB());
		shaderProgram.setUniformMatrix("model", modelMatrix);
		shaderProgram.setUniformMatrix("view", viewMatrix);

		vao.bind();
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
		vao.unbind();
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
