package main.input;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Display;

public class MouseInput implements MouseListener, MouseMotionListener, MouseWheelListener {

	private Display display;
	private boolean isInDisplay;
	private boolean[] bool;

	private static final Cursor BLANK_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank");
	private static final Cursor DEFAULT_CURSOR = Cursor.getDefaultCursor();

	private Point lastCursorPos;
	private int dX;
	private int dY;
	private float scrollAmount;

	private ArrayList<Integer> buttonsDown;
	private Robot bot;

	public MouseInput(Display d, boolean[] bool) {
		this.display = d;
		this.display.addMouseListener(this);
		this.display.addMouseMotionListener(this);
		this.display.addMouseWheelListener(this);
		this.bool = bool;
		
		this.buttonsDown = new ArrayList<>(3);
		try {
			bot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	// =======================================

	public boolean isButtonDown(int btn) {
		return buttonsDown.contains(btn);
	}

	public String toString() {
		ArrayList<String> str = new ArrayList<>(3);
		for (int btn : buttonsDown) {
			str.add(btn == MouseEvent.BUTTON1 ? "Left" : (btn == MouseEvent.BUTTON2 ? "Middle" : (btn == MouseEvent.BUTTON3 ? "Right" : "")));
		}
		return str.toString();
	}

	private void hideCursor() {
		display.setCursor(BLANK_CURSOR);
	}

	private void showCursor() {
		display.setCursor(DEFAULT_CURSOR);
	}

	public void toggleLock() {
		if (bool[Display.MOUSE_LOCK]) {
			unlock();
		} else {
			lock();
		}
	}

	/**
	 * @return This MouseInput's dx movement between the last and the next time this
	 *         method is called.
	 */
	public int getDX() {
		int ret = this.dX;
		this.dX = 0;
		return ret;
	}

	/**
	 * @return This MouseInput's dy movement between the last and the next time this
	 *         method is called.
	 */
	public int getDY() {
		int ret = this.dY;
		this.dY = 0;
		return ret;
	}

	/**
	 * @return This MouseInput's wheel scroll amount between the last and the next
	 *         time this method is called.
	 */
	public float getScrollAmount() {
		float ret = this.scrollAmount;
		this.scrollAmount = 0.0f;
		return ret;
	}

	// =======================================

	public void mouseClicked(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {
//		System.out.println("Mouse entered");
		if (!isInDisplay) {
			if (bool[Display.MOUSE_LOCK]) {
				lastCursorPos = MouseInfo.getPointerInfo().getLocation();
				setMouseToCenter();
			}

			isInDisplay = true;
		}
	}

	public void mouseExited(MouseEvent e) {
//		System.out.println("Mouse exited");
		isInDisplay = false;
	}

	public void mousePressed(MouseEvent e) {
		Integer btn = e.getButton();
		if (!buttonsDown.contains(btn)) {
			buttonsDown.add(btn);
//			System.out.println("Mouse Input: " + this.toString());
		}
	}

	public void mouseReleased(MouseEvent e) {
		Integer btn = e.getButton();
		buttonsDown.remove(btn);
//		System.out.println("Mouse Input: " + this.toString());
	}

	public void mouseDragged(MouseEvent e) {
		if (isInDisplay && bool[Display.MOUSE_LOCK]) {
			mouseMoved();
		}
	}

	public void mouseMoved(MouseEvent e) {
		if (isInDisplay && bool[Display.MOUSE_LOCK]) {
			mouseMoved();
		}
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		double r = e.getPreciseWheelRotation();
		if (isInDisplay && bool[Display.MOUSE_LOCK]) {
			this.scrollAmount += r;
		}
//		System.out.println("Mouse Input: Scrolled " + r);
	}

	// =======================================

	private void setMouseToCenter() {
		Point center = display.getCenterOnScreen();
		bot.mouseMove(center.x, center.y);
	}

	void lock() {
//		System.out.println("enabled");
		lastCursorPos = MouseInfo.getPointerInfo().getLocation();
		hideCursor();
		setMouseToCenter();
		isInDisplay = true;
		bool[Display.MOUSE_LOCK] = true;
	}

	void unlock() {
//		System.out.println("disabled");
		if (isInDisplay) {
			bot.mouseMove(lastCursorPos.x, lastCursorPos.y);
		}
		showCursor();
		bool[Display.MOUSE_LOCK] = false;
	}

	private void mouseMoved() {
		Point currentPos = MouseInfo.getPointerInfo().getLocation();
		Point displayCenter = display.getCenterOnScreen();
		this.dX += currentPos.x - displayCenter.x;
		this.dY += displayCenter.y - currentPos.y;
		setMouseToCenter();
	}

}
