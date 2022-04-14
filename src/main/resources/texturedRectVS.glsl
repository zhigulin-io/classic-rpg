#version 330

in vec2 position;
in vec3 color;
in vec2 textureCoordinate;

out vec3 Color;
out vec2 TextureCoordinate;

void main() {
    gl_Position = vec4(position, 0.0, 1.0);
    Color = color;
    TextureCoordinate = textureCoordinate;
}
