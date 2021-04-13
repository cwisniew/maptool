package net.rptools.lib.memento;

public interface Originator<T extends Originator> {
    Memento<T> getState();
    void setState(Memento<T> state);
}
