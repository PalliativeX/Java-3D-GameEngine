#version 330 core

out vec4 FragColor;

in vec2 TexCoord;

uniform sampler2D screenTexture;

void main()
{
    //float depthValue = texture(screenTexture, TexCoord).r;
    //FragColor = vec4(vec3(screenTexture), 1.0);
    FragColor = texture(screenTexture, TexCoord);
}