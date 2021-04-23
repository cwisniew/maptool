package net.rptools.lib.memento;

/**
 * Interface implemented by Originator classes whose state is represented by {@link Memento}
 * objects.
 *
 * @param <T> The Class for the originator.
 */
public interface Originator<T extends Memento<? extends Originator<?>>> {

    /**
     * Returns the state of the object as a {@link Memento}.
     * @return the state of the object as a {@link Memento}.
     */
    T getState();
}
