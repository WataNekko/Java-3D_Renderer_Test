package main.input;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import main.Camera;
import main.Display;
import util.model.ModelAdder;

public class KeyboardInput implements KeyListener, FocusListener {

	private Display display;
	private ArrayList<Integer> keysDown;
	private ArrayList<Integer> specialKeys;
	private boolean[] bool;
	private MouseInput mouse;
	private Camera camera;
	
	public KeyboardInput(Display d, boolean[] bool, MouseInput mouse, Camera cam) {
		this.display = d;
		this.display.addKeyListener(this);
		this.display.addFocusListener(this);
		this.bool = bool;
		this.mouse = mouse;
		this.camera = cam;
		
		this.keysDown = new ArrayList<>();
		this.specialKeys = new ArrayList<>();
	}

	// =======================================

	public ArrayList<Integer> getSpecialKeyList() {
		return specialKeys;
	}

	public boolean isKeyDown(int key) {
		return keysDown.contains(key);
	}

	public String toString() {
		ArrayList<String> str = new ArrayList<>();
		for (int key : keysDown) {
			str.add(KeyEvent.getKeyText(key));
		}
		return str.toString();
	}

	// =======================================

	public void keyPressed(KeyEvent e) {
		Integer key = e.getKeyCode();
		if (!keysDown.contains(key)) {
			keysDown.add(key);
//			System.out.println("Keyboard Input: " + this.toString());

			toggleAllTime(key);

			if (!bool[Display.PAUSE]) {
				toggle(key);
				if (e.isControlDown())
					toggleCtrl(key);
				else if (e.isShiftDown())
					toggleShift(key);
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		Integer key = e.getKeyCode();
		keysDown.remove(key);
//		System.out.println("Keyboard Input: " + this.toString());
	}

	public void keyTyped(KeyEvent e) {

	}

	public void focusGained(FocusEvent e) {

	}

	public void focusLost(FocusEvent e) {
		if (!keysDown.isEmpty()) {
			keysDown.clear();
//			System.out.println("Keyboard Input: " + this.toString());
		}

		if (bool[Display.MOUSE_LOCK])
			mouse.unlock();
	}

	// =======================================
	
	private void toggleDebug() {
		bool[Display.SHOW_DEBUG] = !bool[Display.SHOW_DEBUG];
	}
	
	private void togglePause() {
		bool[Display.PAUSE] = !bool[Display.PAUSE];
	}

	private void toggle(int key) {
		switch (key) {
		case KeyEvent.VK_F3:
			toggleDebug();
			break;
		case KeyEvent.VK_E:
			mouse.toggleLock();
			break;
		case KeyEvent.VK_ADD:
			new ModelAdder(display);
			break;
		case KeyEvent.VK_ENTER:
			display.showModelManager();
			break;
		}
	}

	private void toggleCtrl(int key) {
		switch (key) {
		case KeyEvent.VK_R:
			camera.reset();
			break;
		}
	}

	private void toggleShift(int key) {
		switch (key) {
		case KeyEvent.VK_EQUALS:
			new ModelAdder(display);
			break;
		}
	}

	// Toggle even when paused
	private void toggleAllTime(int key) {
		switch (key) {
		case KeyEvent.VK_F2:
			specialKeys.add(key);
			break;
		case KeyEvent.VK_ESCAPE:
			togglePause();
			break;
		}
	}
	
}
