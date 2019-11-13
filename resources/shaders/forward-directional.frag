#version 330
#include "lighting.glh"

layout (location = 0) out vec4 FragColor;
layout (location = 1) out vec4 BrightColor;

in vec2 TexCoord;
in mat3 TBNmatrix;
in vec3 FragPos;

in vec4 FragPosLightSpace;

uniform sampler2D diffuse;
uniform sampler2D normalMap;
uniform sampler2D shadowMap;

uniform DirectionalLight directionalLight;

float calculateShadows(vec4 fragPosLightSpace)
{
    // perform perspective divide
    vec3 projCoords = fragPosLightSpace.xyz / fragPosLightSpace.w;
    // transform to [0,1] range
    projCoords = projCoords * 0.5 + 0.5;
    // get closest depth value from light's perspective (using [0,1] range fragPosLight as coords)
    float closestDepth = texture(shadowMap, projCoords.xy).r;
    // get depth of current fragment from light's perspective
    float currentDepth = projCoords.z;
    // check whether current frag pos is in shadow
    float shadow = currentDepth > closestDepth  ? 2 : 0;

    return shadow;
}

void main()
{
    vec3 normal = normalize(TBNmatrix * (2.0 * texture2D(normalMap, TexCoord.xy).xyz - 1));

    // calculate shadows
    float shadow = calculateShadows(FragPosLightSpace);
    FragColor = texture(diffuse, TexCoord.xy) * calcDirectionalLight(directionalLight, normal, FragPos) * vec4(shadow, shadow, shadow, 1.0);

    float brightness = dot(FragColor.rgb, vec3(0.2126, 0.7152, 0.0722));
    if(brightness > 1.0)
        BrightColor = vec4(FragColor.rgb, 1.0);
    else
        BrightColor = vec4(0.0, 0.0, 0.0, 1.0);
}
