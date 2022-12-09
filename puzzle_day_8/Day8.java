import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.List;

public class Day8 {
    private static List<int[]> matrix = new ArrayList<>();

    public static void main(String[] args) {
        File file = new File("input.txt");
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                int[] lineArr = Arrays.stream(line.split(""))
                                    .mapToInt(Integer::parseInt)
                                    .toArray();
                matrix.add(lineArr);
            }

            scanner.close();
            int bestScenicScore = -1;
            int rows = matrix.size();
            int cols = matrix.get(0).length;
            for (int row = 1; row < rows-1; row++) {
                for (int col = 1; col < cols-1; col++) {
                    int num = matrix.get(row)[col];
                    int scenicScoreUp = viewingDistance(row-1, col, rows, cols, num, 'N');
                    int scenicScoreRight = viewingDistance(row, col+1, rows, cols, num, 'E');
                    int scenicScoreDown = viewingDistance(row+1, col, rows, cols, num, 'S');
                    int scenicScoreLeft = viewingDistance(row, col-1, rows, cols, num, 'W');
                    int scenicScore = scenicScoreUp * scenicScoreDown * scenicScoreLeft * scenicScoreRight;
                    bestScenicScore = Math.max(bestScenicScore, scenicScore);
                }
            }
            System.out.println(bestScenicScore);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static int viewingDistance(int row, int col, int rows, int cols, int num, char dir) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return 0;
        }

        if (num <= matrix.get(row)[col]) {
            return 1;
        }

        switch (dir) {
            case 'N':
                return 1 + viewingDistance(row-1, col, rows, cols, num, dir);
            case 'E':
                return 1 + viewingDistance(row, col+1, rows, cols, num, dir);
            case 'S':
                return 1 + viewingDistance(row+1, col, rows, cols, num, dir);
            case 'W':
                return 1 + viewingDistance(row, col-1, rows, cols, num, dir);

        }
        return -1;
    }
}