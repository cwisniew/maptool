package net.rptools.maptool.mtresource;

class MTAbstractResource implements MTResource {

  private String name;
  private String path;
  private MTResourceType resourceType;

  MTAbstractResource(String resourceName, String resourcePath, MTResourceType type) {
    name = resourceName;
    path = resourcePath;
    resourceType = type;
  }

  @Override
  public MTResourceType getResourceType() {
    return resourceType;
  }

  @Override
  public boolean isText() {
    return MTResourceType.TEXT.equals(getResourceType());
  }

  @Override
  public String getText() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getPath() {
    return path;
  }
}
