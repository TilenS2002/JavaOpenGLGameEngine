package javaopenglgameengine.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joml.Vector2f;
import org.joml.Vector3f;
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
import javaopenglgameengine.core.entety.Texture;
import javaopenglgameengine.core.entety.terrain.Terrain;
import javaopenglgameengine.core.lighting.DirectionalLight;
import javaopenglgameengine.core.lighting.PointLight;
import javaopenglgameengine.core.lighting.SpotLight;
import javaopenglgameengine.core.rendering.RenderManager;
import javaopenglgameengine.core.utils.Consts;

public class TestLogic implements iLogic{

    private static final float CAMERA_MOVE_SPEED = 0.05f;

    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;

    private List<Entity> entities;
    private List<Terrain> terrains;
    private Camera camera;

    private float lightAngle;
    private DirectionalLight directionalLight;
    private PointLight[] pointLights;
    private SpotLight[] spotLights;

    Vector3f cameraInc;

    public TestLogic() {
        renderer = new RenderManager();
        window = App.getWindow();
        loader = new ObjectLoader();
        camera = new Camera();
        cameraInc = new Vector3f(0, 0, 0);
        lightAngle = -90;
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        Model model = loader.loadOBJModel("/models/bunny.obj");
        model.setTexture(new Texture(loader.loadTexture("textures/images.jpg")), 1f);

        terrains = new ArrayList<>();
        Terrain terrain = new Terrain(new Vector3f(0, -1, -800), loader, new Material(new Texture(loader.loadTexture("textures/images.jpg")), 0.1f));
        Terrain terrain2 = new Terrain(new Vector3f(-800, -1, -800), loader, new Material(new Texture(loader.loadTexture("textures/images.jpg")), 0.1f));
        terrains.add(terrain);
        terrains.add(terrain2);


        entities = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < 200; i++) {
            float x = rand.nextFloat() * 100 - 50;
            float y = rand.nextFloat() * 100 - 50;
            float z = rand.nextFloat() * -300;
            entities.add(new Entity(model, new Vector3f(x,y,z), new Vector3f(rand.nextFloat() * 180, rand.nextFloat() * 180, 0), 1));
        }
        entities.add(new Entity(model, new Vector3f(0,0,-2f), new Vector3f(0,0,0), 1));

        // entity = new Entity(model, new Vector3f(0, 0, -5), new Vector3f(0, 0, 0), 5);
    
        float lightIntensity = 1.0f;
        Vector3f lightPosition = new Vector3f(0, 0, -3.2f);
        Vector3f lightColor = new Vector3f(1, 1, 1);
        PointLight pointLight = new PointLight(lightColor, lightPosition, lightIntensity);

        Vector3f coneDir = new Vector3f(0, 0, 1);
        float cutoff = (float) Math.cos(Math.toRadians(180));
        SpotLight spotLight = new SpotLight(new PointLight(lightColor, new Vector3f(0, 0, 1f), lightIntensity, 0, 0, 1), coneDir, cutoff);
        
        SpotLight spotLight2 = new SpotLight(pointLight, coneDir, cutoff);

        lightPosition = new Vector3f(0, 0, -3);
        lightColor = new Vector3f(1, 1, 1);
        directionalLight = new DirectionalLight(lightColor, lightPosition, lightIntensity);

        pointLights = new PointLight[]{pointLight};
        spotLights = new SpotLight[]{spotLight, spotLight2};
    }

    @Override
    public void input() {
        cameraInc.set(0, 0, 0);
        spotLights[0].getPointLight().setPosition(cameraInc);
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
        
        float lightPos = spotLights[0].getPointLight().getPosition().z;

        if (window.isKeyPressed(GLFW.GLFW_KEY_O))
            spotLights[0].getPointLight().getColor().z = lightPos + 0.1f;
        if (window.isKeyPressed(GLFW.GLFW_KEY_P))
            spotLights[0].getPointLight().getColor().z = lightPos - 0.1f;
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        camera.movePosition(cameraInc.x * Consts.CAMERA_STEP, cameraInc.y * Consts.CAMERA_STEP, cameraInc.z * CAMERA_MOVE_SPEED);

        if (mouseInput.isRightButtonPress())
        {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * Consts.MOUSE_SENSITIVITY, rotVec.y * Consts.MOUSE_SENSITIVITY, 0);
        }

        // entity.incRotation(0.0f, 0.25f, 0.0f);
        lightAngle += 0.25f;
        if (lightAngle > 90) {
            directionalLight.setIntensity(0);
            if (lightAngle >= 360)
                lightAngle = -90;
        } else if (lightAngle <= -80 || lightAngle >= 80) {
            float factor = 1 - (Math.abs(lightAngle) - 80) / 10.0f;
            directionalLight.setIntensity(factor);
            directionalLight.getColor().y = Math.max(factor, 0.9f);
            directionalLight.getColor().z = Math.max(factor, 0.5f);
        } else {
            directionalLight.setIntensity(1);
            directionalLight.getColor().x = 1;
            directionalLight.getColor().y = 1;
            directionalLight.getColor().z = 1;
        }
        double angRad = Math.toRadians(lightAngle);
        directionalLight.getDirection().x = (float) Math.sin(angRad);
        directionalLight.getDirection().y = (float) Math.cos(angRad);

        for (Entity entity : entities) {
            renderer.processEntity(entity);
        }

        for(Terrain terrain : terrains) {
            renderer.processTerrain(terrain);
        }
    }

    @Override
    public void render() {
        

        renderer.render(camera, directionalLight, pointLights, spotLights);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }
    
}
