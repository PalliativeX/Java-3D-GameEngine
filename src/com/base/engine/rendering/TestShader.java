package com.base.engine.rendering;

public class TestShader extends Shader
{
        public TestShader()
        {
            super();

            addVertexShaderFromFile("testShader.vert");
            addFragmentShaderFromFile("testShader.frag");
            compileShader();
        }
}
