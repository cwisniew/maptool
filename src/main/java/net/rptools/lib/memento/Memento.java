package net.rptools.lib.memento;


public interface Memento<T extends Originator<?>> {
    Class<T> getOriginatorClass();
}
