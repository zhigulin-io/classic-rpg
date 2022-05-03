package ru.drifles.crpg.accessory;

import static org.lwjgl.opengl.GL20.*;

public record VertexAttribPointer(int position, int size, int type, boolean normalized, int stride, long offset) {

    public void enable() {
        glVertexAttribPointer(position, size, type, normalized, stride, offset);
        glEnableVertexAttribArray(position);
    }
}
