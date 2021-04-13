package net.rptools.lib.memento;

import com.google.gson.JsonObject;

public interface MementoBuilder <T extends Memento<?>>{
  T build();
  T fromJson(JsonObject jsonObject);
}
