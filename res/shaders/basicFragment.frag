#version 330

in vec2 TexCoord;


uniform vec3 color;
uniform sampler2D sampler;

void main()
{
  vec4 textureColor = texture2D(sampler, TexCoord.xy);

  if (textureColor == 0)
    gl_FragColor = vec4(color, 1);
  else
    gl_FragColor = textureColor * vec4(color, 1);
}
