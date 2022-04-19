package ru.drifles.crpg.common;

import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public final class VertexArrayObject {

    private final int vao = glGenVertexArrays();

    public VertexArrayObject(BufferObject[] objects, VertexAttribPointer[] pointers) {
        fill(objects, pointers);
    }

    public void bind() {
        glBindVertexArray(vao);
    }

    public void unbind() {
        glBindVertexArray(0);
    }

    private void fill(BufferObject[] objects, VertexAttribPointer[] pointers) {
        bind();

        for (BufferObject object : objects)
            object.bind();

        for (VertexAttribPointer pointer : pointers)
            pointer.enable();

        unbind();
        for (BufferObject object : objects)
            object.unbind();
    }
}
