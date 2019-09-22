package com.base.engine.shaders;

import com.base.engine.*;
import com.base.engine.light.BaseLight;
import com.base.engine.light.DirectionalLight;
import com.base.engine.light.PointLight;
import com.base.engine.light.SpotLight;
import com.base.engine.math.Matrix4f;
import com.base.engine.math.Vector3f;

import java.awt.*;


public class PhongShader extends Shader
{
    private static final int MAX_POINT_LIGHTS = 4;
    private static final int MAX_SPOT_LIGHTS = 4;

    private static final PhongShader instance = new PhongShader();

    public static PhongShader getInstance() { return instance; }

    private static Vector3f ambientLight = new Vector3f(0.15f, 0.15f, 0.15f);
    private static DirectionalLight directionalLight = new DirectionalLight(new BaseLight(new Vector3f(1, 1, 1), 0), new Vector3f(0, 0, 0));
    private static PointLight[] pointLights = new PointLight[] {};
    private static SpotLight[] spotLights = new SpotLight[] {};

    private PhongShader()
    {
        super();

        addVertexShaderFromFile("phongVertex.vert");
        addFragmentShaderFromFile("phongFragment.frag");
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

        setUniformDirLight("directionalLight", directionalLight);

        //specular light
        setUniformf("specularIntensity", material.getSpecularIntensity());
        setUniformf("specularPower", material.getSpecularPower());
        setUniformVec3("eyePos", Transform.getCamera().getPos());

        // set point lights
        for (int i = 0; i < pointLights.length; i++)
            setUniform("pointLights[" + i + "]", pointLights[i]);

        // setting spot lights
        for (int i = 0; i <spotLights.length; i++)
            setUniformSpotLight("spotLights[" + i + "]", spotLights[i]);

    }

    public static Vector3f getAmbientLight()
    {
        return ambientLight;
    }

    public static void setAmbientLight(Vector3f ambientLight)
    {
        PhongShader.ambientLight = ambientLight;
    }

    public static void setDirectionalLight(DirectionalLight directionalLight)
    {
        PhongShader.directionalLight = directionalLight;
    }

    public static void setPointLights(PointLight[] pointLights)
    {
        if (pointLights.length > MAX_POINT_LIGHTS) {
            System.err.println("Error: You passed in too many point lights. Max allowed is " + MAX_POINT_LIGHTS + ", you passed in " + pointLights.length);
            new Exception().printStackTrace();
            System.exit(1);
        }

        PhongShader.pointLights = pointLights;
    }

    public static void setSpotLights(SpotLight[] spotLights)
    {
        if (spotLights.length > MAX_SPOT_LIGHTS) {
            System.err.println("Error: You passed in too many point lights. Max allowed is " + MAX_POINT_LIGHTS + ", you passed in " + spotLights.length);
            new Exception().printStackTrace();
            System.exit(1);
        }

        PhongShader.spotLights = spotLights;
    }


    public void setUniform(String uniformName, BaseLight baseLight)
    {
        setUniformVec3(uniformName + ".color", baseLight.getColor());
        setUniformf(uniformName + ".intensity", baseLight.getIntensity());
    }

    public void setUniformDirLight(String uniformName, DirectionalLight directionalLight)
    {
        setUniform(uniformName + ".base", directionalLight.getBase());
        setUniformVec3(uniformName + ".direction", directionalLight.getDirection());
    }

    public void setUniform(String uniformName, PointLight pointLight)
    {
        setUniform(uniformName + ".base", pointLight.getBaseLight());
        setUniformf(uniformName + ".attenuation.constant", pointLight.getAttenuation().getConstant());
        setUniformf(uniformName + ".attenuation.linear", pointLight.getAttenuation().getLinear());
        setUniformf(uniformName + ".attenuation.exponent", pointLight.getAttenuation().getExponent());
        setUniformVec3(uniformName + ".position", pointLight.getPosition());
        setUniformf(uniformName + ".range", pointLight.getRange());
    }

    public void setUniformSpotLight(String uniformName, SpotLight spotLight)
    {
        setUniform(uniformName + ".pointLight", spotLight.getPointLight());
        setUniformVec3(uniformName + ".direction", spotLight.getDirection());
        setUniformf(uniformName + ".cutOff", spotLight.getCutOff());
    }


}
