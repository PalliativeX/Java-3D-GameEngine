#version 330
#include "lighting.glh"

out vec4 fragColor;

in vec2 TexCoord;
in vec3 Normal;
in vec3 FragPos;

uniform sampler2D diffuse;
uniform PointLight pointLight;

void main()
{
    fragColor = texture(diffuse, TexCoord.xy) * calcPointLight(pointLight, normalize(Normal), FragPos);
}



