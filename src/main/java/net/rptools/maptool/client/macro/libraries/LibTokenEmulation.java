package net.rptools.maptool.client.macro.libraries;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;

public class LibTokenEmulation {

  private final static String UNDEFINED_PROPERTY_PATH = "data/undefined-properties/";


  private final String libTokenName;
  private final String libTokenVersion;
  private final Map<String, String> macroFunctionMap;
  private final Map<String, String> macroPropertyMap;
  private final Library library;

  public LibTokenEmulation(String name, String version, Library macroLibrary, Map<String, String> functionMap, Map<String, String> propertyMap) {
    libTokenName = name;
    libTokenVersion = version;
    macroFunctionMap = Collections.unmodifiableSortedMap(new TreeMap<>(functionMap));
    macroPropertyMap = Collections.unmodifiableSortedMap(new TreeMap<>(propertyMap));
    library = macroLibrary;
  }

  public String getLibTokenName() {
    return libTokenName;
  }

  public String getLibTokenVersion() {
    return libTokenVersion;
  }


  /**
   * Returns the {@link LibraryFunction} for the defined MTS function.
   * @param name the name of the defined function.
   * @return the defined function.
   */
  public Optional<LibraryFunction> getFunction(String name) {
    String path = macroFunctionMap.get(name);
    if (path == null) {
      return Optional.empty();
    }

    return library.getFunction(path);
  }


  public DataValue getProperty(String name) {
    String path = macroPropertyMap.get(name);
    if (path == null) {
      return DataValue.UNDEFINED;
    }

    return library.getData(path);
  }


  public void setProperty(String name, DataValue value) {
    String path = macroPropertyMap.get(name);
    if (path == null) {
      path = UNDEFINED_PROPERTY_PATH + UUID.randomUUID();
      macroPropertyMap.put(name, path);
    }

    library.setData(path, value);
  }

  /**
   * Returns the names of the functions defined for MTScript.
   * @return the names of the functions defined for MTScript.
   */
  public Collection<String> getFunctionNames() {
    return macroFunctionMap.keySet();
  }

  public Collection<String> getPropertyNames() {
    return macroPropertyMap.keySet();
  }

}
