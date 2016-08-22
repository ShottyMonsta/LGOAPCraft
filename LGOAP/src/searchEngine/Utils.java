package searchEngine;

public interface Utils
{
	
	void addOpenList (Node nodeToAdd);
	void removeOpenList (Node nodeToRemove);
	boolean inOpenList (Node nodeToCheck);
	Node getCheapestOpen ();	
	void addClosedList (Node nodeToAdd);	
	boolean inClosedList (Node nodeToCheck);		
	boolean isFinished (Node current);
	float getEdgeCost (Node Neighbour);
	float getHeuristicDistance (Node currentNode, boolean firstTime);
	boolean isReached (Node currentNode);
	void resetLists ();
}
