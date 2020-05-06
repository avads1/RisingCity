/* RedBlackNode Class : Encapsulates building object and its attributes like color, parent, left and right child
 * Has setters and getter for RedBlackNode attributes*/
public class RedBlackNode {

	private Building building;
	private int color;
	private RedBlackNode parent;
	private RedBlackNode left;
	private RedBlackNode right;

	public RedBlackNode(Building aRecord) {
		this.color = 1;
		this.parent = null;
		this.building = aRecord;
		this.right = null;
		this.left = null;
	}

	public int getColor() {
		return this.color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public Building getBuilding() {
		return this.building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public RedBlackNode getLeft() {
		return this.left;
	}

	public void setLeft(RedBlackNode node) {
		this.left = node;
	}

	public RedBlackNode getParent() {
		return this.parent;
	}

	public void setParent(RedBlackNode node) {
		this.parent = node;
	}

	public RedBlackNode getRight() {
		return this.right;
	}

	public void setRight(RedBlackNode node) {
		this.right = node;
	}

}