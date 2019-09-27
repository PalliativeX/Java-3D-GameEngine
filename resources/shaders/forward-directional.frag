#version 330

out vec4 fragColor;

in vec2 TexCoord;
in vec3 Normal;
in vec3 FragPos;

struct BaseLight
{
    vec3 color;
    float intensity;
};

struct DirectionalLight
{
    BaseLight base;
    vec3 direction;
};

uniform vec3 eyePos;
uniform sampler2D diffuse;

uniform float specularIntensity;
uniform float specularPower;

uniform DirectionalLight directionalLight;

vec4 calcLight(BaseLight base, vec3 direction, vec3 normal);
vec4 calcDirectionalLight(DirectionalLight directionalLight, vec3 normal);

void main()
{
    fragColor = texture(diffuse, TexCoord.xy) * calcDirectionalLight(directionalLight, normalize(Normal));
}



vec4 calcLight(BaseLight base, vec3 direction, vec3 normal)
{
    float diffuseFactor = dot(normal, -direction);

    vec4 diffuseColor  = vec4(0, 0, 0, 0);
    vec4 specularColor = vec4(0, 0, 0, 0);

    if (diffuseFactor > 0) {
        diffuseColor = vec4(base.color, 1.0) * base.intensity * diffuseFactor;

        // calculating specular color
        vec3 directionToEye = normalize(eyePos - FragPos);
        vec3 reflectDir = normalize(reflect(direction, normal));

        float specularFactor = dot(directionToEye, reflectDir);
        specularFactor = pow(specularFactor, specularPower);

        if (specularFactor > 0) {
            specularColor = vec4(base.color, 1.0) * specularIntensity * specularFactor;
        }
    }

    return diffuseColor + specularColor;
}

vec4 calcDirectionalLight(DirectionalLight directionalLight, vec3 normal)
{
    return calcLight(directionalLight.base, -directionalLight.direction, normal);
}