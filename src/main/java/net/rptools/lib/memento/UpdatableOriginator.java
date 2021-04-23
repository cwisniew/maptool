package net.rptools.lib.memento;

/**
 * Interface implemented by Originator classes whose state is represented by {@link Memento}
 * objects.
 *
 * @param <T> The Class for the originator.
 */
public interface UpdatableOriginator<T extends Memento<? extends UpdatableOriginator<?>>> extends Originator<T>{

  /**
     * Updates a {@code Originator} object based on the state that is passed.
     *
     * @param state the state of the object.
     */
    void setState(T state);
}
