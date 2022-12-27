import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Day24 {
    private static class Blizzard {
        public int[] startingRc; 
        public char dir; 

        public Blizzard(int[] startingRc, char dir) {
            this.startingRc = startingRc;
            this.dir = dir;
        }

        public int[] getBlizzardPosition(int t, int maxRow, int maxCol) {
            switch (dir) {
                case '<':
                    return new int[]{startingRc[0], Math.floorMod(startingRc[1]-1-t, maxCol) + 1};
                case '>':
                    return new int[]{startingRc[0], Math.floorMod(startingRc[1]-1+t, maxCol) + 1};
                case '^':
                    return new int[]{Math.floorMod(startingRc[0]-1-t, maxRow) + 1, startingRc[1]};
                case 'v':
                    return new int[]{Math.floorMod(startingRc[0]-1+t, maxRow) + 1, startingRc[1]};
            }            
            return new int[]{};
        }
    }

    private static class Board {
        public List<Blizzard> blizzards = new ArrayList<>();
        public int[] dr = {-1, 0, 1, 0, 0}; // Up, Right, Down, Left, Stay in same spot
        public int[] dc = {0, 1, 0, -1, 0}; // Up, Right, Down, Left, Stay in same spot
        public int minRow = 1;
        public int minCol = 1;
        public int maxCol = Integer.MIN_VALUE;
        public int maxRow = Integer.MIN_VALUE;
        public int minutes = 0;

        public void initializeBoard(List<String> input) {
            for (int i = 0; i < input.size(); i++) {
                String row = input.get(i);
                for (int j = 0; j < row.length(); j++) {
                    char c = row.charAt(j);
                    if (c != '.' && c != '#') {
                        int[] rc = {i, j};
                        blizzards.add(new Blizzard(rc, c));
                    }
                }
            }
            maxRow = input.size() - 2;
            maxCol = input.get(0).length() - 2;
            System.out.println("Max Row: " + maxRow + ". Max Col: " + maxCol);
        }

        public int bfs(List<Integer> start, List<Integer> end) {
            Queue<List<Integer>> q = new LinkedList<>();
            q.add(start);

            while (!q.isEmpty()) {
                Set<List<Integer>> blizzardLocations = new HashSet<>();
                for (Blizzard blizzard : blizzards) {
                    int[] rc = blizzard.getBlizzardPosition(minutes, maxRow, maxCol);
                    List<Integer> rcList = Arrays.asList(rc[0], rc[1]);
                    blizzardLocations.add(rcList);
                }

                int N = q.size();
                Set<List<Integer>> visited = new HashSet<>();
                for (int i = 0; i < N; i++) {
                    List<Integer> rc = q.poll();
                    if (visited.contains(rc)) {
                        continue;
                    }

                    if (rc.equals(end)) {
                        return minutes;
                    }

                    visited.add(rc);
                    for (int dir = 0; dir < dr.length; dir++) {
                        int newR = rc.get(0) + dr[dir];
                        int newC = rc.get(1) + dc[dir];
                        List<Integer> newRc = Arrays.asList(newR, newC);
                        if (inBounds(newRc) && !blizzardLocations.contains(newRc)) {
                            q.add(newRc);
                        }
                    }
                }
                minutes++;
            }
            return -1;
        }

        private boolean inBounds(List<Integer> rc) {
            if (rc.equals(Arrays.asList(0, 1)) || rc.equals(Arrays.asList(maxRow+1, maxCol))) { // Special case at origin
                return true;
            }

            int r = rc.get(0);
            int c = rc.get(1);
            return 
                r >= minRow &&
                r <= maxRow &&
                c >= minCol && 
                c <= maxCol;
        }

        private void printBlizzards() {
            List<List<String>> map = new ArrayList<>();
            for (int i = 0; i < maxRow+2; i++) {
                if (i == 0 || i == maxRow+1) {
                    map.add(new ArrayList<>(Collections.nCopies(maxCol+2, "#")));
                } else {
                    map.add(new ArrayList<>(Collections.nCopies(maxCol+2, ".")));
                }
            }

            for (int i = 0; i < maxRow+2; i++) {
               map.get(i).set(0, "#");
               map.get(i).set(map.get(i).size()-1, "#");
            }

            for (Blizzard blizzard : blizzards) {
                int[] rc = blizzard.getBlizzardPosition(minutes, maxRow, maxCol);
                int r = rc[0];
                int c = rc[1];
                if (!map.get(r).get(c).equals(".")) {
                    map.get(r).set(c, "@");
                } else {
                    map.get(r).set(c, "" + blizzard.dir);
                }
            }

            for (List<String> row : map) {
                System.out.println(String.join("", row));
            }
            System.out.println("====================================================================================================================================");
        }
    }

    public static void main(String[] args) {
        File file = new File("input.txt");
        List<String> input = new ArrayList<>();
        Board board = new Board();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                input.add(scanner.nextLine());
            }

            board.initializeBoard(input);
            int maxCol = board.maxCol;
            int maxRow = board.maxRow;
            List<Integer> start = Arrays.asList(0, 1);
            List<Integer> end = Arrays.asList(maxRow+1, maxCol);
            board.bfs(start, end);
            board.bfs(end, start);
            System.out.println(board.bfs(start, end)-1);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }   
}