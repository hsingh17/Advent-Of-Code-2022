import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Day5 {
    private static List<StringBuilder> stacks = new ArrayList<>();

    public static void main(String[] args) {
        File file = new File("input.txt");
        boolean doneParsingStacks = false;
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isEmpty()) {
                    doneParsingStacks = true;
                    continue;
                }

                if (doneParsingStacks) {
                    String[] directions = line.split(" ");
                    int numberOfCrates = Integer.parseInt(directions[0]);
                    int from = Integer.parseInt(directions[1]);
                    int to = Integer.parseInt(directions[2]);
                    executeDirections(numberOfCrates, from, to);
                } else {
                    stacks.add(new StringBuilder(line));
                }
            }
            
            System.out.println(stacks.toString());
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void executeDirections(int numberOfCrates, int from, int to) {
        StringBuilder fromSb = stacks.get(from-1);
        StringBuilder toSb = stacks.get(to-1);
        int startIndex = fromSb.length()-numberOfCrates;
        StringBuilder appendSb = new StringBuilder(fromSb.substring(startIndex));
        toSb.append(appendSb);
        fromSb.delete(startIndex, fromSb.length());
    }
}