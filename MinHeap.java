
public class MinHeap {
	private HeapNode[] Heap;
	private int heapSize;
	private int maxsize;
	private static final int FRONT = 1;

	public MinHeap(int maxsize) {
		this.maxsize = maxsize;
		this.heapSize = 0;
		Heap = new HeapNode[this.maxsize + 1];
		HeapNode aRecord = new HeapNode(new Building(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE), null);
		Heap[0] = aRecord;
	}

	/* Returns the parent index for a particular given index */
	private int getParentIndex(int index) {
		index = index / 2;
		return index;
	}

	/* Returns the left child index for a particular given index */
	private int getLeftChildIndex(int index) {
		index = 2 * index;
		return index;
	}

	/* Returns the right child index for a particular given index */
	private int getRightChildIndex(int index) {
		index = (2 * index) + 1;
		return index;
	}

	/* Checks if the particular node is a leaf */
	private boolean isLeaf(int index) {
		if (index > (heapSize / 2) && index <= heapSize)
			return true;
		return false;
	}

	private void swap(int val1, int val2) {
		HeapNode tmp;
		tmp = Heap[val1];
		Heap[val1] = Heap[val2];
		Heap[val2] = tmp;
	}

	public HeapNode getMin() {
		if (heapSize == 0)
			return null;
		return Heap[1];
	}

	public int getSize() {
		return heapSize;
	}

	/*
	 * Min heapifies on the basis of execution times. If the execution times are
	 * equal, the building with the lesser number is picked and made as min
	 */
	public void performMinHeapify(int index) {
		if (!isLeaf(index)) {
			HeapNode rightChild = new HeapNode(new Building(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE),
					null);
			HeapNode left = new HeapNode(new Building(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE), null);

			if (getRightChildIndex(index) <= heapSize) {
				rightChild = Heap[getRightChildIndex(index)];
			}
			if (getLeftChildIndex(index) <= heapSize) {
				left = Heap[getLeftChildIndex(index)];
			}
			if (Heap[index].getBuilding().getExecutedTime() > left.getBuilding().getExecutedTime()
					|| Heap[index].getBuilding().getExecutedTime() > rightChild.getBuilding().getExecutedTime()) {
				if (left.getBuilding().getExecutedTime() < rightChild.getBuilding().getExecutedTime()) {
					swap(index, getLeftChildIndex(index));
					performMinHeapify(getLeftChildIndex(index));
				} else if (left.getBuilding().getExecutedTime() > rightChild.getBuilding().getExecutedTime()) {
					swap(index, getRightChildIndex(index));
					performMinHeapify(getRightChildIndex(index));
				} else {
					if (left.getBuilding().getBuildingNum() < rightChild.getBuilding().getBuildingNum()) {
						swap(index, getLeftChildIndex(index));
						performMinHeapify(getLeftChildIndex(index));
					} else {
						swap(index, getRightChildIndex(index));
						performMinHeapify(getRightChildIndex(index));
					}
				}
			} else if (Heap[index].getBuilding().getExecutedTime() == left.getBuilding().getExecutedTime()
					&& Heap[index].getBuilding().getExecutedTime() < rightChild.getBuilding().getExecutedTime()) {
				if (Heap[index].getBuilding().getBuildingNum() > left.getBuilding().getBuildingNum()) {
					swap(index, getLeftChildIndex(index));
					performMinHeapify(getLeftChildIndex(index));
				}
			} else if (Heap[index].getBuilding().getExecutedTime() < left.getBuilding().getExecutedTime()
					&& Heap[index].getBuilding().getExecutedTime() == rightChild.getBuilding().getExecutedTime()) {
				if (Heap[index].getBuilding().getBuildingNum() > rightChild.getBuilding().getBuildingNum()) {
					swap(index, getRightChildIndex(index));
					performMinHeapify(getRightChildIndex(index));
				}
			} else if (Heap[index].getBuilding().getExecutedTime() < left.getBuilding().getExecutedTime()
					&& Heap[index].getBuilding().getExecutedTime() < rightChild.getBuilding().getExecutedTime()) {

			} else {
				if (Heap[index].getBuilding().getBuildingNum() > left.getBuilding().getBuildingNum()
						|| Heap[index].getBuilding().getBuildingNum() > rightChild.getBuilding().getBuildingNum()) {
					if (left.getBuilding().getBuildingNum() < rightChild.getBuilding().getBuildingNum()) {
						swap(index, getLeftChildIndex(index));
						performMinHeapify(getLeftChildIndex(index));
					} else {
						swap(index, getRightChildIndex(index));
						performMinHeapify(getRightChildIndex(index));
					}
				}
			}
		}

	}

	/* Inserts node into the min heap */
	public void insert(RedBlackNode rbNode, Building building) {
		HeapNode currentHeapNode = new HeapNode(building, rbNode);
		Heap[++heapSize] = currentHeapNode;
		int current = heapSize;
		boolean change = true;
		while (change && Heap[current].getBuilding().getExecutedTime() <= Heap[getParentIndex(current)].getBuilding()
				.getExecutedTime() && current != getParentIndex(current)) {
			if (Heap[current].getBuilding().getExecutedTime() < Heap[getParentIndex(current)].getBuilding()
					.getExecutedTime()) {
				change = false;
				swap(current, getParentIndex(current));
				current = getParentIndex(current);
			} else if (Heap[current].getBuilding().getExecutedTime() == Heap[getParentIndex(current)].getBuilding()
					.getExecutedTime()
					&& Heap[current].getBuilding().getBuildingNum() < Heap[getParentIndex(current)].getBuilding()
							.getBuildingNum()) {
				change = false;
				swap(current, getParentIndex(current));
				current = getParentIndex(current);
			}
			change = !change;
		}
	}

	/* Removes min node from min heap */
	public HeapNode remove() {
		if (heapSize == 0)
			return null;
		HeapNode popped = Heap[FRONT];
		Heap[FRONT] = Heap[heapSize--];
		performMinHeapify(FRONT);
		return popped;
	}

}
