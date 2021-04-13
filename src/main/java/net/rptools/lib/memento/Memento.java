package net.rptools.lib.memento;

import com.google.gson.JsonObject;

public interface Memento<T extends Originator> {
    Class<T> getOriginatorClass();
    JsonObject getStateAsJson();
    void setStateFromJson(JsonObject json);
}
