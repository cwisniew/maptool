package net.rptools.maptool.client.framework.library;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import net.rptools.lib.memento.MementoBuilderParseException;
import net.rptools.maptool.client.AppStyle;
import net.rptools.maptool.model.Asset;

public class FrameworkLoader {

  public void load(File file) throws IOException {
    Set<Asset> assets = new HashSet<>();

      Path archivePath = file.toPath();
      byte[] frameworkArchiveBytes = Files.readAllBytes(archivePath);

      FileSystem fwZip = FileSystems.newFileSystem(archivePath);
      Path infoFile = fwZip.getPath("framework.json");
      String info = Files.readString(infoFile);

    FrameworkLibraryMemento memento = null;
    try {
      memento = new FrameworkLibraryMementoBuilder().fromJson(info).build();
    } catch (MementoBuilderParseException e) {
      throw  new IOException(e); // TODO: CDW need to clean up this error.
    }
    var framework = new FrameworkLibrary(memento);

      // At the very least we add the framework archive as an asset.
      assets.add(Asset.createFrameworkArchiveAsset(framework.getName(), frameworkArchiveBytes));
      System.out.println("DEBUG: would load " + file.getPath());
  }
}
