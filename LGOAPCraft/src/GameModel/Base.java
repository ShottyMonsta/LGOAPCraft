package GameModel;

import java.util.ArrayList;
import java.util.List;

import bwapi.Game;
import bwapi.Player;
import bwapi.Position;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BaseLocation;

public class Base
{
	private List<MineralPatch> minerals;
	private List<GasPatch> gas;
	private List<Building> buildings;
	private BaseLocation baseLocation;
	private Unit base;
	private Game game;
	private Player owner;
	
	public Base (BaseLocation baseLocation, Game game)
	{
		this.game = game;
		this.baseLocation = baseLocation;
		
		minerals = new ArrayList<MineralPatch>();
		for (Unit mineralUnit : baseLocation.getMinerals())
		{
			minerals.add(new MineralPatch(mineralUnit));
		}
		
		gas = new ArrayList<GasPatch>();
		for (Unit gasUnit : baseLocation.getGeysers())
		{
			gas.add(new GasPatch (gasUnit));
		}
		
		//Find base
		for (Unit unit : game.getUnitsOnTile(baseLocation.getTilePosition()))
		{
			if (unit.getType().equals(UnitType.Terran_Command_Center))
			{
				base = unit;
				//System.out.println("Base found... Setting base to " +base.getType().toString());
				break;
			}
			if (unit.getType().equals(UnitType.Protoss_Nexus))
			{
				base = unit;
				//System.out.println("Base found... Setting base to " +base.getType().toString());
				break;
			}
			if (unit.getType().equals(UnitType.Zerg_Hatchery))
			{
				base = unit;
				//System.out.println("Base found... Setting base to " +base.getType().toString());
				break;
			}
		}		
		if (base == null)
		{
			//System.out.println("Base was not found at location " +baseLocation.getTilePosition().toString());
		}
	}
	
	public MineralPatch getFreeMineralPatch ()
	{
		for (MineralPatch mineralPatch : minerals)
		{
			if (!mineralPatch.isFull())
			{
				return mineralPatch;
			}
		}
		// TODO if can't find a free mineral patch at this base
		return null;
	}
	
	public GasPatch getFreeGasPatch ()
	{
		for (GasPatch gasPatch : gas)
		{
			if (gasPatch.canBeMined())
			{
				if (!gasPatch.isFull())
				{
					return gasPatch;
				}
			}		
			//System.out.println("Gas patch does not have a refinery...");
		}
		// TODO if can't find a free mineral patch at this base
		//System.out.println("Couldn't find a free gas patch at this base...");
		return null;
	}
	
	public void setOwner (Player owner)
	{
		this.owner = owner;
	}
	
	public boolean hasBase ()
	{
		if (base != null)
		{
			return true;
		}
		return false;
	}
	
	public void setBase (Unit base)
	{
		this.base = base;
	}
	
	public TilePosition getPosition ()
	{
		return baseLocation.getTilePosition();
	}
	
	public TilePosition getBestBuildPosition (Worker worker, UnitType buildingType)
	{
		if (buildingType == UnitType.Terran_Refinery)
		{
			for (GasPatch gasPatch : gas)
			{
				if (!gasPatch.canBeMined())
				{
					gasPatch.setRefinery();
					return gasPatch.getTile();
				}
			}
		}
		//System.out.println("Finding best build position");
		//System.out.println("Worker = " +worker +", buildingType = " +buildingType +", game = " +game);
		int x = baseLocation.getTilePosition().getX();
		int y = baseLocation.getTilePosition().getY();
		int dx = 0;
		int dy = 1;
		int length = 1;
		int stepsInDirection = 0;
		boolean firstTurn = true;
		
		//System.out.println("base loc x = " +x +", y = " +y);
		//System.out.println("map width x = " +game.mapWidth() +", height = " +game.mapHeight());
		
		while (length < game.mapWidth())
		{
			if ((x >= 0 && x < game.mapWidth()) && (y >= 0 && y < game.mapHeight()))
			{
				//System.out.println("Checking if a building can be built at location");
				if (canBuildHere(worker.getUnit(), buildingType, new TilePosition (x, y)))
				{
					return new TilePosition (x, y);
				}
			}
			
			x += dx;
			y += dy;
			stepsInDirection += 1;
			if (stepsInDirection == length)
			{
				stepsInDirection = 0;
				
				if (!firstTurn)
				{
					length += 1;
				}
				
				firstTurn = false;
				
				if (dx == 0)
				{
					dx = dy;
					dy = 0;
				}
				else
				{
					dy = -dx;
					dx = 0;
				}
			}			
		}
		return TilePosition.None;
	}
	
	private boolean canBuildHere (Unit unit, UnitType buildingType, TilePosition positionToCheck)
	{
		//System.out.println("Checking if a building can be built at location");
		if (!game.canBuildHere(positionToCheck, buildingType, unit))
		{
			return false;
		}
		if (tileIsOccupied(positionToCheck))
		{
			return false;
		}
		return true;
	}

	private boolean tileIsOccupied (TilePosition position) {
		for (Unit unit : game.getAllUnits())
		{
			if (unit.getTilePosition().equals(position))
			{
				return true;
			}
		}	
		return false;
	}
	
	public void update ()
	{
		for (GasPatch gasPatch : gas)
		{
			gasPatch.update();
		}
	}

}
