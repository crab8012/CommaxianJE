package us.bluesakuradev.commaxian.desktop;
/**
 * Name: Jordan Petersen
 * Date: December 11, 2021
 * Assignment: Term Project
 * Class: CSC 240 C40 - Java Programming
 * Purpose: Entertain a bored person
 */
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import us.bluesakuradev.commaxian.Commaxian;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Commaxian(), config);
	}
}
