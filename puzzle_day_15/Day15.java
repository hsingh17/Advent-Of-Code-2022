import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Day15 {
    private static class Sensor {
        public int sensorX;
        public int sensorY;
        public int beaconX;
        public int beaconY;
        public int distanceToBeacon;

        public Sensor(int sensorX, int sensorY, int beaconX, int beaconY, int distanceToBeacon) {
            this.sensorX = sensorX;
            this.sensorY = sensorY;
            this.beaconX = beaconX;
            this.beaconY = beaconY;
            this.distanceToBeacon = distanceToBeacon;
        }

        @Override
        public String toString() {
            return "Sensor position: ("+ sensorX + "," + sensorY +"). Closest beacon: (" + beaconX + "," + beaconY + ")" +". Distance to beacon:" + distanceToBeacon;
        }
    }

    private static List<Sensor> sensors = new ArrayList<>();
    public static void main(String[] args) {
        File file = new File("input.txt");
        int startX = Integer.MAX_VALUE;
        int endX = Integer.MIN_VALUE;
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] arr = line.split(" ");
                String sensorXStr = arr[2].split("=")[1];
                String sensorYStr = arr[3].split("=")[1];
                String beaconXStr = arr[8].split("=")[1];
                String beaconYStr = arr[9].split("=")[1];
                
                int sensorX = Integer.parseInt(sensorXStr.substring(0, sensorXStr.lastIndexOf(',')));
                int sensorY = Integer.parseInt(sensorYStr.substring(0, sensorYStr.lastIndexOf(':')));
                int beaconX = Integer.parseInt(beaconXStr.substring(0, beaconXStr.lastIndexOf(',')));
                int beaconY = Integer.parseInt(beaconYStr);
                
                Sensor sensor = new Sensor(sensorX, sensorY, beaconX, beaconY, 
                                            manDistance(sensorX, sensorY, beaconX, beaconY));
                sensors.add(sensor);
            }
            scanner.close();

            List<Integer> xCoords = new ArrayList<>();
            List<Integer> yCoords = new ArrayList<>();
            List<Integer> distances = new ArrayList<>();
            for (Sensor sensor : sensors) {
                xCoords.add(sensor.sensorX);
                yCoords.add(sensor.sensorY);
                distances.add(sensor.distanceToBeacon);
            }


            // Used Desmos to solve manually :P
            System.out.println(xCoords);
            System.out.println(yCoords);
            System.out.println(distances);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static int manDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1-x2) + Math.abs(y1-y2);
    }

    private static boolean canBeaconExist(int x, int y) {
        int cnt = 0;
        for (Sensor sensor : sensors) {
            int dist = manDistance(sensor.sensorX, sensor.sensorY, x, y);
            if (dist > sensor.distanceToBeacon) {
                cnt++;
            } else {
                break;
            }
        }
        return cnt == sensors.size();
    }
}