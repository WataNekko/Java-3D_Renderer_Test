package util.model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import main.Display;
import model.Mesh;

public class ModelManager extends JDialog {

	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();

	private Display display;
	private ArrayList<Mesh> models;
	private JList<Mesh> list;
	private JPanel modelPane;
	private JTextField txtName;
	private Mesh select;
	private DefaultListModel<Mesh> dlm = new DefaultListModel<>();
	private JSpinner spScale;
	private JLabel lblPosVal;
	private JSpinner spPosY;
	private JSpinner spPosZ;
	private JSpinner spPosX;
	private JSpinner spRotX;
	private JSpinner spRotY;
	private JSpinner spRotZ;
	private JLabel lblRotVal;
	private ColorChooserButton btnChooseColor;
	private JCheckBox chbRenderType;

	private boolean loaded;
	private JButton btnReset;
	private JLabel lblFaces;

	public static void main(String[] args) {
		new ModelManager(null, null);
	}

	public ModelManager(Display d, ArrayList<Mesh> models) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.display = d;
		this.models = models;

		setResizable(false);
		setTitle("List Manager");
		setIconImage(new ImageIcon("src/resources/textures/icon/watame.png").getImage());
		setAlwaysOnTop(true);
		setBounds(100, 100, 406, 259);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		updateList();

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 129, 201);
		contentPanel.add(scrollPane);
		list = new JList<>();
		list.setModel(dlm);
		scrollPane.setViewportView(list);

		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (list.getSelectedIndices().length == 1) {
					select = list.getSelectedValue();
					loadSelectedValue();
					modelPane.setVisible(true);
				} else {
					modelPane.setVisible(false);
				}
			}
		});

		modelPane = new JPanel();
		modelPane.setBounds(149, 35, 242, 184);
		contentPanel.add(modelPane);
		modelPane.setLayout(null);
		modelPane.setVisible(false);

		JLabel lblScale = new JLabel("Scale");
		lblScale.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblScale.setBounds(0, 5, 78, 20);
		modelPane.add(lblScale);

		JLabel lblPos = new JLabel("Position");
		lblPos.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPos.setBounds(0, 30, 58, 20);
		modelPane.add(lblPos);

		JLabel lblRot = new JLabel("Rotation");
		lblRot.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblRot.setBounds(0, 55, 58, 20);
		modelPane.add(lblRot);

		JLabel lblColor = new JLabel("Color");
		lblColor.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblColor.setBounds(0, 80, 58, 20);
		modelPane.add(lblColor);

		JLabel lblRenderType = new JLabel("Render");
		lblRenderType.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblRenderType.setBounds(0, 105, 58, 20);
		modelPane.add(lblRenderType);

		JLabel lblName = new JLabel("Name");
		lblName.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblName.setBounds(0, 130, 78, 20);
		modelPane.add(lblName);

		// Scale
		spScale = new JSpinner();
		spScale.setModel(new SpinnerNumberModel(new Float(1), new Float(0), null, new Float(0.25f)));
		spScale.setBounds(60, 5, 50, 20);
		spScale.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (loaded) {
					select.setScale((Float) spScale.getValue());
				}
			}
		});
		modelPane.add(spScale);

		// Position
		ChangeListener updatePosVal = new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (loaded) {
					lblPosVal.setText("(" + spPosX.getValue() + ", " + spPosY.getValue() + ", " + spPosZ.getValue() + ")");
				}
			}
		};

		lblPosVal = new JLabel("(0.0, 0.0, 0.0)");
		lblPosVal.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblPosVal.setBounds(120, 15, 120, 12);
		modelPane.add(lblPosVal);

		spPosX = new JSpinner();
		spPosX.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(0.25f)));
		spPosX.setBounds(60, 30, 50, 20);
		spPosX.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (loaded) {
					select.setPosX((Float) spPosX.getValue());
				}
			}
		});
		spPosX.addChangeListener(updatePosVal);
		modelPane.add(spPosX);

		spPosY = new JSpinner();
		spPosY.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(0.25f)));
		spPosY.setBounds(120, 30, 50, 20);
		spPosY.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (loaded) {
					select.setPosY((Float) spPosY.getValue());
				}
			}
		});
		spPosY.addChangeListener(updatePosVal);
		modelPane.add(spPosY);

		spPosZ = new JSpinner();
		spPosZ.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(0.25f)));
		spPosZ.setBounds(180, 30, 50, 20);
		spPosZ.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (loaded) {
					select.setPosZ((Float) spPosZ.getValue());
				}
			}
		});
		spPosZ.addChangeListener(updatePosVal);
		modelPane.add(spPosZ);

		// Rotation
		ChangeListener updateRotVal = new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (loaded) {
					lblRotVal.setText("(" + spRotX.getValue() + ", " + spRotY.getValue() + ", " + spRotZ.getValue() + ")");
				}
			}
		};

		lblRotVal = new JLabel("(0.0, 0.0, 0.0)");
		lblRotVal.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblRotVal.setBounds(120, 75, 120, 12);
		modelPane.add(lblRotVal);

		spRotX = new JSpinner();
		spRotX.setModel(new SpinnerNumberModel(new Float(0), new Float(-180), new Float(180), new Float(1)));
		spRotX.setBounds(60, 55, 50, 20);
		spRotX.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (loaded) {
					select.setRotX((Float) spRotX.getValue());
				}
			}
		});
		spRotX.addChangeListener(updateRotVal);
		modelPane.add(spRotX);

		spRotY = new JSpinner();
		spRotY.setModel(new SpinnerNumberModel(new Float(0), new Float(0), new Float(360), new Float(1)));
		spRotY.setBounds(120, 55, 50, 20);
		spRotY.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (loaded) {
					select.setRotY((Float) spRotY.getValue());
				}
			}
		});
		spRotY.addChangeListener(updateRotVal);
		modelPane.add(spRotY);

		spRotZ = new JSpinner();
		spRotZ.setModel(new SpinnerNumberModel(new Float(0), new Float(-180), new Float(180), new Float(1)));
		spRotZ.setBounds(180, 55, 50, 20);
		spRotZ.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (loaded) {
					select.setRotZ((Float) spRotZ.getValue());
				}
			}
		});
		spRotZ.addChangeListener(updateRotVal);
		modelPane.add(spRotZ);

		// Color
		btnChooseColor = new ColorChooserButton(this, null);
		btnChooseColor.setBounds(60, 79, 23, 23);
		btnChooseColor.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (loaded) {
					select.setColor(btnChooseColor.getColor());
				}
			}
		});
		modelPane.add(btnChooseColor);

		// Rendering Type
		chbRenderType = new JCheckBox("Solid");
		chbRenderType.setFocusPainted(false);
		chbRenderType.setBounds(60, 104, 86, 23);
		chbRenderType.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (loaded) {
					if (chbRenderType.isSelected()) {
						select.setRenderAttribute(Mesh.SOLID);
					} else {
						select.setRenderAttribute(Mesh.HOLLOW);
					}
				}
			}
		});
		modelPane.add(chbRenderType);

		// Name
		txtName = new JTextField();
		txtName.setHorizontalAlignment(SwingConstants.LEFT);
		txtName.setColumns(10);
		txtName.setBounds(60, 130, 170, 20);
		txtName.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {
				if (loaded) {
					select.setName(txtName.getText());
				}
			}

			public void insertUpdate(DocumentEvent e) {
				if (loaded) {
					select.setName(txtName.getText());
				}
			}

			public void changedUpdate(DocumentEvent e) {
				if (loaded) {
					select.setName(txtName.getText());
				}
			}
		});
		modelPane.add(txtName);

		btnReset = new JButton("Reset");
		btnReset.setFocusPainted(false);
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				select.reset();
				loadSelectedValue();
			}
		});
		btnReset.setFont(new Font("Tahoma", Font.ITALIC, 11));
		btnReset.setForeground(new Color(139, 0, 0));
		btnReset.setBounds(162, 156, 70, 23);
		modelPane.add(btnReset);

		lblFaces = new JLabel("This mesh has ");
		lblFaces.setForeground(Color.DARK_GRAY);
		lblFaces.setFont(new Font("Tahoma", Font.ITALIC, 10));
		lblFaces.setBounds(0, 160, 161, 14);
		modelPane.add(lblFaces);

		JCheckBox chbSelect = new JCheckBox("Select All");
		chbSelect.setBounds(145, 9, 83, 23);
		chbSelect.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (chbSelect.isSelected()) {
					int[] indices = new int[models.size()];

					for (int i = 0; i < models.size(); i++) {
						indices[i] = i;
					}

					list.setSelectedIndices(indices);
				} else {
					list.clearSelection();
				}
			}
		});
		contentPanel.add(chbSelect);

		JButton btnDelete = new JButton("Delete");
		btnDelete.setFocusPainted(false);
		btnDelete.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<Mesh> rm = list.getSelectedValuesList();
				if (!rm.isEmpty())
					display.removeObject(rm);
			}
		});
		btnDelete.setForeground(new Color(139, 0, 0));
		btnDelete.setBounds(232, 9, 70, 23);
		contentPanel.add(btnDelete);

		JButton btnAdd = new JButton("Add");
		btnAdd.setFocusPainted(false);
		btnAdd.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new ModelAdder(display);
			}
		});
		btnAdd.setForeground(new Color(0, 100, 0));
		btnAdd.setBounds(312, 9, 70, 23);
		contentPanel.add(btnAdd);
		setLocationRelativeTo(display);

		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	}

	// ===================================

	public void updateList() {
		dlm.clear();
		for (Mesh m : models) {
			dlm.addElement(m);
		}
	}

	private void loadSelectedValue() {
		loaded = false;

		spScale.setValue(select.getScale());

		spPosX.setValue(select.getPosX());
		spPosY.setValue(select.getPosY());
		spPosZ.setValue(select.getPosZ());

		lblPosVal.setText("(" + select.getPosX() + ", " + select.getPosY() + ", " + select.getPosZ() + ")");

		spRotX.setValue(select.getRotX());
		spRotY.setValue(select.getRotY());
		spRotZ.setValue(select.getRotZ());

		lblRotVal.setText("(" + select.getRotX() + ", " + select.getRotY() + ", " + select.getRotZ() + ")");

		btnChooseColor.setColor(select.getColor());
		btnChooseColor.setText("Change " + select.getName() + "'s Color");

		if (select.getRenderAttribute() == Mesh.SOLID) {
			chbRenderType.setSelected(true);
		} else {
			chbRenderType.setSelected(false);
		}

		txtName.setText(select.getName());

		lblFaces.setText("This mesh has " + select.triangles.size() + " faces.");

		loaded = true;
	}
}
