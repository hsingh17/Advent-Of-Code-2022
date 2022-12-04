import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Day3 {
    public static void main(String[] args) {
        File file = new File("input.txt");
        int sum = 0;
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                int counter = 0;
                String[] group = new String[3];
                for (int i = 0; i < 3; i++) {
                    group[i] = scanner.nextLine();
                }
                sum += getItemTypePriority(findBadge(group[0], group[1], group[2]));
            }
            scanner.close();
            System.out.println(sum);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static char findBadge(String firstRuckSack, String secondRuckSack, String thirdRuckSack) {
        Set<Character> firstRuckSackSet = firstRuckSack.chars().mapToObj(e -> (char)e).collect(Collectors.toSet());
        Set<Character> secondRuckSackSet = secondRuckSack.chars().mapToObj(e -> (char)e).collect(Collectors.toSet());
        Set<Character> thirdRuckSackSet = thirdRuckSack.chars().mapToObj(e -> (char)e).collect(Collectors.toSet());
        firstRuckSackSet.retainAll(secondRuckSackSet);
        firstRuckSackSet.retainAll(thirdRuckSackSet);
        return firstRuckSackSet.iterator().next();
    }

    private static int getItemTypePriority(char itemType) {
        char charOffset = itemType >= 'a' ? 'a' : 'A';
        int intOffset = charOffset == 'A' ? 27 :  1;
        return (itemType - charOffset) + intOffset;
    }
}