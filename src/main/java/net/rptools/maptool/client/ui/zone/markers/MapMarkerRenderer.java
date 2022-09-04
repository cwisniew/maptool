package net.rptools.maptool.client.ui.zone.markers;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.SwingUtilities;
import net.rptools.maptool.client.ScreenPoint;
import net.rptools.maptool.client.ui.zone.ZoneRenderer;
import net.rptools.maptool.model.map.MapMarker;

public class MapMarkerRenderer {
  private final Graphics2D g2d;
  private final ZoneRenderer renderer;

  private final BufferedImage image;


  private record TextMetrics(int width, int height, int ascent, int padding) {}
  private record MarkerBounds(Rectangle2D bounds, Rectangle2D screenBounds) {}

  public MapMarkerRenderer(ZoneRenderer renderer, Graphics2D g2d) {
    this.renderer = renderer;
    this.g2d = (Graphics2D) g2d.create();
    this.g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice device = env.getDefaultScreenDevice();
    GraphicsConfiguration config = device.getDefaultConfiguration();
    this.image = config.createCompatibleImage(renderer.getWidth() * 2, renderer.getHeight() * 2,
        Transparency.TRANSLUCENT);
  }

  public Rectangle2D render(MapMarker mapMarker) {


    var textMetrics = getTextMetrics(mapMarker.getMapKey());
    var markerBounds = getBounds(textMetrics, mapMarker);

    var bounds = markerBounds.bounds();
    var screenBounds = markerBounds.screenBounds();

    var ellipse = new Ellipse2D.Double(0, 0, screenBounds.getWidth(), screenBounds.getHeight());

    var imgG2d = (Graphics2D) image.getGraphics();
    imgG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    imgG2d.setBackground(new Color(1, 1, 1, 0));
    imgG2d.clearRect(0, 0, image.getWidth(), image.getHeight());

    imgG2d.setPaint(mapMarker.getFillColor());
    imgG2d.fill(ellipse);
    imgG2d.setPaint(mapMarker.getTextColor());
    Font font = imgG2d.getFont();
    var newSize = (float) (font.getSize() * renderer.getScale());
    Font newFont =  font.deriveFont( newSize);
    imgG2d.setFont(newFont);
    int padding = (int) Math.ceil(textMetrics.padding() * renderer.getScale());
    int ascent = (int) Math.ceil(textMetrics.ascent() * renderer.getScale());

    imgG2d.drawString(mapMarker.getMapKey(), padding, padding + ascent);
    imgG2d.setPaint(mapMarker.getBorderColor());
    imgG2d.draw(ellipse);
    imgG2d.dispose();

    g2d.setPaint(new Color(0, 0, 0, 0));


    g2d.drawImage(image, (int)screenBounds.getX(), (int)screenBounds.getY(),
        (int)screenBounds.getMaxX(), (int)screenBounds.getMaxY(),
        0, 0, (int)screenBounds.getWidth(), (int)screenBounds.getHeight(), null);

    return screenBounds;
  }


  public Rectangle2D getScreenBounds(MapMarker mapMarker) {
    return getBounds(getTextMetrics(mapMarker.getMapKey()), mapMarker).screenBounds();
  }

  private MarkerBounds getBounds(TextMetrics textMetrics, MapMarker mapMarker) {
    int textWidth = textMetrics.width() + textMetrics.padding() * 2;
    int textHeight = textMetrics.height() + textMetrics.padding() * 2;
    var bounds = new Rectangle2D.Double(mapMarker.getIconX() - textWidth / 2.0,
        mapMarker.getIconY() - textHeight / 2.0,
        textWidth, textHeight);

    ScreenPoint sp1 = ScreenPoint.fromZonePointRnd(renderer, bounds.x, bounds.y);
    ScreenPoint sp2 = ScreenPoint.fromZonePointRnd(renderer, bounds.x + bounds.width, bounds.y + bounds.height);
    double width = sp2.x - sp1.x;
    double height = sp2.y - sp1.y;
    var screenBounds = new Rectangle2D.Double(sp1.x, sp1.y,  width,  height);


    return new MarkerBounds(bounds, screenBounds);
  }

  private TextMetrics getTextMetrics(String text) {
    // Calculate text sizes
    FontMetrics metrics = g2d.getFontMetrics();
    int textWidth = SwingUtilities.computeStringWidth(metrics, text);
    int textHeight = metrics.getHeight();
    int padding = g2d.getFontMetrics().stringWidth("M");
    int ascent = metrics.getAscent();

    return new TextMetrics(textWidth, textHeight, ascent, padding);


  }

  public void dispose() {
    this.g2d.dispose();
  }

}
