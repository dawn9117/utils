package dawn.utils.tree;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TreeNode {

	protected String id;

	protected String parentId;

	protected List<TreeNode> children = new ArrayList<>();

	protected void add(TreeNode node) {
		children.add(node);
	}
}
