package javaopenglgameengine.test;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import javaopenglgameengine.App;
import javaopenglgameengine.core.RenderManager;
import javaopenglgameengine.core.WindowManager;
import javaopenglgameengine.core.iLogic;

public class TestLogic implements iLogic{

    private int direction = 0;
    private float color = 0.0f;

    private final RenderManager renderer;
    private final WindowManager window;

    public TestLogic() {
        renderer = new RenderManager();
        window = App.getWindow();
    }

    @Override
    public void init() throws Exception {
        renderer.init();
    }

    @Override
    public void input() {
        if (window.isKeyPressed(GLFW.GLFW_KEY_UP))
            direction = 1;
        else if (window.isKeyPressed(GLFW.GLFW_KEY_DOWN))
            direction = -1;
        else
            direction = 0;
    }

    @Override
    public void update() {
        color += direction * 0.01f;
        if (color > 1)
            color = 1.0f;
        else if (color <= 0)
            color = 0.0f;
    }

    @Override
    public void render() {
        if (window.isResize()) {
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResize(true);
        }

        window.setClearColor(color, color, color, 0.0f);
        renderer.clear();
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
    }
    
}
