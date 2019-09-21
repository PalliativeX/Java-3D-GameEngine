#version 330

in vec2 TexCoord;

out vec4 fragColor;

uniform sampler2D sampler;

void main()
{
  fragColor = texture2D(sampler, TexCoord.xy);
}
