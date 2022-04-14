#version 330

in vec3 Color;
in vec2 TextureCoordinate;

out vec4 frag_color;

uniform sampler2D rectTexture;

void main() {
    //frag_color = texture(rectTexture, TextureCoordinate);
    frag_color = texture(rectTexture, TextureCoordinate);
}
