package scheme.syntax;

import java.util.Iterator;
import java.util.LinkedList;

class PushbackIterator<T> implements Iterator<T> {
    private final Iterator<T> it;
    private final LinkedList<T> stack;

    public PushbackIterator(Iterator<T> it) {
        this.it = it;
        this.stack = new LinkedList<>();
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty() || it.hasNext();
    }

    @Override
    public T next() {
        return !stack.isEmpty() ? stack.pop() : it.next();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    public void stash(T x) {
        stack.push(x);
    }
}
