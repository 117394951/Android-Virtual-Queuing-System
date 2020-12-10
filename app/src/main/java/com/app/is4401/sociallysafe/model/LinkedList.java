package com.app.is4401.sociallysafe.model;

public class LinkedList<E> {

    private int size;
    private Node<E> head;
    private Node<E> tail;

    // default constructor
    public LinkedList() {
        size = 0;
        head = null;
        tail = null;
    }

    // read-only property
    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        // replaces an if/else
        return (size == 0);
    }

    // return but not remove head of the list
    public E first() {
        if (isEmpty()) {
            return null;
        } else {
            return head.getElement();
        }
    }

    public E last() {
        if (isEmpty()) {
            return null;
        } else {
            return tail.getElement();
        }
    }

    public void addFirst(E e) {
        // create a new node and make it the new head of the list
        head = new Node<>(e, head);

        if (size == 0) {
            head = tail; // special case first item in the list
        }
        size++;
    }

    public void addLast(E e) {
        // create a new node and add to the tail of the list
        Node<E> newest = new Node<>(e, null);
        if (size == 0) {
            head = newest; // special case for the first item
        } else {
            tail.setNext(newest);
        }

        tail = newest;
        size++;
    }

    public E removeFirst() {
        if (isEmpty()) {
            return null;
        } else {
            E answer = head.getElement();
            head = head.getNext();
            size--;
            if (size == 0) {
                tail = null; // list is now empty
            }
            return answer;
        }
    }

    // nested class
    public class Node<E> {
        private E element;
        private Node<E> next;


        // custom constructor
        public Node(E e, Node<E> n) {
            element = e;
            next = n;

        }

        // get element
        public E getElement() {
            return element;
        }

        public Node<E> getNext() {
            return next;
        }

        public void setNext(Node<E> n) {
            next = n;
        }

    } // end nested node class

} // end of LinkedList
