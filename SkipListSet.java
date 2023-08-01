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
        private E value;
        private SkipListSetItem<E> [] forward; 

        // Parameterless constructor?
        @SuppressWarnings({"unchecked"})
        SkipListSetItem()
        {
            value = null;
            forward = new SkipListSetItem[1];
            forward[0] = null;
        }

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

        public void setValue(E s)
        {
            this.value = s;
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

        @SuppressWarnings({"unchecked"})
        @Override
        public void remove()
        {
            if (cursor == null)
                return;

            if (cursor.getNext() != null)
            {
                Object temp = cursor.value();
                cursor.setValue((T)temp);
                SkipListSet.this.remove(temp);
                return;
            }

            // last element in the skipList
            cursor.setValue(null);
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
        this.head = new SkipListSetItem<>(null, 0);  
        this.level = -1;
        this.size = 0;

        for (T t : c)
            this.add(t);
    }

    // Suggested by the professor: Creating a new SkipList. Ensures new heights for all nodes.
    public void reBalance()
    {
        SkipListSet<T> newSkipList = new SkipListSet<>();
        SkipListSetItem<T> temp = head.getNext();
        while (temp != null)
        {
            newSkipList.add(temp.value());
            temp = temp.getNext();
        }

        this.head = newSkipList.head();
    }

    // Helper function to randomize the level of a new node. Randomization will guarantee log(n) performance
    private int randomizeLevel()
    {
        int newLevel;
        for (newLevel = 0; Math.abs(ran.nextInt() % 2 ) == 0; newLevel++);
        return newLevel;
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

    // Returns true if this set contains all of the elements of the specified collection.
    public boolean containsAll(Collection <?> c)
    {
        for (Object o : c)
        {
            if (!contains(o))
                return false;
        }
        return true;
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

    // Retains only the elements in this set that are contained in the specified collection (optional
    // operation).
    @Override
    public boolean retainAll(Collection<?> c)
    {
        for (T t : this)
        {
            for (Object o : c)
            {
                if (!contains(o))
                    remove(t);
            }
        }
        return true;
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
}