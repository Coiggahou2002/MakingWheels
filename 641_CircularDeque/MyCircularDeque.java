/**
 * @author coiggahou
 */
public class MyCircularDeque {

    /**
     * [状态定义]
     * 队列满: rear == front - 1
     * 队列空: front == rear
     * 队列只有一个元素: rear == front + 1
     */

    private int[] elem;
    private int front;
    private int rear;
    private int capacity;

    public MyCircularDeque(int k) {
        this.elem = new int[k+1];
        this.capacity = k;
        this.front = 0;
        this.rear = 0;
    }

    public boolean insertFront(int value) {
        if (isFull()) return false;
        if (front == 0) {
            front = capacity;
            elem[front] = value;
        }
        else {
            elem[--front] = value;
        }
        return true;
    }

    public boolean insertLast(int value) {
        if (isFull()) return false;
        if (rear == capacity) {
            elem[rear] = value;
            rear = 0;
        }
        else {
            elem[rear++] = value;
        }
        return true;
    }

    public boolean deleteFront() {
        if (isEmpty()) return false;
        if (front == capacity) front = 0;
        else front++;
        return true;
    }

    public boolean deleteLast() {
        if (isEmpty()) return false;
        if (rear == 0) rear = capacity;
        else rear--;
        return true;
    }

    public int getFront() {
        if (isEmpty()) return -1;
        return elem[front];
    }

    public int getRear() {
        if (isEmpty()) return -1;
        return elem[rear == 0 ? capacity : rear-1];
    }

    public boolean isEmpty() {
        return front == rear;
    }

    public boolean isFull() {
        return rear == capacity ? (front == 0) : (rear + 1 == front);
    }
}
