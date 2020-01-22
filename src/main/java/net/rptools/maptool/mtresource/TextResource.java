package net.rptools.maptool.mtresource;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import org.apache.commons.io.IOUtils;

public class TextResource extends MTAbstractResource {

  private String text;

  TextResource(String resourceName, String resourcePath, String txt) {
    super(resourceName, resourcePath, MTResourceType.TEXT);
    text = txt;
  }

  TextResource(String resourceName, String resourcePath, InputStream in) throws IOException {
    super(resourceName, resourcePath, MTResourceType.TEXT);
    StringWriter writer = new StringWriter();
    IOUtils.copy(in, writer, Charset.defaultCharset());
  }

  @Override
  public String getText() {
    return text;
  }



}
