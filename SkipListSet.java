// This code was written by Carlos Daniel Arciniegas Murillas
// CS2 Summer. Matthew Gerber.
// Project
 
import java.util.*;

public class SkipListSet <T extends Comparable<T>> implements SortedSet<T>
{
    private SkipListSetItem<T> head;
    private int level;
    private int size;
    private Random ran = new Random(); // random  object to randomize levels

    // this wrapper class implements comparable
    public class SkipListSetItem<E extends Comparable<E>>
    {
        // private Object value;
        private E value;
        private SkipListSetItem<E> [] forward; 

        // Parameterless constructor?

        @SuppressWarnings({"unchecked"})
        SkipListSetItem(E key, int lev)
        {
            value = key;

            forward = new SkipListSetItem[lev+1];
            for (int i = 0; i < lev; i++) 
                forward[i] = null;
        }

        public E value()
        {
            return this.value;
        }

        public SkipListSetItem<E> getNext()
        {
            return forward[0];
        }
    }

    // Implement this functions from the Iterator interface: This class implements the Iterator interface
    private class SkipListSetIterator<T extends Comparable<T>> implements Iterator<T>
    {
        SkipListSetItem<T> cursor;

        SkipListSetIterator(SkipListSetItem<T> h)
        {
            this.cursor = h;
        }

        @Override
        public boolean hasNext()
        {
            return cursor != null;
        }

        // @SuppressWarnings({"unchecked"})
        public T next()
        {
            T temp = cursor.value();
            cursor = cursor.getNext();
            return temp;
        }


        @Override
        public void remove()
        {
            // working on it
        } 
    }

    // Default Constructor that does not accept arguments. Returns an empty skiplist  
    public SkipListSet()
    {
        this.head = new SkipListSetItem<>(null, 0);  

        // "Bottom level" starts being -1, this is where all the values are stored. forward pointers start at level 0
        this.level = -1;
        this.size = 0;
    }

    public SkipListSet(Collection<? extends T> c)
    {
        SkipListSet<T> sk = new SkipListSet<>();
        for (T t : c)
            sk.add(t);
    }

    public void reBalance()
    {
        for (int i = 0; Math.abs(ran.nextInt() % 2 ) == 0; i++);
    }

    public void reBalance(int x)
    {
        SkipListSetItem<T> temp = head;

        for (int i = 0; i < size(); i++)
        {
            int newLevel = randomizeLevel();
            if (newLevel > maxHeadHeight())
                adjustHeadLevel(newLevel);

            System.out.println("new Level "+ newLevel);
            // adjustNodeLevel(newLevel, temp.getNext());
            if (temp != null)
            {
                SkipListSetItem<T> oldNode = temp.forward[0];

                // no need to replace nodes if the newLevel generated is the same as the height of the node to be replaced.
                // This saves run time.
                if ( (oldNode == null) || (newLevel + 1 == oldNode.forward.length) )
                {
                    temp = temp.getNext();
                    continue;
                }

                else 
                {
                    SkipListSetItem<T> newNode = new SkipListSetItem<>(oldNode.value(), newLevel);
                    if (oldNode.forward.length > newNode.forward.length)
                    {
                        for (int j = 0; j < newNode.forward.length; j++)
                        {
                            newNode.forward[j] = oldNode.forward[j];
                            temp.forward[j] = newNode.forward[j];
                        }
        
                        // linking the remaining references stored in oldNode 
                        for (int k = newNode.forward.length; k < oldNode.forward.length; k++)
                        {
                            if (oldNode.forward[k] != null)
                                temp.forward[k] = oldNode.forward[k].forward[k];
                        }
                    }
                    else // means newNode height is greater than the old node height. 
                    {
                        for (int k = 0; k < oldNode.forward.length; k++)
                        {
                            newNode.forward[k] = oldNode.forward[k];
                            temp.forward[k] = newNode.forward[k];
                        }
        
                        for (int k = newNode.forward.length; k < oldNode.forward.length; k++)
                        {
                            if (temp.forward[k] != null)
                                newNode.forward[k] = temp.forward[k];
                            if (newNode.forward[k] != null)
                                temp.forward[k] = newNode.forward[k];
                        }
                    }
                }
                temp = temp.getNext();
            }
        }
    }

    public void print() {    // Prints out contents of a skiplist
        SkipListSetItem<T> temp = head;

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

    // Working on reBalance() 
    private SkipListSetItem<T> adjustNodeLevel(int newLevel, SkipListSetItem<T> node)
    {
        SkipListSetItem<T> temp = node;
        node = new SkipListSetItem<>(temp.value(), newLevel);
        for (int i = 0; i < temp.forward.length; i++)
        {
            if (temp.forward[i] != null)
            {
                node.forward[i] = temp.forward[i];
            }
        }

        return null; // for now
    }

    private void adjustHeadLevel(int newLevel)
    {
        SkipListSetItem<T> temp = head;
        head = new SkipListSetItem<>(null, newLevel);
        for (int i = 0; i <= level; i++)
            head.forward[i] = temp.forward[i];
        level = newLevel;
    }

    //  inserts a key to an appropiate place in the skipList. I am inserting everything in sorted order for now.
    @SuppressWarnings({"unchecked"})
    @Override
    public boolean add(T key)
    {

        // pass 2 to get a geometric distrubution for node's randomized level
        int newLevel = randomizeLevel();
        if (newLevel > level)
            adjustHeadLevel(newLevel); 
            
        SkipListSetItem<T> [] tempNodeReferences = new SkipListSetItem[level+1];
        
        SkipListSetItem<T> temp = head;
        
        for (int i = level; i >= 0; i--)
        {
            while ((temp.forward[i] != null) && ( (temp.forward[i].value().compareTo(key)) < 0) )
            {
                temp = temp.forward[i];
            }
            tempNodeReferences[i] = temp;
        }

        if (contains(key))
            return false;

        temp = new SkipListSetItem<>(key, newLevel);

        for (int i = 0; i <= newLevel; i++)
        {
            temp.forward[i] = tempNodeReferences[i].forward[i];
            tempNodeReferences[i].forward[i] = temp;
        }

        this.size++;
        return true;
    }

    // Adds all the elements in the specified collection to this set if they are not already present
    @Override
    public boolean addAll(Collection<? extends T> c)
    {
        // Set<T> sk = new SkipListSet<>();
        for (T t : c)
        {
            if (!contains(t))
                add(t);
        }
        return true;
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

    @SuppressWarnings({"unchecked"})
    @Override
    public boolean contains(Object key)
    {
        if (size() > 0)
        {
            SkipListSetItem<T> temp = head;
            for (int i = level; i >= 0; i--)
            {
                while ( (temp.forward[i] != null) && (temp.forward[i].value().compareTo((T)key) <= 0) )
                {
                    temp = temp.forward[i];
                    if (temp.value().compareTo((T)key) == 0)
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

    @SuppressWarnings({"unchecked"})
    @Override
    public boolean equals(Object o)
    {
        if ( !(o instanceof Set) )
            return false;

        Set<T> temp = (Set<T>)o;
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
            return head.forward[0].value();
            

        return null;
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

    // Returns skip list set iterator
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
            SkipListSetItem<T> temp = head;
            for (int i = level; i >= 0; i--)
            {
                while (temp.forward[i] != null)
                    temp = temp.forward[i];
            }

            // When i becomes negative we will have iterated to the very last entry
            return temp.value();
        }

        return null; // returns null if there is not any items
    } 

    @SuppressWarnings({"unchecked"})
    @Override
    public boolean remove(Object key)
    {
        // ArrayList<SkipListSetItem<T>> tempNodeReferences = new ArrayList<>(level+1);
        SkipListSetItem<T> [] tempNodeReferences = new SkipListSetItem[level+1];
        SkipListSetItem<T> temp = head; 
        for (int i = level; i >= 0; i--)
        {
            while ( (temp.forward[i] != null) && (temp.forward[i].value().compareTo((T)key) < 0) )
            {
                temp = temp.forward[i];
            }
            tempNodeReferences[i] = temp;
        }

        // key not found
        if (temp.forward[0] == null)
            return false;
        
        // if key is found
        else if(temp.forward[0].value().compareTo((T)key) == 0)
        {
            // iteraring through all nodes stored in tempNodeReferences[].
            SkipListSetItem<T> delNode = temp.forward[0];
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

        // extraneous case
        return false;
    }

    // Removes from this set all of its elements that are contained in the specified collection
    @SuppressWarnings({"unchecked"})
    @Override
    public boolean removeAll(Collection<?> c)
    {
        for (Object t : c)
        {
            if (contains(t))
                remove(t);
        }
        return true;
    }

    // Retains only the elements in this collection that are contained in the specified collection (optional
    // operation).
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

    public int maxHeadHeight()
    {
        return head.forward.length;
    }

    public SkipListSetItem<T> head()
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

    // // Returns an Object array of each entry in the skipList
    @Override
    public Object[] toArray()
    {   
        Object [] arr = new Object[size()];
        SkipListSetItem<T> temp = head;

        for (int i = 0; i < size(); i++)
        {
            arr[i] = temp.forward[0].value();
            temp = temp.forward[0];
        }
            
        return arr;
    }


    // Returns an array containing all of the elements in this collection; the runtime type of the returned
    // array is that of the specified array. 
    @SuppressWarnings({"unchecked"})
    public <E> E[] toArray(E[] temp)
    {
        Object[] arr = new Object[size()];
        for (int i = 0; i < temp.length; i++)
        {
            arr[i] = temp[i];
        }

        return (E[])arr;
    }

    public static void main(String[] args) 
    {
        SkipListSet<Integer> sk = new SkipListSet<>();
        
        System.out.println("array size in head: " + sk.head.forward.length);
        System.out.println("Level: " + sk.level);

        
        // System.out.println("After first insertion:");
        
        
        // System.out.println(sk.head.forward[0].value);
        // System.out.println("After second insertion:");
        sk.add(20);
        
        // System.out.println(sk.head.forward[0].forward[0].value);
        // System.out.println(sk.head.forward[0].forward[0].forward[0]);
        sk.add(30);
        
        // System.out.println(sk.head.forward[0].forward[0].forward[0].value);
        sk.add(25);
        sk.add(10);
        sk.print();
        
        System.out.println("Size: " + sk.size());

        sk.reBalance();
        sk.print();
        // System.out.println("Testing Remove:");
        // sk.remove(20);
        
        // System.out.println("Size after remove: " + sk.size());
        // sk.print();

        // Object [] temp = sk.toArray();
        // for (int i = 0; i < temp.length; i++)
        // {
        //     System.out.println(i+1+ ": "+ temp[i]);
        // }

        // System.out.println("Testing contains: ");
        // if (sk.contains(10))
        //     System.out.println("Item found");
        // else
        //     System.out.println("Item not found");

        System.out.println("Testing iterator");
        for (Integer s : sk)
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