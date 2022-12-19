import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Day16 {
    private static class Valve {
        public String name;
        public List<String> tunnelsTo = new ArrayList<>();
        public int flowRate;
        public boolean isOpen = false;

        @Override
        public String toString() {
            return "Valve: " + name + ". Flow Rate: " + flowRate + ". Tunnels to: " + tunnelsTo.toString();
        }
    }

    private static Map<String, Valve> valves = new HashMap<>();
    private static  Map<String, Set<List<Integer>>> memo = new HashMap<>();
    private static int maximumPressure = Integer.MIN_VALUE;
    
    public static void main(String[] args) {
        File file = new File("input.txt");
        try {   
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] arr = line.split(" ");
                Valve valve = new Valve();
                valve.name = arr[1];    
                valve.flowRate = Integer.parseInt(arr[4].split("=")[1].replace(";", ""));
                for (int i = 9; i < arr.length; i++) {
                    valve.tunnelsTo.add(arr[i].substring(0, 2));
                }
                valves.put(valve.name, valve);

            }
            scanner.close();

            for (Valve valve : valves.values()) {
                memo.put(valve.name, new HashSet<>());
            }

            dfs("AA", 31, 0, 0);
            System.out.println(maximumPressure);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void dfs(String valveName, int minutesRemaining, int pressureRate, int totalPressure) {
        if (minutesRemaining == 1) {
            maximumPressure = Math.max(maximumPressure, totalPressure);
            return;
        }

        if (memo.get(valveName).contains(Arrays.asList(totalPressure, pressureRate))) {
            return; 
        }

        memo.get(valveName).add(Arrays.asList(totalPressure, pressureRate));
        Valve valve = valves.get(valveName);
        if (valve.flowRate != 0 && !valve.isOpen) { // Open the valve
            valve.isOpen = true;
            dfs(valveName, minutesRemaining-1, pressureRate + valve.flowRate, totalPressure + pressureRate + valve.flowRate);
            valve.isOpen = false;
        } 

        for (String neighborValve : valve.tunnelsTo) {
            dfs(neighborValve, minutesRemaining-1, pressureRate, totalPressure + pressureRate);
        }
    }
}