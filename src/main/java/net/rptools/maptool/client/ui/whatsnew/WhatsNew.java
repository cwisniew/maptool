package net.rptools.maptool.client.ui.whatsnew;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import javax.swing.SwingUtilities;
import net.rptools.lib.MD5Key;
import net.rptools.maptool.client.AppConstants;
import net.rptools.maptool.client.ui.WhatsNewDialog;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WhatsNew {

  private static final Logger log = LogManager.getLogger(WhatsNew.class);


  private final String FILE_PREFIX = "whats-new";
  private final String FILE_SUFFIX = ".html";
  private final String MD5_SUFFIX = ".md5";

  private final String FALLBACK_LANG_CODE = "en";

  private final String WHATS_NEW_URL_BASE = "https://maptool-info.rptools.net/whats-new/";

  private final Path whatsNewDir;
  private final Path lastReadMD5Path;
  private final Path whatsNewFile;
  private final String whatsNewURL;
  private final String whatsNewFallBackURL;

  private String whatsNewText;
  private MD5Key whatsNewMD5;


  public WhatsNew() {
    whatsNewDir = AppConstants.WHATS_NEW_DIR.toPath();
    String languageCode = Locale.getDefault().getLanguage();
    lastReadMD5Path = whatsNewDir.resolve(FILE_PREFIX + "-" + languageCode + MD5_SUFFIX);
    whatsNewFile = whatsNewDir.resolve(FILE_PREFIX + "-" + languageCode + FILE_SUFFIX);
    whatsNewURL = WHATS_NEW_URL_BASE + FILE_PREFIX + "-" + languageCode + FILE_SUFFIX;
    whatsNewFallBackURL = WHATS_NEW_URL_BASE + FILE_PREFIX + "-" + FALLBACK_LANG_CODE + FILE_SUFFIX;
  }


  public void fetchBackgroundAndDisplay() {
    new Thread(() -> {
      fetch();
      MD5Key current = getWhatsNewMD5();
      MD5Key last = getLastReadMD5();
      if (last == null || !last.equals(current)) {
        display();
        updateLastReadMD5(current);
      }
    }).start();
  }

  private void updateLastReadMD5(MD5Key current) {
    try {
      Files.writeString(lastReadMD5Path, current.toString());
    } catch (IOException e) {
      log.error("Unable to read file containing last read 'whats new' details", e);
    }
  }

  private MD5Key getLastReadMD5() {
    if (!lastReadMD5Path.toFile().exists()) {
      return null;
    }
    try {
      String last = Files.readString(lastReadMD5Path);
      return new MD5Key(last.getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      log.error("Unable to read file containing last read 'whats new' details", e);
      return null;
    }
  }

  public void fetchBackground() {
    new Thread(this::fetch).start();
  }

  public void display() {
    if (SwingUtilities.isEventDispatchThread()) {
      displayEDT();
    } else {
      SwingUtilities.invokeLater(this::displayEDT);
    }
  }


  private void displayEDT() {
    try {
      String whatsNewText = Files.readString(whatsNewFile);
      WhatsNewDialog dialog = new WhatsNewDialog(whatsNewText);
      dialog.show();
    } catch (IOException e) {
      log.error("Problem reading whats new file", e);
    }
  }

  private void fetch() {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder().url(whatsNewURL).build();


    String wnText = null;
    try (Response response = client.newCall(request).execute()) {
      if (response.code() == 404) {
        Request requestFallback = new Request.Builder().url(whatsNewFallBackURL).build();
        try (Response responseFallback = client.newCall(requestFallback).execute()) {
          if (responseFallback.code() == 200) {
            if (responseFallback.body() != null) {
              wnText = responseFallback.body().string();
            }
          }
        }
      } else if (response.code() == 200) {
        if (response.body() != null) {
          wnText = response.body().string();
        }
      }
    } catch (IOException e) {
      log.error("Problem fetching whats new", e);
    }

    if (wnText != null) {
      setWhatsNewText(wnText);
      try {
        Files.writeString(whatsNewFile, wnText);
        calcWhatsNewMD5(wnText);
      } catch (Exception e) {
        log.error("Problem fetching whats new", e);
      }
    }
  }

  private synchronized void calcWhatsNewMD5(String wnText) {
    whatsNewMD5 = new MD5Key(wnText.getBytes(StandardCharsets.UTF_8));
  }

  private synchronized MD5Key getWhatsNewMD5() {
    return whatsNewMD5;
  }

  private synchronized void setWhatsNewText(String text) {
    whatsNewText = text;
  }

  private synchronized String getWhatsNewText() {
    return whatsNewText;
  }
}
