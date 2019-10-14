#version 330

in vec2 TexCoord;

uniform vec3 ambientIntensity;
uniform sampler2D sampler;

void main()
{
    vec4 color = texture2D(sampler, TexCoord);
    if (color.a < 0.125)
        discard;
    gl_FragColor = color * vec4(ambientIntensity, 1.0);
}
