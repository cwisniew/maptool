/*
 * This software Copyright by the RPTools.net development team, and
 * licensed under the Affero GPL Version 3 or, at your option, any later
 * version.
 *
 * MapTool Source Code is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public
 * License * along with this source Code.  If not, please visit
 * <http://www.gnu.org/licenses/> and specifically the Affero license
 * text at <http://www.gnu.org/licenses/agpl.html>.
 */
package net.rptools.maptool.framework.urlhandler;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.concurrent.ExecutionException;
import net.rptools.lib.MD5Key;
import net.rptools.lib.image.ImageUtil;
import net.rptools.maptool.api.frameworks.LibTokenApi;
import net.rptools.maptool.api.frameworks.LibTokenInfo;
import net.rptools.maptool.api.util.ApiResult;
import net.rptools.maptool.api.util.ApiResultStatus;
import net.rptools.maptool.model.AssetManager;
import net.rptools.maptool.util.ImageManager;

/**
 * Support "asset://" in Swing components
 *
 * @author Azhrei
 */
public class LibTokenURLStreamHandler extends URLStreamHandler {

  @Override
  protected URLConnection openConnection(URL u) {
    return new LibTokenURLConnection(u);
  }

  private static class LibTokenURLConnection extends URLConnection {

    private String libTokenName;
    private String location;

    public LibTokenURLConnection(URL url) {
      super(url);
      libTokenName = url.getHost();
      location = url.getPath();
    }

    @Override
    public void connect() {
      // Nothing to do
    }

    @Override
    public InputStream getInputStream() throws IOException {
      // TODO CDW: break up URL like
      // lib:<tokenName>/<macroName>
      LibTokenInfo libTokenInfo;
      try {
        ApiResult<LibTokenInfo> result = new LibTokenApi().getLibToken(libTokenName).get();
        if (result.getStatus() == ApiResultStatus.ERROR) {
          throw new IOException(result.getException());
        } else if (result.getStatus() == ApiResultStatus.NONE) {
          throw new IOException(url.toExternalForm() + " not found");
        }

        if (!libTokenInfo.macroNames().contains())

      } catch (InterruptedException | ExecutionException e) {
        throw new IOException(e);
      }
      LibTo
      String id = url.getHost();
      if (url.getQuery() == null) {
        var asset = AssetManager.getAssetAndWait(new MD5Key(id));
        var stream = new ByteArrayInputStream(asset.getImage());
        return stream;
      }

      BufferedImage img = ImageManager.getImageFromUrl(url);
      return new ByteArrayInputStream(ImageUtil.imageToBytes(img, "png"));
    }
  }
}
