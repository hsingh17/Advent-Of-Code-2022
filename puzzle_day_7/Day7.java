import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class Day7 {    
    private static int MAX_SPACE = 70000000;
    private static int NEEDED_FREE_SPACE = 30000000;
    private static int minDeletion = 0;
    private static int totalFreeSpace = 0;

    public static void main(String[] args) {
        File file = new File("input.txt");
        try {
            Scanner scanner = new Scanner(file);
            DirectoryNode curNode = null;
            DirectoryNode rootNode = null;
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(" ");
                switch (line[0]) {
                    case "ls": case "dir":
                        break;
                    case "cd":
                        String dir = line[1];
                        if (dir.equals("..")) {
                            if (curNode.parent != null) {
                                curNode.parent.totalSize += curNode.totalSize;
                            }
                            curNode = curNode.parent;
                        } else if (curNode != null) {
                            DirectoryNode childNode = new DirectoryNode(curNode);
                            curNode.childNodes.add(childNode);
                            curNode = childNode;
                        } else {
                            DirectoryNode node = new DirectoryNode(null);
                            curNode = node;
                            rootNode = node;
                        }
                        break;
                    default:
                        curNode.totalSize += Integer.parseInt(line[0]);
                }
            }
            scanner.close();
            totalFreeSpace = MAX_SPACE - rootNode.totalSize;
            minDeletion = rootNode.totalSize;
            traverseDirectories(rootNode);
            System.out.println(minDeletion);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void traverseDirectories(DirectoryNode node) {
        if (totalFreeSpace + node.totalSize >= NEEDED_FREE_SPACE) {
            minDeletion = Math.min(node.totalSize, minDeletion);
        }

        for (DirectoryNode childNode : node.childNodes) {
            traverseDirectories(childNode);
        }
    }
}