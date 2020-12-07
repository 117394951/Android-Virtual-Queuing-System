package com.app.is4401.sociallysafe.model;

import com.app.is4401.sociallysafe.Utils.QueueInterface;

public class LinkedListQueue<E> implements QueueInterface<E> {

    private LinkedList<E> list = new LinkedList<>(); // an empty list

    public LinkedListQueue() {
    } // new queue relies on the initially empty list

    public int size() {
            return list.getSize();
        }

        public boolean isEmpty() {
            return list.isEmpty();
        }

        public void enqueue(E element) {
            list.addLast(element);
        }

        public E first() {
            return list.first();
        }

        public E dequeue() {
            return list.removeFirst();
        }

    }
