import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Day13 {
    private static class Node {
        public List<Node> children;
        public Integer num;

        public Node() {
            num = null;
            children = new ArrayList<>();
        }

        @Override
        public String toString() {
            if (num != null) {
                return num.toString();
            }

            String ret = "";
            for (int i = 0; i < children.size(); i++) {
                Node child = children.get(i);
                ret += child.toString();
                if (i != children.size()-1) {
                    ret += ",";
                }
            }
            return "[" + ret + "]";
        }
    }

    private static class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node left, Node right) {
            int idxLeft = 0;
            int idxRight = 0;
            int N = left.children.size();
            int M = right.children.size();
            while (idxLeft < N && idxRight < M) {
                Node leftChild = left.children.get(idxLeft);
                Node rightChild = right.children.get(idxRight);
                // Both left and right are integers
                if (leftChild.num != null && rightChild.num != null && leftChild.num != rightChild.num) {
                    return leftChild.num < rightChild.num ? -1 : 1;
                }
    
                // Both left and right are lists
                if (leftChild.num == null && rightChild.num == null) {
                    int ret = compare(leftChild, rightChild);
                    if (ret != 0) {
                        return ret;
                    } 
                }
    
                // Left is an integer and right is a list
                if (leftChild.num != null && rightChild.num == null) {
                    Node wrapperNode = new Node();
                    wrapperNode.children.add(leftChild);
                    int ret = compare(wrapperNode, rightChild);
                    if (ret != 0) {
                        return ret;
                    }
                }
    
                // Left is a list and right is an integer
                if (leftChild.num == null && rightChild.num != null) {
                    Node wrapperNode = new Node();
                    wrapperNode.children.add(rightChild);
                    int ret = compare(leftChild, wrapperNode);
                    if (ret != 0) {
                        return ret;
                    }
                }
    
                idxLeft++;
                idxRight++;
            }
    
            // Right list ran out of input first
            if (idxLeft < N && idxRight == M) {
                return 1;
            }
    
            // Left list ran out of input first
            if (idxLeft == N && idxRight < M) {
                return -1;
            }
    
            return 0;
        }
    }

    public static void main(String[] args) {
        File file = new File("input.txt");
        List<Node> packets = new ArrayList<>();
        String dividerPacket1 = "[[2]]";
        String dividerPacket2 = "[[6]]";
        packets.add(parsePacket(dividerPacket1));
        packets.add(parsePacket(dividerPacket2));

        try {
            Scanner scanner = new Scanner(file);
            int index = 1;
            while (scanner.hasNextLine()) {
                String packet = scanner.nextLine();
                if (packet.isEmpty()) {
                    continue;
                }
                Node node = parsePacket(packet);
                packets.add(node);
                index++;
            }

            scanner.close();

            packets.sort(new NodeComparator());
            List<String> packetStringList = packets.stream().map(Node::toString).collect(Collectors.toList());
            int dividerPacket1Idx = packetStringList.indexOf(dividerPacket1) + 1;
            int dividerPacket2Idx = packetStringList.indexOf(dividerPacket2) + 1;
            System.out.println("Index of " + dividerPacket1 + ": " + dividerPacket1Idx);
            System.out.println("Index of " + dividerPacket2 + ": " + dividerPacket2Idx);
            System.out.println("Decoder key: " + (dividerPacket1Idx * dividerPacket2Idx));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static Node parsePacket(String packet) {
        Stack<Node> s = new Stack<>();
        String num = "";
        for (int i = 0; i < packet.length()-1; i++) {
            char c = packet.charAt(i);
            if (c == '[') {
                s.add(new Node());
            } else if (c == ']') {
                if (!num.isEmpty()) {
                    Node node = new Node();
                    node.num = Integer.parseInt(String.valueOf(num));
                    s.peek().children.add(node);
                    num = "";
                }
                Node node = s.pop();
                s.peek().children.add(node);
            } else if (c == ',') {
                if (num.isEmpty()) {
                    continue;
                }
                
                Node node = new Node();
                node.num = Integer.parseInt(String.valueOf(num));
                s.peek().children.add(node);
                num = "";
            } else {
                num += c;
            }
        } 

        if (!num.isEmpty()) {
            Node node = new Node();
            node.num = Integer.parseInt(String.valueOf(num));
            s.peek().children.add(node);
        }
        return s.pop();
    }
}