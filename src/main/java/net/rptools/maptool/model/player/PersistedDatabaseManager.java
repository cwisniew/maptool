package net.rptools.maptool.model.player;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import net.rptools.maptool.client.AppUtil;

public class PersistedDatabaseManager {

  private enum DatabaseType {
    FILE
  };

  private final Map<String, DatabaseInfo> databaseInfoMap = new ConcurrentHashMap<>();

  private static final File PASSWORD_DIRECTORY =
      AppUtil.getAppHome("config").toPath().resolve("passwords.json").toFile();
  private static final String FILE_PERSISTED_DATABASE_CONFIG = PASSWORD_DIRECTORY +  "/password_db.json";


  public CompletableFuture<List<String>> getDatabaseNames() {
    return CompletableFuture.supplyAsync(() -> {
      if (!Files.exists(Path.of(FILE_PERSISTED_DATABASE_CONFIG))) {
        createConfigFile();
      }

      readDatabaseConfigFile();

      return new ArrayList<>(databaseInfoMap.keySet());
    });
  }

  public CompletableFuture<PersistedPlayerDatabase> getDatabase(String name) {
    return CompletableFuture.supplyAsync(() -> )
  }

  private void readDatabaseConfigFile() {
  }

  private void createConfigFile() {
  }




  private record DatabaseInfo(String name, DatabaseType databaseType, Object[] args) {};

}
