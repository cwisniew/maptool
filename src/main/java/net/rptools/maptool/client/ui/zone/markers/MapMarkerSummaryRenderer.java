package net.rptools.maptool.client.ui.zone.markers;

import java.awt.Color;
import javax.swing.UIManager;
import net.rptools.maptool.model.map.MapMarker;

public class MapMarkerSummaryRenderer {

  public final static String POPUP_TAG = "MapMarkerDetail";

  private final static MapMarkerSummaryRenderer instance = new MapMarkerSummaryRenderer();

  private MapMarker previousMarker = null;

  private final MapMarkerSummary summaryPanel = new MapMarkerSummary();

  private MapMarkerSummaryRenderer() {
  }

  public static MapMarkerSummaryRenderer getInstance() {
    return instance;
  }



  public void showDetail(MapMarker mapMarker, int x, int y) {
    if (previousMarker == mapMarker) {
      return;
    }

    if (mapMarker == null) {
      hideDetail();
      return;
    }
    previousMarker = mapMarker;
    summaryPanel.showSummary(mapMarker, x, y);
  }

  public void hideDetail() {
    previousMarker = null;
    summaryPanel.hideSummary();
  }
}
