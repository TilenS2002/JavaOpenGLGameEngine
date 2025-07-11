package javaopenglgameengine.test;

import java.util.Random;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import javaopenglgameengine.App;
import javaopenglgameengine.core.Camera;
import javaopenglgameengine.core.MouseInput;
import javaopenglgameengine.core.ObjectLoader;
import javaopenglgameengine.core.WindowManager;
import javaopenglgameengine.core.iLogic;
import javaopenglgameengine.core.entety.Entity;
import javaopenglgameengine.core.entety.Material;
import javaopenglgameengine.core.entety.Model;
import javaopenglgameengine.core.entety.SceneManager;
import javaopenglgameengine.core.entety.Texture;
import javaopenglgameengine.core.entety.terrain.BlendMapTerrain;
import javaopenglgameengine.core.entety.terrain.Terrain;
import javaopenglgameengine.core.entety.terrain.TerrainTexture;
import javaopenglgameengine.core.lighting.DirectionalLight;
import javaopenglgameengine.core.lighting.PointLight;
import javaopenglgameengine.core.lighting.SpotLight;
import javaopenglgameengine.core.rendering.RenderManager;
import javaopenglgameengine.core.utils.Consts;

public class TestLogic implements iLogic {

    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;

    private SceneManager sceneManager;

    private Camera camera;

    Vector3f cameraInc;

    public TestLogic() {
        renderer = new RenderManager();
        window = App.getWindow();
        loader = new ObjectLoader();
        camera = new Camera();
        cameraInc = new Vector3f(0, 0, 0);
        sceneManager = new SceneManager(-90);
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        Model model = loader.loadOBJModel("/models/bunny.obj");
        model.setTexture(new Texture(loader.loadTexture("textures/Tileable classic water texture.jpg")), 1f);
        // model.getMaterial().setDisableCulling(true);

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("textures/stone.png"));
        TerrainTexture redTexture = new TerrainTexture(loader.loadTexture("textures/dirt.png"));
        TerrainTexture greenTexture = new TerrainTexture(loader.loadTexture("textures/grass03.png"));
        TerrainTexture blueTexture = new TerrainTexture(loader.loadTexture("textures/Tileable classic water texture.jpg"));
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("textures/images.jpg"));

        BlendMapTerrain blendMapTerrain = new BlendMapTerrain(backgroundTexture, redTexture, greenTexture, blueTexture);

        Terrain terrain = new Terrain(new Vector3f(0, -1, -800), loader, new Material(new Vector4f(0.0f, 0.0f, 0.0f, 0.0f), 0.1f), blendMapTerrain, blendMap);
        Terrain terrain2 = new Terrain(new Vector3f(-800, -1, -800), loader, new Material(new Vector4f(0.0f, 0.0f, 0.0f, 0.0f), 1.0f), blendMapTerrain, blendMap);
        sceneManager.addTerrain(terrain);
        sceneManager.addTerrain(terrain2);

        Random rand = new Random();
        for (int i = 0; i < 200; i++) {
            float x = rand.nextFloat() * 100 - 50;
            float y = rand.nextFloat() * 100 - 50;
            float z = rand.nextFloat() * -300;
            sceneManager.addEntity(new Entity(model, new Vector3f(x,y,z), new Vector3f(rand.nextFloat() * 180, rand.nextFloat() * 180, 0), 50));
        }
        sceneManager.addEntity(new Entity(model, new Vector3f(0,0,-2f), new Vector3f(0,0,0), 50));
    
        float lightIntensity = 0.0f;
        Vector3f lightPosition = new Vector3f(-1, -10, 0);
        Vector3f lightColor = new Vector3f(1, 1, 1);
        PointLight pointLight = new PointLight(lightColor, lightPosition, lightIntensity);

        Vector3f coneDir = new Vector3f(0, 0, 1);
        float cutoff = (float) Math.cos(Math.toRadians(180));
        SpotLight spotLight = new SpotLight(new PointLight(lightColor, new Vector3f(0, 0, 1f), lightIntensity, 0, 0, 1), coneDir, cutoff);
        
        SpotLight spotLight2 = new SpotLight(pointLight, coneDir, cutoff);

        lightPosition = new Vector3f(0, 0, -3);
        lightColor = new Vector3f(1, 1, 1);
        sceneManager.setDirectionalLight(new DirectionalLight(lightColor, lightPosition, lightIntensity));

        sceneManager.setPointLights(new PointLight[]{pointLight});
        sceneManager.setSpotLights(new SpotLight[]{spotLight, spotLight2});
    }

    @Override
    public void input() {
        cameraInc.set(0, 0, 0);

        if (window.isKeyPressed(GLFW.GLFW_KEY_W))
            cameraInc.z = -1;
        if (window.isKeyPressed(GLFW.GLFW_KEY_S))
            cameraInc.z = 1;
        if (window.isKeyPressed(GLFW.GLFW_KEY_A))
            cameraInc.x = -1;
        if (window.isKeyPressed(GLFW.GLFW_KEY_D))
            cameraInc.x = 1;
        if (window.isKeyPressed(GLFW.GLFW_KEY_Z))
            cameraInc.y = -1;
        if (window.isKeyPressed(GLFW.GLFW_KEY_X))
            cameraInc.y = 1;
        
        float lightPos = sceneManager.getSpotLights()[0].getPointLight().getPosition().z;

        if (window.isKeyPressed(GLFW.GLFW_KEY_O))
            sceneManager.getSpotLights()[0].getPointLight().getColor().z = lightPos + 0.1f;
        if (window.isKeyPressed(GLFW.GLFW_KEY_P))
            sceneManager.getSpotLights()[0].getPointLight().getColor().z = lightPos - 0.1f;
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        camera.movePosition(cameraInc.x * Consts.CAMERA_STEP, cameraInc.y * Consts.CAMERA_STEP, cameraInc.z * Consts.CAMERA_MOVE_SPEED);

        if (mouseInput.isRightButtonPress())
        {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * Consts.MOUSE_SENSITIVITY, rotVec.y * Consts.MOUSE_SENSITIVITY, 0);
        }

        // entity.incRotation(0.0f, 0.25f, 0.0f);

        sceneManager.incSpotAngle(0.15f);
        sceneManager.setSpotAngle(sceneManager.getSpotInc() * 0.15f);
        if (sceneManager.getSpotAngle() > 4)
            sceneManager.setSpotInc(-1);
        else if (sceneManager.getSpotAngle() <= -4)
            sceneManager.setSpotInc(1);
        
        double spotAngleRed = Math.toRadians(sceneManager.getSpotAngle());
        Vector3f coneDir = sceneManager.getSpotLights()[0].getPointLight().getPosition();
        coneDir.x = (float) Math.sin(spotAngleRed);

        coneDir = sceneManager.getSpotLights()[1].getPointLight().getPosition();
        coneDir.z = (float) Math.cos(spotAngleRed * 0.15f);

        sceneManager.incLightAngle(0.5f);
        if (sceneManager.getLightAngle() > 90) {
            sceneManager.getDirectionalLight().setIntensity(0);
            if (sceneManager.getLightAngle() >= 360)
                sceneManager.setLightAngle(-90);
        } else if (sceneManager.getLightAngle() <= -80 || sceneManager.getLightAngle() >= 80) {
            float factor = 1 - (float) (Math.abs(sceneManager.getLightAngle()) - 80) / 10.0f;
            sceneManager.getDirectionalLight().setIntensity(factor);
            sceneManager.getDirectionalLight().getColor().y = Math.max(factor, 0.9f);
            sceneManager.getDirectionalLight().getColor().z = Math.max(factor, 0.5f);
        } else {
            sceneManager.getDirectionalLight().setIntensity(1);
            sceneManager.getDirectionalLight().getColor().x = 1;
            sceneManager.getDirectionalLight().getColor().y = 1;
            sceneManager.getDirectionalLight().getColor().z = 1;
        }
        double angRad = Math.toRadians(sceneManager.getLightAngle());
        sceneManager.getDirectionalLight().getDirection().x = (float) Math.sin(angRad);
        sceneManager.getDirectionalLight().getDirection().y = (float) Math.cos(angRad);

        for (Entity entity : sceneManager.getEntities()) {
            renderer.processEntity(entity);
        }

        for(Terrain terrain : sceneManager.getTerrains()) {
            renderer.processTerrain(terrain);
        }
    }

    @Override
    public void render() {
        renderer.render(camera, sceneManager);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }
    
}
