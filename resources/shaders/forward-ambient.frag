#version 330

layout (location = 0) out vec4 FragColor;
layout (location = 1) out vec4 BrightColor;

in vec2 TexCoord;

uniform vec3 ambientIntensity;
uniform sampler2D sampler;

void main()
{
    vec4 color = texture2D(sampler, TexCoord);
    if (color.a < 0.125)
        discard;

    FragColor = color * vec4(ambientIntensity, 1.0);

    float brightness = dot(FragColor.rgb, vec3(0.2126, 0.7152, 0.0722));
    if(brightness > 1.0)
        BrightColor = vec4(FragColor.rgb, 1.0);
    else
        BrightColor = vec4(0.0, 0.0, 0.0, 1.0);
}
