#version 330
#include "lighting.glh"

out vec4 FragColor;

in vec2 TexCoord;
in mat3 TBNmatrix;
in vec3 FragPos;

uniform sampler2D diffuse;
uniform sampler2D normalMap;
uniform DirectionalLight directionalLight;

void main()
{
    vec3 normal = normalize(TBNmatrix * (255.0/128.0 * texture2D(normalMap, TexCoord.xy).xyz - 1));
    FragColor = texture(diffuse, TexCoord.xy) * calcDirectionalLight(directionalLight, normal, FragPos);
}
