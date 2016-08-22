package CraftPlanner;

import java.util.ArrayList;
import java.util.List;

import Actions.*;
import Actuators.*;
import Agent.*;
import Debugging.*;
import Goals.*;
import LGOAP.Metrics;
import Sensors.*;
import bwapi.Game;
import bwapi.Unit;
import bwapi.UnitType;

public class CraftPlanner extends Agent
{
	//The agent needs sensors	
	public BaseSensor baseSensor;
	public EnemySensor enemySensor;	
	public UnitSensor unitSensor;
	
	//The agent needs a reference to the game!
	public Game game;
	
	//The agent needs actuators
	public WorkerActuator workerActuator;
	public BuildingActuator buildingActuator;
	public SquadActuator squadActuator;
	public SupplyActuator supplyActuator;
	public ResourceActuator resourceActuator;
	
	//The agent needs a unit debugger
	private UnitDebugger debugger;
	
	public CraftPlanner(Game game)
	{
		//Create a new blackboard to pass to parent constructor
		super(new CraftPlannerBlackboard());		
		
		//Set the game manager
		this.game = game;
		
		//Start the sensors
		baseSensor = new BaseSensor(memory, game);
		enemySensor = new EnemySensor(memory, game);
		unitSensor = new UnitSensor(memory, game, this);		
		
		//Setup actuators
		workerActuator = new WorkerActuator (this, this.blackboard, this.game);
		squadActuator = new SquadActuator (this, this.blackboard, this.game);
		supplyActuator = new SupplyActuator (this, this.blackboard, this.game);
		resourceActuator = new ResourceActuator (this, this.game);
		buildingActuator = new BuildingActuator (this, this.blackboard, resourceActuator);
		
		//Setup debugger
		debugger = new UnitDebugger (workerActuator, game);
				
		//Add actions
		planManager.getPlanner().addActionToPlanner(new H_LaunchAttack(), 2);
		planManager.getPlanner().addActionToPlanner(new H_TrainBioArmy(), 2);
		planManager.getPlanner().addActionToPlanner(new M_Advance(), 1);
		planManager.getPlanner().addActionToPlanner(new M_FindEnemy(), 1);
		planManager.getPlanner().addActionToPlanner(new M_BuildBioProduction(), 1);
		planManager.getPlanner().addActionToPlanner(new M_TrainBioSquad(), 1);
		planManager.getPlanner().addActionToPlanner(new L_BuildBarrack(), 0);
		planManager.getPlanner().addActionToPlanner(new L_TrainMarines(), 0);
		planManager.getPlanner().addActionToPlanner(new L_Scout(), 0);
		planManager.getPlanner().addActionToPlanner(new L_BuildOutpost(), 0);

		//Add goals
		planManager.getPlanner().addGoal(new WinGame(this, this.game));
		
		//Initialize world state
		initWorldState();
	}
	
	public void update ()
	{		
		baseSensor.update();
		enemySensor.update();
		unitSensor.update();
		
		planManager.getPlanner().getGoalManager().update();
		
		workerActuator.update();		
		buildingActuator.update();
		squadActuator.update();
		supplyActuator.update();
		resourceActuator.update();
		
		worldStateUpdate();
		
		planManager.update();
				
		debugger.update();
	}
	
	//Here we initialize all world state variables to their starting values
	//IE what the starting game state is in relation to these variables
	private void initWorldState ()
	{
		worldState.setProperty("HasMarines", new WorldStateValue(0));
		worldState.setProperty("HasBarracks", new WorldStateValue(0));
		worldState.setProperty("HasBioSquads", new WorldStateValue(0));
		worldState.setProperty("HasBioArmy", new WorldStateValue(false));
	}
	
	//TODO - Should the world state be updated every frame?
	//TODO - Should the world state be updated from the actions or from here?
	//TODO - Figure out the dependancies between lower layer state and higher layer state
	private void worldStateUpdate ()
	{
		//Barracks update
		updateBarracks();
		
		//Marines update
		updateMarines();
		
		//Bio Squads update
		updateSquads();

		//Bio Army update
		updateArmys();
		
		//Production update
		updateProduction();
		
//		"HasLaunchedAttack" (bool)
//		"HasBioArmy" (bool)
//		"HasBarracks" (int)
//		"HasOutpost" (bool)
//		"HasScouted" (bool)
//		"HasMarines" (int)
//		"HasAdvanced" (bool)
//		"HasBioProduction" (bool)
//		"HasFoundEnemy" (bool)
//		"HasBioSquads" (int)
	}
	
	private void updateBarracks ()
	{
		WorkingMemoryFact[] playerFacts = memory.getFactsByType(FactType.PlayerBuilding, null);	
		int totalBarracks = 0;
		for (WorkingMemoryFact playerFact : playerFacts)
		{
			CraftPlannerMemoryFact playerCraftFact = (CraftPlannerMemoryFact) playerFact;
			Unit playerUnit = (Unit) playerCraftFact.getFact();
			if (playerUnit.getType() == UnitType.Terran_Barracks)
			{
				totalBarracks = playerCraftFact.getCount();
				break;
			}
		}		
		worldState.setProperty("HasBarracks", new WorldStateValue(totalBarracks));
	}
	
	private void updateMarines ()
	{
		WorkingMemoryFact[] playerFacts = memory.getFactsByType(FactType.PlayerUnit, null);	
		int totalMarines = 0;
		for (WorkingMemoryFact playerFact : playerFacts)
		{
			CraftPlannerMemoryFact playerCraftFact = (CraftPlannerMemoryFact) playerFact;
			Unit playerUnit = (Unit) playerCraftFact.getFact();
			if (playerUnit.getType() == UnitType.Terran_Marine)
			{
				totalMarines = playerCraftFact.getCount();
				break;
			}
		}		
		worldState.setProperty("HasMarines", new WorldStateValue(totalMarines));
	}
	
	private void updateSquads()
	{
		int totalSquads = squadActuator.getSquads().size();
		worldState.setProperty("HasBioSquads", new WorldStateValue(totalSquads));
	}
	
	private void updateArmys ()
	{
		int totalSquads = squadActuator.getSquads().size();
		if (totalSquads >= 5)
		{
			worldState.setProperty("HasBioArmy", new WorldStateValue(true));
		}
		else
		{
			worldState.setProperty("HasBioArmy", new WorldStateValue(false));
		}
	}
	
	private void updateProduction()
	{
		int totalBarracks = ((Integer) worldState.getPropertyValue("HasBarracks").getValue()).intValue();
		if (totalBarracks < 3)
		{
			worldState.setProperty("HasBioProduction", new WorldStateValue(false));
		}
		else
		{
			worldState.setProperty("HasBioProduction", new WorldStateValue(true));
		}
	}
	
}
