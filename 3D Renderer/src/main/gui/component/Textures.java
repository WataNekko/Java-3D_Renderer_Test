package main.gui.component;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Textures {
	public static final BufferedImage SLIDER_CONTAINER;
	public static final BufferedImage BUTTON_NORMAL;
	public static final BufferedImage BUTTON_HIGHLIGHTED;
	
	static {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("src/resources/textures/gui/widgets.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		SLIDER_CONTAINER = img.getSubimage(0, 0, 200, 20);
		BUTTON_NORMAL = img.getSubimage(0, 20, 200, 20);
		BUTTON_HIGHLIGHTED = img.getSubimage(0, 40, 200, 20);
	}
	
	private Textures() {
	}
}
