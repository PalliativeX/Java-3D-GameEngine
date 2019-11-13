#version 330
layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec3 normal;
layout (location = 3) in vec3 tangent;

out vec2 TexCoord;
out vec3 FragPos;
out mat3 TBNmatrix;

out vec4 FragPosLightSpace;

uniform mat4 model;
uniform mat4 MVP;
uniform mat4 lightSpaceMatrix;

void main()
{
    gl_Position = MVP * vec4(position, 1.0);
    TexCoord = texCoord;
    FragPos = (model * vec4(position, 1.0)).xyz;

    vec3 n = normalize((model * vec4(normal, 0.0)).xyz);
    vec3 t = normalize((model * vec4(tangent, 0.0)).xyz);
    t = normalize(t - dot(t, n) * n);

    vec3 biTangent = cross(t, n);
    TBNmatrix = mat3(t, biTangent, n);

    FragPosLightSpace = lightSpaceMatrix * vec4(FragPos, 1.0);
}
