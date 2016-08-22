package Actuators;

import Agent.Agent;
import bwapi.Game;
import bwapi.UnitType;

public class ResourceActuator
{
	private Agent agent;
	private Game game;
	private int gameMinerals;
	private int actualMinerals;
	private int gameGas;
	private int actualGas;
	
	public ResourceActuator (Agent agent, Game game)
	{
		this.agent = agent;
		this.game = game;
		this.gameMinerals = game.self().minerals();
		this.actualMinerals = gameMinerals;
		this.gameGas = game.self().gas();
		this.actualGas = gameGas;
	}
	
	public boolean canAfford (UnitType unitToMake)
	{
		if (unitToMake.mineralPrice() <= actualMinerals)
		{
			if (unitToMake.gasPrice() <= actualGas)
			{
				return true;
			}
			return false;
		}
		return false;
	}
	
	public void refundMinerals (int amount)
	{
		this.actualMinerals += amount;
	}
	
	public void refundGas (int amount)
	{
		this.actualGas += amount;
	}
	
	public void spendMinerals (int amount)
	{
		this.actualMinerals -= amount;
	}
	
	public void spendGas (int amount)
	{
		this.actualGas -= amount;
	}
	
	public void update ()
	{
		int mineralDelta = game.self().minerals() - gameMinerals;
		int gasDelta = game.self().gas() - gameGas;
		gameMinerals = game.self().minerals();
		gameGas = game.self().gas();
		actualMinerals += mineralDelta;
		actualGas += gasDelta;
		agent.metrics.updateResources(game.self().gatheredMinerals(), game.self().gatheredGas());
	}
}
