package main.gui;

import java.awt.AlphaComposite;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.Camera;
import main.Display;
import main.gui.component.MaiButton;
import main.gui.component.MaiSlider;
import util.ImageEditor;

public class PauseMenu extends JPanel {
	private static final long serialVersionUID = 1L;

	private CardLayout layout;
	private BufferedImage bg;
	private boolean[] bool;
	private Display display;
	private Camera camera;
	private BufferedImage image;

	private Container contentPane;
	private CardLayout parentLayout;

	private MaiSlider sldFov;
	private MaiSlider sldRendDist;

	public PauseMenu() {
		setDoubleBuffered(true);
		setIgnoreRepaint(true);
		setBackground(new Color(46, 41, 33));
		layout = new CardLayout();
		setLayout(layout);
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				if (bool[Display.PAUSE]) {
					updateBackground();
					repaint();
				}
			}
		});

		JPanel main = new JPanel() {
			private static final long serialVersionUID = 1L;

			{
				setOpaque(false);

				GridBagLayout gbl = new GridBagLayout();
				gbl.columnWidths = new int[] { 1, 1, 1 };
				gbl.rowHeights = new int[] { 1, 1, 1 };
				gbl.columnWeights = new double[] { 1.0, 1.0, 1.0 };
				gbl.rowWeights = new double[] { 1.0, 0.5, 1.0 };
				setLayout(gbl);

				GridBagConstraints gbc = new GridBagConstraints();
				gbc.fill = GridBagConstraints.BOTH;

				JPanel btnPane = new JPanel();
				gbc.gridx = 1;
				gbc.gridy = 1;
				btnPane.setOpaque(false);
				btnPane.setLayout(new GridBagLayout());
				add(btnPane, gbc);

				gbc.weightx = 1.0;
				gbc.weighty = 1.0;
				gbc.insets = new Insets(5, 5, 5, 5);
				gbc.gridx = 0;
				gbc.gridy = GridBagConstraints.RELATIVE;

				MaiButton btnBack = new MaiButton("Back");
				btnBack.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						done();
					}
				});
				btnPane.add(btnBack, gbc);

				MaiButton btnSettings = new MaiButton("Settings");
				btnSettings.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						layout.show(getThis(), "settings");
					}
				});
				btnPane.add(btnSettings, gbc);

				MaiButton btnQuit = new MaiButton("Quit");
				btnQuit.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						display.dispatchEvent(new WindowEvent(display, WindowEvent.WINDOW_CLOSING));
					}
				});
				btnPane.add(btnQuit, gbc);
			}
		};
		add(main, "main");

		JPanel settings = new JPanel() {
			private static final long serialVersionUID = 1L;

			{
				setOpaque(false);

				GridBagLayout gbl = new GridBagLayout();
				gbl.columnWidths = new int[] { 1, 1, 1 };
				gbl.rowHeights = new int[] { 1, 1, 1 };
				gbl.columnWeights = new double[] { 1.0, 2.0, 1.0 };
				gbl.rowWeights = new double[] { 1.0, 1.0, 1.0 };
				setLayout(gbl);

				GridBagConstraints gbc = new GridBagConstraints();
				gbc.fill = GridBagConstraints.BOTH;

				// Main panel
				JPanel btnPane = new JPanel();
				gbc.gridx = 1;
				gbc.gridy = 1;
				btnPane.setOpaque(false);
				btnPane.setLayout(new GridBagLayout());
				add(btnPane, gbc);

				// Split
				gbc.weightx = 1;
				gbc.gridx = 0;
				JPanel settingsPane = new JPanel(new GridBagLayout());
				settingsPane.setOpaque(false);
				gbc.gridy = 0;
				gbc.weighty = 3;
				gbc.insets = new Insets(0, 0, 5, 0);
				btnPane.add(settingsPane, gbc);

				JPanel returnPane = new JPanel(new GridBagLayout());
				returnPane.setOpaque(false);
				gbc.gridy = 1;
				gbc.weighty = 1;
				gbc.insets = new Insets(5, 0, 0, 0);
				btnPane.add(returnPane, gbc);

				// Left
				gbc.gridx = 0;
				gbc.insets = new Insets(5, 0, 5, 10);
				sldFov = new MaiSlider("FOV: " + Camera.DEFAULT_FOV);
				sldFov.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						float fov = (sldFov.getValue() * 80) + 30;
						camera.setFov(fov);
						sldFov.setText("FOV: " + fov);
						updateDisplay();
					}
				});
				gbc.gridy = 0;
				settingsPane.add(sldFov, gbc);

				MaiButton btn1 = new MaiButton();
				gbc.gridy = 1;
				settingsPane.add(btn1, gbc);

				MaiButton btn2 = new MaiButton();
				gbc.gridy = 2;
				settingsPane.add(btn2, gbc);

				// Right
				gbc.gridx = 1;
				gbc.insets = new Insets(5, 10, 5, 0);
				sldRendDist = new MaiSlider("Render Distance: " + Camera.DEFAULT_RENDER_DISTANCE);
				sldRendDist.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						float rd = sldRendDist.getValue() * 500;
						camera.setRenderDistance(rd);
						sldRendDist.setText("Render Distance: " + rd);
						updateDisplay();
					}
				});
				gbc.gridy = 0;
				settingsPane.add(sldRendDist, gbc);

				MaiButton btn3 = new MaiButton();
				gbc.gridy = 1;
				settingsPane.add(btn3, gbc);

				MaiButton btnReset = new MaiButton("Reset");
				btnReset.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						sldFov.reset();
						sldRendDist.reset();
					}
				});
				gbc.gridy = 2;
				settingsPane.add(btnReset, gbc);

				// Bottom
				gbc.gridy = 0;
				gbc.weightx = 1;
				gbc.weighty = 1;
				gbc.fill = GridBagConstraints.BOTH;
				MaiButton btnBack = new MaiButton("Back");
				btnBack.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						layout.show(getThis(), "main");
					}
				});
				gbc.gridx = 0;
				gbc.insets = new Insets(0, 20, 10, 5);
				returnPane.add(btnBack, gbc);

				MaiButton btnDone = new MaiButton("Done");
				btnDone.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						done();
					}
				});
				gbc.gridx = 1;
				gbc.insets = new Insets(0, 5, 10, 20);
				returnPane.add(btnDone, gbc);

			}
		};
		add(settings, "settings");

	}

	public PauseMenu(Display d, boolean[] bool, Camera cam) {
		this();
		this.display = d;
		this.bool = bool;
		this.camera = cam;

		contentPane = this.display.getContentPane();
		parentLayout = (CardLayout) contentPane.getLayout();
	}

	private void updateDisplay() {
		display.renderToImage();
		updateBackground();
		repaint();
	}

	private void done() {
		bool[Display.PAUSE] = false;
	}

	private PauseMenu getThis() {
		return this;
	}

	private void updateBackground() {
		this.bg = ImageEditor.blurImage(this.image);
		Graphics2D g2 = bg.createGraphics();
		g2.setColor(this.getBackground());
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
		g2.fillRect(0, 0, bg.getWidth(), bg.getHeight());
		g2.dispose();
	}

	public void updateImage(BufferedImage image) {
		this.image = image;
	}

	protected void paintComponent(Graphics g) {
		g.drawImage(bg, 0, 0, null);
	}

	public void pause() {
		updateBackground();
		updateSettings();
		layout.first(this);
		parentLayout.show(contentPane, "pause");
	}

	public void unpause() {
		parentLayout.show(contentPane, "in game");
	}

	private void updateSettings() {
		sldFov.setValue((camera.getFov() - 30) / 80);
		sldRendDist.setValue(camera.getRenderDistance() / 500);
	}

}