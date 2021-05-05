package net.rptools.lib.memento;

import java.util.Set;

public class MementoBuilderParseException extends Exception {

  private final Set<String> errors;

  public MementoBuilderParseException(Set<String> errors) {
    super(errors.size() == 1 ? "Error parsing JSON: " : "Errors parsing JSON: " + String.join(", ", errors));
    this.errors = Set.copyOf(errors);
  }

  public Set<String> getErrors() {
    return errors;
  }
}
