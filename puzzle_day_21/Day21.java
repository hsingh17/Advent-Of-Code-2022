import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class Day21 {
    private static Map<String,Monkey> monkeys = new HashMap<>();
    private static long humanValue = Long.MIN_VALUE;
    private static Stack<String> s = new Stack<>();

    private static class Monkey {
        public String monkeyName;
        public Long numberYelling;
        public String waitingMonkey1;
        public String waitingMonkey2;
        public String operation;

        private Monkey(String monkeyName, Long numberYelling, String  waitingMonkey1, String waitingMonkey2, String operation) {
            this.monkeyName = monkeyName;
            this.numberYelling = numberYelling;
            this.waitingMonkey1 = waitingMonkey1;
            this.waitingMonkey2 = waitingMonkey2;
            this.operation = operation;
        }

        public long solve() {
            if (monkeyName.equals("humn")) {
                return Long.MAX_VALUE;
            }
            
            if (numberYelling != null) {
                return numberYelling;
            }

            long monkey2Result = monkeys.get(waitingMonkey2).solve();
            long monkey1Result = monkeys.get(waitingMonkey1).solve();
            if (monkey1Result == Long.MAX_VALUE || monkey2Result == Long.MAX_VALUE) {
                long num = monkey1Result == Long.MAX_VALUE ? monkey2Result : monkey1Result;
                if (operation.equals("-") && monkey2Result == Long.MAX_VALUE) { // NUM - EXPR
                    s.add("*" + " " + -1L);
                    s.add("+" + " " + num);
                } else if (operation.equals("/") && monkey2Result == Long.MAX_VALUE) { // NUM / EXPR
                    s.add("@" + " " + num);
                } else {
                    s.add(operation + " " + num);
                }
                return Long.MAX_VALUE;
            }

            switch (operation) {
                case "+":
                    return monkey1Result + monkey2Result;
                case "-":
                    return monkey1Result - monkey2Result;
                case "*":
                    return monkey1Result * monkey2Result;
                case "/":
                    return monkey1Result / monkey2Result;
            }

            return Integer.MAX_VALUE;
        }

        @Override
        public String toString() {
            if (monkeyName.equals("humn")) {
                return "x";
            } else if (monkeyName.equals("root")) {
                return monkeys.get(waitingMonkey1) + "=" + monkeys.get(waitingMonkey2); 
            } else if (numberYelling != null) {
                return numberYelling.toString();
            } 

            return "(" + monkeys.get(waitingMonkey1) + operation + monkeys.get(waitingMonkey2) + ")";
        }
    }

    public static void main(String[] args) {
        File file = new File("input.txt");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(":");
                String monkeyName = line[0];
                if (isExpression(line[1])) {
                    String[] expression = line[1].trim().split(" ");
                    monkeys.put(monkeyName, new Monkey(monkeyName, null, expression[0], expression[2], expression[1]));
                } else {
                    monkeys.put(monkeyName, new Monkey(monkeyName, Long.parseLong(line[1].trim()), null, null, null));
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Monkey root = monkeys.get("root");
        long waitingMonkey1 = monkeys.get(root.waitingMonkey1).solve();
        long waitingMonkey2 = monkeys.get(root.waitingMonkey2).solve();
        System.out.println(monkeys.get(root.waitingMonkey1));
        System.out.println(monkeys.get(root.waitingMonkey2));
        long ret = solveForHumanValue((waitingMonkey1 == Long.MAX_VALUE ? waitingMonkey2 : waitingMonkey1));
        System.out.println(ret);
    }   

    private static boolean isExpression(String line) {
        return 
            line.contains("*") ||
            line.contains("/") ||
            line.contains("-") ||
            line.contains("+");
    }

    private static long solveForHumanValue(long num) {
        System.out.println(num);
        while (!s.empty()) {
            String str = s.pop();
            String[] arr = str.split(" ");
            String operation = arr[0];
            long num2 = Long.parseLong(arr[1]);
            System.out.println(str);
            switch (operation) {
                case "+":
                    num -= num2;
                    break;
                case "-":
                    num += num2;
                    break;
                case "*":
                    num /= num2;
                    break;
                case "/":
                    num *= num2;
                    break;
                case "@":
                    num = (num2 / num);
                    break;
            }
            System.out.println(num);
        }
        return num;
    }
}