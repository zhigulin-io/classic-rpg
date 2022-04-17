package ru.drifles.crpg.common;

import static org.lwjgl.opengl.GL15.*;

public final class BufferObject {
	
    private final int bufferObject;
    private final BufferType bufferType;

    public BufferObject(BufferType bufferType) {
        this.bufferType = bufferType;
        this.bufferObject = glGenBuffers();
    }
    
    public BufferObject(BufferType bufferType, float[] data, DrawType drawType) {
    	this(bufferType);
    	loadData(data, drawType);
    }
    
    public BufferObject(BufferType bufferType, double[] data, DrawType drawType) {
    	this(bufferType);
    	loadData(data, drawType);
    }

    public BufferObject(BufferType bufferType, short[] data, DrawType drawType) {
    	this(bufferType);
    	loadData(data, drawType);
    }
    
    public BufferObject(BufferType bufferType, int[] data, DrawType drawType) {
    	this(bufferType);
    	loadData(data, drawType);
    }
    
    public BufferObject(BufferType bufferType, long[] data, DrawType drawType) {
    	this(bufferType);
    	loadData(data, drawType);
    }

	public void loadData(float[] data, DrawType drawType) {
        glBindBuffer(bufferType.value, bufferObject);
        glBufferData(bufferType.value, data, drawType.value);
        glBindBuffer(bufferType.value, 0);
    }

    public void loadData(int[] data, DrawType drawType) {
        glBindBuffer(bufferType.value, bufferObject);
        glBufferData(bufferType.value, data, drawType.value);
        glBindBuffer(bufferType.value, 0);
    }

    public void loadData(long[] data, DrawType drawType) {
        glBindBuffer(bufferType.value, bufferObject);
        glBufferData(bufferType.value, data, drawType.value);
        glBindBuffer(bufferType.value, 0);
    }

    public void loadData(double[] data, DrawType drawType) {
        glBindBuffer(bufferType.value, bufferObject);
        glBufferData(bufferType.value, data, drawType.value);
        glBindBuffer(bufferType.value, 0);
    }

    public void loadData(short[] data, DrawType drawType) {
        glBindBuffer(bufferType.value, bufferObject);
        glBufferData(bufferType.value, data, drawType.value);
        glBindBuffer(bufferType.value, 0);
    }

    public void bind() {
        glBindBuffer(bufferType.value, bufferObject);
    }

    public void unbind() {
        glBindBuffer(bufferType.value, 0);
    }

    public enum BufferType {
        ELEMENT(GL_ELEMENT_ARRAY_BUFFER), VERTEX(GL_ARRAY_BUFFER);

        private final int value;

        BufferType(int value) {
            this.value = value;
        }
    }

    public enum DrawType {
        STATIC(GL_STATIC_DRAW), DYNAMIC(GL_DYNAMIC_DRAW), STREAM(GL_STREAM_DRAW);

        private final int value;

        DrawType(int value) {
            this.value = value;
        }
    }
}
