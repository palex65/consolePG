package isel.leic.pg.concurrent;

/**
 * A concurrent set of positive integer values.
 * <p>In addition to the basic operations of a set (add, remove, contains, isEmpty)
 * also has a method to obtain any element (getAny) that blocks if no elements.</p>
 *
 * <p>Used in the Console Frame class to store the keys that are currently pressed.</p>
 * @author palex
 */
public class PositiveIntSet {
    private final int[] elems;          // Array to contain the elements
    private volatile int size =0;       // Number of indexes used in array

    /**
     * Value returned by getAny(timeout) if there are no elements after the elapsed time indicated.
     */
    public static final int NO_ELEM = -1;

    /**
     * Constructs a new set with the indicated maximum capacity
     * @param maxSize maximum of values stored
     */
    public PositiveIntSet(int maxSize) {
        elems = new int[maxSize];
    }

    /**
     * Adds a value to the set.
     * <p>Releases the possible caller of getAny (if exists).</p>
     * @param value The value to store.
     * @return true if the value was stored or was already in the set.
     */
    public synchronized boolean add(int value) {
        if (contains(value)) return true;
        store:
        {
            for (int i = 0; i < size; ++i)
                if (elems[i] == NO_ELEM) {
                    elems[i] = value;
                    break store;
                }
            if (size==elems.length)
                return false;
            elems[size++] = value;
        }
        notifyAll();
        return true;
    }

    /**
     * Removes the specified value from the set.
     * @param value The value to be removed, if present.
     * @return true if the value was removed.
     */
    public synchronized boolean remove(int value) {
        if (size == 0)
            return false;
        if (elems[size - 1] == value) {
            --size;
            while (size > 0 && elems[size - 1] == NO_ELEM)
                --size;
            return true;
        }
        for (int i = 0; i < size - 1; ++i)
            if (elems[i] == value) {
                elems[i] = -1;
                return true;
            }
        return false;
    }

    /**
     *
     * @return true if this set contains no elements.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Verify if one value is contained in set.
     * @param value The value to be tested
     * @return true if this set contains the specified value
     */
    public synchronized boolean contains(int value) {
        for (int i = 0; i < size; ++i)
            if (elems[i] == value)
                return true;
        return false;
    }

    /**
     * Expected to be added any value to the set.
     * <p>Returns immediately if there is any value in the set</p>
     * @param timeout Maximum time to wait, in milliseconds, for some value is added
     * @return The value added or already contained in the set or NO_ELEM(-1) after elapsed the timeout
     * @throws InterruptedException
     */
    public synchronized int getAny(long timeout) throws InterruptedException {
        if(size ==0) {
            if (timeout<=0) return NO_ELEM;
            wait(timeout);
            if (size ==0) return NO_ELEM;
        }
        return elems[size -1];
    }

    public synchronized void clear() {
        size = 0;
    }
}
