/**
 * @author Coiggahou2002
 */
public class MyCircularQueue {

    /**
     * containter
     */
    int[] elem;

    /**
     * points to the front element
     */
    int front;

    /**
     * points to the next position of tail element
     */
    int rear;

    /**
     * how long is elem[]
     */
    int size;

    /**
     * how many elements can the queue hold (logically)
     */
    int capacity;

    public MyCircularQueue(int k) {
        this.elem = new int[k+1];
        this.front = 0;
        this.rear = 0;
        this.size = k+1;
        this.capacity = k;
    }

    /**
     * enter queue
     * @param value
     * @return if queue is full, return false
     */
    public boolean enQueue(int value) {
        if (isFull()) {
            return false;
        }
        elem[rear] = value;
        rear = (rear + 1) % size;
        return true;
    }

    /**
     * pop the front element of the queue
     * @return if queue empty, return false
     */
    public boolean deQueue() {
        if (isEmpty()) {
            return false;
        }
        front = (front + 1) % size;
        return true;
    }

    /**
     * get the front element
     * @return -1 if queue empty
     */
    public int Front() {
        if (isEmpty()) {
            return -1;
        }
        return elem[front];
    }

    /**
     * get the tail element
     * @return -1 if queue empty
     */
    public int Rear() {
        if (isEmpty()) {
            return -1;
        }
        int prev = (rear == 0) ? capacity : (rear - 1);
        return elem[prev];
    }

    /**
     * is queue empty
     */
    public boolean isEmpty() {
        return rear == front;
    }

    /**
     * is queue full
     */
    public boolean isFull() {
        return front == 0 ? (rear == capacity) : (rear == front - 1);
    }
}