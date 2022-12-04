import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day2 {
    private static String moves = "ABC";

    public static void main(String[] args) {
        File file = new File("input.txt");
        try {
            Scanner scanner = new Scanner(file);
            int totalScore = 0;
            while (scanner.hasNextLine()) {
                String round = scanner.nextLine();
                String[] roundArray = round.split(" ");
                String opponentMove = roundArray[0];
                String roundOutcome = roundArray[1];
                totalScore += winLossScore(roundOutcome) + moveScore(opponentMove, roundOutcome);
            }    

            scanner.close();
            System.out.println(totalScore);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static int winLossScore(String roundOutcome) {
        switch (roundOutcome) {
            case "X":
                return 0;
            case "Y":
                return 3;
            case "Z":
                return 6;
        }
        return -1;
    }

    private static int moveScore(String opponentMove, String roundOutcome) {
        // idx = 0, N = 3, outcome = X (lose), opponentMove = A (rock)
        // i need to play scissors
        // scissors have move score of 3
        // -1 % 3
        int idx = moves.indexOf(opponentMove); 
        int N = moves.length();
        switch (roundOutcome) {
            case "X": // Lose
                return Math.floorMod(idx-1, N) + 1;
            case "Y": // Draw          
                return idx+1;
            case "Z": 
                return Math.floorMod(idx+1, N) + 1;
        }
        return -1;
    }
}