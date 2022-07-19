package net.torocraft.torohealth;

import net.torocraft.torohealth.display.Hud;
import net.torocraft.torohealth.util.RayTrace;

public class ToroHealthClient {
	public static Hud HUD = new Hud();
	public static RayTrace RAYTRACE = new RayTrace();

	public static void init() {
		ClientEventHandler.init();
	}
}
