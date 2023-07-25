import java.util.*;
public class SkipListSet
{
    private SkipListSetNode head;
    private int level;
    private Random ran = new Random(); // random  object to randomize levels

    private class SkipListSetNode
    {
        private Object value;
        private SkipListSetNode [] forward;
        // this class implements comparable

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

    private class SkipListSetIterator
    {
        // Implement this functions from the Iterator interface: This class implements the Iterator interface

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
        this.level = -1;
        // this.ran = 
    }

    public void print() {    // Print out contents of a skiplist
        SkipListSetNode temp = head;
        while (temp != null) {
          System.out.print(temp.value() + ": length is "
                           + temp.forward.length + ":");
          for (int i=0; i<temp.forward.length; i++)
            if (temp.forward[i] == null)
              System.out.print("null ");
            else
              System.out.print(temp.forward[i].value() + " ");
          System.out.println();
          temp = temp.forward[0];
        }
        System.out.println();
      }

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

    //  inserts a key to an appropiate place in the skipList
    private void insertNode(int key)
    {
        // pass 2 to get a geometric distrubution for node's randomized level
        int newLevel = randomizeLevel();
        if (newLevel > level)
            adjustHeadLevel(newLevel);

        SkipListSetNode [] tempNodeReferences = new SkipListSetNode[level+1];

        // iterating
        SkipListSetNode temp = head;
        // System.out.println("Level: " + level);

        for (int i = level; i >= 0; i--)
        {
            while ((temp.forward[i] != null) && (((int)temp.forward[i].value()) < key) )
            {
                temp = temp.forward[i];
            }
            tempNodeReferences[i] = temp;
        }

        // System.out.println("Temp node references: ");
        // System.out.println(tempNodeReferences[0].value);

        // System.out.println("Inserting: " + key);
        temp = new SkipListSetNode(key, newLevel);
        // System.out.println("After creating a node: " + temp.value);

        for (int i = 0; i <= newLevel; i++)
        {
            temp.forward[i] = tempNodeReferences[i].forward[i];
            tempNodeReferences[i].forward[i] = temp;
        }

        // printing
        // System.out.println("forward size: " + tempNodeReferences[0].forward.length);
    }

    // Implement all this functions from the SortedSet Interface: 
    // add
    // addAll
    // clear
    // comparator
    // contains
    // containsAll
    // equals
    // first
    // hashCode
    // headSet
    // isEmpty
    // iterator
    // last 
    // remove
    // removeAll
    // retainAll
    // size
    // subSet
    // tailSet
    // toArray

    public static void main(String[] args) 
    {
        SkipListSet sk = new SkipListSet();
        System.out.println("array size in head: " + sk.head.forward.length);
        System.out.println("Level: " + sk.level);

        // System.out.println("After first insertion:");
        sk.insertNode(10);
        System.out.println(sk.head.forward[0].value);
        // System.out.println("After second insertion:");
        sk.insertNode(20);
        System.out.println(sk.head.forward[0].forward[0].value);
        // System.out.println(sk.head.forward[0].forward[0].forward[0]);
        sk.insertNode(30);
        System.out.println(sk.head.forward[0].forward[0].forward[0].value);
        sk.insertNode(25);
        System.out.println(sk.head.forward[0].forward[0].forward[0].forward[0].value);
        sk.print();
        
    }
}