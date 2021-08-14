package util;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class ImageEditor {
	private static final BufferedImageOp BLUR;

	static {
		float weight = 1f / 9f;
		Kernel kernel = new Kernel(3, 3, new float[] { weight, weight, weight, weight, weight, weight, weight, weight, weight });
		BLUR = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
	}

	private ImageEditor() {
	}

	public static BufferedImage blurImage(BufferedImage image) {
		return BLUR.filter(image, null);
	}
}
