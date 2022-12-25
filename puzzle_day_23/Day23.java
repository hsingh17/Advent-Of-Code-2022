import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

public class Day23 {
    private static class Elf {
        public List<Integer> rc;

        public Elf(List<Integer> rc) {
            this.rc = rc;
        }
    }

    private static class Board {
        private List<Elf> elves = new ArrayList<>();
        private Map<List<Integer>, Elf> elfPositions = new HashMap<>();
        private int[] dr = {-1, -1, -1, 1, 1, 1, 0, -1, 1, 0, -1, 1}; // N, NE, NW, S, SE, SW, W, NW, SW, E, NE, SE
        private int[] dc = {0, 1, -1, 0, 1, -1, -1, -1, -1, 1, 1, 1}; // N, NE, NW, S, SE, SW, W, NW, SW, E, NE, SE
        private final int MAX_ROUNDS = 10;
        private Queue<Integer> proposeDirections;
        
        public void initElves(List<String> board) {
            for (int r = 0; r < board.size(); r++) {
                for (int c = 0; c < board.get(0).length(); c++) {
                    if (board.get(r).charAt(c) == '#') {
                        List<Integer> rc = Arrays.asList(r, c);
                        Elf elf = new Elf(rc);
                        elfPositions.put(rc, elf);
                        elves.add(elf);
                    }
                }
            }
        }

        public void simulate() {
            proposeDirections = new LinkedList<>();
            proposeDirections.addAll(Arrays.asList(0, 3, 6, 9));
            int rnd = 1;
            while (doRound()) {
                rnd++;
                proposeDirections.add(proposeDirections.poll());
            }
            printBoard();
            System.out.println("Elves done at round: " + rnd);
        }

        private boolean doRound() {
            Map<List<Integer>, List<Elf>> proposedMoves = proposeMoves();
            if (proposedMoves.isEmpty()) {
                return false;
            }
            makeMoves(proposedMoves);
            return true;
        }

        private Map<List<Integer>, List<Elf>> proposeMoves() {
            Map<List<Integer>, List<Elf>> proposedMoves = new HashMap<>();
            for (Elf elf : elves) {
                boolean noElvesAround = true;
                Integer proposedDir = null;
                for (int dir : proposeDirections) {
                    int elvesInDir = elvesInDirection(elf, dir);
                    if (elvesInDir != 0) {
                        noElvesAround = false;
                    } 
                    if (elvesInDir == 0 && proposedDir == null) { // First direction considered
                        proposedDir = dir;
                    }
                }

                if (noElvesAround || proposedDir == null) { // No elves adjacent to this elf so nothing to do or nowhere to go
                    continue;
                }

                // System.out.println("Elf: " + elf.rc + ". Proposed Dir: " + proposedDir);
                List<Integer> cur = elf.rc;
                List<Integer> next = Arrays.asList(cur.get(0), cur.get(1));
                if (proposedDir == 0) {
                    next.set(0, next.get(0)-1);
                } else if (proposedDir == 3) {
                    next.set(0, next.get(0)+1);
                } else if (proposedDir == 6) {
                    next.set(1, next.get(1)-1);
                } else if (proposedDir == 9) {
                    next.set(1, next.get(1)+1);
                }

                if (proposedMoves.get(next) == null) {
                    proposedMoves.put(next, new ArrayList<>()); 
                }
                proposedMoves.get(next).add(elf);
            }
            return proposedMoves;
        }

        private void makeMoves(Map<List<Integer>, List<Elf>> proposedMoves) {
            for (List<Integer> rc : proposedMoves.keySet()) {
                if (proposedMoves.get(rc).size() != 1) {
                    continue;
                }

                // This the only elf wanting to move to this proposed position
                Elf elf = proposedMoves.get(rc).get(0);
                elfPositions.remove(elf.rc); // Remove the old position of this elf
                elf.rc = rc; // Move elf's position to its proposed position
                elfPositions.put(rc, elf); // Add elf's new position
            }
        }

        private int elvesInDirection(Elf elf, int dir) {
            int r = elf.rc.get(0);
            int c = elf.rc.get(1);
            int cnt = 0;
            for (int i = dir; i < dir+3; i++) {
                List<Integer> checkRC = Arrays.asList(r+dr[i], c +dc[i]);
                if (elfPositions.get(checkRC) != null) {
                    cnt++;
                }
            }
            return cnt;
        }

        public void printBoard() {
            List<List<String>> board = createBoard();
            for (List<String> row : board) {
                System.out.println(String.join("", row));
            }
            System.out.println("==============================================");
        }

        private List<List<String>> createBoard() {
            List<Integer> boundingBox = getBoundingBox();
            int minRow = boundingBox.get(0);
            int maxRow = boundingBox.get(1);
            int minCol = boundingBox.get(2);
            int maxCol = boundingBox.get(3);
            int width = maxCol-minCol+1;
            int height = maxRow-minRow+1;
            List<List<String>> ret = new ArrayList<>();
            for (int h = 0; h < height; h++) {
                List<String> row = new ArrayList<>(Collections.nCopies(width, "."));
                ret.add(row);
            }

            for (Elf elf : elves) {
                int r = elf.rc.get(0);
                int c = elf.rc.get(1);
                ret.get(r-minRow).set(c-minCol, "#");
            }
            return ret;
        }

        private List<Integer> getBoundingBox() {
            int minRow = Integer.MAX_VALUE;
            int maxRow = Integer.MIN_VALUE;
            int minCol = Integer.MAX_VALUE;
            int maxCol = Integer.MIN_VALUE;
            for (Elf elf : elves) {
                int r = elf.rc.get(0);
                int c = elf.rc.get(1);
                minRow = Math.min(minRow, r);
                maxRow = Math.max(maxRow, r);
                minCol = Math.min(minCol, c);
                maxCol = Math.max(maxCol, c);
            }
            return Arrays.asList(minRow, maxRow, minCol, maxCol);
        }
    }

    public static void main(String[] args) {
        File file = new File("input.txt");
        Board board = new Board();
        List<String> initPositions = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                initPositions.add(scanner.nextLine());
            }
            board.initElves(initPositions);
            board.simulate();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }   
}