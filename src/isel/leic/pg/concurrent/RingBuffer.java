package isel.leic.pg.concurrent;

/**
 * Ring buffer
 * Se buffer cheio perde os elementos mais recentes
 * @author Palex
 */
public class RingBuffer<T> {
    private final T[] buffer;
    private final boolean loseNewest;
    private volatile int put=0, get=0;

    @SuppressWarnings("unchecked")
    public RingBuffer(int bufSize, boolean loseNewest) {
        buffer = (T[]) new Object[bufSize];
        this.loseNewest = loseNewest;
    }

    private int inc(int value) { return (++value==buffer.length) ? 0 : value; }
    private int dec(int value) { return (--value<0) ? buffer.length-1 : value; }

    public synchronized void put(T elem) {
        if (loseNewest && inc(put)==get)
            return;
        buffer[put] = elem;
        put = inc(put);
        if (!loseNewest && put==get)
            get = inc(get);
        notifyAll();
    }

    public synchronized T get(long timeout) throws InterruptedException {
        if(put == get) {
            if (timeout<=0) return null;
            wait(timeout);
            if (put == get) return null;
        }
        T elem = buffer[get];
        buffer[get] = null;
        get = inc(get);
        return elem;
    }

    public boolean isEmpty() { return put == get; }

    public void clear() { put = get = 0; }
}
