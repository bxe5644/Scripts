package scripts.utility;

import javax.swing.*;
import java.awt.*;

public class GUI {

    private final JDialog jDialog;

    private final JComboBox<String> jComboBoxLocation;
    private final JComboBox<String> jComboBoxStyle;
    private final JCheckBox jCheckBoxBank;
    private final JCheckBox jCheckBoxDrop;

    public GUI() {
        jDialog = new JDialog();
        jDialog.setModal(true);
        jDialog.setResizable(false);
        jDialog.setTitle("scripts.Overfished");
        jDialog.setSize(300, 400);
        jDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(null);

        JLabel jLabelLocation = new JLabel("Location");
        JLabel jLabelStyle = new JLabel("Style");
        jComboBoxLocation = new JComboBox<>();
        jComboBoxStyle = new JComboBox<>();

        jComboBoxLocation.addItem("Barbarian Village");
        jComboBoxLocation.addItem("Draynor");
        jComboBoxLocation.addItem("Lumbridge Swamp");
        jComboBoxLocation.addItem("Karamja");

        jComboBoxStyle.addItem("Bait");
        jComboBoxStyle.addItem("Fly");

        jCheckBoxBank = new JCheckBox("Bank");
        jCheckBoxDrop = new JCheckBox("Drop");
        ButtonGroup buttonGroup = new ButtonGroup();

        buttonGroup.add(jCheckBoxBank);
        buttonGroup.add(jCheckBoxDrop);
        jCheckBoxBank.setSelected(true);

        JButton buttonStart = new JButton("Start");
        buttonStart.setPreferredSize(new Dimension(100, 50));
        int buttonWidth = (int) buttonStart.getPreferredSize().getWidth();
        int buttonHeight = (int) buttonStart.getPreferredSize().getHeight();

        jPanel.add(jLabelLocation);
        jPanel.add(jLabelStyle);
        jPanel.add(jComboBoxLocation);
        jPanel.add(jComboBoxStyle);
        jPanel.add(jCheckBoxBank);
        jPanel.add(jCheckBoxDrop);
        jPanel.add(buttonStart);

        jLabelLocation.setBounds(20, 50, 50, 20);
        jLabelStyle.setBounds(20, 120, 50, 20);
        jComboBoxLocation.setBounds(100, 50, 150, 20);
        jComboBoxStyle.setBounds(100, 120, 150, 20);
        jCheckBoxBank.setBounds(20, 190, 60, 20);
        jCheckBoxDrop.setBounds(100, 190, 60, 20);
        buttonStart.setBounds(jDialog.getWidth() / 2 - buttonWidth / 2, 260, buttonWidth, buttonHeight);

        jDialog.getContentPane().add(jPanel);

        jComboBoxLocation.addActionListener(e -> {
            String selection = jComboBoxLocation.getSelectedItem().toString();
            if (selection.equals("Barbarian Village")) {
                jComboBoxStyle.removeAllItems();
                jComboBoxStyle.addItem("Bait");
                jComboBoxStyle.addItem("Fly");
            } else if (selection.equals("Draynor") || selection.equals("Lumbridge Swamp")) {
                jComboBoxStyle.removeAllItems();
                jComboBoxStyle.addItem("Bait");
                jComboBoxStyle.addItem("Net");
            } else {
                jComboBoxStyle.removeAllItems();
                jComboBoxStyle.addItem("Cage");
                jComboBoxStyle.addItem("Harpoon");
            }
        });

        buttonStart.addActionListener(e-> {
            close();
        });
    }

    public String getFishingLocation() {
        return jComboBoxLocation.getSelectedItem().toString();
    }

    public String getFishingStyle() {
        return jComboBoxStyle.getSelectedItem().toString();
    }

    public boolean isBanking() {
        return jCheckBoxBank.isSelected();
    }

    public void open() {
        jDialog.setVisible(true);
    }

    public void close() {
        jDialog.setVisible(false);
        jDialog.dispose();
    }
}