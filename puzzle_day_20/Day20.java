import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day20 {
    private static List<Long> nums = new ArrayList<>();
    private static Map<Integer,Integer> indices = new HashMap<>();
    private static final long DECRYPTION_KEY = 811589153;

    public static void main(String[] args) {
        File file = new File("input.txt");
        try (Scanner scanner = new Scanner(file)) {
            int i = 0;
            while (scanner.hasNextInt()) {
                long num = scanner.nextInt() * DECRYPTION_KEY;
                nums.add(num);
                indices.put(i, i++);
            }


            List<Long> numsCopy = new ArrayList<>(nums);
            for (int k = 0; k < 10; k++) {
                for (int j = 0; j < numsCopy.size(); j++) {
                    long num = numsCopy.get(j);
                    int cur = indices.get(j);
                    nums.remove(cur);
                    
                    for (Map.Entry<Integer,Integer> p : indices.entrySet()) {
                        if (p.getValue() > cur) {
                            indices.put(p.getKey(), Math.floorMod(p.getValue()-1, nums.size()));
                        }
                    }

                    int newIdx = (int) Math.floorMod((long)(cur+num), (long)nums.size());
                    nums.add(newIdx, num);

                    for (Map.Entry<Integer,Integer> p : indices.entrySet()) {
                        if (p.getValue() >= newIdx) {
                            indices.put(p.getKey(), Math.floorMod(p.getValue()+1, nums.size()));
                        }
                    }

                    indices.put(j, newIdx);

                }
            }
            
            int idxZero = nums.indexOf(0L);
            long grove1 = nums.get(Math.floorMod(idxZero+1000, nums.size())); 
            long grove2 = nums.get(Math.floorMod(idxZero+2000, nums.size())); 
            long grove3 = nums.get(Math.floorMod(idxZero+3000, nums.size())); 
            System.out.println("Grove 1: " + grove1 + ". Grove 2: " + grove2 + ". Grove 3: " + grove3);
            System.out.println("Sum: " + (grove1 + grove2 + grove3));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }   
}