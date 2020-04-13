package dawn.utils.tree;

import lombok.Getter;
import lombok.Setter;

/**
 * @author HEBO
 */
@Getter
@Setter
public class SimpleTree extends TreeNode<SimpleTree> {

	private String name;

	private String age;
}
