package net.rptools.maptool.mtresource;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class CampaignResourceBundle implements MTResourceBundle {

  private String name;
  private String qualifiedName;
  private Map<String, MTResource> resources = new TreeMap<>();
  private String version;


  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String bundleName) {
    name = bundleName;
  }

  @Override
  public String getVersion() {
    return version;
  }

  @Override
  public void setVersion(String versionString) {
    version = versionString;
  }

  @Override
  public String getQualifiedName() {
    return qualifiedName;
  }

  @Override
  public void setQualifiedName(String qname) {
    qualifiedName = qname;
  }

  @Override
  public Optional<MTResource> getResource(String path) {
    return Optional.ofNullable(resources.get(path));
  }

  @Override
  public void putResource(String path, MTResource res) {
    resources.put(path, res);
  }
}
