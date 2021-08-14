package util.model;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ColorChooserButton extends JPanel {
	private static final long serialVersionUID = 1L;

	private String text;
	private Component component;
	private boolean hover;
	private boolean pressing;
	private ArrayList<ChangeListener> changeListener;

	private static final Color BORDER_NORMAL = new Color(175, 175, 175);
	private static final Color BORDER_HOVER = new Color(2, 121, 215);
	private static final Color BORDER_CLICK = new Color(2, 86, 154);

	public ColorChooserButton(Component component, String text, Color c) {
		this.component = component;
		this.changeListener = new ArrayList<>(1);
		setText(text);
		setColor(c);
		addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				hover = true;
				repaint();
			}

			public void mouseExited(MouseEvent e) {
				hover = false;
				pressing = false;
				repaint();
			}

			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					pressing = true;
					repaint();

					Color c = JColorChooser.showDialog(getComponent(), getText(), getColor());
					if (c != null) {
						setColor(c);

						for (ChangeListener l : changeListener) {
							l.stateChanged(new ChangeEvent(getThis()));
						}
					}
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					pressing = false;
					repaint();
				}
			}
		});
	}

	public ColorChooserButton(Component component, String text) {
		this(component, text, Color.WHITE);
	}

	// ========================================

	public ColorChooserButton getThis() {
		return this;
	}

	public void addChangeListener(ChangeListener l) {
		changeListener.add(l);
	}

	private Component getComponent() {
		return component;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setColor(Color c) {
		super.setBackground(c);
	}

	public Color getColor() {
		return super.getBackground();
	}

	public void paintBorder(Graphics g) {
		super.paintBorder(g);
		Color c;
		if (pressing)
			c = BORDER_CLICK;
		else if (hover)
			c = BORDER_HOVER;
		else
			c = BORDER_NORMAL;
		g.setColor(c);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
	}
}
