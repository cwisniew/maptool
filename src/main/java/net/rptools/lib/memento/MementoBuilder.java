package net.rptools.lib.memento;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class MementoBuilder <T extends Memento<?>>{


  private final transient Class<T> mementoClass;

  protected MementoBuilder(Class<T> mementoClass) {
    this.mementoClass = mementoClass;
  }

  public abstract T build();

  public abstract MementoBuilder<T> fromState(T state);

  public abstract MementoBuilder<T> fromJson(JsonObject json) throws MementoBuilderParseException;

  public MementoBuilder<T> fromJson(String json) throws MementoBuilderParseException {
    return fromJson(JsonParser.parseString(json).getAsJsonObject());
  }

  public String toJsonString() {
    return new Gson().toJson(toJson());
  }

  public JsonObject toJson() {
    return new Gson().toJsonTree(this).getAsJsonObject();
  }


}
