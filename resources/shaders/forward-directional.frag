#version 330
#include "lighting.glh"

layout (location = 0) out vec4 FragColor;
layout (location = 1) out vec4 BrightColor;

in vec2 TexCoord;
in mat3 TBNmatrix;
in vec3 FragPos;

uniform sampler2D diffuse;
uniform sampler2D normalMap;
uniform DirectionalLight directionalLight;

void main()
{
    vec3 normal = normalize(TBNmatrix * (2.0 * texture2D(normalMap, TexCoord.xy).xyz - 1));
    FragColor = texture(diffuse, TexCoord.xy) * calcDirectionalLight(directionalLight, normal, FragPos);

    float brightness = dot(FragColor.rgb, vec3(0.2126, 0.7152, 0.0722));
    if(brightness > 1.0)
        BrightColor = vec4(FragColor.rgb, 1.0);
    else
        BrightColor = vec4(0.0, 0.0, 0.0, 1.0);
}
