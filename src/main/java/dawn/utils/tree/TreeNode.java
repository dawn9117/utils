package dawn.utils.tree;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TreeNode<T extends TreeNode> {

	protected String id;

	protected String parentId;

	protected List<T> children = new ArrayList<>();

	protected void add(T node) {
		children.add(node);
	}
}
