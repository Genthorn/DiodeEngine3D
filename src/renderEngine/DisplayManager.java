package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.*;

public class DisplayManager {

	private static final int WIDTH = 1280;
	private static final int HEIGHT = WIDTH / 16 * 9;
	private static final int FPS_CAP = 120;

	private static final String TITLE = "Diode Engine";

	private static long lastFrameTime;
	private static float delta;

	public static void createDisplay(String title) {

		ContextAttribs attribs = new ContextAttribs(3, 3)
				.withForwardCompatible(true)
				.withProfileCore(true);

		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			//Display.setDisplayMode(Display.getDesktopDisplayMode());
			//Display.setFullscreen(true);
			Display.create(new PixelFormat().withSamples(8).withDepthBits(24), attribs);
			GL11.glEnable(GL13.GL_MULTISAMPLE);
			if(title == null) Display.setTitle(TITLE);
			else Display.setTitle(title);
			
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		lastFrameTime = getCurrentTime();
	}

	public static void updateDisplay() {
		Display.sync(FPS_CAP);
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
	}

	public static float getFrameTimeSeconds() {
		return delta;
	}

	public static void closeDisplay() {
		Display.destroy();
		System.exit(0);
	}

	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}

}