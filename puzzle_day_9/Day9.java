import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

import javax.sql.PooledConnection;

public class Day9 {
    private static class Point {
        public int x;
        public int y;
        
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Point(Point point) {
            this.x = point.x;
            this.y = point.y;
        }

        @Override
        public String toString() {
            return x + " " + y;
        }

        @Override
        public boolean equals(Object o) {
            Point oPoint = (Point) o;
            return 
                this.x == oPoint.x &&
                this.y == oPoint.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.x, this.y);
        }
    }

    private static int KNOTS = 10;
    private static Point[] rope = new Point[KNOTS]; // Point[0] = head, ... , Point[9] = tail
    private static Set<Point> visited = new HashSet<>();

    public static void main(String[] args) {
        File file = new File("input.txt");
        for (int i = 0; i < KNOTS; i++) {
            rope[i] = new Point(0, 0);
        }

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] motion = line.split(" ");
                String dir = motion[0];
                int steps = Integer.parseInt(motion[1]);
                moveRope(dir, steps);
            }

            scanner.close();
            System.out.println(visited.size());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void moveRope(String dir, int steps) {
        for (int step = 0; step < steps; step++) {
            moveHead(dir);
            for (int pIdx = 1; pIdx < KNOTS; pIdx++) {
                Point p1 = rope[pIdx-1];
                Point p2 = rope[pIdx];
                if (areNearby(p1, p2)) {
                    continue;
                }

                followKnot(p1, p2);

                if (pIdx == KNOTS-1) {
                    visited.add(new Point(p2));
                }
            }
        }
    }

    private static boolean areNearby(Point left, Point right) {
        int dx = Math.abs(left.x - right.x);
        int dy = Math.abs(left.y - right.y);

        return 
            (dx == 0 && dy == 0) || // Overlapping
            (dx == 1 && dy == 1) || // Diagonal
            (dx == 0 && dy == 1) || // Nearby in the y direction
            (dx == 1 && dy == 0);   // Nearby in the x direction
    }

    private static void moveHead(String dir) {
        switch (dir) {
            case "L":
                rope[0].x--;
                break;
            case "D":
                rope[0].y--;
                break;
            case "R":
                rope[0].x++;
                break;
            case "U":
                rope[0].y++;
                break;
        }
    }

    private static void followKnot(Point leader, Point follower) {
        int dx = leader.x - follower.x;
        int dy = leader.y - follower.y;
        int offsetX = dx > 0 ? -1 : 1;
        int offsetY = dy > 0 ? -1 : 1;

        if (dx == 0 && dy != 0) { // Vertical move
            follower.y = leader.y + offsetY;
        } else if (dx != 0 && dy == 0) { // Horzontal move
            follower.x = leader.x + offsetX;
        } else if (dx > 0 && dy > 0) { // Up and to the right move
            follower.x++;
            follower.y++;
        } else if (dx > 0 && dy < 0) { // Down and to the right move
            follower.x++;
            follower.y--;
        } else if (dx < 0 && dy > 0) { // Up and to the left move
            follower.x--;
            follower.y++;
        } else if (dx < 0 && dy < 0) { // Down and to the left move
            follower.x--;
            follower.y--;
        }
    }
}
