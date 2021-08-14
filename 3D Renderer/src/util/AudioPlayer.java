package util;

import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioPlayer {
	private static final String PATH = "src/resources/sounds/";

	public static void playSound(String sound) {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File(PATH + sound + ".wav")));
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
