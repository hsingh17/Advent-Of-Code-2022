import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day18 {
    private static int[] dx = {-1,1,0,0,0,0};
    private static int[] dy = {0,0,-1,1,0,0};
    private static int[] dz = {0,0,0,0,-1,1};
    private static Set<List<Integer>> points = new HashSet<>();
    private static int startX = Integer.MAX_VALUE;
    private static int startY = Integer.MAX_VALUE;
    private static int startZ = Integer.MAX_VALUE;
    private static int endX = Integer.MIN_VALUE;
    private static int endY = Integer.MIN_VALUE;
    private static int endZ = Integer.MIN_VALUE;
    private static int externalSurfaceArea = 0;
    
    public static void main(String[] args) {
        File file = new File("input.txt");
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                List<Integer> point = Arrays.stream(scanner.nextLine().split(",")).map(Integer::parseInt).collect(Collectors.toList());
                points.add(point);
                startX = Math.min(startX, point.get(0)-1);
                startY = Math.min(startY, point.get(1)-1);
                startZ = Math.min(startZ, point.get(2)-1);
                endX = Math.max(endX, point.get(0)+1);
                endY = Math.max(endY, point.get(1)+1);
                endZ = Math.max(endZ, point.get(2)+1);
            }
            scanner.close();

            List<List<Integer>> startingPoints = Stream.of(xPlane(startX), xPlane(endX),
                                                            yPlane(startY), yPlane(endY),
                                                            zPlane(startZ), zPlane(endZ))
                                                        .flatMap(Collection::stream)
                                                        .collect(Collectors.toList());
            bfs(startingPoints);
            System.out.println(externalSurfaceArea);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }   

    private static void bfs(List<List<Integer>> startingPoints) {
        Queue<List<Integer>> q = new LinkedList<>();
        Map<List<Integer>,Set<List<Integer>>> visited = new HashMap<>();
        for (List<Integer> startingPoint : startingPoints) {
            List<Integer> newStartingPoint = new ArrayList<>();
            newStartingPoint.addAll(startingPoint);
            newStartingPoint.addAll(startingPoint);
            q.add(newStartingPoint);
        }

        while (!q.isEmpty()) {
            List<Integer> fullPoint = q.poll();
            List<Integer> dstPoint = fullPoint.subList(0, 3);
            List<Integer> srcPoint = fullPoint.subList(3, fullPoint.size());
            if (!inBounds(dstPoint)) {
                continue;
            }

            if (visited.get(dstPoint) == null) {
                visited.put(dstPoint, new HashSet<>());
            }

            if (visited.get(dstPoint).contains(srcPoint)) {
                continue;
            }
            
            visited.get(dstPoint).add(srcPoint);
            List<List<Integer>> facePoints = calculateFacePoints(dstPoint);
            if (points.contains(dstPoint)) {
                externalSurfaceArea++;
                continue;
            }

            List<List<Integer>> nextPoints = new ArrayList<>();
            for (List<Integer> face : facePoints) {
                List<Integer> nextPoint = new ArrayList<>();
                nextPoint.addAll(face);
                nextPoint.addAll(dstPoint);
                nextPoints.add(nextPoint);
            }
            q.addAll(nextPoints);
        }
    }
    
    private static List<List<Integer>> xPlane(int xPos) {
        List<List<Integer>> ret = new ArrayList<>();
        for (int y = startY; y <= endY; y++) {
            for (int z = startZ; z <= endZ; z++) {
                ret.add(Arrays.asList(xPos, y, z));
            }
        }
        return ret;
    }

    private static List<List<Integer>> yPlane(int yPos) {
        List<List<Integer>> ret = new ArrayList<>();
        for (int x = startX; x <= endX; x++) {
            for (int z = startZ; z <= endZ; z++) {
                ret.add(Arrays.asList(x, yPos, z));
            }
        }
        return ret;
    }

    private static List<List<Integer>> zPlane(int zPos) {
        List<List<Integer>> ret = new ArrayList<>();
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                ret.add(Arrays.asList(x, y, zPos));
            }
        }
        return ret;
    }

    private static List<List<Integer>> calculateFacePoints(List<Integer> point) {
        List<List<Integer>> facePoints = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            List<Integer> newPoint = Arrays.asList(point.get(0)+dx[i], point.get(1)+dy[i], point.get(2)+dz[i]);
            facePoints.add(newPoint);
        }
        return facePoints;
    }

    private static boolean inBounds(List<Integer> point) {
        int x = point.get(0);
        int y = point.get(1);
        int z = point.get(2);
        return
            x >= startX &&
            x <= endX && 
            y >= startY &&
            y <= endY &&
            z >= startZ &&
            z <= endZ;
    }
}