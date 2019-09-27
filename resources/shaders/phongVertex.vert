#version 330
layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec3 normal;

out vec2 TexCoord;
out vec3 Normal;
out vec3 FragPos;

uniform mat4 model;
uniform mat4 MVP;

void main()
{
    gl_Position = MVP * vec4(position, 1.0);
    TexCoord = texCoord;
    Normal = (model * vec4(normal, 0.0)).xyz;
    //Normal = mat3(transpose(inverse(transform))) * normal;
    FragPos = (model * vec4(position, 1.0)).xyz;
}
