package ru.drifles.crpg.common;

import static org.lwjgl.opengl.GL20.*;

public class VertexAttribPointer {

    private final int position;
    private final int size;
    private final int type;
    private final int stride;
    private final long offset;
    private final boolean normalized;

    public VertexAttribPointer(int position, int size, int type, boolean normalized, int stride, long offset) {
        this.position = position;
        this.size = size;
        this.type = type;
        this.stride = stride;
        this.offset = offset;
        this.normalized = normalized;
    }

    public void enable() {
        glVertexAttribPointer(position, size, type, normalized, stride, offset);
        glEnableVertexAttribArray(position);
    }
}
