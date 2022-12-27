import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class Day25 {
    private static class SNAFUConvertor {
        private class StackElement {
            public long curTotal;
            public long remainder;
            public String curSnafu;
            public long exp;

            public StackElement(long curTotal, long remainder, String curSnafu, long exp) {
                this.curTotal = curTotal;
                this.remainder = remainder;
                this.curSnafu = curSnafu;
                this.exp = exp;
            }

            @Override
            public String toString() {
                return "Cur: " + curTotal + ". Remainder: " + remainder + ". String: " + curSnafu + ". Exp: " + exp;
            }
        }

        private Map<Character, Integer> conversion = new HashMap<>(); 
        private Map<Long, String> memo = new HashMap<>();
        private final int MAX_PRECOMPUTATIONS = 20;

        public SNAFUConvertor() {
            conversion.put('2', 2);
            conversion.put('1', 1);
            conversion.put('0', 0);
            conversion.put('-', -1);
            conversion.put('=', -2);
            precompute();
        }

        public long convertSnafusToDecimal(List<String> snafuNums) {
            long ret = 0;
            for (String num : snafuNums) {
                ret += convertToDecimal(num);
            }
            return ret;
        }

        public long convertToDecimal(String snafuNum) {
            long ret = 0;
            int N = snafuNum.length()-1;
            for (int i = 0; i <= N; i++) {
                ret += Math.pow(5, N-i) * conversion.get(snafuNum.charAt(i));
            }
            return ret;
        }

        public String convertDecimalToSnafu(long target) {
            String ret = "";
            int startingIdx = -1;
            /// Begin by finding the base that is as large or larger than target
            while (target > Math.pow(5, ++startingIdx)) {}

            // Now we greedily choose each option
            for (int i = startingIdx; i >= 0; i--) {
                char digitChosen = ' ';
                long numChosen = Long.MAX_VALUE;
                for (char c : conversion.keySet()) {
                    long num = (long) (Math.pow(5, i) * conversion.get(c));
                    if (Math.abs(target-num) < Math.abs(target-numChosen)) {
                        digitChosen = c;
                        numChosen = num;
                    }
                }
                ret += digitChosen;
                target -= numChosen;
            }
            return ret;
        }

        private void precompute() {
            String snafu = "";
            for (int i = 0; i <= MAX_PRECOMPUTATIONS; i++) {
                for (Character digit : conversion.keySet()) {
                    if (digit == '0') {
                        continue;
                    }
                    memo.put((long) Math.pow(5, i) * conversion.get(digit), digit + snafu);
                }
                snafu += '0';
            }
            memo.put(0L, "0");
        }
    }

    public static void main(String[] args) {
        File file = new File("input.txt");
        List<String> input = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                input.add(scanner.nextLine());
            }
            
            SNAFUConvertor snafuConvertor = new SNAFUConvertor();
            long target = snafuConvertor.convertSnafusToDecimal(input);
            System.out.println(snafuConvertor.convertDecimalToSnafu(target));
            System.out.println(target);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }   
}