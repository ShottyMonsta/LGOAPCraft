package Actuators;

import java.util.ArrayList;
import java.util.List;

import Agent.Agent;
import Agent.Blackboard;
import Agent.FactType;
import Agent.WorkingMemoryFact;
import CraftPlanner.CraftPlanner;
import CraftPlanner.CraftPlannerBlackboard;
import GameModel.Base;
import GameModel.Worker;
import bwapi.Game;
import bwapi.Unit;

public class WorkerActuator
{
	private Agent agent;
	private CraftPlannerBlackboard blackboard;
	private Game game;
	private List<Worker> workers;
	
	public WorkerActuator (Agent agent, Blackboard blackboard, Game game)
	{
		//System.out.println("Creating worker manager");
		this.agent = agent;
		this.blackboard = (CraftPlannerBlackboard) blackboard;
		this.game = game;
		CraftPlanner craftAgent = (CraftPlanner) agent;
		workers = new ArrayList<Worker>();
		workers.clear();
	}
	
	public int getTotalWorkers ()
	{
		return workers.size();
	}
	
	public void update ()
	{
		//For each worker
		for (Worker worker : workers)
		{
			//If the worker is still being trained then ignore it
			if (!worker.getUnit().isCompleted())
			{
				continue;
			}		
			
			//Update the worker
			worker.update(blackboard);
		}
	}
	
	public void onWorkerCreate (Unit worker)
	{
		WorkingMemoryFact[] baseFacts = agent.getWorkingMemory().getFactsByType(FactType.PlayerBase, null);
		int closestBaseIndex = 0;
		float closest = Float.POSITIVE_INFINITY;
		for (int i = 0; i < baseFacts.length; i++)
		{
			Base base = (Base) baseFacts[i].getFact();
			if (base.getPosition().getDistance(worker.getPosition().toTilePosition()) < closest)
			{
				closestBaseIndex = i;
				closest = (float) base.getPosition().getDistance(worker.getPosition().toTilePosition());
			}
		}
		Base closestBase = (Base) baseFacts[closestBaseIndex].getFact();		
		Worker newWorker = new Worker (worker, closestBase);
		workers.add(newWorker);
		//System.out.println("Added Worker " +newWorker +"to workers!");
	}
	
	public Base getClosestDifferentBase (Worker worker)
	{
		WorkingMemoryFact[] baseFacts = agent.getWorkingMemory().getFactsByType(FactType.PlayerBase, null);
		int closestBaseIndex = 0;
		float closest = Float.POSITIVE_INFINITY;
		for (int i = 0; i < baseFacts.length; i++)
		{
			Base base = (Base) baseFacts[i].getFact();
			
			if (base == worker.getBase())
			{
				continue;
			}
			
			if (base.getPosition().getDistance(worker.getUnit().getPosition().toTilePosition()) < closest)
			{
				closestBaseIndex = i;
				closest = (float) base.getPosition().getDistance(worker.getUnit().getPosition().toTilePosition());
			}
		}
		if (closest == Float.POSITIVE_INFINITY)
		{
			//No base was found
			return null;
		}
		Base closestBase = (Base) baseFacts[closestBaseIndex].getFact();		
		return closestBase;
	}

	private void onWorkerDestroy (Unit worker)
	{
		
	}
	
	public List<Worker> getSelectedWorkers ()
	{
		List<Worker> selectedWorkers = new ArrayList<Worker>();
		selectedWorkers.clear();
		for (Worker worker : workers)
		{
			if (worker.getUnit().isSelected())
			{
				selectedWorkers.add(worker);
			}
		}
		return selectedWorkers;
	}

}
