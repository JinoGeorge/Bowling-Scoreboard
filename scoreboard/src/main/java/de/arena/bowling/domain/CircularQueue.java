package de.arena.bowling.domain;

import java.util.LinkedList;

/**
 * Circular queue with a fixed size.
 * It maintains the fixed size by removing the oldest entry when a new value is pushed into the queue.
 *
 * @param <E> the type parameter
 */
public class CircularQueue<E> extends LinkedList<E> {
    private int capacity = 10;

    public CircularQueue(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public void addFirst(E e) {
        if (size() >= capacity) {
            removeLast();
        }
        super.addFirst(e);
    }
}
