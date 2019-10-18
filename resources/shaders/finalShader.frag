#version 330 core

out vec4 FragColor;

in vec2 TexCoord;

uniform sampler2D screenTexture;

void main()
{
    vec4 color = texture2D(screenTexture, TexCoord);

    FragColor = color;
}