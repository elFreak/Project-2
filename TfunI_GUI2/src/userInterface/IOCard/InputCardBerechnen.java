package userInterface.IOCard;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Model;
import projectT_Fun_I.GlobalSettings;
import userInterface.Controller;
import userInterface.MyBorderFactory;
import userInterface.Numbers;

public class InputCardBerechnen extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	Controller controller;

	private JPanel panelOrdnung = new JPanel(new GridBagLayout());
	private JButton btAll = new JButton("Alle");
	private JButton btNone = new JButton("Keine");
	private JCheckBox[] cB = new JCheckBox[9];
	private JLabel[] lB = new JLabel[9];


	public InputCardBerechnen(Controller controller) {
		this.controller = controller;

		// GŁtebestimmung Filter
		panelOrdnung.setBackground(GlobalSettings.colorBackground);
		panelOrdnung.setBorder(MyBorderFactory.createMyBorder("Ordnung anzeigen"));

		for (int i = 0; i < cB.length; i++) {
			cB[i] = new JCheckBox();
			cB[i].setOpaque(false);
			cB[i].addActionListener(this);
			cB[i].setEnabled(false);
			cB[i].setOpaque(true);
			cB[i].setBackground(GlobalSettings.colorsTraceSolution[i]);
			lB[i] = new JLabel();
			lB[i].setText("" + (i + 2) + ": ");

		}

		panelOrdnung.add(lB[0], new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END,
				GridBagConstraints.NONE, new Insets(20, 20, 10, 10), 0, 0));
		panelOrdnung.add(lB[1], new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END,
				GridBagConstraints.NONE, new Insets(20, 20, 10, 10), 0, 0));
		panelOrdnung.add(lB[2], new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END,
				GridBagConstraints.NONE, new Insets(20, 20, 10, 10), 0, 0));
		panelOrdnung.add(lB[3], new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END,
				GridBagConstraints.NONE, new Insets(20, 20, 10, 10), 0, 0));
		panelOrdnung.add(lB[4], new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END,
				GridBagConstraints.NONE, new Insets(20, 20, 10, 10), 0, 0));
		panelOrdnung.add(lB[5], new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END,
				GridBagConstraints.NONE, new Insets(20, 20, 10, 10), 0, 0));
		panelOrdnung.add(lB[6], new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END,
				GridBagConstraints.NONE, new Insets(20, 20, 10, 10), 0, 0));
		panelOrdnung.add(lB[7], new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END,
				GridBagConstraints.NONE, new Insets(20, 20, 10, 10), 0, 0));
		panelOrdnung.add(lB[8], new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END,
				GridBagConstraints.NONE, new Insets(20, 20, 10, 10), 0, 0));

		panelOrdnung.add(cB[0], new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(20, 0, 10, 40), 0, 0));
		panelOrdnung.add(cB[1], new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(20, 0, 10, 40), 0, 0));
		panelOrdnung.add(cB[2], new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(20, 0, 10, 40), 0, 0));
		panelOrdnung.add(cB[3], new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(20, 0, 10, 40), 0, 0));
		panelOrdnung.add(cB[4], new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(20, 0, 10, 40), 0, 0));
		panelOrdnung.add(cB[5], new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(20, 0, 10, 40), 0, 0));
		panelOrdnung.add(cB[6], new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(20, 0, 10, 40), 0, 0));
		panelOrdnung.add(cB[7], new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(20, 0, 10, 40), 0, 0));
		panelOrdnung.add(cB[8], new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(20, 0, 10, 40), 0, 0));
		panelOrdnung.add(btAll, new GridBagConstraints(0, 5, 4, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		panelOrdnung.add(btNone, new GridBagConstraints(0, 6, 4, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));

		// Cardpanel konfigurieren
		this.setBackground(GlobalSettings.colorBackground);

		// Cardpanel konfigurieren
		this.setLayout(new GridBagLayout());
		this.setBackground(GlobalSettings.colorBackground);

		this.add(panelOrdnung, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(20, 10, 10, 10), 0, 0));
		JPanel panelBackground2 = new JPanel();
		panelBackground2.setBackground(GlobalSettings.colorBackground);
		this.add(panelBackground2, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.BOTH, new Insets(20, 10, 10, 10), 0, 0));

		btAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean[] active = new boolean[cB.length];
				for (int i = 0; i < cB.length; i++) {
					cB[i].setSelected(true);
					controller.calculateUTF(i+2);
					active[i] = cB[i].isSelected();
				}
				controller.setBerechnenCBActive(active);
			}
		});

		btNone.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean[] active = new boolean[cB.length];
				for (int i = 0; i < cB.length; i++) {
					cB[i].setSelected(false);
					active[i] = cB[i].isSelected();
				}
				controller.setBerechnenCBActive(active);
			}
		});

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(cB[0])) {
			controller.calculateUTF(2);
		}
		if (e.getSource().equals(cB[1])) {
			controller.calculateUTF(3);
		}
		if (e.getSource().equals(cB[2])) {
			controller.calculateUTF(4);
		}
		if (e.getSource().equals(cB[3])) {
			controller.calculateUTF(5);
		}
		if (e.getSource().equals(cB[4])) {
			controller.calculateUTF(6);
		}
		if (e.getSource().equals(cB[5])) {
			controller.calculateUTF(7);
		}
		if (e.getSource().equals(cB[6])) {
			controller.calculateUTF(8);
		}
		if (e.getSource().equals(cB[7])) {
			controller.calculateUTF(9);
		}
		if (e.getSource().equals(cB[8])) {
			controller.calculateUTF(10);
		}
		boolean[] active = new boolean[cB.length];
		for (int i = 0; i < cB.length; i++) {
			active[i] = cB[i].isSelected();
		}
		controller.setBerechnenCBActive(active);
	}

	/**
	 * 
	 * @param obs
	 * @param obj
	 */
	public void update(java.util.Observable obs, Object obj) {
		if (((int) obj) == Model.NOTIFY_REASON_NETWORK_START_VALUES) {
			for (int i = 0; i < cB.length; i++) {
				cB[i].setEnabled(true);
			}
		}
	}
}
