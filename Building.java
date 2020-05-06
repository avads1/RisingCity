/* Building Class : Encapsulates building number, executed time and total time
 * Has setters and getter for Building attributes*/
public class Building {
	private int buildingNum;
	private int executedTime;
	private int totalTime;

	public Building(int buildingNum, int executedTime, int totalTime) {
		this.buildingNum = buildingNum;
		this.executedTime = executedTime;
		this.totalTime = totalTime;
	}

	public int getBuildingNum() {
		return buildingNum;
	}

	public void setExecutedTime(int execTime) {
		this.executedTime = execTime;
	}

	public int getExecutedTime() {
		return executedTime;
	}

	public int getTotalTime() {
		return totalTime;
	}
}