package model;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import math.Rotation;
import math.Vector3D;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ModelSlider extends JDialog implements Runnable {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	public JSlider sliderX;
	public JSlider sliderY;
	public JSlider sliderZ;
	public JSlider sliderRX;
	public JSlider sliderRY;
	public JSlider sliderRZ;
	private JButton btnReset;
	public static final int DEFAULT_VALUE = 50;
	private Thread thread;
	private boolean running;
	private Model3D model;
	private int x = DEFAULT_VALUE;
	private int y = DEFAULT_VALUE;
	private int z = DEFAULT_VALUE;
	private int rx = DEFAULT_VALUE;
	private int ry = DEFAULT_VALUE;
	private int rz = DEFAULT_VALUE;
	private float posAmplitude;
	private final static float DEFAULT_AMPLITUDE = 5.0f;

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		ModelSlider dialog = new ModelSlider();
	}

	private void init() {
		setAlwaysOnTop(true);
		setResizable(false);
		setBounds(100, 100, 450, 266);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		{
			JLabel lblNewLabel = new JLabel("x");
			lblNewLabel.setBounds(10, 11, 46, 14);
			contentPanel.add(lblNewLabel);
		}
		{
			sliderX = new JSlider();
			sliderX.setBounds(66, 11, 368, 26);
			contentPanel.add(sliderX);
		}
		{
			JLabel lblY = new JLabel("y");
			lblY.setBounds(10, 36, 46, 14);
			contentPanel.add(lblY);
		}
		{
			sliderY = new JSlider();
			sliderY.setBounds(66, 36, 368, 26);
			contentPanel.add(sliderY);
		}
		{
			JLabel lblZ = new JLabel("z");
			lblZ.setBounds(10, 61, 46, 14);
			contentPanel.add(lblZ);
		}
		{
			sliderZ = new JSlider();
			sliderZ.setBounds(66, 61, 368, 26);
			contentPanel.add(sliderZ);
		}
		{
			JLabel lblRx = new JLabel("rx");
			lblRx.setBounds(10, 112, 46, 14);
			contentPanel.add(lblRx);
		}
		{
			sliderRX = new JSlider();
			sliderRX.setBounds(66, 112, 368, 26);
			contentPanel.add(sliderRX);
		}
		{
			JLabel lblRy = new JLabel("ry");
			lblRy.setBounds(10, 137, 46, 14);
			contentPanel.add(lblRy);
		}
		{
			sliderRY = new JSlider();
			sliderRY.setBounds(66, 137, 368, 26);
			contentPanel.add(sliderRY);
		}
		{
			JLabel lblRz = new JLabel("rz");
			lblRz.setBounds(10, 162, 46, 14);
			contentPanel.add(lblRz);
		}
		{
			sliderRZ = new JSlider();
			sliderRZ.setBounds(66, 162, 368, 26);
			contentPanel.add(sliderRZ);
		}

		btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reset();
			}
		});
		btnReset.setBounds(335, 195, 89, 23);
		contentPanel.add(btnReset);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	public ModelSlider() {
		init();
		setTitle("Slider");
	}

	public ModelSlider(Model3D m, float posAmplitude) {
		init();
		setTitle(m.getName() + " Slider");

		this.model = m;
		this.posAmplitude = posAmplitude;

		this.thread = new Thread(this, m.getName() + " Slider");
		this.start();
	}

	public ModelSlider(Model3D m) {
		this(m, DEFAULT_AMPLITUDE);
	}

	public synchronized void start() {
		running = true;
		this.thread.start();
	}

	public synchronized void stop() {
		running = false;
		try {
			this.thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void reset() {
		this.sliderRX.setValue(DEFAULT_VALUE);
		this.sliderRY.setValue(DEFAULT_VALUE);
		this.sliderRZ.setValue(DEFAULT_VALUE);
		this.sliderX.setValue(DEFAULT_VALUE);
		this.sliderY.setValue(DEFAULT_VALUE);
		this.sliderZ.setValue(DEFAULT_VALUE);
	}

	public void run() {
		long lastUpdate = System.currentTimeMillis();
		long now;
		final int updateFrequency = 20; // times per second
		final float T = 1000 / updateFrequency;

		Vector3D oriPos = new Vector3D(this.model);
		Rotation oriRot = new Rotation(this.model);

		while (running) {
			now = System.currentTimeMillis();

			if ((now - lastUpdate) >= T) {
				update(oriPos, oriRot);
				lastUpdate = now;
			}
		}

		stop();
	}

	public void update(Vector3D oriPos, Rotation oriRot) {
		// ========================
		float rotA = 180;

		int curRx = this.sliderRX.getValue();
		if (this.rx != curRx) {
			this.model.setRotX(oriRot.getRx() + (rotA * (curRx - 50) / 50));
			this.rx = curRx;
		}

		int curRy = this.sliderRY.getValue();
		if (this.ry != curRy) {
			this.model.setRotY(oriRot.getRy() + (rotA * (curRy - 50) / 50));
			this.ry = curRy;
		}

		int curRz = this.sliderRZ.getValue();
		if (this.rz != curRz) {
			this.model.setRotZ(oriRot.getRz() + (rotA * (curRz - 50) / 50));
			this.rz = curRz;
		}

		// ------------------------

		int curX = this.sliderX.getValue();
		if (this.x != curX) {
			this.model.setPosX(oriPos.x + (this.posAmplitude * (curX - 50) / 50));
			this.x = curX;
		}

		int curY = this.sliderY.getValue();
		if (this.y != curY) {
			this.model.setPosY(oriPos.y + (this.posAmplitude * (curY - 50) / 50));
			this.y = curY;
		}

		int curZ = this.sliderZ.getValue();
		if (this.z != curZ) {
			this.model.setPosZ(oriPos.z + (this.posAmplitude * (curZ - 50) / 50));
			this.z = curZ;
		}
		// ========================
	}
}