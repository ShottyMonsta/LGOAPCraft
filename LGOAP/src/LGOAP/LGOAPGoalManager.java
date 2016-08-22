package LGOAP;

import java.util.ArrayList;
import java.util.List;

import LGOAP.*;

//This class manages all the agents available goals at all layers
//It also provides functionality for updating goals relevance and retrieving the most relevant goal
public class LGOAPGoalManager
{
	//Reference to the goals
	private List<LGOAPGoal> goalPool;
	
	//Creates a new goal manager object
	public LGOAPGoalManager ()
	{
		goalPool = new ArrayList<LGOAPGoal>();
		goalPool.clear();
	}
		
	public void update ()
	{
		for (LGOAPGoal goal : goalPool)
		{
			goal.updateRelevance();
		}
	}
	
	//Returns the most relevant goal in the specified layer
	public LGOAPGoal getMostRelevantGoal ()
	{
		int mostRelevant = 0;
		for (int i = 0; i < goalPool.size(); i++)
		{
			if (goalPool.get(i).relevance > goalPool.get(mostRelevant).relevance)
			{
				mostRelevant = i;
			}
		}
		return goalPool.get(mostRelevant);
	}

	//Adds a goal to the goal pool at the specified layer
	public void addGoal (LGOAPGoal goal)
	{
		goalPool.add(goal);
	}
	
	//Returns the list of goals that are in the given layer
	public List<LGOAPGoal> getGoals ()
	{		
		return goalPool;
	}
}
