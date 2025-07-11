package javaopenglgameengine.core.lighting;

import org.joml.Vector3f;

public class PointLight {

    private Vector3f color, position;
    private float intensity;
    private Attenuation attenuation;

    public PointLight(Vector3f color, Vector3f position, float intenstiy, Attenuation attenuation) {
        this.color = color;
        this.position = position;
        this.intensity = intenstiy;
        this.attenuation = attenuation;
    }

    public PointLight(Vector3f color, Vector3f position, float intensity) {
        this(color, position, intensity, new Attenuation(1, 0, 0));
    }

    public PointLight(PointLight pointLight) {
        this(new Vector3f(pointLight.getColor()), new Vector3f(pointLight.getPosition()), pointLight.getIntensity(), pointLight.getAttenuation());
    }

    public static class Attenuation {
        private float constant, linear, exponent;

        public Attenuation(float constant, float linear, float exponent) {
            this.constant = constant;
            this.linear = linear;
            this.exponent = exponent;
        }

        public float getConstant() {
            return this.constant;
        }
        public void setConstant(float constant) {
            this.constant = constant;
        }
        public float getLinear() {
            return this.linear;
        }
        public void setLinear(float linear) {
            this.linear = linear;
        }
        public float getExponent() {
            return this.exponent;
        }
        public void setExponent(float exponent) {
            this.exponent = exponent;
        }
    }

    public Attenuation getAttenuation() {
        return attenuation;
    }
    public void setAttenuation(Attenuation attenuation) {
        this.attenuation = attenuation;
    }
    public Vector3f getColor() {
        return this.color;
    }
    public void setColor(Vector3f color) {
        this.color = color;
    }
    public Vector3f getPosition() {
        return this.position;
    }
    public void setPosition(Vector3f position) {
        this.position = position;
    }
    public float getIntensity() {
        return this.intensity;
    }
    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

}
