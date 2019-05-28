package pathFinder;

import java.util.ArrayList;
import java.util.List;

public class Test {
    // this class is just for testing, don't submit it.
    public static void main(String[] args) {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        list1.add(4);


        List<Integer> list2 = new ArrayList<>(list1);
        System.out.println(list2);
    }
}
