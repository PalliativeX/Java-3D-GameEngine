package com.base.engine.rendering.shaders;

import com.base.engine.core.RenderingEngine;
import com.base.engine.core.math.Transform;
import com.base.engine.rendering.Material;
import com.base.engine.core.Util;
import com.base.engine.core.math.Matrix4f;
import com.base.engine.core.math.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;

public class Shader
{
    private RenderingEngine renderingEngine;
    private int program;
    private HashMap<String, Integer> uniforms;

    public Shader()
    {
        program = glCreateProgram();
        uniforms = new HashMap<>();

        if (program == 0) {
            System.err.println("Shader creation failed: Could not find valid memory location in ctor");
            System.exit(1);
        }
    }

    public void bind()
    {
        glUseProgram(program);
    }


    public void updateUniforms(Transform transform, Material material)
    {

    }

    public void addUniform(String uniform)
    {
        int uniformLocation = glGetUniformLocation(program, uniform);
        if (uniformLocation == 0xFFFFFFFF) {
            System.err.println("Error: Could not find uniform:" + uniform);
            new Exception().printStackTrace();
            System.exit(1);
        }

        uniforms.put(uniform, uniformLocation);
    }

    public void addVertexShaderFromFile(String text)
    {
        addProgram(loadShader(text), GL_VERTEX_SHADER);
    }

    public void addGeometryShaderFromFile(String text)
    {
        addProgram(loadShader(text), GL_GEOMETRY_SHADER);
    }

    public void addFragmentShaderFromFile(String text)
    {
        addProgram(loadShader(text), GL_FRAGMENT_SHADER);
    }

    public void addVertexShader(String text)
    {
        addProgram(text, GL_VERTEX_SHADER);
    }

    public void addGeometryShader(String text)
    {
        addProgram(text, GL_GEOMETRY_SHADER);
    }

    public void addFragmentShader(String text)
    {
        addProgram(text, GL_FRAGMENT_SHADER);
    }

    public void compileShader()
    {
        glLinkProgram(program);
        if (glGetProgram(program, GL_LINK_STATUS) == 0) {
            System.err.println(glGetShaderInfoLog(program, 1024));
            System.exit(1);
        }

        glValidateProgram(program);
        if (glGetProgram(program, GL_VALIDATE_STATUS) == 0) {
            System.err.println(glGetShaderInfoLog(program, 1024));
            System.exit(1);
        }
    }

    private void addProgram(String text, int type)
    {
        int shader = glCreateShader(type);
        if (shader == 0) {
            System.err.println("Shader creation failed: Could not find vaild memory location when adding shader");
            System.exit(1);
        }

        glShaderSource(shader, text);
        glCompileShader(shader);
        if (glGetShader(shader, GL_COMPILE_STATUS) == 0) {
            System.err.println(glGetShaderInfoLog(shader, 1024));
            System.exit(1);
        }

        glAttachShader(program, shader);
    }

    public void setUniformi(String uniformName, int value)
    {
        addUniform(uniformName);
        glUniform1i(uniforms.get(uniformName), value);
    }

    public void setUniformf(String uniformName, float value)
    {
        addUniform(uniformName);
        glUniform1f(uniforms.get(uniformName), value);
    }

    public void setUniformVec3(String uniformName, Vector3f value)
    {
        addUniform(uniformName);
        glUniform3f(uniforms.get(uniformName), value.getX(), value.getY(), value.getZ());
    }

    public void setUniformMat4(String uniformName, Matrix4f value)
    {
        addUniform(uniformName);
        glUniformMatrix4(uniforms.get(uniformName), true, Util.createFlippedBuffer(value));
    }

    private static String loadShader(String fileName)
    {
        StringBuilder shaderSource = new StringBuilder();
        BufferedReader shaderReader = null;

        try {
            shaderReader = new BufferedReader(new FileReader("./resources/shaders/" + fileName));
            String line;

            while((line = shaderReader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }

            shaderReader.close();
        }
        catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }


        return shaderSource.toString();
    }

    public void setRenderingEngine(RenderingEngine renderingEngine)
    {
        this.renderingEngine = renderingEngine;
    }

    public RenderingEngine getRenderingEngine()
    {
        return renderingEngine;
    }

}
