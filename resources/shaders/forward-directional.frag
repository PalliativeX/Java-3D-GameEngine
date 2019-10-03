#version 330
#include "lighting.glh"

out vec4 fragColor;

in vec2 TexCoord;
in vec3 Normal;
in vec3 FragPos;

uniform sampler2D diffuse;
uniform DirectionalLight directionalLight;

void main()
{
    fragColor = texture(diffuse, TexCoord.xy) * calcDirectionalLight(directionalLight, normalize(Normal), FragPos);
}
