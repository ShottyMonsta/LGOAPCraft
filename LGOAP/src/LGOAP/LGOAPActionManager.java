package LGOAP;

import java.util.List;
import java.util.ArrayList;

//This manages all the available actions across all layers
public class LGOAPActionManager
{
	//This is the LGOAP data that contains every action available to the agent at all layers
	private List<LGOAPAction> actionPool;	
	
	//Creates a new layered action manager object
	public LGOAPActionManager ()
	{
		actionPool = new ArrayList<LGOAPAction>();
		actionPool.clear();
	}
	
	//Adds an action to the action pool at the specified layer
	public void addAction (LGOAPAction action)
	{
		actionPool.add(action);
	}
	
	//Returns the list of actions that are in the given layer
	public List<LGOAPAction> getActions ()
	{
		return actionPool;
	}
}
