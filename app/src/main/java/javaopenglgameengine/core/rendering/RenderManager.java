package javaopenglgameengine.core.rendering;

import java.util.ArrayList;
import java.util.List;


import org.lwjgl.opengl.GL11;
import javaopenglgameengine.App;
import javaopenglgameengine.core.Camera;
import javaopenglgameengine.core.ShaderManager;
import javaopenglgameengine.core.WindowManager;
import javaopenglgameengine.core.entety.Entity;
import javaopenglgameengine.core.entety.SceneManager;
import javaopenglgameengine.core.entety.terrain.Terrain;
import javaopenglgameengine.core.lighting.DirectionalLight;
import javaopenglgameengine.core.lighting.PointLight;
import javaopenglgameengine.core.lighting.SpotLight;
import javaopenglgameengine.core.utils.Consts;

public class RenderManager {
    
    private final WindowManager window;
    private EntityRenderer entityRenderer;
    private TerrainRenderer terrainRenderer;

    private static boolean isCulling = false;

    public RenderManager() {
        window = App.getWindow();
    }

    public void init() throws Exception {
        entityRenderer = new EntityRenderer();
        terrainRenderer = new TerrainRenderer();
        entityRenderer.init();
        terrainRenderer.init();
    }

    public static void renderLights(PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight directionalLight, ShaderManager shader) {
        shader.setUniform("ambientLight", Consts.AMBIENT_LIGHT);
        shader.setUniform("specularPower", Consts.SPECULAR_POWER);

        int numLights = spotLights != null ? spotLights.length : 0;
        for (int i = 0; i < numLights; i++) {
            shader.setUniform("spotLights", spotLights[i], i);
        }

        numLights = pointLights != null ? pointLights.length : 0;
        for (int i = 0; i < numLights; i++) {
            shader.setUniform("pointLights", pointLights[i], i);
        }

        shader.setUniform("directionalLight", directionalLight);
    }

    public void render(Camera camera, SceneManager scene) {
        clear();

        if (window.isResize()) {
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResize(true);
        }

        entityRenderer.render(camera, scene.getPointLights(), scene.getSpotLights(), scene.getDirectionalLight());
        terrainRenderer.render(camera, scene.getPointLights(), scene.getSpotLights(), scene.getDirectionalLight());
    }

    public static void enableCulling() {
        if(!isCulling) {
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glCullFace(GL11.GL_BACK);
            isCulling = true;
        }
    }

    public static void disableCulling() {
        if(!isCulling) {
            GL11.glDisable(GL11.GL_CULL_FACE);
            isCulling = false;
        }
    }

    public void processEntity(Entity entity) {
        List<Entity> entityList = entityRenderer.getEntities().get(entity.getModel());
        if (entityList != null)
            entityList.add(entity);
        else {
            List<Entity> newEntityList = new ArrayList<>();
            newEntityList.add(entity);
            entityRenderer.getEntities().put(entity.getModel(), newEntityList);
        }
    }

    public void processTerrain(Terrain terrain) {
        terrainRenderer.getTerrain().add(terrain);
    }

    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup() {
        entityRenderer.cleanup();
        terrainRenderer.cleanup();
    }
}
