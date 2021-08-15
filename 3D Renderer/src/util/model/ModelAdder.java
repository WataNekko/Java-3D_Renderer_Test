package util.model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.Display;
import math.Rotation;
import math.Vector3D;
import model.Mesh;
import model.shape.Cube;

public class ModelAdder extends JDialog {

	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();
	private JButton btnBrowse;
	private JComboBox<String> cbSelectModelOption;
	private JTextField txtFilePath;
	private JSpinner spScale;
	private JSpinner spPosX;
	private JSpinner spPosY;
	private JSpinner spPosZ;
	private JSpinner spRotY;
	private JSpinner spRotX;
	private JSpinner spRotZ;
	private JCheckBox chbRenderType;
	private ColorChooserButton btnChooseColor;
	private JTextField txtModelName;
	private JPanel paneAttribute;
	private JPanel paneSelect;
	private JLabel lblPosVal;
	private JLabel lblRotVal;

	private Display display;
	private JLabel lblScale;
	private JLabel lblName;
	private JPanel randomPane;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JSpinner spRange;
	private JSpinner spAmount;
	
	public static void main(String[] args) {
		new ModelAdder();
	}

	private void init() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		setResizable(false);
		setTitle("Add model");
		setIconImage(new ImageIcon("src/resources/textures/icon/icon2.png").getImage());
		setAlwaysOnTop(true);
		setBounds(100, 100, 440, 365);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout());
		setLocationRelativeTo(display);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPanel.add(tabbedPane);

		JPanel addPane = new JPanel();
		addPane.setLayout(null);
		tabbedPane.addTab("Add a model", addPane);

		JLabel lblSelectOption = new JLabel("Select a model");
		lblSelectOption.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSelectOption.setBounds(10, 13, 86, 20);
		addPane.add(lblSelectOption);

		cbSelectModelOption = new JComboBox<String>();
		cbSelectModelOption.setBackground(new Color(230, 230, 250));
		cbSelectModelOption.setModel(new DefaultComboBoxModel<String>(new String[] { "Load from an obj file", "Cube" }));
		cbSelectModelOption.setBounds(106, 13, 153, 20);
		cbSelectModelOption.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getItem() == cbSelectModelOption.getItemAt(0)) {
					if (e.getStateChange() == ItemEvent.SELECTED)
						showSelectFileComponents();
					else
						hideSelectFileComponents();
				}
			}
		});
		addPane.add(cbSelectModelOption);

		// ===================================

		paneSelect = new JPanel();
		paneSelect.setBounds(0, 38, 424, 32);
		addPane.add(paneSelect);
		paneSelect.setLayout(null);

		txtFilePath = new JTextField();
		txtFilePath.setBounds(10, 6, 325, 20);
		txtFilePath.setColumns(10);
		paneSelect.add(txtFilePath);

		btnBrowse = new JButton("Browse...");
		btnBrowse.setBackground(Color.LIGHT_GRAY);
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String path = txtFilePath.getText();
				if (path.isEmpty() || !(new File(path).exists())) {
					path = System.getProperty("user.home") + "/Desktop";
				}
				JFileChooser chooser = new JFileChooser(path) {
					private static final long serialVersionUID = 1L;

					protected JDialog createDialog(Component arg0) throws HeadlessException {
						JDialog dialog = super.createDialog(arg0);
						dialog.setIconImage(new ImageIcon("src/resources/textures/icon/watame.png").getImage());
						return dialog;
					}
				};

				chooser.setFileFilter(new FileNameExtensionFilter("*.obj", "obj"));
				if (chooser.showDialog(getThis(), "Load") == JFileChooser.APPROVE_OPTION) {
					txtFilePath.setText(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		btnBrowse.setBounds(341, 5, 60, 22);
		btnBrowse.setMargin(new Insets(0, 0, 0, 0));
		btnBrowse.setFocusPainted(false);
		paneSelect.add(btnBrowse);

		// ===================================

		paneAttribute = new JPanel();
		paneAttribute.setBounds(0, 70, 424, 190);
		addPane.add(paneAttribute);
		paneAttribute.setLayout(null);

		// Label
		lblScale = new JLabel("Model Scale");
		lblScale.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblScale.setBounds(10, 5, 78, 20);
		paneAttribute.add(lblScale);

		JLabel lblPos = new JLabel("Position");
		lblPos.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPos.setBounds(10, 36, 58, 20);
		paneAttribute.add(lblPos);

		JLabel lblRot = new JLabel("Rotation");
		lblRot.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblRot.setBounds(10, 67, 58, 20);
		paneAttribute.add(lblRot);

		JLabel lblColor = new JLabel("Color");
		lblColor.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblColor.setBounds(10, 98, 58, 20);
		paneAttribute.add(lblColor);

		JLabel lblRenderType = new JLabel("Rendering Type");
		lblRenderType.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblRenderType.setBounds(10, 129, 100, 20);
		paneAttribute.add(lblRenderType);

		lblName = new JLabel("Model Name");
		lblName.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblName.setBounds(10, 160, 78, 20);
		paneAttribute.add(lblName);

		// Scale
		spScale = new JSpinner();
		spScale.setModel(new SpinnerNumberModel(Float.valueOf(1), Float.valueOf(0), null, Float.valueOf(0.25f)));
		spScale.setBounds(106, 5, 75, 20);
		paneAttribute.add(spScale);

		// Position
		lblPosVal = new JLabel("(0.0, 0.0, 0.0)");
		lblPosVal.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblPosVal.setBounds(30, 52, 120, 12);
		paneAttribute.add(lblPosVal);

		ChangeListener updatePosVal = new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				lblPosVal.setText("(" + spPosX.getValue() + ", " + spPosY.getValue() + ", " + spPosZ.getValue() + ")");
			}
		};

		spPosX = new JSpinner();
		spPosX.setModel(new SpinnerNumberModel(Float.valueOf(0), null, null, Float.valueOf(0.25f)));
		spPosX.setBounds(156, 36, 75, 20);
		spPosX.addChangeListener(updatePosVal);
		paneAttribute.add(spPosX);

		spPosY = new JSpinner();
		spPosY.setModel(new SpinnerNumberModel(Float.valueOf(0), null, null, Float.valueOf(0.25f)));
		spPosY.setBounds(241, 36, 75, 20);
		spPosY.addChangeListener(updatePosVal);
		paneAttribute.add(spPosY);

		spPosZ = new JSpinner();
		spPosZ.setModel(new SpinnerNumberModel(Float.valueOf(0), null, null, Float.valueOf(0.25f)));
		spPosZ.setBounds(326, 36, 75, 20);
		spPosZ.addChangeListener(updatePosVal);
		paneAttribute.add(spPosZ);

		// Rotation
		lblRotVal = new JLabel("(0.0, 0.0, 0.0)");
		lblRotVal.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblRotVal.setBounds(30, 83, 120, 12);
		paneAttribute.add(lblRotVal);

		ChangeListener updateRotVal = new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				lblRotVal.setText("(" + spRotX.getValue() + ", " + spRotY.getValue() + ", " + spRotZ.getValue() + ")");
			}
		};

		spRotX = new JSpinner();
		spRotX.setModel(new SpinnerNumberModel(Float.valueOf(0), Float.valueOf(-180), Float.valueOf(180), Float.valueOf(1)));
		spRotX.setBounds(156, 67, 75, 20);
		spRotX.addChangeListener(updateRotVal);
		paneAttribute.add(spRotX);

		spRotY = new JSpinner();
		spRotY.setModel(new SpinnerNumberModel(Float.valueOf(0), Float.valueOf(0), Float.valueOf(360), Float.valueOf(1)));
		spRotY.setBounds(241, 67, 75, 20);
		spRotY.addChangeListener(updateRotVal);
		paneAttribute.add(spRotY);

		spRotZ = new JSpinner();
		spRotZ.setModel(new SpinnerNumberModel(Float.valueOf(0), Float.valueOf(-180), Float.valueOf(180), Float.valueOf(1)));
		spRotZ.setBounds(326, 67, 75, 20);
		spRotZ.addChangeListener(updateRotVal);
		paneAttribute.add(spRotZ);

		// Color
		btnChooseColor = new ColorChooserButton(this, "Choose the model's color");
		btnChooseColor.setBounds(106, 97, 23, 23);
		paneAttribute.add(btnChooseColor);

		// Rendering Type
		chbRenderType = new JCheckBox("Solid");
		chbRenderType.setFocusPainted(false);
		chbRenderType.setBounds(106, 128, 86, 23);
		paneAttribute.add(chbRenderType);

		// Name
		txtModelName = new JTextField();
		txtModelName.setHorizontalAlignment(SwingConstants.LEFT);
		txtModelName.setBounds(106, 160, 170, 20);
		txtModelName.setColumns(10);
		paneAttribute.add(txtModelName);

		// ===================================

		randomPane = new JPanel();
		tabbedPane.addTab("Random", randomPane);
		randomPane.setLayout(null);

		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (tabbedPane.getSelectedIndex() == 0) {
					if (cbSelectModelOption.getSelectedIndex() == 0) {
						getThis().setSize(440, 365);
					} else {
						getThis().setSize(440, 334);
					}
				} else {
					getThis().setSize(440, 150);
				}
			}
		});

		lblNewLabel = new JLabel("Amount");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel.setBounds(10, 11, 65, 14);
		randomPane.add(lblNewLabel);

		spAmount = new JSpinner();
		spAmount.setModel(new SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
		spAmount.setBounds(71, 8, 80, 20);
		randomPane.add(spAmount);

		lblNewLabel_1 = new JLabel("Range");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_1.setBounds(209, 11, 46, 14);
		randomPane.add(lblNewLabel_1);

		spRange = new JSpinner();
		spRange.setModel(new SpinnerNumberModel(Float.valueOf(50), Float.valueOf(0), null, Float.valueOf(1)));
		spRange.setBounds(265, 8, 80, 20);
		randomPane.add(spRange);

		// ===================================

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setForeground(new Color(0, 100, 0));
				okButton.setBackground(new Color(240, 255, 255));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (display != null) {
							if (tabbedPane.getSelectedIndex() == 0) {
								float scale = (float) spScale.getValue();
								Vector3D pos = new Vector3D((float) spPosX.getValue(), (float) spPosY.getValue(), (float) spPosZ.getValue());
								Rotation rot = new Rotation((float) spRotX.getValue(), (float) spRotY.getValue(), (float) spRotZ.getValue());
								Color c = btnChooseColor.getColor();
								int render = chbRenderType.isSelected() ? Mesh.SOLID : Mesh.HOLLOW;
								String name = txtModelName.getText();
								if (name.isEmpty())
									name = null;

								switch (cbSelectModelOption.getSelectedIndex()) {
								case 0:
									String filePath = txtFilePath.getText();

									if (filePath.isEmpty()) {
										beep();
										JOptionPane.showMessageDialog(getThis(), "Please select a file path.", "Error", JOptionPane.ERROR_MESSAGE);
										return;
									}

									try {
										display.addObject(new Mesh(filePath, scale, pos, rot, c, render, name));
									} catch (FileNotFoundException e1) {
										beep();
										JOptionPane.showMessageDialog(getThis(), "File not found.", "Error", JOptionPane.ERROR_MESSAGE);
										return;
									} catch (IOException e2) {
										beep();
										JOptionPane.showMessageDialog(getThis(), "Invalid file path.", "Error", JOptionPane.ERROR_MESSAGE);
										return;
									}

									break;

								case 1:
									display.addObject(new Cube(scale, pos, rot, c, render, name));
									break;
								}
							} else {
								int amount = (Integer) spAmount.getValue();
								for (int i = 0; i < amount; i++) {
									Random ran = new Random();
									float range = (Float) spRange.getValue();
									Vector3D pos = new Vector3D((ran.nextFloat() * range) - (range / 2), (ran.nextFloat() * range) - (range / 2), (ran.nextFloat() * range) - (range / 2));
									Rotation rot = new Rotation((ran.nextFloat() * 360) - 180, (ran.nextFloat() * 360), (ran.nextFloat() * 360) - 180);
									float scale = (ran.nextFloat() * 1.25f) + 0.25f;
									Color c = new Color(ran.nextInt(256), ran.nextInt(256), ran.nextInt(256));
									int render = Mesh.SOLID;

									display.addObject(new Cube(scale, pos, rot, c, render, null));
								}
							}

						}

						getThis().dispose();
					}
				});
				okButton.setActionCommand("OK");
				okButton.setFocusPainted(false);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setForeground(new Color(139, 0, 0));
				cancelButton.setBackground(new Color(240, 255, 255));
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						getThis().dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				cancelButton.setFocusPainted(false);
				buttonPane.add(cancelButton);
			}
		}

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	public ModelAdder() {
		init();
	}

	public ModelAdder(Display d) {
		this.display = d;
		init();
	}

	// ===================================

	private void showSelectFileComponents() {
		this.setSize(440, 365);
		paneAttribute.setLocation(0, 70);
		paneSelect.setVisible(true);
	}

	private void hideSelectFileComponents() {
		paneSelect.setVisible(false);
		paneAttribute.setLocation(0, 39);
		this.setSize(440, 334);
	}

	private ModelAdder getThis() {
		return this;
	}

	private void beep() {
		Toolkit.getDefaultToolkit().beep();
	}
}
