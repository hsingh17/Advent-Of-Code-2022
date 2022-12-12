import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day12 {
    private static class Point {
        public int row;
        public int col;

        public Point(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    private static List<List<Character>> grid = new ArrayList<>();
    private static List<List<Integer>> visited = new ArrayList<>();
    private static Queue<Point> q = new LinkedList<>();
    private static int[] dr = {-1, 0, 1, 0};
    private static int[] dc = {0, 1, 0, -1};
    private static int endRow = -1;
    private static int endCol = -1;

    public static void main(String[] args) {
        File file = new File("input.txt");
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split("");
                visited.add(new ArrayList<>(Collections.nCopies(line.length, 0)));
                for (int i = 0; i < line.length; i++) {
                    if (line[i].equals("S")) {
                        line[i] = "a";
                    }

                    if (line[i].equals("E")) {
                        line[i] = "z";
                        endRow = grid.size();
                        endCol = i;
                    }

                    if (line[i].equals("a")) {
                        q.add(new Point(grid.size(), i));
                        visited.get(visited.size()-1).set(i, 1);
                    }
                }
                grid.add(Arrays.stream(line).map(e -> e.charAt(0)).collect(Collectors.toList()));
            }

            scanner.close();
            System.out.println(bfs());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static int bfs() {;
        int steps = 0;
 
        while (!q.isEmpty()) {
            int N = q.size();
            for (int i = 0; i < N; i++) {
                Point p = q.poll();
                char c = grid.get(p.row).get(p.col);
                if (p.row == endRow && p.col == endCol) {
                    return steps;
                }

                for (int j = 0; j < 4; j++) {
                    int newRow = p.row + dr[j];
                    int newCol = p.col + dc[j];
                    if (inBounds(newRow, newCol) && haventVisited(newRow, newCol) && canMove(newRow, newCol, c)) {
                        q.add(new Point(newRow, newCol));
                        visited.get(newRow).set(newCol, 1);
                    }
                }
            }
            steps++;
        }
        return -1;
    }

    private static boolean inBounds(int row, int col) {
        return
            row >= 0 &&
            row < grid.size() &&
            col >= 0 &&
            col < grid.get(0).size();
    }

    private static boolean haventVisited(int row, int col) {
        return visited.get(row).get(col) == 0;
    }

    private static boolean canMove(int row, int col, char currentChar) {
        return grid.get(row).get(col)-currentChar <= 1;
    }

    private static void printVisited() {
        for (List<Integer> row : visited) {
            System.out.println(Arrays.toString(row.toArray()));
        }
        System.out.println("------------------------------------------------------------------------------");
    }
}