package com.app.is4401.sociallysafe.Utils;

public interface QueueInterface<E> {
    int size();

    boolean isEmpty();

    // adds an item to the stack
    void enqueue(E e);

    // return but not remove the top item on the stack
    E first();

    // remove item at the top of the stack
    E dequeue();

}
