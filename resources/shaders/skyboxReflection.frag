#version 330 core

layout (location = 0) out vec4 FragColor;
//layout (location = 1) out vec4 BrightColor;

in vec3 FragPos;
in vec3 Normal;

uniform vec3 cameraPos;
uniform samplerCube skybox;

void main()
{
    vec3 normal = Normal;

    vec3 I = normalize(FragPos - cameraPos);
    vec3 R = reflect(I, normalize(normal));
    FragColor = vec4(texture(skybox, R).rgb, 1.0);

//    float brightness = dot(FragColor.rgb, vec3(0.2126, 0.7152, 0.0722));
//    if(brightness > 1.0)
//        BrightColor = vec4(FragColor.rgb, 1.0);
//    else
//        BrightColor = vec4(0.0, 0.0, 0.0, 1.0);
}