package net.rptools.maptool.client.framework.library;

import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import net.rptools.maptool.model.Asset;

public class FrameworkLoader {

  public Set<Asset> load(URL url) {
    Set<Asset> assets = new HashSet<>();

    try {
      Path archivePath = Paths.get(url.getPath());
      byte[] frameworkArchiveBytes = Files.readAllBytes(archivePath);

      FileSystem fwZip = FileSystems.newFileSystem(archivePath);
      Path infoFile = fwZip.getPath("framework.json");
      String info = Files.readString(infoFile);

      var memento = new FrameworkLibraryMementoBuilder().fromJson(info).build();
      var framework = new FrameworkLibrary(memento);

      // At the very least we add the framework archive as an asset.
      assets.add(Asset.createFrameworkArchiveAsset(framework.getName(), frameworkArchiveBytes));
      System.out.println("DEBUG: would load " + url.getPath());
    } catch (Exception e) {
      System.err.println("DEBUG: ERROR " + e.getMessage());
    }

    return new HashSet<>();
  }
}
