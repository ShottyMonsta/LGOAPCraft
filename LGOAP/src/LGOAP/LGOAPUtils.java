package LGOAP;

import java.util.ArrayList;
import java.util.List;

import Agent.*;
import searchEngine.*;

//LGOAPUtils implements the interface Utils and provides definitions for all the methods required in a GOAP graph search
public class LGOAPUtils implements Utils
{
	private List<Node> openList;
	private List<Node> closedList;
	public Agent agent;
	public LGOAPGoal goal;
	
	public LGOAPUtils (Agent agent)
	{
		openList = new ArrayList<Node>();
		closedList = new ArrayList<Node>();
		this.agent = agent;
	}
	
	public void setGoal (LGOAPGoal goal)
	{
		this.goal = goal;
	}

	public void addOpenList(Node nodeToAdd)
	{
		openList.add(nodeToAdd);
	}

	public void removeOpenList(Node nodeToRemove)
	{
		openList.remove(nodeToRemove);
	}

	public boolean inOpenList(Node nodeToCheck)
	{
		return openList.contains(nodeToCheck);
	}

	public Node getCheapestOpen()
	{
		int cheapestIndex = 0;
		for (int i = 0; i < openList.size(); i++)
		{
			if (openList.get(i).f < openList.get(cheapestIndex).f)
			{
				cheapestIndex = i;
			}
		}
		return openList.get(cheapestIndex);
	}

	public void addClosedList(Node nodeToAdd)
	{
		closedList.add(nodeToAdd);		
	}

	public boolean inClosedList(Node nodeToCheck)
	{
		return closedList.contains(nodeToCheck);
	}

	public boolean isFinished(Node current)
	{
		//System.out.println("Checking if planner has finished...");
		LGOAPNode node = (LGOAPNode) current;
		if (node.goalPlanState.getDifferences(node.currentPlanState) == 0)
		{
			return true;
		}
		return false;
	}

	public float getEdgeCost(Node neighbour)
	{
		LGOAPNode node = (LGOAPNode) neighbour;
		return node.actionTaken.cost +node.actionTaken.priority;
	}

	public float getHeuristicDistance(Node current, boolean firstTime)
	{
		LGOAPNode node = (LGOAPNode) current;
		if (firstTime)
		{
			node.goalPlanState.resetProperties();
			goal.setWorldStateSatisfactionConditions(node.goalPlanState);
		}
		else
		{			
			LGOAPAction action = node.actionTaken;
			action.updateCurrentStatePreconditions(node.currentPlanState);
			action.updateGoalStatePreconditions(node.goalPlanState);
		}
		mergeStates(node.currentPlanState, node.goalPlanState);
		return node.goalPlanState.getDifferences(node.currentPlanState);
	}
	
	private void mergeStates (WorldState currentPlanState, WorldState goalPlanState)
	{
		List<WorldStateProperty> props = goalPlanState.getValues();
		for (WorldStateProperty prop : props)
		{
			if (!currentPlanState.containsKey(prop.key))
			{
				if (agent.containsWorldStateProperty(prop.key))
				{
					currentPlanState.setProperty(prop.key, agent.getWorldStateValue(prop.key));
				}
				else
				{
					//TODO revert back to commented code below:
					//currentPlanState.setProperty(prop.key, new WorldStateValue(false));
					if (prop.value.getValue().getClass() == java.lang.Integer.class)
					{
						currentPlanState.setProperty(prop.key, new WorldStateValue(0));
					}
					else if (prop.value.getValue().getClass() == java.lang.Boolean.class)
					{
						currentPlanState.setProperty(prop.key, new WorldStateValue(false));
					}
				}
			}
		}
	}
	
	public String getGoal ()
	{
		String rtn = "";
		for (WorldStateProperty prop : goal.goalState.getProperties().values())
		{
			rtn += prop.key;
		}
		return rtn;
	}

	public boolean isReached(Node currentNode)
	{
		LGOAPNode node = (LGOAPNode) currentNode;
		if (node.currentPlanState.getDifferences(node.goalPlanState) == 0)
		{
			return true;
		}
		return false;
	}

	public void resetLists() {
		openList.clear();
		closedList.clear();
	}
	
	public LGOAPGoal getCurrentGoal ()
	{
		return goal;
	}
	
}
