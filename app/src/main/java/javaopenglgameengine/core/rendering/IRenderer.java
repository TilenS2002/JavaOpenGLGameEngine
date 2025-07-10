package javaopenglgameengine.core.rendering;

import javaopenglgameengine.core.Camera;
import javaopenglgameengine.core.entety.Model;
import javaopenglgameengine.core.lighting.DirectionalLight;
import javaopenglgameengine.core.lighting.PointLight;
import javaopenglgameengine.core.lighting.SpotLight;

public interface IRenderer<T> {
    public void init() throws Exception;

    public void render(Camera camera, PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight directionalLight);

    abstract void bind(Model model);

    public void unbind();

    public void prepare(T t, Camera camera);

    public void cleanup();
}
