import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day14 {
    private static class MyPair {
        public int x;
        public int y;

        public MyPair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return x + "," + y;
        }
    }

    private static class Path {
        public List<MyPair> pathList = new ArrayList<>();

        @Override
        public String toString() {
            return String.join(" -> ", pathList.stream().map(MyPair::toString).collect(Collectors.toList()));
        }
    }

    private static class Grid {
        public List<Path> paths = new ArrayList<>();
        public List<List<Character>> grid = new ArrayList<>();
        public int startX = Integer.MAX_VALUE;
        public int endX = Integer.MIN_VALUE;
        public int depth = 0;
        public int width;
        private static int SAND_START_X = 500;
        private static int PADDING = 200;

        public void setBoundaries(int x, int y) {
            startX = Math.min(startX, x);
            endX = Math.max(endX, x);
            depth = Math.max(depth, y);
        }

        public void initializeGrid() {
            startX -= PADDING;
            width = endX - startX + PADDING;
            depth += 2;
            for (int i = 0; i <= depth; i++) {
                grid.add(new ArrayList<>(Collections.nCopies(width, '.')));
            }
            drawPaths();
            grid.set(depth, new ArrayList<>(Collections.nCopies(width, '#')));
        }


        private void drawPaths() {
            for (Path path : paths) {
                for (int i = 1; i < path.pathList.size(); i++) {
                    MyPair p1 = path.pathList.get(i-1);
                    MyPair p2 = path.pathList.get(i);
                    int dx = p2.x - p1.x;
                    int dy = p2.y - p1.y;
                    int xDir = (dx == 0 ? dx : Math.abs(dx) / dx);
                    int yDir = (dy == 0 ? dy : Math.abs(dy) / dy);
                    int N = Math.abs(dx != 0 ? dx : dy);
                    for (int j = 0; j <= N; j++) {
                        grid.get(p1.y + j * yDir).set((p1.x-startX) + j * xDir, '#');
                    }
                }
            }
        }  

        public int simulate() {
            int sandUnits = 0;
            while (!sandAtSource()) {
                dropSand();
                sandUnits++;
            }
            return sandUnits;
        }

        private boolean dropSand() {
            MyPair sand = new MyPair(SAND_START_X-startX, 0);
            while (true) {
                MyPair down = new MyPair(sand.x, sand.y+1);
                MyPair downLeft = new MyPair(sand.x-1, sand.y+1);
                MyPair downRight = new MyPair(sand.x+1, sand.y+1);
                MyPair selectedMove = null;
                if (canMove(down)) {
                    selectedMove = down;
                } else if (canMove(downLeft)) {
                    selectedMove = downLeft;
                } else if (canMove(downRight)) {
                    selectedMove = downRight;
                }
                
                if (selectedMove == null) { // All moves are blocked (sand is at rest)
                    break;
                }

                if (!inBounds(selectedMove)) { // Can move but is out of bounds
                    return false;
                }
                
                sand = selectedMove;
            }
            grid.get(sand.y).set(sand.x, 'o');
            return true;
        }

        private boolean sandAtSource() {
            return grid.get(0).get(SAND_START_X-startX) == 'o';
        }

        private boolean canMove(MyPair pair) {
            return 
                !inBounds(pair) || 
                grid.get(pair.y).get(pair.x) == '.';
        }

        private boolean inBounds(MyPair pair) {
            return 
                pair.x >= 0 && 
                pair.x < grid.get(0).size() &&
                pair.y >= 0 &&
                pair.y < grid.size();
        }
        
        @Override
        public String toString() {
            String ret = "";
            for (int i = 0; i <= depth; i++) {
                for (int j = 0; j < width; j++) {
                    ret += grid.get(i).get(j);
                }
                ret += "\n";
            }
            return ret;
        }
    }

    public static void main(String[] args) {
        File file = new File("input.txt");
        try {
            Scanner scanner = new Scanner(file);
            Grid grid = new Grid();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] lineArr = line.split("->");
                Path path = new Path();
                for (String s : lineArr) {
                    String[] xyPair = s.trim().split(",");
                    int x = Integer.valueOf(xyPair[0]);
                    int y = Integer.valueOf(xyPair[1]);
                    path.pathList.add(new MyPair(x, y));
                    grid.setBoundaries(x, y);
                }

                grid.paths.add(path);
            }
            scanner.close();
            
            grid.initializeGrid();
            System.out.println(grid.simulate());
            System.out.println(grid);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}