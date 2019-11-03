#version 330 core
layout (location = 0) out vec4 FragColor;

in vec2 TexCoord;

uniform sampler2D hdrBuffer;
uniform sampler2D bloomBlur;

uniform float exposure;

void main()
{
    vec3 hdrColor = texture(hdrBuffer, TexCoord).rgb;
    vec3 bloomColor = texture(bloomBlur, TexCoord).rgb;

    hdrColor += bloomColor;

    // reinhard
    // vec3 result = hdrColor / (hdrColor + vec3(1.0));
    vec3 result = vec3(1.0) - exp(-hdrColor * exposure);

    // gamma correction
    //const float gamma = 2.2;
    //result = pow(result, vec3(1.0 / gamma));

    FragColor = vec4(result, 1.0);

}