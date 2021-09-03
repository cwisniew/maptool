package net.rptools.maptool.model.framework;

import net.rptools.lib.AppEvent;
import net.rptools.lib.AppEventListener;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.MapTool.ZoneEvent;
import net.rptools.maptool.model.ModelChangeEvent;
import net.rptools.maptool.model.ModelChangeListener;

public class Framework {


  private static class Listener implements ModelChangeListener, AppEventListener {

    private Listener() {
      MapTool.getEventDispatcher().addListener(this, ZoneEvent.Added);
      MapTool.getEventDispatcher().addListener(this, ZoneEvent.Removed);
      MapTool.getFrame().getCurrentZoneRenderer().getZone().addModelChangeListener(this);
    }

    @Override
    public void handleAppEvent(AppEvent event) {

    }

    @Override
    public void modelChanged(ModelChangeEvent event) {

    }
  }


}
