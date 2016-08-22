package CraftPlanner;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Agent.*;
import GameModel.Squad;
import LGOAP.*;
import Orders.*;
import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;

//Blackboard acts as central hub for communications between the agents various subsystems
//Planner will add orders to the blackboard and the subsystems will query these orders to know what to do
public class CraftPlannerBlackboard implements Blackboard
{	
	//This map stores the agents currently active goals from each layer
	private List<BuildOrder> buildQueue;
	private List<TrainingOrder> trainingQueue;
	private List<ResearchOrder> researchQueue;
	private List<MilitaryOrder> militaryQueue;	
	private List<Squad> squads;
	
	public CraftPlannerBlackboard ()
	{
		reset();
	}
	
//	public void update ()
//	{
//		System.out.println("Build queue size: " +buildQueue.size());
//	}

	public void reset ()
	{
		buildQueue = new ArrayList<BuildOrder>();
		buildQueue.clear();
		trainingQueue = new ArrayList<TrainingOrder>();
		trainingQueue.clear();
		researchQueue = new ArrayList<ResearchOrder>();
		researchQueue.clear();
		militaryQueue = new ArrayList<MilitaryOrder>();
		militaryQueue.clear();
		squads = new ArrayList<Squad>();
		squads.clear();
	}
	
	public void addBuildQueue (BuildOrder order)
	{
		buildQueue.add(order);
	}
	
	public List<BuildOrder> getBuildQueue ()
	{
		return buildQueue;
	}
	
	public int getBuildQueueCount ()
	{
		return buildQueue.size();
	}
	
	public boolean hasBuildOrder ()
	{
		if (buildQueue.size() > 0)
		{
			return true;
		}
		return false;
	}
	
	public boolean hasResearchOrder ()
	{
		if (researchQueue.size() > 0)
		{
			return true;
		}
		return false;
	}
	
	public boolean hasTrainingOrder ()
	{
		if (trainingQueue.size() > 0)
		{
			return true;
		}
		return false;
	}
	
	public boolean hasMilitaryOrder ()
	{
		if (militaryQueue.size() > 0)
		{
			return true;
		}
		return false;
	}

	
	public BuildOrder getNextBuildOrder ()
	{
		//System.out.println("Getting next build order from blackboard build queue...");
		if (buildQueue.size() > 0)
		{
			return buildQueue.remove(0);
		}
		return null;	
	}
	
	public ResearchOrder getNextResearchOrder ()
	{
		//System.out.println("Getting next build order from blackboard build queue...");
		if (researchQueue.size() > 0)
		{
			return researchQueue.remove(0);
		}
		return null;	
	}
	
	public boolean getNextTrainingOrder (TrainingOrder toRemove)
	{
		//System.out.println("Getting next build order from blackboard build queue...");
		if (trainingQueue.size() > 0)
		{
			return trainingQueue.remove(toRemove);
		}
		return false;	
	}
		
	public void addTrainingQueue (TrainingOrder order)
	{
		trainingQueue.add(order);
	}
	
	public List<TrainingOrder> getTrainingQueue ()
	{
		return trainingQueue;
	}
	
	public void addResearchQueue (TechType type)
	{
		ResearchOrder order = new ResearchOrder(type);
		researchQueue.add(order);
	}
	
	public List<ResearchOrder> getReseachQueue ()
	{
		return researchQueue;
	}
	
	public List<MilitaryOrder> getMilitaryQueue ()
	{
		return militaryQueue;
	}
	
	public void addMilitaryQueue (MilitaryOrder order)
	{
		militaryQueue.add(order);
	}
	
}
