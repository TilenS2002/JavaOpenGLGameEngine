package javaopenglgameengine.core.lighting;

import org.joml.Vector3f;

public class DirectionalLight {
    
    private Vector3f color, direction;
    private float intensity;

    public DirectionalLight(Vector3f color, Vector3f direction, float intensity) {
        this.color = color;
        this.direction = direction;
        this.intensity = intensity;
    }

    public Vector3f getColor() {
        return this.color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public Vector3f getDirection() {
        return this.direction;
    }

    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }

    public float getIntensity() {
        return this.intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }
    
}
