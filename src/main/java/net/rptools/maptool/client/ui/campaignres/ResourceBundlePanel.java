package net.rptools.maptool.client.ui.campaignres;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import net.rptools.maptool.mtresource.CampaignResourceLibrary;
import net.rptools.maptool.mtresource.ResourceBundleTableModel;

public class ResourceBundlePanel extends JPanel {

  private JTable resourceBundlesTable;

  ResourceBundlePanel() {
    resourceBundlesTable = new JTable(new ResourceBundleTableModel(new CampaignResourceLibrary()));
    resourceBundlesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JScrollPane scrollPane = new JScrollPane(resourceBundlesTable);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    resourceBundlesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    setLayout(new BorderLayout());
    add(scrollPane, BorderLayout.CENTER);


    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    buttonPanel.add(new JButton("New"));
    buttonPanel.add(new JButton("Delete"));

    add(buttonPanel, BorderLayout.PAGE_END);
  }

}
