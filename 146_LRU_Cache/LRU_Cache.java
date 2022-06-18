class Node {
    Integer key;
    Integer val;
    public Node(int key, int val) {
        this.key = key;
        this.val = val;
    }
}

class LRUCache {

    private LinkedList<Node> list;
    private Map<Integer, Node> map;
    private int capacity;
    private int count;


    public LRUCache(int capacity) {
        this.list = new LinkedList<>();
        this.map = new HashMap<>();
        this.capacity = capacity;
        this.count = 0;
    }

    public int get(int key) {
        Node node = map.get(key);
        if (node != null) {
            list.remove(node);
            list.addFirst(node);
            return node.val;
        } else {
            return -1;
        }
    }

    public void put(int key, int value) {
        
        Node node = map.get(key);
        if (node != null) {
            // if already exist,
            // delete the old one, change its cal, 
            // and then move it to the head, 
            // the size of the list will not change
            list.remove(node);
            node.val = value;
            list.addFirst(node);
        } else {
            // if not exist,
            // first check capacity
            if (count == capacity) {
                // if full, we should first kick the Least-Recently-Used out
                Node lastNode = list.getLast();
                map.remove(lastNode.key);
                list.removeLast();
                count--;
            }
            Node newNode = new Node(key, value);
            map.put(key, newNode);
            list.addFirst(newNode);
            count++;
        }
       
    }
}