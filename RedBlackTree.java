
public class RedBlackTree {
	public int BLACK = 1;
	public int RED = 0;

	RedBlackNode nullRBNode = new RedBlackNode(new Building(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE));
	int flag = 0;
	int checkComma = 0;
	public RedBlackNode root = nullRBNode;

	/*
	 * Traverse the Red black tree and return the Red Black Node with the required
	 * building number
	 */
	public RedBlackNode traverse(int buildingNum, RedBlackNode node) {
		if (root == nullRBNode) {
			return null;
		}

		if (buildingNum < node.getBuilding().getBuildingNum()) {
			if (node.getLeft() != nullRBNode) {
				return traverse(buildingNum, node.getLeft());
			}
		} else if (buildingNum > node.getBuilding().getBuildingNum()) {
			if (node.getRight() != nullRBNode) {
				return traverse(buildingNum, node.getRight());
			}
		} else if (buildingNum == node.getBuilding().getBuildingNum()) {
			System.out.println("(" + node.getBuilding().getBuildingNum() + "," + node.getBuilding().getExecutedTime()
					+ "," + node.getBuilding().getTotalTime() + ")");
			return node;
		}
		return null;
	}

	/* Prints buildings of within given range */
	public void inorderTraverse(RedBlackNode node, int lower, int upper) {
		if (node == null) {
			return;
		}
		inorderTraverse(node.getLeft(), lower, upper);
		if (node.getBuilding().getBuildingNum() <= upper && node.getBuilding().getBuildingNum() >= lower) {
			flag = 1;
			if (isPrintIndividualBuilding()) {
				checkComma = 1;
				System.out.print("(" + node.getBuilding().getBuildingNum() + "," + node.getBuilding().getExecutedTime()
						+ "," + node.getBuilding().getTotalTime() + ")");
			} else {
				System.out.print("," + "(" + node.getBuilding().getBuildingNum() + ","
						+ node.getBuilding().getExecutedTime() + "," + node.getBuilding().getTotalTime() + ")");
			}
		}
		inorderTraverse(node.getRight(), lower, upper);
	}

	/*
	 * Check to see if the Print Building command takes only one or multiple
	 * arguments
	 */
	private boolean isPrintIndividualBuilding() {
		return checkComma == 0;
	}

	/* Looks up particular building */
	public RedBlackNode lookUp(int buildingNum) {
		RedBlackNode rbNode = traverse(buildingNum, root);
		if (rbNode == null) {
			System.out.println("(0,0,0)");
		}
		return rbNode;
	}

	public void printRangeOfBuildings(int lower, int upper) {
		checkComma = 0;
		flag = 0;
		if (root == null)
			System.out.println("(0,0,0)");
		inorderTraverse(root, lower, upper);
		if (flag == 0) {
			System.out.println("(0,0,0)");
		} else {
			System.out.println();
		}
	}

	/* Balance tree once a node is inserted */
	public void balanceTree(RedBlackNode rbNode) {
		while (rbNode.getParent().getColor() == RED) {
			RedBlackNode aunt = nullRBNode;
			if (rbNode.getParent() == rbNode.getParent().getParent().getLeft()) {
				aunt = rbNode.getParent().getParent().getRight();

				if (aunt != nullRBNode && aunt.getColor() == RED) {
					rbNode = checkIfAuntIsRed(rbNode, aunt);
					continue;
				}
				if (rbNode == rbNode.getParent().getRight()) {
					// LL Rotate
					rbNode = rbNode.getParent();
					rotateLeft(rbNode);
				}
				rbNode.getParent().setColor(BLACK);
				rbNode.getParent().getParent().setColor(RED);
				rotateRight(rbNode.getParent().getParent());
			} else {
				aunt = rbNode.getParent().getParent().getLeft();
				if (aunt != nullRBNode && aunt.getColor() == RED) {
					rbNode = checkIfAuntIsRed(rbNode, aunt);
					continue;
				}
				if (rbNode == rbNode.getParent().getLeft()) {
					// RR Rotate
					rbNode = rbNode.getParent();
					rotateRight(rbNode);
				}
				rbNode.getParent().setColor(BLACK);
				rbNode.getParent().getParent().setColor(RED);
				rotateLeft(rbNode.getParent().getParent());
			}
		}
		root.setColor(BLACK);
	}

	public RedBlackNode insert(Building building) {
		RedBlackNode currentNode = new RedBlackNode(building);
		initCurrentNode(currentNode);
		RedBlackNode tempNode = root;
		if (root == nullRBNode) {
			root = currentNode;
			currentNode.setColor(BLACK);
			currentNode.setParent(nullRBNode);
		} else {
			currentNode.setColor(RED);
			while (true) {
				if (currentNode.getBuilding().getBuildingNum() < tempNode.getBuilding().getBuildingNum()) {
					if (isLeftChildNull(tempNode)) {
						tempNode.setLeft(currentNode);
						currentNode.setParent(tempNode);
						break;
					} else {
						tempNode = tempNode.getLeft();
					}
				} else if (currentNode.getBuilding().getBuildingNum() > tempNode.getBuilding().getBuildingNum()) {
					if (isRightChildNull(tempNode)) {
						tempNode.setRight(currentNode);
						currentNode.setParent(tempNode);
						break;
					} else {
						tempNode = tempNode.getRight();
					}
				} else {
					// Check if building is a duplicate
					Building duplicateBuilding = new Building(-1, -1, -1);
					RedBlackNode dupRBNode = new RedBlackNode(duplicateBuilding);
					return dupRBNode;
				}
			}
			balanceTree(currentNode);
		}
		return currentNode;
	}

	boolean deleteRBNode(RedBlackNode currentNode) {
		if (currentNode == null)
			return false;
		RedBlackNode x;
		RedBlackNode y = currentNode;
		int yInitialColor = y.getColor();

		if (isLeftChildNull(currentNode)) {
			x = currentNode.getRight();
			swapTargetWith(currentNode, currentNode.getRight());
		} else if (isRightChildNull(currentNode)) {
			x = currentNode.getLeft();
			swapTargetWith(currentNode, currentNode.getLeft());
		} else {
			y = getMinTree(currentNode.getRight());
			yInitialColor = y.getColor();
			x = y.getRight();
			if (y.getParent() == currentNode)
				x.setParent(y);
			else {
				swapTargetWith(y, y.getRight());
				y.setRight(currentNode.getRight());
				y.getRight().setParent(y);
			}
			swapTargetWith(currentNode, y);
			performSwap(currentNode, y);
		}
		if (yInitialColor == BLACK)
			deleteMoveUp(x);
		return true;
	}

	/**** UTILITY FUNCTIONS FOR RED BLACK TREE ***/

	void deleteMoveUp(RedBlackNode currentNode) {
		while (currentNode != root && currentNode.getColor() == BLACK) {
			if (currentNode == currentNode.getParent().getLeft()) {
				RedBlackNode aunt = currentNode.getParent().getRight();
				if (aunt.getColor() == RED) {
					aunt.setColor(BLACK);
					currentNode.getParent().setColor(RED);
					rotateLeft(currentNode.getParent());
					aunt = currentNode.getParent().getRight();
				}
				if (aunt.getLeft().getColor() == BLACK && aunt.getRight().getColor() == BLACK) {
					aunt.setColor(RED);
					currentNode = currentNode.getParent();
					continue;
				} else if (aunt.getRight().getColor() == BLACK) {
					aunt = handleIfAuntRightChildIsBlack(currentNode, aunt);
				}
				if (aunt.getRight().getColor() == RED) {
					currentNode = handleIfAuntRightChildIsRed(currentNode, aunt);
				}
			} else {
				RedBlackNode aunt = currentNode.getParent().getLeft();
				if (aunt.getColor() == RED) {
					aunt.setColor(BLACK);
					currentNode.getParent().setColor(RED);
					rotateRight(currentNode.getParent());
					aunt = currentNode.getParent().getLeft();
				}
				if (aunt.getRight().getColor() == BLACK && aunt.getLeft().getColor() == BLACK) {
					aunt.setColor(RED);
					currentNode = currentNode.getParent();
					continue;
				} else if (aunt.getLeft().getColor() == BLACK) {
					aunt.getRight().setColor(BLACK);
					aunt.setColor(RED);
					rotateLeft(aunt);
					aunt = currentNode.getParent().getLeft();
				}
				if (aunt.getLeft().getColor() == RED) {
					aunt.setColor(currentNode.getParent().getColor());
					currentNode.getParent().setColor(BLACK);
					aunt.getLeft().setColor(BLACK);
					rotateRight(currentNode.getParent());
					currentNode = root;
				}
			}
		}
		currentNode.setColor(BLACK);
	}

	/*
	 * Initialise the current node with black color and null left and right children
	 */
	private void initCurrentNode(RedBlackNode currentNode) {
		currentNode.setRight(nullRBNode);
		currentNode.setLeft(nullRBNode);
		currentNode.setColor(BLACK);
		currentNode.setParent(nullRBNode);
	}

	/* Rotate left utility function */
	void rotateLeft(RedBlackNode node) {
		if (node.getParent() != nullRBNode) {
			if (node == node.getParent().getLeft()) {
				node.getParent().setLeft(node.getRight());
			} else {
				node.getParent().setRight(node.getRight());
			}
			node.getRight().setParent(node.getParent());
			node.setParent(node.getRight());
			if (node.getRight().getLeft() != nullRBNode) {
				node.getRight().getLeft().setParent(node);
			}
			node.setRight(node.getRight().getLeft());
			node.getParent().setLeft(node);
		} else {
			moveRootToRight();
		}
	}

	/* Moves the root to the right child of parent */
	private void moveRootToRight() {
		RedBlackNode right = root.getRight();
		root.setRight(right.getLeft());
		root.getLeft().setParent(root);
		root.setParent(right);
		right.setLeft(root);
		right.setParent(nullRBNode);
		root = right;
	}

	/* Moves the root to the left child of parent */
	private void moveRootToLeft() {
		RedBlackNode left = root.getLeft();
		root.setLeft(root.getLeft().getRight());
		left.getRight().setParent(root);
		root.setParent(left);
		left.setRight(root);
		left.setParent(nullRBNode);
		root = left;
	}

	/* Rotate to the right */
	void rotateRight(RedBlackNode node) {
		if (node.getParent() != nullRBNode) {
			if (node == node.getParent().getLeft()) {
				node.getParent().setLeft(node.getLeft());
			} else {
				node.getParent().setRight(node.getLeft());
			}
			node.getLeft().setParent(node.getParent());
			node.setParent(node.getLeft());
			if (node.getLeft().getRight() != nullRBNode) {
				node.getLeft().getRight().setParent(node);
			}
			node.setLeft(node.getLeft().getRight());
			node.getParent().setRight(node);
		} else {
			moveRootToLeft();
		}
	}

	void swapTargetWith(RedBlackNode target, RedBlackNode source) {
		if (target.getParent() == nullRBNode) {
			root = source;
		} else if (target == target.getParent().getLeft()) {
			target.getParent().setLeft(source);
		} else
			target.getParent().setRight(source);
		source.setParent(target.getParent());
	}

	private RedBlackNode checkIfAuntIsRed(RedBlackNode rbNode, RedBlackNode aunt) {
		rbNode.getParent().setColor(BLACK);
		aunt.setColor(BLACK);
		rbNode.getParent().getParent().setColor(RED);
		rbNode = rbNode.getParent().getParent();
		return rbNode;
	}

	private RedBlackNode handleIfAuntRightChildIsBlack(RedBlackNode currentNode, RedBlackNode aunt) {
		aunt.getLeft().setColor(BLACK);
		aunt.setColor(RED);
		rotateRight(aunt);
		aunt = currentNode.getParent().getRight();
		return aunt;
	}

	private RedBlackNode handleIfAuntRightChildIsRed(RedBlackNode currentNode, RedBlackNode aunt) {
		aunt.setColor(currentNode.getParent().getColor());
		currentNode.getParent().setColor(BLACK);
		aunt.getRight().setColor(BLACK);
		rotateLeft(currentNode.getParent());
		currentNode = root;
		return currentNode;
	}

	private boolean isRightChildNull(RedBlackNode currentNode) {
		return currentNode.getRight() == nullRBNode;
	}

	private boolean isLeftChildNull(RedBlackNode currentNode) {
		return currentNode.getLeft() == nullRBNode;
	}

	private void performSwap(RedBlackNode currentNode, RedBlackNode y) {
		y.setLeft(currentNode.getLeft());
		y.getLeft().setParent(y);
		y.setColor(currentNode.getColor());
	}

	RedBlackNode getMinTree(RedBlackNode subTreeRoot) {
		while (subTreeRoot.getLeft() != nullRBNode) {
			subTreeRoot = subTreeRoot.getLeft();
		}
		return subTreeRoot;
	}

}
