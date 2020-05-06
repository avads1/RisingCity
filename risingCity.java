
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class risingCity {

	public static void main(String args[]) throws Exception {
		String inputFileName = args[0];
		PrintStream out;
		try {
			out = new PrintStream(new FileOutputStream("output_file.txt"));
			System.setOut(out);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		Queue<List<Integer>> operations = new LinkedList<List<Integer>>();
		// Reading from input file
		try {
			List<String> inputLines = Files.readAllLines(Paths.get(inputFileName), StandardCharsets.UTF_8);
//			List<String> inputLines = Files.readAllLines(Paths.get("I3.txt"), StandardCharsets.UTF_8);
			for (int currentReadLine = 0; currentReadLine < inputLines.size(); currentReadLine++) {
				List<Integer> currentOperation = new ArrayList<Integer>();
				String[] globalCounterPart = inputLines.get(currentReadLine).split(": ");
				String[] commandPart = globalCounterPart[1].split("\\(");
				int operationType = 0;
				int globalTime = Integer.parseInt(globalCounterPart[0]);
				//Add global time to the current operation list
				currentOperation.add(globalTime);
				//Add operation type to the current operation list
				switch (commandPart[0]) {
				case "Insert":
					operationType = 1;
					currentOperation.add(1);
					break;
				case "PrintBuilding":
					if (commandPart[1].contains(",")) {
						operationType = 2;
						currentOperation.add(2);
					} else {
						operationType = 3;
						currentOperation.add(3);
					}
					break;
				default:
					System.out.println("Invalid operation!!");
				}
				//Add arguments of the current instruction to the current operation list
				if (operationType != 3) {
					String[] insertOrPrintRangeCommandPart = commandPart[1].split("\\,");
					currentOperation.add(Integer.parseInt(insertOrPrintRangeCommandPart[0]));
					String[] totalTimePart = insertOrPrintRangeCommandPart[1].split("\\)");
					currentOperation.add(Integer.parseInt(totalTimePart[0]));// operator2
				} else {
					String[] printCommandPart = commandPart[1].split("\\)");
					currentOperation.add(Integer.parseInt(printCommandPart[0]));
					currentOperation.add(-1);
				}
				operations.add(currentOperation);
			}
		} catch (Exception e) {
			System.out.println("Input file not found!!");
			e.printStackTrace();
		}
		//Initialise red black and min heap
		RedBlackTree redBlackTree = new RedBlackTree();
		MinHeap minheap = new MinHeap(2000);
		
		//Global and time variables are set to 0
		int globaltime = 0;
		int timer = 0;
		List<Integer> inputCommand = null;
		HeapNode currentHeapNode = null;
		
		//Continue execution till all the instructions are read or min heap size is zero
		while (operations.size() != 0 || minheap.getSize() != 0 || currentHeapNode != null) {
			if (inputCommand == null && operations.size() != 0)
				inputCommand = operations.poll();
			while (inputCommand != null) {
				if (globaltime == inputCommand.get(0)) {
					switch (inputCommand.get(1)) {
					case 1:
						//Insert node
						Building building = new Building(inputCommand.get(2), 0, inputCommand.get(3));
						RedBlackNode rbNode = redBlackTree.insert(building);
						minheap.insert(rbNode, building);
						break;
					case 2:
						//Print range of buildings
						redBlackTree.printRangeOfBuildings(inputCommand.get(2), inputCommand.get(3));
						break;
					case 3:
						//Print particular building
						RedBlackNode node = redBlackTree.lookUp(inputCommand.get(2));
						break;
					}
					if (operations.size() != 0) {
						inputCommand = operations.poll();
					} else {
						inputCommand = null;
					}
				} else
					break;
			}
			//Check to see if current building has completed its execution
			if (currentHeapNode != null && currentHeapNode.getBuilding().getTotalTime()
					- currentHeapNode.getBuilding().getExecutedTime() == 0) {
				timer = 0;
				System.out.println("(" + currentHeapNode.getBuilding().getBuildingNum() + "," + globaltime + ')' + " ");
				redBlackTree.deleteRBNode(currentHeapNode.getRBTreePtr());
				currentHeapNode = minheap.getMin();
				minheap.remove();

			} else if (timer % 5 == 0) {
				//Check to see if the current building has executed 5 times
				timer = 0;
				if (currentHeapNode != null) {
					minheap.insert(currentHeapNode.getRBTreePtr(), currentHeapNode.getBuilding());
				}
				currentHeapNode = minheap.getMin();
				minheap.remove();

			}
			//Increment the executed time and timer state by 1 for the current building
			if (currentHeapNode != null) {
				int execTime = currentHeapNode.getBuilding().getExecutedTime();
				execTime = execTime + 1;
				currentHeapNode.getBuilding().setExecutedTime(execTime);
				RedBlackNode rbNode = currentHeapNode.getRBTreePtr();
				rbNode.getBuilding().setExecutedTime(currentHeapNode.getBuilding().getExecutedTime());
				timer++;
			}
			//Update global time
			globaltime++;
		}
	}
}
