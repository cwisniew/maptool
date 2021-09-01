package net.rptools.maptool.api.frameworks;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import net.rptools.maptool.api.util.ApiCall;
import net.rptools.maptool.api.util.ApiListResult;
import net.rptools.maptool.api.util.ApiResult;
import net.rptools.maptool.client.MapTool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LibTokenApi {
  private static final Logger log = LogManager.getLogger(LibTokenApi.class);


  public CompletableFuture<ApiListResult<LibTokenInfo>> getLibTokens() {
    return new ApiCall<LibTokenInfo>().runOnSwingThreadList(this::doGetLibTokens);
  }


  public CompletableFuture<ApiResult<LibTokenInfo>> getLibToken(String name) {
    return new ApiCall<LibTokenInfo>().runOnSwingThread(() ->
      doGetLibTokens().stream().filter(lti -> name.equals(lti.name())).findFirst().orElse(null)
    );
  }

  private Set<LibTokenInfo> doGetLibTokens() {
    return MapTool.getFrame()
        .getZoneRenderers()
        .stream()
        .flatMap(
          zr -> zr.getZone()
              .getTokensFiltered(t -> t.getName().toLowerCase().startsWith("lib:"))
              .stream()
        )
        .map(t -> new LibTokenInfo(t.getId(), t.getName(), t.getMacroNames(false),
            t.getPropertyNames()))
        .collect(Collectors.toSet());
    }

}
