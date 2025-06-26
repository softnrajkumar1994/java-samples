package samples;

public class MyCustomeMapDemo {
    public static void main(String args[]) {
        MyCustomMap<String, Integer> map = new MyCustomMap<>();
        map.put("hi", 1);
        System.out.println(map);
        map.put("hi1", 2);
        System.out.println(map);
    }
}
