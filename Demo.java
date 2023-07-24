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
        
        System.out.println(level);
    }
}
