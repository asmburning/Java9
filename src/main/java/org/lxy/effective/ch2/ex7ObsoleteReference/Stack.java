package org.lxy.effective.ch2.ex7ObsoleteReference;

import java.util.Arrays;
import java.util.EmptyStackException;

// Can you spot the "memory leak"?

/**
 * So where is the memory leak? If a stack grows and then shrinks, the objects that
 * were popped off the stack will not be garbage collected, even if the program
 * using the stack has no more references to them. This is because the stack
 * maintains obsolete(废弃的) references to these objects. An obsolete reference is simply a
 * reference that will never be dereferenced(解除关联) again. In this case, any references
 * outside of the “active portion” of the element array are obsolete. The active
 * portion consists of the elements whose index is less than size.
 */

public class Stack {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public Object pop() {
        if (size == 0)
            throw new EmptyStackException();
        return elements[--size];
    }

    /**
     * Nulling out object references should be the exception rather than the norm.
     * The best way to eliminate an obsolete reference is to let the variable that
     * contained the reference fall out of scope.
     * @return
     */
    public Object pop2() {
        if (size == 0)
            throw new EmptyStackException();
        Object result = elements[--size];
        elements[size] = null; // Eliminate obsolete reference
        return result;
    }

    /**
     * Ensure space for at least one more element, roughly
     * doubling the capacity each time the array needs to grow.
     */
    private void ensureCapacity() {
        if (elements.length == size)
            elements = Arrays.copyOf(elements, 2 * size + 1);
    }
}

/**
 *  whenever a class manages its own memory, the
 *    programmer should be alert for memory leaks. Whenever an element is freed,
 *    any object references contained in the element should be nulled out.
 *  Another common source of memory leaks is caches.
 *  A third common source of memory leaks is listeners and other callbacks.
 *    One way toensure that callbacks are garbage collected promptly is to store only weak
 *    references to them, for instance, by storing them only as keys in a
 *    WeakHashMap
 */
