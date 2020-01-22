package net.rptools.maptool.mtresource;

public interface MTResource {

  MTResourceType getResourceType();

  boolean isText();

  String getText();

  String getName();

  String getPath();

}
