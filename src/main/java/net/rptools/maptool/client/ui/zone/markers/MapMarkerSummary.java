package net.rptools.maptool.client.ui.zone.markers;

import java.awt.Color;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.model.map.MapMarker;
import org.checkerframework.checker.guieffect.qual.UI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class MapMarkerSummary extends JPanel {


  private SummaryPanel summaryPanel = new SummaryPanel();
  private SummaryPanel playerSummaryPanel = new SummaryPanel();

  private static class SummaryPanel extends JFXPanel {
    private static final Color WEB_VIEW_BACKGROUND_COLOR = UIManager.getColor("EditorPane.background");
    private static final Color WEB_VIEW_FOREGROUND_COLOR = UIManager.getColor("EditorPane.foreground");

    private WebView webView;

    public void setContent(String summary) {
      webView = new WebView();
      var pane = new StackPane();
      pane.getChildren().add(webView);

      var scene = new Scene(pane, 250, 150);
      setScene(scene);
      webView.getEngine().loadContent(summary);
      webView.getEngine().getLoadWorker().stateProperty().addListener(this::changed);
    }

    private void changed(ObservableValue<? extends State> observable,
        Worker.State oldState,
        Worker.State newState) {
      if (newState == Worker.State.SUCCEEDED) {
        Document doc = webView.getEngine().getDocument();

        // Add default CSS as first element of the head tag
        Element styleNode = doc.createElement("style");
        var styleString = String.format("body { background: #%06X; color: #%06X; }",
            WEB_VIEW_BACKGROUND_COLOR.getRGB() & 0x00FFFFFF, WEB_VIEW_FOREGROUND_COLOR.getRGB() & 0x00FFFFFF
        );
        Text styleContent = doc.createTextNode(styleString);
        styleNode.appendChild(styleContent);
        Node head = doc.getDocumentElement().getElementsByTagName("head").item(0);
        Node nodeCSS = head.insertBefore(styleNode, head.getFirstChild());
      }
    }
  }


  public MapMarkerSummary() {
    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    setOpaque(true);
    setBorder(UIManager.getBorder("Panel.border"));
    setBackground(UIManager.getColor("Panel.background"));
    setForeground(UIManager.getColor("Panel.foreground"));
  }


  public void showSummary(MapMarker mapMarker, int x, int y) {
    removeAll();
    var headingLabel = new JLabel(mapMarker.getMapKey() + " : " + mapMarker.getName());
    headingLabel.setFont(UIManager.getFont("h2.font"));

    add(headingLabel);


    var summary = mapMarker.getSummary();
    var playerSummary = mapMarker.getPlayerSummary();

    if (summary != null && !summary.isEmpty()) {
      add(new JSeparator());
      add(summaryPanel);
    }

    if (playerSummary != null && !playerSummary.isEmpty()) {
      add(new JSeparator());
      add(playerSummaryPanel);
    }

    var background = UIManager.getColor("EditorPane.background");
    var foreground = UIManager.getColor("EditorPane.foreground");

    Platform.runLater(() -> {
      setSummaries(summary, playerSummary, background, foreground);

      SwingUtilities.invokeLater(() -> {
        MapTool.getFrame().getZonePopupManager()
            .addComponent(this, MapMarkerSummaryRenderer.POPUP_TAG, x, y);
      });
    });
  }

  public void hideSummary() {
    MapTool.getFrame().getZonePopupManager().removeComponent(this);
  }

  private void setSummaries(String playerSummary, String summary, Color background,
      Color foreground) {
    if (playerSummary != null && !playerSummary.isEmpty()) {
      playerSummaryPanel.setContent(playerSummary);
    }
    if (summary != null && !summary.isEmpty()) {
      summaryPanel.setContent(summary);
    }

  }
}
