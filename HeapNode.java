/* HeapNode Class : Encapsulates building object and red black node
 * Has setters and getter for HeapNode attributes*/
public class HeapNode {
	private Building building;
	private RedBlackNode RBTreePtr;

	public HeapNode(Building building, RedBlackNode RBPtr) {
		this.building = building;
		this.RBTreePtr = RBPtr;
	}

	public Building getBuilding() {
		return this.building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public RedBlackNode getRBTreePtr() {
		return this.RBTreePtr;
	}

	public void setRBTreePtr(RedBlackNode RBPtr) {
		this.RBTreePtr = RBPtr;
	}
}
