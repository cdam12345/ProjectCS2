import java.util.*;
public class SkipListSet
{
    private SkipListSetNode head;
    private int level;
    private int size;
    private Random ran = new Random(); // random  object to randomize levels

    // this wrapper class implements comparable
    private class SkipListSetNode
    {
        private Object value;
        private SkipListSetNode [] forward; // Should use ArrayList of pointers to the right because regular arrays can't handle generics 

        private Object value()
        {
            return this.value;
        }

        SkipListSetNode(Object key, int lev)
        {
            value = key;

            // an array of skiplist to save the references of all skip list nodes
            forward = new SkipListSetNode[lev+1];
            for (int i = 0; i < lev; i++) // test later if i <= lev works
                forward[i] = null;
        }
    }

    // Implement this functions from the Iterator interface: This class implements the Iterator interface
    private class SkipListSetIterator
    {

        // hasNext
        // next
        // remove 
    }

    // Default Constructor that does not accept arguments. Returns an empty skiplist  
    SkipListSet()
    {
        // passing 0 for for the value of the head. Later, make this to null because the constructor will get a reference
        // to an object. For now, I am setting 0 as if it were null for the head
        this.head = new SkipListSetNode(0, 0);

        // "Floor level" starts being -1, this is where all the values are stored. forward pointers start at level 0
        this.level = -1;
        this.size = 0;
    }

    public void print() {    // Prints out contents of a skiplist
        SkipListSetNode temp = head;

        if (temp == null)
        {
            System.out.println("=====Nothing to print======");
        }

        else 
        {
            while (temp != null) {
            System.out.print(temp.value() + ": length is " + temp.forward.length + ":");
            for (int i=0; i<temp.forward.length; i++)
            {
                if (temp.forward[i] == null)
                    System.out.print("null ");
                else
                    System.out.print(temp.forward[i].value() + " ");
            }
            System.out.println();
            temp = temp.forward[0];
            }
            System.out.println();
        }
      }

    // Helper function to randomize the level of a new node
    private int randomizeLevel()
    {
        int newLevel;
        for (newLevel = 0; Math.abs(ran.nextInt() % 2 ) == 0; newLevel++);
        return newLevel;
    }

    private void adjustHeadLevel(int newLevel)
    {
        SkipListSetNode temp = head;
        head = new SkipListSetNode(0, newLevel);
        for (int i = 0; i <= level; i++)
            head.forward[i] = temp.forward[i];
        level = newLevel;
    }

    //  inserts a key to an appropiate place in the skipList. I am inserting everything in sorted order for now.
    private void insertNode(int key)
    {
        // pass 2 to get a geometric distrubution for node's randomized level
        int newLevel = randomizeLevel();
        if (newLevel > level)
            adjustHeadLevel(newLevel);

        SkipListSetNode [] tempNodeReferences = new SkipListSetNode[level+1];

        SkipListSetNode temp = head;

        for (int i = level; i >= 0; i--)
        {
            while ((temp.forward[i] != null) && (((int)temp.forward[i].value()) < key) )
            {
                temp = temp.forward[i];
            }
            tempNodeReferences[i] = temp;
        }

        temp = new SkipListSetNode(key, newLevel);

        for (int i = 0; i <= newLevel; i++)
        {
            temp.forward[i] = tempNodeReferences[i].forward[i];
            tempNodeReferences[i].forward[i] = temp;
        }

        this.size++;
    }

    // Implement all this functions from the SortedSet Interface: 

    // add
    // addAll

    // Removes all of the elements from this skipList set
    private void clear()
    {
        head = null;
    }
    // comparator
    private boolean contains(int key)
    {
        if (size() > 0)
        {
            SkipListSetNode temp = head;
            for (int i = level; i >= 0; i--)
            {
                while ( (temp.forward[i] != null) && ((int)temp.forward[i].value() <= key) )
                {
                    temp = temp.forward[i];
                    if ((int)temp.value() == key)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }
    // containsAll
    // equals

    // Returns the first (lowest) element in current SkipListSet. Returns an int for now
    private int first()
    {
        // SkipList is not empty
        if (size > 0 ) 
            return (int)head.forward[0].value; // typecasting for now
        return -1; // returns -1 for now if skipList is empty
    }

    // hashCode
    // headSet

    // Returns true or false if current skipList set is empty or not
    private boolean isEmpty()
    {
        return size == 0;
    }
    // iterator

    // Returns the last (highest) element in current SKipListSet. Returns int for now
    private int last()
    {   
        // Ierate as long as there is at least an item in the skipList
        if (size() > 0)
        {
            SkipListSetNode temp = head;
            for (int i = level; i >= 0; i--)
            {
                while (temp.forward[i] != null)
                    temp = temp.forward[i];
            }

            // When i becomes negative we will have iterated to the very last entry
            return (int)temp.value();
        }

        return -1; // returns -1 from now
    } 

    // remove
    // removeAll
    // retainAll
    private int size()
    {
        return size;
    }

    private int maxLevel()
    {
        return head.forward.length;
    }
    // subSet
    // tailSet

    // returns an array containing all the elements in this set. 

    // Returns an Object array of each entry in the skipList
    // toArray

    // Returns an Generic Array <T> [] of each entry in the skipList 
    // toArray

    public static void main(String[] args) 
    {
        SkipListSet sk = new SkipListSet();
        // System.out.println("array size in head: " + sk.head.forward.length);
        // System.out.println("Level: " + sk.level);

        // System.out.println("After first insertion:");

        sk.insertNode(10);

        // System.out.println(sk.head.forward[0].value);
        // System.out.println("After second insertion:");
        sk.insertNode(20);

        // System.out.println(sk.head.forward[0].forward[0].value);
        // System.out.println(sk.head.forward[0].forward[0].forward[0]);
        sk.insertNode(30);

        // System.out.println(sk.head.forward[0].forward[0].forward[0].value);
        sk.insertNode(25);

        // System.out.println(sk.head.forward[0].forward[0].forward[0].forward[0].value);
        sk.print();
        System.out.println("Retrieval of current size:");

        sk.print();

        System.out.println("First item: " + sk.first());
        // System.out.println("Testing clear()");
        // sk.clear();
        sk.print();
        System.out.println("Max level: " + sk.maxLevel());
        System.out.println("Level variable: " + sk.level);

        System.out.println("Testing last(): ");
        System.out.println(sk.last());
        System.out.println("Testing find(): ");

        if (sk.contains(20))
        {
            System.out.println("Value found");
        }
        else 
            System.out.println("Value not found");
    }
}