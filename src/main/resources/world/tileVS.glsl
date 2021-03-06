#version 330

in vec2 position;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

void main() {
    gl_PointSize = 8.0f;
    gl_Position = projection * view * model * vec4(position, 0.0, 1.0);
}
