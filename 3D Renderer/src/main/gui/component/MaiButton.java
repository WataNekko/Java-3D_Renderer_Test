package main.gui.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import util.AudioPlayer;

public class MaiButton extends JPanel {
	private static final long serialVersionUID = 1L;

	private boolean hover;
	private String text;
	private Font font;
	private ArrayList<ActionListener> actionListener;

	public MaiButton(String text) {
		this.text = text;
		actionListener = new ArrayList<>(1);

		addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				hover = true;
				repaint();
			}

			public void mouseExited(MouseEvent e) {
				hover = false;
				repaint();
			}

			public void mousePressed(MouseEvent e) {
				for (ActionListener l : actionListener) {
					l.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_FIRST, "Click"));
				}
				AudioPlayer.playSound("click");
			}
		});

		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				if (text != null)
					updateFont();
			}
		});

	}

	public MaiButton() {
		this(null);
	}

	public void addActionListener(ActionListener l) {
		actionListener.add(l);
	}

	public void setText(String text) {
		this.text = text;
		repaint();
	}

	public String getText() {
		return text;
	}

	private void updateFont() {
		int size = (int) (this.getHeight() * 0.36);
		font = new Font("Comic Sans MS", Font.BOLD, size);
	}

	protected void paintComponent(Graphics g) {
		BufferedImage texture;
		if (hover)
			texture = Textures.BUTTON_HIGHLIGHTED;
		else
			texture = Textures.BUTTON_NORMAL;

		g.drawImage(texture.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT), 0, 0, null);

		if (text != null) {
			if (font == null) {
				updateFont();
			}

			g.setFont(font);
			FontMetrics fm = g.getFontMetrics();

			int x = (this.getWidth() - fm.stringWidth(text)) / 2;
			int y = (this.getHeight() + fm.getHeight() / 2) / 2;

			g.setColor(Color.BLACK);
			g.drawString(text, x + 2, y + 2);

			Color c;
			if (hover)
				c = Color.YELLOW;
			else
				c = Color.WHITE;

			g.setColor(c);
			g.drawString(text, x, y);
		}
	}

}
