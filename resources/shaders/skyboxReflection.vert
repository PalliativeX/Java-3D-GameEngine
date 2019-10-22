#version 330
layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec3 normal;
layout (location = 3) in vec3 tangent;

out vec3 FragPos;
out vec3 Normal;

uniform mat4 model;
uniform mat4 MVP;

void main()
{
    gl_Position = MVP * vec4(position, 1.0);
    FragPos = (model * vec4(position, 1.0)).xyz;

    Normal = mat3(transpose(inverse(model))) * normal;
}
