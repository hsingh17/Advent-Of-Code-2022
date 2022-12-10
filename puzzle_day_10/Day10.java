import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Day10 {
    private static int cycle = 1;
    private static int registerXValue = 1;
    private static final int CRT_HEIGHT = 6;
    private static final int CRT_WIDTH = 40;
    private static char[][] CRT = new char[CRT_HEIGHT][CRT_WIDTH];

    public static void main(String[] args) {
        File file = new File("input.txt");
        try {
            Scanner scanner = new Scanner(file);
            initializeCRT();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] arr = line.split(" ");
                String command = arr[0];
                drawToCRT();
                cycle++;
                if (command.equals("addx")) {
                    drawToCRT();
                    cycle++;
                    registerXValue += Integer.parseInt(arr[1]);
                }
            }

            scanner.close();
            for (char[] row : CRT) {
                System.out.println(Arrays.toString(row));
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initializeCRT() {
        for (int i = 0; i < CRT_HEIGHT; i++) {
            for (int j = 0; j < CRT_WIDTH; j++) {
                CRT[i][j] = '.';
            }
        }
    }

    private static void drawToCRT() {
        int curRow = Math.floorDiv(cycle, CRT_WIDTH);
        int curCol = Math.floorMod(cycle-1, CRT_WIDTH);
        if (curCol >= registerXValue-1 || curCol <= registerXValue+1) {
            return;
        }

        CRT[curRow][curCol] = '#';
    }
}