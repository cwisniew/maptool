package net.rptools.lib.memento;

public interface Originator<T extends Memento<? extends Originator<?>>> {
    T getState();
    void setState(T state);
}
