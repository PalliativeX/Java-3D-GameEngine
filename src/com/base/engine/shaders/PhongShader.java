package com.base.engine.shaders;

import com.base.engine.*;
import com.base.engine.math.Matrix4f;
import com.base.engine.math.Vector3f;

import static org.lwjgl.opengl.GL20.glUniform1i;

public class PhongShader extends Shader
{
    private static final PhongShader instance = new PhongShader();

    public static PhongShader getInstance() { return instance; }

    private static Vector3f ambientLight = new Vector3f(0.15f, 0.15f, 0.15f);
    private static DirectionalLight directionalLight = new DirectionalLight(new BaseLight(new Vector3f(1, 1, 1), 0), new Vector3f(0, 0, 0));

    private PhongShader()
    {
        super();

        addVertexShader(ResourceLoader.loadShader("phongVertex.vert"));
        addFragmentShader(ResourceLoader.loadShader("phongFragment.frag"));
        compileShader();
    }

    public void updateUniforms(Matrix4f worldMatrix, Matrix4f projectedMatrix, Material material)
    {
        if (material.getTexture() != null)
            material.getTexture().bind();
        else
            RenderUtil.unbindTextures();

        setUniformMat4("transformProjected", projectedMatrix);
        setUniformMat4("transform", worldMatrix);
        setUniformVec3("baseColor", material.getColor());
        setUniformVec3("ambientLight", ambientLight);

        setUniform("directionalLight", directionalLight);
    }

    public static Vector3f getAmbientLight() {
        return ambientLight;
    }

    public static void setAmbientLight(Vector3f ambientLight) {
        PhongShader.ambientLight = ambientLight;
    }

    public static void setDirectionalLight(DirectionalLight directionalLight) {
        PhongShader.directionalLight = directionalLight;
    }

    public void setUniform(String uniformName, BaseLight baseLight)
    {
        setUniformVec3(uniformName + ".color", baseLight.getColor());
        setUniformf(uniformName + ".intensity", baseLight.getIntensity());
    }

    public void setUniform(String uniformName, DirectionalLight directionalLight)
    {
        setUniform(uniformName + ".base", directionalLight.getBase());
        setUniformVec3(uniformName + ".direction", directionalLight.getDirection());
    }

}
