package main;

import java.awt.BasicStroke;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import main.gui.PauseMenu;
import main.input.KeyboardInput;
import main.input.MouseInput;
import math.Vector3D;
import model.Mesh;
import model.shape.Cube;
import text.Text;
import util.ProgramClock;
import util.model.ModelManager;

public class Display extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;

	private Thread thread;
	private boolean running;
	private BufferedImage image;
	private boolean[] bool;

	public static final String TITLE = "3D Renderer";
	public static final int DEFAULT_WIDTH = 800;
	public static final int DEFAULT_HEIGHT = 600;

	private ProgramClock clock;
	private int updateFrequency = 60;

	private KeyboardInput keyboard;
	private MouseInput mouse;

	private int framerate;
	private Text debugInfo;

	private Camera camera;

	private ArrayList<Mesh> models;
	private ModelManager modMg;
	private ArrayList<Mesh> removeList;
	private ArrayList<Mesh> addList;

	private Point imageCenter;
	private Insets insets;

	private PauseMenu pauseMenu;

	public static final int PAUSE = 0;
	public static final int SHOW_DEBUG = 1;
	public static final int MOUSE_LOCK = 2;

	// ============= Constructor =============

	public Display() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.thread = new Thread(this, "Display");
		this.bool = new boolean[] { false, true, false };
		this.mouse = new MouseInput(this, bool);
		this.camera = new Camera();
		this.keyboard = new KeyboardInput(this, bool, mouse, camera);

		this.models = new ArrayList<>();
		this.removeList = new ArrayList<>(1);
		this.addList = new ArrayList<>(1);
		this.debugInfo = new Text(Color.green, new Font("Meiryo UI", Font.PLAIN, 12));

		// Test cube
		this.models.add(new Cube(1.0f, new Vector3D(-2f, -1f, 4.5f), Color.red, Mesh.SOLID));

		this.modMg = new ModelManager(this, models);

		pack();
		insets = this.getInsets();
		setSize(insets.left + DEFAULT_WIDTH + insets.right, insets.top + DEFAULT_HEIGHT + insets.bottom);
		setResizable(true);
		updateTitle();
		setIconImage(new ImageIcon("src/resources/textures/icon/icon.png").getImage());
		setBackground(Color.BLACK);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setFocusTraversalKeysEnabled(false);

		// Layout
		Container contentPane = getContentPane();
		CardLayout layout = new CardLayout();
		contentPane.setLayout(layout);

		JPanel inGame = new JPanel() {
			private static final long serialVersionUID = 1L;

			protected void paintComponent(Graphics g) {
			}
		};
		contentPane.add(inGame, "in game");

		pauseMenu = new PauseMenu(this, bool, camera);
		contentPane.add(pauseMenu, "pause");

		//
		createOffscreenImage();
		imageCenter = new Point();
		updateCenterPoint();

		contentPane.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				createOffscreenImage();
				updateCenterPoint();
				if (bool[PAUSE]) {
					renderToImage();
				}
				updateTitle();
			}
		});

		setVisible(true);
		this.clock = new ProgramClock(ProgramClock.IN_MILLISECOND);

		start();
	}

	// ---------------------------------------

	private synchronized void start() {
		running = true;
		this.thread.start();
	}

	private synchronized void stop() {
		running = false;
		try {
			this.thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		final double period = 1000000000.0 / this.updateFrequency;

		long last = System.nanoTime();
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;

		this.createBufferStrategy(2);
		BufferStrategy bs = this.getBufferStrategy();

		boolean resume = true;

		while (running) {
			System.out.print("");
			// Main
			if (!bool[PAUSE]) {
				if (!resume) {
					pauseMenu.unpause();

					last = System.nanoTime();
					delta = 0;
					timer = System.currentTimeMillis();
					frames = 0;

					resume = true;
				}

				long now = System.nanoTime();
				delta += (now - last) / period;
				last = now;

				while (delta >= 1) {
					update();
					delta--;
				}

				renderToScreen(bs);

				// Fps count
				frames++;

				if (System.currentTimeMillis() - timer > 1000) {
					timer += 1000;
					this.framerate = frames;
					updateTitle();
					frames = 0;
				}

			} else if (resume) {
				if (bool[MOUSE_LOCK]) {
					mouse.toggleLock();
				}

				renderToImage();
				pauseMenu.pause();

				resume = false;
			}

			// Screenshot
			if (keyboard.getSpecialKeyList().contains(KeyEvent.VK_F2)) {
				takeScreenshot();
				keyboard.getSpecialKeyList().remove(Integer.valueOf(KeyEvent.VK_F2));
			}
		}

		stop();
	}

	// =======================================

	private void update() {
//		models.get(0).rotateX(2);
//		models.get(0).rotateY(5);
//		models.get(0).moveZ(.2f);
//		models.get(0).translateZ(.05f);
//		camera.rotateY(-1);

//		models.get(0).rotateY(4);
//		models.get(0).rotateZ(3);

		// Update model list
		updateModelList();

		// Camera movement
		updateCamera();

	}

	// --------------------

	private void updateModelList() {
		if (!removeList.isEmpty() || !addList.isEmpty()) {
			if (!removeList.isEmpty()) {
				models.removeAll(removeList);
				removeList.clear();
			}

			if (!addList.isEmpty()) {
				models.addAll(addList);
				addList.clear();
			}

			modMg.updateList();
		}
	}

	private void updateCamera() {
		if (bool[MOUSE_LOCK]) {

			float speed = (Camera.MOVEMENT_SPEED / this.updateFrequency);

			// Movement
			float x = 0;
			float z = 0;

			if (keyboard.isKeyDown(KeyEvent.VK_W))
				z += 1;
			if (keyboard.isKeyDown(KeyEvent.VK_S))
				z -= 1;
			if (keyboard.isKeyDown(KeyEvent.VK_D))
				x += 1;
			if (keyboard.isKeyDown(KeyEvent.VK_A))
				x -= 1;

			if (z != 0 || x != 0) {
				// Get directional unit vector
				Vector3D dir = new Vector3D(x, 0, z);
				dir.normalise();

				float cos = (float) Math.cos(Math.toRadians(this.camera.getRotY()));
				float sin = (float) Math.sin(Math.toRadians(this.camera.getRotY()));

				z = dir.z * cos - dir.x * sin;
				x = dir.z * sin + dir.x * cos;

				// Get speed
				float move = speed;

				if (keyboard.isKeyDown(KeyEvent.VK_TAB))
					move *= 2f;

				// Move
				z *= move;
				x *= move;

				this.camera.translateX(x);
				this.camera.translateZ(z);
			}

			// Up Down
			float y = 0;

			if (keyboard.isKeyDown(KeyEvent.VK_SPACE))
				y += 1;
			if (keyboard.isKeyDown(KeyEvent.VK_SHIFT))
				y -= 1;

			if (y != 0) {
				y *= speed * 2.5;
				this.camera.translateY(y);
			}

			// Rotation
			float turnSpeed = Camera.TURN_SPEED / this.updateFrequency;

			float ry = mouse.getDX() * turnSpeed;
			float rx = mouse.getDY() * turnSpeed;
			float rz = mouse.getScrollAmount() * 20.0f * turnSpeed;

			this.camera.rotate(rx, ry, rz);
		}

	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private void renderToScreen(BufferStrategy bs) {
		do {
			do {
				renderToImage();

				Graphics gBs = bs.getDrawGraphics();
				gBs.translate(insets.left, insets.top);
				gBs.drawImage(image, 0, 0, this);
				gBs.dispose();
			} while (bs.contentsRestored());

			bs.show();

		} while (bs.contentsLost());
	}

	public void renderToImage() {
		Graphics2D g = image.createGraphics();
		g.translate(imageCenter.x, imageCenter.y);

		// ====================================
		clearScreen(g);

		// Render Object
		for (Mesh model : this.models) {
			model.render(this, camera, g);
		}

		// Render crosshair
		if (bool[MOUSE_LOCK]) {
			Color c = this.getBackground();
			c = new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
			g.setColor(c);

			g.setStroke(new BasicStroke(2));
			g.drawLine(8, 0, -8, 0);
			g.drawLine(0, 8, 0, -8);
		}

		// Show Debug
		if (bool[SHOW_DEBUG]) {
			showDebug(g);
		}

		// ====================================

		g.dispose();
	}

	// --------------------

	private void clearScreen(Graphics g) {
		g.setColor(this.getBackground());
		g.fillRect(-this.width() / 2, -this.height() / 2, this.width(), this.height());
	}

	private void showDebug(Graphics g) {
		String debug = "";

		debug += this.framerate + " fps\n\n";

		debug += this.clock.toString() + " have passed\n\n";

		debug += camera.toDebugString() + "\n";
		for (Mesh model : this.models) {
			debug += model.toDebugString() + "\n";
		}

		debugInfo.setText(debug);
		debugInfo.print(g, (-this.width() / 2) + 3, (-this.height() / 2));
	}

	// =======================================

	public static void main(String[] args) {
		new Display();
	}

	// ============ Getter Setter ============

	// =======================================

	public void showModelManager() {
		modMg.setVisible(true);
		modMg.requestFocus();
	}

	public void removeObject(List<Mesh> m) {
		this.removeList.addAll(m);
	}

	public void addObject(Mesh m) {
		this.addList.add(m);
	}

	public int width() {
		return this.getContentPane().getWidth();
	}

	public int height() {
		return this.getContentPane().getHeight();
	}

	private void updateCenterPoint() {
		imageCenter.setLocation(width() / 2, height() / 2);
	}

	private void createOffscreenImage() {
		image = new BufferedImage(width(), height(), BufferedImage.TYPE_INT_RGB);
		pauseMenu.updateImage(image);
	}

	public Point getCenterOnScreen() {
		Point p = new Point(imageCenter);
		SwingUtilities.convertPointToScreen(p, getContentPane());
		return p;
	}

	private void takeScreenshot() {
		BufferedImage screenshot;
		if (bool[PAUSE]) {
			screenshot = new BufferedImage(width(), height(), BufferedImage.TYPE_INT_RGB);
			pauseMenu.paint(screenshot.getGraphics());
		} else {
			screenshot = image;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss");
		LocalDateTime now = LocalDateTime.now();
		String filePath = "src/screenshots/" + formatter.format(now);

		File output = new File(filePath + ".png");
		int count = 1;
		while (output.exists()) {
			output = new File(filePath + "_" + count++ + ".png");
		}

		try {
			ImageIO.write(screenshot, "png", output);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Unable to save screenshot.");
		}

		System.out.println("Saved screenshot as " + output.getName());
	}

	private void updateTitle() {
		this.setTitle(TITLE + " (" + this.framerate + " fps) | " + width() + "x" + height());
	}

}