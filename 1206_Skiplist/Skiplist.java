
class Node {
    /**
     * value of the node
     */
    int val;

    /**
     * how many level does the node occupy
     * (level starts from 1)
     */
    int levels;

    /**
     * count repeat times for repeated value
     * for example, when insert x, if x is inserted before, just do num++
     * when delete x, if x is repeated multiple times, just do num--
     */
    int num;

    /**
     * store pointers pointed to next nodes
     */
    Node[] nextNodes;

    public Node(int val, int levels) {
        this.val = val;
        this.levels = levels;
        this.num = 1;
        this.nextNodes = new Node[levels+1];
    }
}

/**
 * @author Coiggahou 2002
 */
public class Skiplist {

    Node headerNode;

    /**
     * determined by MAX_LEVEL, the skip list had better hold no more than 2^{MAX_LEVEL} elements.
     */
    static final int MAX_LEVEL = 15;

    /**
     * We assume that only non-negative elements will be put into the skiplist,
     * so the val of headerNode is set to -1 because -1 is less than any non-negative integer
     * (to ensure that any element inserted would be put after the headerNode)
     * otherwise it should be set to -INF
     */
    public Skiplist() {
        this.headerNode = new Node(-1, MAX_LEVEL);
    }

    /**
     * search for target in the skiplist
     * @param target
     * @return true for found, false for notfound
     */
    public boolean search(int target) {
        // start from the top level
        int curLevel = headerNode.levels;
        Node curNode = headerNode;
        while (curLevel >= 1) {
            // keep moving forward till meet the next node whose val >= val of curNode
            while (curNode != null && curNode.nextNodes[curLevel] != null && curNode.nextNodes[curLevel].val < target) {
                curNode = curNode.nextNodes[curLevel];
            }
            if (curNode.nextNodes[curLevel] != null && curNode.nextNodes[curLevel].val == target) {
                // found!
                return true;
            } else {
                // move down
                curLevel--;
            }
        }
        // now curLevel = 0 (invalid)
        // if still not found, then target doesn't exist
        return false;
    }

    /**
     * produce randomize level for a newly-inserted node
     * (by flipping coins)
     * @return random level (<= MAX_LEVEL)
     */
    int produceRandomLevel() {
        int randomLevel = 1;
        double p = Math.random();
        while (p < 0.5 && randomLevel < MAX_LEVEL) {
            randomLevel++;
            p = Math.random();
        }
        return randomLevel;
    }

    /**
     * add num to the skiplist
     * @param num
     */
    public void add(int num) {
        int curLevel = headerNode.levels;
        Node curNode = headerNode;

        // to collect the pointers to update
        Node[] nodesToUpdate = new Node[headerNode.levels+1];

        while (curLevel >= 1) {
            // keep moving forward till meet the next node whose val >= val of curNode
            while (curNode != null && curNode.nextNodes[curLevel] != null && curNode.nextNodes[curLevel].val < num) {
                curNode = curNode.nextNodes[curLevel];
            }
            if (curNode.nextNodes[curLevel] != null && curNode.nextNodes[curLevel].val > num) {
                // when move down, keep record of the node for the later update
                nodesToUpdate[curLevel] = curNode;
                // move down
                curLevel--;
            } else if (curNode.nextNodes[curLevel] != null && curNode.nextNodes[curLevel].val == num){
                // stop insert because num already exist
                // node num++
                curNode.nextNodes[curLevel].num++;
                return;
            } else {
                // when move down, collect the curNode for the later update
                nodesToUpdate[curLevel] = curNode;
                // move down
                curLevel--;
            }
        }

        // now curLevel = 0 (invalid)

        int newNodeLevel = produceRandomLevel();

        Node newNode = new Node(num, newNodeLevel);

        // update all links to new node
        // and connect new node to all next nodes
        for (int level = 1; level <= newNodeLevel; level++) {
            Node originNextNode = nodesToUpdate[level].nextNodes[level];
            nodesToUpdate[level].nextNodes[level] = newNode;
            newNode.nextNodes[level] = originNextNode;
        }

    }


    /**
     * delete num if num exists (just delete once)
     * @param num
     * @return true for deleted once successfully, false for not found
     */
    public boolean erase(int num) {
        int curLevel = headerNode.levels;
        Node curNode = headerNode;
        Node[] nodesToUpdate = new Node[headerNode.levels+1];
        while (curLevel >= 1) {
            // keep moving forward till meet the next node whose val >= val of curNode
            while (curNode != null && curNode.nextNodes[curLevel] != null && curNode.nextNodes[curLevel].val < num) {
                curNode = curNode.nextNodes[curLevel];
            }
            // now the node in front of us is either null or a node whose val greater or equals num
            if (curNode.nextNodes[curLevel] != null) {
                // compare next val (should be either greater or equals curVal)
                int nextVal = curNode.nextNodes[curLevel].val;
                if (nextVal == num) {
                    // found!
                    
                    // if has more than one, than do not delete, just decrease its num
                    if (curNode.nextNodes[curLevel].num > 1) {
                        curNode.nextNodes[curLevel].num--;
                        return true;
                    }
                    
                    // else if num == 1, we do delete it
                    
                    // record current node to updates[]
                    nodesToUpdate[curLevel] = curNode;

                    // deal with the last few updates[]
                    Node trgtNode = curNode.nextNodes[curLevel];

                    for (int level = curLevel - 1; level >= 1; level--) {
                        Node itr = headerNode;
                        while (itr.nextNodes[level] != null && itr.nextNodes[level] != trgtNode) {
                            itr = itr.nextNodes[level];
                        }
                        nodesToUpdate[level] = itr;
                    }


                    // move all links of curNode to the nextNode of the node to delete
                    // delete it!
                    for (int level = 1; level <= trgtNode.levels; level++) {
                        nodesToUpdate[level].nextNodes[level] = trgtNode.nextNodes[level];
                    }
                    return true;
                } else { // nextVal > target
                    // move down
                    nodesToUpdate[curLevel] = curNode;
                    curLevel--;
                }
            } else {
                // move down
                nodesToUpdate[curLevel] = curNode;
                curLevel--;
            }
        }
        return false;
    }
}