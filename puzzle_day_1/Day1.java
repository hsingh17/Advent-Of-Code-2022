import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Day1 {
    public static void main(String[] args) {
        File file = new File("input.txt");
        List<Integer> elfCalories = new ArrayList<>();
        elfCalories.add(0);

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isBlank()) {
                    elfCalories.add(0);
                    continue;
                }

                int calories = Integer.parseInt(line);
                int elves = elfCalories.size()-1;
                elfCalories.set(elves, elfCalories.get(elves) + calories);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        elfCalories.sort(Collections.reverseOrder());
        System.out.println(elfCalories.get(0) + elfCalories.get(1) + elfCalories.get(2));
    }
}