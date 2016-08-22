package Agent;

import java.util.List;

import LGOAP.*;

//This is the default agent class, it contains:
//A world state, that is the actual state of the game world for this agent
//A working memory, where facts about the world are stored
//A blackboard, which is the central hub for communications between the various subsystems
//An action manager, which manages all of the agents available actions across the various layers
//A goal manage, which manages all of the agents available goals across the various layers
public class Agent
{
	protected WorldState worldState;
	protected WorkingMemory memory;
	public Blackboard blackboard;
	protected PlanManager planManager;
	public Metrics metrics;
	
	//Creates a new agent
	public Agent (Blackboard blackboard)
	{
		this.worldState = new WorldState();
		this.memory = new WorkingMemory();
		this.blackboard = blackboard;
		this.planManager = new PlanManager(this);
		this.metrics = new Metrics();
	}
	
	public PlanManager getPlanManager ()
	{
		return planManager;
	}
	
	//Sets a property in the agents world state model
	public void setWorldStateProperty (String key, WorldStateValue<?> value)
	{
		this.worldState.setProperty(key, value);
	}
	
	//Returns true if the agents world state model contains the passed property specified by the passed key
	public boolean containsWorldStateProperty (String key)
	{
		return worldState.containsKey(key);
	}
	
	//Returns a property from the agents world state, specified by the passed key
	public <T> WorldStateValue<T> getWorldStateValue (String key)
	{
		return this.worldState.<T>getPropertyValue(key);
	}

	//Returns the agents world state
	public WorldState getWorldState() {
		return worldState;
	}
	
	//Returns the agents blackboard
	public Blackboard getBlackboard ()
	{
		return blackboard;
	}
	
	//Returns the agents working memory
	public WorkingMemory getMemory ()
	{
		return memory;
	}
		
	public WorkingMemory getWorkingMemory ()
	{
		return memory;
	}
	
	public void printWorldState()
	{
		System.out.println("\n");
		System.out.println("Agent current state: ");
		List<WorldStateProperty> worldProps = worldState.getValues();
		for (WorldStateProperty worldProp : worldProps)
		{
			System.out.println(worldProp.key +" = " +worldProp.value);
		}
		System.out.println("\n");
	}

}
