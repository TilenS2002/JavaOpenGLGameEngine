package javaopenglgameengine.core;

public interface iLogic {
    void init() throws Exception;
    void input();
    void update(float interval, MouseInput mouseInput);
    void render();
    void cleanup();
}
