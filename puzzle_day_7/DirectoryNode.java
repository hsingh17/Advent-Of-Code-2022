import java.util.ArrayList;
import java.util.List;

public class DirectoryNode {
    public List<DirectoryNode> childNodes = new ArrayList<>();
    public DirectoryNode parent;
    public int totalSize = 0;

    public DirectoryNode(DirectoryNode parent) {
        this.parent = parent;
    }
}
