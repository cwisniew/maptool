package net.rptools.maptool.mtresource;

import java.util.Optional;

public interface MTResourceBundle {

  String getName();


  void setName(String bundleName);

  String getVersion();

  void setVersion(String versionString);

  String getQualifiedName();

  void setQualifiedName(String qname);

  Optional<MTResource> getResource(String path);

  void putResource(String path, MTResource res);

}
