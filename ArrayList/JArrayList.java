import java.util.Arrays;

/**
 * 自扩容变长数组 (线程不安全)
 * @author Coiggahou2002
 */
public class JArrayList<T extends Object> implements RandomAccess{

    /**
     * 暴露给客户端的容器大小，等于含有的有效元素个数
     */
    private int size;

    /**
     * 实际数组容量大小
     */
    private int capacity;

    /**
     * 默认初始化容量
     */
    private static final int DEFAULT_INIT_CAPACITY = 8;

    /**
     * 默认扩容倍数
     */
    private static final int DEFAULT_EXPAND_RATIO = 2;

    /**
     * 最大允许的capacity
     * 目前设置为 2^30 - 1
     */
    private static final int MAX_CAPACITY = Integer.MAX_VALUE >> 1;

    private Object[] elem;

    public JArrayList() {
        this.size = 0;
        this.capacity = DEFAULT_INIT_CAPACITY;
        this.elem = new Object[DEFAULT_INIT_CAPACITY];
    }

    public JArrayList(int capacity) {
        validateCapacity(capacity);
        this.size = 0;
        this.capacity = capacity;
        this.elem = new Object[capacity];
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return capacity;
    }

    private boolean isFull() {
        return size == capacity;
    }

    private void grow() {
        capacity *= DEFAULT_EXPAND_RATIO;
        /**
         * Arrays.copyOf() 给出一份给定长度的数组副本
         * 长度超过源数组的部分会用null填充
         */
        this.elem = Arrays.copyOf(this.elem, capacity);
    }

    public void add(T val) {
        if (isFull()) {
            grow();
        }
        this.elem[size++] = val;
    }

    private void validateIndexRange(int index) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("invalid index out of bounds");
        }
    }

    private void validateCapacity(int capacity) {
        if (capacity <= 0 || capacity > MAX_CAPACITY) {
            throw new IllegalArgumentException("invalid capacity");
        }
    }

    public T get(int index) {
        validateIndexRange(index);
        return (T) elem[index];
    }

    public int indexOf(T val) {
        /**
         * 为什么要先找 null ?
         * 为了下面能够用 equals
         */
        if (val == null) {
            /**
             * 为什么这里是 i < size 而不是 i < elem.length ?
             * 因为超过 size 大小的部分都是 null
             * 而我们应在“用户有意装入的部分”中查找 null 的存在
             */
            for (int i = 0; i < size; i++) {
                if (elem[i] == null) {
                    return i;
                }
            }
        }
        else {
            for (int i = 0; i < size; i++) {
                /**
                 * 1.为什么要用 equals ?
                 *    因为 T 可能重写过 equals 方法
                 * 2.为什么不是 elem[i].equals(val) ?
                 *    因为 elem[i]是 Object 类型
                 *    用 val.equals 才能调出T类型的equals方法
                 */
                if (val.equals(elem[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int lastIndexOf(T val) {
        if (val == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (elem[i] == null) {
                    return i;
                }
            }
        }
        else {
            for (int i = size - 1; i >= 0; i--) {
                if (val.equals(elem[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    public boolean contains(T val) {
        return indexOf(val) >= 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Object[] toArray() {
        return Arrays.copyOf(elem, size);
    }

    /**
     * for test
     * @return
     */
    @Override
    public String toString() {
        return "size: " + size() + ", " +
                "capacity: " + capacity() + ", " +
                "elem[] = " + Arrays.toString(elem) + ", " +
                "addr = " + elem.hashCode();
    }

    public static void main(String[] args) {
        JArrayList<Integer> vec = new JArrayList<>(3);
        System.out.println(vec);
        vec.add(3);
        System.out.println(vec);
        vec.add(4);
        System.out.println(vec);
        vec.add(39);
        System.out.println(vec);
        vec.add(55);
        System.out.println(vec);
        vec.add(222);
        vec.add(39);
        vec.add(224);
        System.out.println(vec);
        System.out.println("contains 55 ? " + vec.contains(55));
        System.out.println("contains 5 ? " + vec.contains(5));
        System.out.println("toArray hash = " + vec.toArray().hashCode() + ", " + Arrays.toString(vec.toArray()));
        System.out.println("index of 39 = " + vec.indexOf(39));
        System.out.println("last idx of 39 = " + vec.lastIndexOf(39));
    }
}
