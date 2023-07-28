import java.util.*;

public class SkipListSet <T extends Comparable<T>> implements SortedSet<T>
{
    private SkipListSetNode<T> head;
    private int level;
    private int size;
    private Random ran = new Random(); // random  object to randomize levels

    // this wrapper class implements comparable
    private class SkipListSetNode<T extends Comparable<T>>
    {
        private Object value;
        private ArrayList<SkipListSetNode> forward; // Should use ArrayList of pointers to the right because regular arrays can't handle generics 

        private Object value()
        {
            return this.value;
        }

        SkipListSetNode(Object key, int lev)
        {
            value = key;

            // an array of skiplist to save the references of all skip list nodes
            forward = new ArrayList<SkipListSetNode>(lev+1);
            for (int i = 0; i < lev; i++) // test later if i <= lev works
                forward[i] = null;
        }

        // Compares this object with the specified object for order
        @Override
        public int compareTo(T o)
        {
            return 0;
        }
    }

    // Implement this functions from the Iterator interface: This class implements the Iterator interface
    private class SkipListSetIterator<T extends Comparable<T>> implements Iterator<T>
    {
        SkipListSetNode<T> cursor;

        SkipListSetIterator(SkipListSetNode<T> h)
        {
            this.cursor = h;
        }

        @Override
        public boolean hasNext()
        {
            return cursor == null;
        }

        // returns an int for now. Generic: returns an element E
        @SuppressWarnings({"unchecked"})
        public T next()
        {
            T temp = (T)cursor.value();
            cursor = cursor.forward[0];
            return temp;
        }

        @Override
        public void remove()
        {

        } 
    }

    // Default Constructor that does not accept arguments. Returns an empty skiplist  
    SkipListSet()
    {
        // passing 0 for for the value of the head. Later, make this to null because the constructor will get a reference
        // to an object. For now, I am setting 0 as if it were null for the head
        this.head = new SkipListSetNode(0, 0);

        // "Bottom level" starts being -1, this is where all the values are stored. forward pointers start at level 0
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
    @Override
    public boolean add(T key)
    {
        // pass 2 to get a geometric distrubution for node's randomized level
        int newLevel = randomizeLevel();
        if (newLevel > level)
            adjustHeadLevel(newLevel); // flag == 1 for insertion

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

    @Override
    public boolean addAll(Collection<? extends T> c)
    {
        return false;
    }

    // Removes all of the elements from this skipList set
    @Override
    public void clear()
    {
        head = null;
    }

    @Override
    public Comparator <? super T> comparator()
    {
        return null;
    } 

    @Override
    private boolean contains(Object key)
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

    public boolean containsAll(Collection <?> c)
    {
        return false;
    }

    @Override
    public boolean equals(Object o)
    {
        if ( !(o instanceof Set) )
            return false;

        Set temp = (Set)o;
        if (temp.size() != size)
        {
            return false;
        }

        for (T current : this)
        {
            if (!temp.contains(current))
            {
                return false;
            }
        }

        return true;
    }

    // Returns the first (lowest) element in current SkipListSet. 
    @Override
    public T first()
    {
        // SkipList is not empty
        if (size > 0 ) 
            return (T)head.forward[0].value; 
        return null; // returns -1 for now if skipList is empty
    }

    @Override
    public int hashCode()
    {
        int temp = 0;
        for (T e : this) 
        {
            temp =+ e.hashCode();
        }

        return temp;
    }

    @Override
    public SortedSet<T> headSet(T toElement)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty()
    {
        return size == 0;
    }

    // Parametrized this return type using <T> type
    public SkipListSetIterator<T> iterator()
    {
        return new SkipListSetIterator<>(head);
    }

    // Returns the last (highest) element in current SKipListSet. Returns int for now
    @Override
    public T last()
    {   
        // Ierate as long as there is at least an item in the skipList
        if (size() > 0)
        {
            SkipListSetNode<T> temp = head;
            for (int i = level; i >= 0; i--)
            {
                while (temp.forward[i] != null)
                    temp = temp.forward[i];
            }

            // When i becomes negative we will have iterated to the very last entry
            return (T)temp.value();
        }

        return null; // returns -1 from now
    } 

    @Override
    public boolean remove(Object key)
    {
        SkipListSetNode [] tempNodeReferences = new SkipListSetNode[level+1];
        SkipListSetNode temp = head; 
        for (int i = level; i >= 0; i--)
        {
            while ( (temp.forward[i] != null) && ((int)temp.forward[i].value() < key) )
            {
                temp = temp.forward[i];
            }
            tempNodeReferences[i] = temp;
        }

        // create new links in saved nodes
        if ((int)temp.forward[0].value() == key)
        {
            // iteraring through all nodes stored in tempNodeReferences[].
            SkipListSetNode delNode = temp.forward[0];
            for (int i = 0; i <= level; i++)
            {
                // first case: height of current tempNodeReferences node is greater than delNode's height
                if (tempNodeReferences[i].forward.length > delNode.forward.length)
                {
                    for (int j = 0; j < delNode.forward.length; j++)
                    {
                        if (tempNodeReferences[i].forward[j] != delNode)
                        {
                            continue;
                        }
                        else 
                            tempNodeReferences[i].forward[j] = delNode.forward[j];
                    }
                }

                else // means height of current tempNodeReferences node is less than delNode's height
                {
                    for (int j = 0; j < tempNodeReferences[i].forward.length; j++)
                    {
                        if (tempNodeReferences[i].forward[j] != delNode)
                        {
                            continue;
                        }
                        else
                            tempNodeReferences[i].forward[j] = delNode.forward[j];
                    }
                }
            }

            this.size--;
            return true;
        }

        // key not found
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        return false;
    }

    @Override
    public int size()
    {
        return size;
    }

    public int maxHeight()
    {
        return head.forward.length;
    }

    public SkipListSetNode head()
    {
        return head;
    }

    @Override
    public SortedSet<T> subSet(T fromElement, T toElement)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedSet<T> tailSet(T fromElement)
    {
        throw new UnsupportedOperationException();
    }

    // // returns an array containing all the elements in this set. 
    // // Returns an Object array of each entry in the skipList
    @Override
    public Object[] toArray()
    {
        return null;
    }


    // // Returns an Generic Array <E> [] of each entry in the skipList 
    @SuppressWarnings({"unchecked"})
    public <E> E[] toArray(E[] temp)
    {
        return null;
    }

    public static void main(String[] args) 
    {
        SkipListSet sk = new SkipListSet();
        // System.out.println("array size in head: " + sk.head.forward.length);
        // System.out.println("Level: " + sk.level);

        // System.out.println("After first insertion:");

        sk.add(10);

        // System.out.println(sk.head.forward[0].value);
        // System.out.println("After second insertion:");
        sk.add(20);

        // System.out.println(sk.head.forward[0].forward[0].value);
        // System.out.println(sk.head.forward[0].forward[0].forward[0]);
        sk.add(30);

        // System.out.println(sk.head.forward[0].forward[0].forward[0].value);
        sk.add(25);

        System.out.println("Testing iterable:");
        for (SkipListSetNode s : sk)
        {
            System.out.println(s);
        }

        // System.out.println(sk.head.forward[0].forward[0].forward[0].forward[0].value);

        // sk.print();

        // System.out.println("Retrieval of current size:");

        // System.out.println("First item: " + sk.first());
        // System.out.println("Testing clear()");
        // sk.clear();
        // sk.print();
        // System.out.println("Max level: " + sk.maxHeight());
        // System.out.println("Level variable: " + sk.level);

        // System.out.println("Testing last(): ");
        // System.out.println(sk.last());
        
        // System.out.println("Testing remove: ");
        // sk.remove(30);
        
        // System.out.println("Testing find(): ");
        // if (sk.contains(25))
        // {
        //     System.out.println("Value found");
        // }
        // else 
        //     System.out.println("Value not found");

        // System.out.println("Printing after removing");
        // sk.print();
    }
}