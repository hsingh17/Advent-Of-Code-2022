import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Day11 {
    private static class Monkey {
        public Queue<Long> items = new LinkedList<>();
        private String operation;
        private String operand;
        private int divisor;
        private int throwToMonkeyTrue;
        private int throwToMonkeyFalse;
        private int inspections = 0;

        public Monkey(String operation, String operand, int divisor, int throwToMonkeyTrue, int throwToMonkeyFalse) {
            this.operation = operation;
            this.operand = operand;
            this.divisor = divisor;
            this.throwToMonkeyTrue = throwToMonkeyTrue;
            this.throwToMonkeyFalse = throwToMonkeyFalse;
        }

        public void addItem(long item) {
            items.add(item);
        }

        public boolean testCondition(long worryLevel) {
            return (worryLevel % divisor) == 0;
        }  

        public void incrementInspections() {
            inspections++;
        }

        public int getInspections() {
            return inspections;
        }

        public long applyOperation(long worryLevel) {
            if (operation.equals("*")) {
                return worryLevel * (operand.equals("old") ? worryLevel : Integer.parseInt(operand));
            } else {
                return worryLevel + (operand.equals("old") ? worryLevel : Integer.parseInt(operand));
            }
        }

        public String toString() {
            return 
                "Monkey: \n" +
                "Starting items : " + items.toString() + "\n" +
                "Operation: " + operation + "\n" +
                "Operand: " + operand + "\n" + 
                "Divisor: " + divisor + "\n" + 
                "Throw in case true: " + throwToMonkeyTrue + "\n" +
                "Throw in case false: " + throwToMonkeyFalse + "\n" +
                "Total Inspections: " + inspections + "\n";
        }
    }

    private static List<Monkey> monkeys = new ArrayList<>();
    public static void main(String[] args) {
        File file = new File("input.txt");
        int rounds = 10000;
        int mod = 1;
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String monkeyLine = scanner.nextLine();
                if (monkeyLine.isEmpty()) {
                    continue;
                }

                String[] itemsLine = scanner.nextLine().trim().split(" ");
                String[] operationLine = scanner.nextLine().trim().split(" ");
                String[] testLine = scanner.nextLine().trim().split(" ");
                String[] trueLine = scanner.nextLine().trim().split(" ");
                String[] falseLine = scanner.nextLine().trim().split(" ");
                Monkey monkey = new Monkey(operationLine[4], operationLine[5], 
                                            Integer.parseInt(testLine[3]), 
                                            Integer.parseInt(trueLine[5]), 
                                            Integer.parseInt(falseLine[5]));

                for (int i = 2; i < itemsLine.length; i++) {
                    monkey.addItem(Integer.parseInt(itemsLine[i].substring(0, 2)));
                }

                monkeys.add(monkey);
                mod *= Integer.parseInt(testLine[3]);
            }
            
            scanner.close();

            for (int round = 0; round < rounds; round++) {
                for (Monkey monkey : monkeys) {
                    while (!monkey.items.isEmpty()) {
                        long worryLevel = monkey.items.poll();
                        long newWorryLevel = monkey.applyOperation(worryLevel) % mod;
                        if (monkey.testCondition(newWorryLevel)) {
                            monkeys.get(monkey.throwToMonkeyTrue).addItem(newWorryLevel);
                        } else {
                            monkeys.get(monkey.throwToMonkeyFalse).addItem(newWorryLevel);
                        }
                        monkey.incrementInspections();
                    }
                }
            }

            for (int i = 0; i < monkeys.size(); i++) {
                System.out.println("Monkey " + i + " inspected items " + monkeys.get(i).getInspections() + " times.");
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}