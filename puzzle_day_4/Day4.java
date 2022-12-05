import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day4 {
    public static void main(String[] args) {
        File file = new File("input.txt");
        int overlaps = 0;
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String pair = scanner.nextLine();
                String[] split = pair.split(",");
                String firstRange = split[0];
                String secondRange = split[1];
                if (isOverlap(firstRange, secondRange)) {
                    overlaps++;
                }
            }
            scanner.close();
            System.out.println(overlaps);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isOverlap(String firstRange, String secondRange) {
        String[] firstRangeSplit = firstRange.split("-");
        String[] secondRangeSplit = secondRange.split("-");
        int firstRangeLow = Integer.parseInt(firstRangeSplit[0]);
        int firstRangeHigh = Integer.parseInt(firstRangeSplit[1]);
        int secondRangeLow = Integer.parseInt(secondRangeSplit[0]);
        int secondRangeHigh = Integer.parseInt(secondRangeSplit[1]);

        return 
            (firstRangeLow <= secondRangeLow && firstRangeHigh >= secondRangeHigh) || // firstRange ENTIRELY contains secondRange
            (secondRangeLow <= firstRangeLow && secondRangeHigh >= firstRangeHigh) || // secondRange ENTIRELY contains firstRange
            (firstRangeHigh >= secondRangeLow && firstRangeHigh <= secondRangeHigh) || // firstRange is PARTIALLY contained in secondRange
            (firstRangeLow >= secondRangeLow && firstRangeLow <= secondRangeHigh); // firstRange is PARTIALLY contained in secondRange
    }
}