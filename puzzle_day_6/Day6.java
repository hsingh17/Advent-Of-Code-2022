import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Day6 {
    public static void main(String[] args) {
        File file = new File("input.txt");
        Map<Character, Integer> freq = new HashMap<>();

        try {
            Scanner scanner = new Scanner(file);
            String data = scanner.next();
            for (int i = 0; i < data.length(); i++) {
                if (i >= 14) {   
                    char c = data.charAt(i-14);
                    freq.put(c, freq.get(c)-1);
                    if (freq.get(c) == 0) {
                        freq.remove(c);
                    }
                }

                freq.merge(data.charAt(i), 1, Integer::sum);
                if (freq.size() == 14) {
                    System.out.println(i+1);
                    break;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}