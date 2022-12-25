import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Day22 {
    private static class Path {
        private List<Integer> tilesToMove = new ArrayList<>();
        private List<Character> rotations = new ArrayList<>();
        private int curTileMove = 0;
        private int curRotation = 0;
        private int first = 0; // 0 = tile move, 1 = rotation
        private int nextMove = 0;

        public void processPath(String rawPath) {
            String num = "";
            for (int i = 0; i < rawPath.length(); i++) {
                char c = rawPath.charAt(i);
                if (c == 'L' || c == 'R') {
                    if (!num.isEmpty()) {
                        tilesToMove.add(Integer.parseInt(num));
                    } else { // Num is empty iff a rotation comes first
                        first = 1;
                    }

                    rotations.add(c);
                    num = "";
                } else {
                    num += c;
                }
            }

            if (!num.isEmpty()) {
                tilesToMove.add(Integer.parseInt(num));
            }

            nextMove = first;
        }

        public boolean doneProcessingPath() {
            return curTileMove >= tilesToMove.size() && curRotation >= rotations.size();
        }

        public Character getRotation() {
            Character ret = null;
            if (nextMove == 1) {
                ret = rotations.get(curRotation++);
            }
            return ret;
        }

        public Integer getTileMove() {
            Integer ret = null;
            if (nextMove == 0) {
                ret = tilesToMove.get(curTileMove++);
            }
            return ret;
        }

        public void toggleNextMove() {
            nextMove = Math.floorMod(nextMove-1, 2);
        }

        public void printPath() {
            Boolean flag = (first == 0);
            int i = 0;
            int j = 0;
            String path = "";
            while (i < tilesToMove.size() || j < rotations.size()) {
                if (flag) {
                    path += tilesToMove.get(i++) + " ";
                } else {
                    path += rotations.get(j++) + " ";
                }
                flag = Boolean.logicalXor(flag, true);
            }
            System.out.println(path);
        }
    }

    private static class Board {
        public List<String> board = new ArrayList<>();
        private int curRow = 0;
        private int curCol = -1;
        private int curDir = 0; // 0 = East, 1 = South, 2 = West, 3 = North
        private int[] dx = {1, 0, -1, 0};
        private int[] dy = {0, 1, 0, -1};

        public void addToBoard(String row) {
            board.add(row);
        }

        public void normalizeRowLengths() {
            int maxLength = board.stream().max(Comparator.comparingInt(String::length)).get().length();
            List<String> normalizedBoard = new ArrayList<>();
            for (String row : board) {
                normalizedBoard.add(row + String.join("", Collections.nCopies(maxLength-row.length(), " ")));
            }
            board = normalizedBoard;
        }

        public void initStartingPosition() {
            int i = 0;
            for ( ;i < board.get(0).length(); i++) {
                if (board.get(0).charAt(i) == '.') {
                    break;
                }
            }
            curCol = i;
        }

        public List<Integer> traverseBoard(Path path) {
            while (!path.doneProcessingPath()) {
                Character rotation = path.getRotation();
                Integer tiles = path.getTileMove();
                path.toggleNextMove();
                if (rotation != null) {
                    rotate(rotation);
                } else {
                    move(tiles);
                }
            }
            return Arrays.asList(curRow, curCol);
        }
        
        private void rotate(char rotation) {
            int d = (rotation == 'R') ? 1 : -1;
            curDir = Math.floorMod(curDir+d, 4);
        }

        private void move(int tiles) {
            for (int i = 0; i < tiles; i++) {
                int nextRow = curRow + dy[curDir];
                int nextCol = curCol + dx[curDir];
                if (isTile(nextRow, nextCol)) {
                    curRow = nextRow;
                    curCol = nextCol;
                    continue;
                }

                if (isWall(nextRow, nextCol)) { // Can't go anywhere since wall ahead so we end
                    break;
                }
                
                // Need to keep wrapping around till we hit a wall or free tile     
                boolean hitWall = false;          
                while (!inBounds(nextRow, nextCol) || isVoid(nextRow, nextCol)) { 
                    nextRow = Math.floorMod(nextRow + dy[curDir], board.size());
                    nextCol = Math.floorMod(nextCol + dx[curDir], board.get(0).length());
                    if (isWall(nextRow, nextCol)) { // Hit a wall while wrapping
                        hitWall = true;
                        break;
                    }

                    if (isTile(nextRow, nextCol)) {
                        curRow = nextRow;
                        curCol = nextCol;
                        break;
                    }
                }

                if (hitWall) {
                    break;
                }
            }
        }

        private boolean inBounds(int row, int col) {
            return 
                row >= 0 &&
                row < board.size() &&
                col >= 0 &&
                col < board.get(0).length();
        }

        private boolean isTile(int row, int col) {
            return inBounds(row, col) && board.get(row).charAt(col) == '.';
        }

        private boolean isWall(int row, int col) {
            return inBounds(row, col) && board.get(row).charAt(col) == '#';
        }

        private boolean isVoid(int row, int col) {
            return board.get(row).charAt(col) == ' ';
        }

        public int getDir() {
            return curDir;
        }

        public void printBoard() {
            board.forEach(System.out::println);
        }
    }

    public static void main(String[] args) {
        File file = new File("input.txt");
        Board board = new Board();
        Path path = new Path();
        boolean flag = false;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isEmpty()) {
                    flag = true;
                    continue;
                }

                if (flag) {
                    path.processPath(line);
                } else {
                    board.addToBoard(line);
                }
            }

            board.normalizeRowLengths();
            board.initStartingPosition();
            List<Integer> finalCoords = board.traverseBoard(path);
            int x = finalCoords.get(0) + 1;
            int y = finalCoords.get(1) + 1;
            System.out.println((1000*x + 4*y + board.getDir()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }   
}