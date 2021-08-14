package text;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Text {
	private String text;
	private Color color;
	private Font font;

	public Text(String t, Color c, Font f) {
		this.text = t;
		this.color = c;
		this.font = f;
	}

	public Text(Color c, Font f) {
		this.text = "";
		this.color = c;
		this.font = f;
	}

	public void print(Graphics g, int x, int y) {
		g.setFont(this.font);
		g.setColor(this.color);
		
		for (String line : text.split("\n")) {
			g.drawString(line, x, y += g.getFontMetrics().getHeight());
		}
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

}
