#version 330 core
out vec4 FragColor;

in vec2 TexCoord;

uniform sampler2D hdrBuffer;
//uniform bool hdr;
uniform float exposure;

void main()
{
    vec3 hdrColor = texture(hdrBuffer, TexCoord).rgb;

    // reinhard
    // vec3 result = hdrColor / (hdrColor + vec3(1.0));
    vec3 result = vec3(1.0) - exp(-hdrColor * exposure);

    // also gamma correct while we're at it
    //const float gamma = 2.2;
    //result = pow(result, vec3(1.0 / gamma));

    FragColor = vec4(result, 1.0);
}