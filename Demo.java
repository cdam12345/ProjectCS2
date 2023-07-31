import java.util.*;

public class Demo {

    static int random(int n)
    {
        Random ra = new Random();
        return Math.abs(ra.nextInt() % n);
    }
    public static void main(String[] args) {

        Random ra = new Random();
        int upperBound = 2;
        int i = ra.nextInt(upperBound);  
        // System.out.println(i);

        int level;
        for (level = 0; random(2) == 0; level++);
        
        // System.out.println(level);

        List<Integer> al = new ArrayList<>(13);
        System.out.println(al.size());

        Object obj = 4;
        System.out.println(obj);

        String s = "myStr";
        int myInt = 5;
        al.add(5);
        al.get(0);
        al.add(2);
        al.add(2);
        al.add(6);
        System.out.println(al);

        for (Integer a : al)
        {
            if (a == 2)
            {
                continue;
            }
            else
                System.out.print(a + " " );
        }


    }
}
