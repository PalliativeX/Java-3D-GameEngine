#version 330

in vec2 TexCoord;


uniform vec3 ambientIntensity;
uniform sampler2D sampler;

void main()
{
    gl_FragColor = texture2D(sampler, TexCoord.xy) * vec4(ambientIntensity, 1.0);
}
