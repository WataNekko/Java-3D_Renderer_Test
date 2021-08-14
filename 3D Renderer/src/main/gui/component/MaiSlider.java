package main.gui.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MaiSlider extends JPanel {
	private static final long serialVersionUID = 1L;

	private boolean hover;
	private boolean hoverBtn;
	private String text;
	private Font font;
	private JPanel sliderBtn;
	private static final int BTN_WIDTH = 10;
	private float value = 0.5f;

	private ArrayList<ChangeListener> changeListener;

	public MaiSlider(String text) {
		this.text = text;
		changeListener = new ArrayList<>(1);

		setLayout(null);

		sliderBtn = new JPanel() {
			private static final long serialVersionUID = 1L;
			private boolean init;
			private boolean dragging;

			{
				addMouseMotionListener(new MouseAdapter() {
					public void mouseDragged(MouseEvent e) {
						updateBtnLocation();
					}
				});

				addMouseListener(new MouseAdapter() {
					public void mouseEntered(MouseEvent e) {
						hoverBtn = true;
						repaint();
					}

					public void mouseExited(MouseEvent e) {
						hoverBtn = false;
						repaint();
					}

					public void mousePressed(MouseEvent e) {
						dragging = true;
					}

					public void mouseReleased(MouseEvent e) {
						dragging = false;
					}
				});

			}

			protected void paintComponent(Graphics g) {
				if (!init) {
					updateBtn();
					init = true;
				}

				BufferedImage texture;
				if (hoverBtn || dragging)
					texture = Textures.BUTTON_HIGHLIGHTED;
				else
					texture = Textures.BUTTON_NORMAL;

				g.drawImage(texture.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT), 0, 0, null);
			}
		};
		add(sliderBtn);

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
				updateBtnLocation();
			}
		});

		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				updateBtn();
				if (text != null)
					updateFont();
			}
		});

	}

	public MaiSlider() {
		this(null);
	}

	public void addChangeListener(ChangeListener l) {
		changeListener.add(l);
	}

	private void updateValue() {
		value = (float) (sliderBtn.getLocation().x + (BTN_WIDTH / 2f)) / this.getWidth();
		stateChanged();
	}

	private void stateChanged() {
		for (ChangeListener l : changeListener) {
			l.stateChanged(new ChangeEvent(this));
		}
	}

	private void updateBtn() {
		sliderBtn.setBounds((int) (getWidth() * value - (BTN_WIDTH / 2)), 0, BTN_WIDTH, this.getHeight());
	}

	private void updateBtnLocation() {
		Point p = getMousePosition(true);
		if (p != null) {
			sliderBtn.setLocation(p.x - (BTN_WIDTH / 2), 0);
			updateValue();
		}
	}

	private void updateFont() {
		int size = (int) (this.getHeight() * 0.36);
		font = new Font("Comic Sans MS", Font.BOLD, size);
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		if (0 <= value && value <= 1) {
			this.value = value;
			updateBtn();
			stateChanged();
		}
		else {
			throw new IllegalArgumentException();			
		}
	}

	public void setText(String text) {
		this.text = text;
		repaint();
	}

	public String getText() {
		return text;
	}

	protected void paintComponent(Graphics g) {
		BufferedImage texture = Textures.SLIDER_CONTAINER;

		g.drawImage(texture.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT), 0, 0, null);

		if (text != null) {
			if (font == null)
				updateFont();

			g.setFont(font);
			FontMetrics fm = g.getFontMetrics();

			int x = (this.getWidth() - fm.stringWidth(text)) / 2;
			int y = (this.getHeight() + fm.getHeight() / 2) / 2;

			Color c;
			if (hover || hoverBtn)
				c = Color.YELLOW;
			else
				c = Color.WHITE;

			g.setColor(c);
			g.drawString(text, x, y);
		}
	}

	public void reset() {
		setValue(0.5f);
	}
}
