package javaopenglgameengine;

import org.lwjgl.Version;

import javaopenglgameengine.core.WindowManager;
import javaopenglgameengine.core.EngineManager;
import javaopenglgameengine.core.utils.Consts;
import javaopenglgameengine.test.TestLogic;

public class App {

    private static WindowManager window;
    private static TestLogic game;

    public static void main(String[] args) {
        System.out.println(Version.getVersion());

        window = new WindowManager(Consts.TITLE, 1280, 720, false);
        game = new TestLogic();
        EngineManager engine = new EngineManager();
        try {
            engine.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static WindowManager getWindow() {
        return window;
    }

    public static TestLogic getGame() {
        return game;
    }
}
