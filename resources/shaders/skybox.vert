#version 330 core
layout (location = 0) in vec3 position;

out vec3 TexCoords;

uniform mat4 viewProjection;

void main()
{
    TexCoords = position;
    gl_Position = viewProjection * vec4(position, 1.0);
}