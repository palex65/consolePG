package isel.leic.pg.concurrent;

/**
 * Circular array of characters with limited size.
 * <p>The consumer blocks if there are no characters in the array</p>
 * <p>If it is full, the producer does not put new characters or crushes the oldest,
 * according to the constructor parameter.</p>
 *
 * <p>Used in the Console Frame class to store typed keys.</p>
 * @author Palex
 */
public class CharRingBuffer {
    private final char[] buffer;
    private final boolean loseNewest;
    private volatile int put=0, get=0;

    public CharRingBuffer(int bufSize, boolean loseNewest) {
        buffer = new char[bufSize];
        this.loseNewest = loseNewest;
    }

    private int inc(int value) { return (++value==buffer.length) ? 0 : value; }

    public synchronized void put(char c) {
        if (loseNewest && inc(put)==get)
            return;
        buffer[put] = c;
        put = inc(put);
        if (!loseNewest && put==get)
            get = inc(get);
        notifyAll();
    }

    public synchronized char get(long timeout) throws InterruptedException {
        if(put == get) {
            if (timeout<=0) return 0;
            wait(timeout);
            if (put == get) return 0;
        }
        char c = buffer[get];
        get = inc(get);
        return c;
    }
}
