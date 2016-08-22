package Debugging;

import java.util.ArrayList;
import java.util.List;

import Actuators.WorkerActuator;
import GameModel.Worker;
import bwapi.Color;
import bwapi.Game;
import bwta.BWTA;
import bwta.BaseLocation;

public class UnitDebugger 
{
	private Game game;
	private WorkerActuator workers;
	
	public UnitDebugger (WorkerActuator workers, Game game)
	{
		this.workers = workers;
		this.game = game;
	}
	
	public void update ()
	{
		for (Worker worker : workers.getSelectedWorkers())
		{
			//System.out.println(worker.status);
		}
		BaseLocation playerBase = BWTA.getNearestBaseLocation(game.self().getStartLocation());
		List<BaseLocation> enemyBases = new ArrayList<BaseLocation>();
		enemyBases.clear();
		enemyBases.addAll(BWTA.getStartLocations());
		for (BaseLocation baseLocation : enemyBases)
		{
			game.drawLineMap(playerBase.getPosition(), baseLocation.getPosition(), Color.Red);
		}
		
	}
}
