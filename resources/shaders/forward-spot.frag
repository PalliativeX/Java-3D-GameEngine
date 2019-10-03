#version 330
#include "lighting.glh"

out vec4 fragColor;

in vec2 TexCoord;
in vec3 Normal;
in vec3 FragPos;

uniform sampler2D diffuse;
uniform SpotLight spotLight;

void main()
{
    fragColor = texture(diffuse, TexCoord.xy) * calcSpotLight(spotLight, normalize(Normal), FragPos);
}



