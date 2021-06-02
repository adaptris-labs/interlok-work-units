package com.adaptris.workunit;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.spi.URLStreamHandlerProvider;

public class WorkUnitURLStreamHandlerProvider extends URLStreamHandlerProvider {

  private static final String WORK_UNIT_PROTOCOL = "workunit";

  @Override
  public URLStreamHandler createURLStreamHandler(String protocol) {
    if (WORK_UNIT_PROTOCOL.equals(protocol)) {
      return new URLStreamHandler() {
        @Override
        protected URLConnection openConnection(URL url) throws IOException {
          return ClassLoader.getSystemClassLoader().getResource(url.getPath()).openConnection();
        }
      };
    }
    return null;
  }

}
