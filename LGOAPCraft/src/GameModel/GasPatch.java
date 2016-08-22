package GameModel;

import java.util.ArrayList;
import java.util.List;

import bwapi.Position;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;

public class GasPatch
{
	private Unit patch;
	private boolean hasRefinery;
	private List<Unit> workers;
	
	public GasPatch (Unit patch)
	{
		this.patch = patch;
		this.workers = new ArrayList<Unit>();
		hasRefinery = false;
	}
	
	public boolean isFull ()
	{
		int workerCap = 0;
		for (Unit worker : workers)
		{
			workerCap += 1;
		}
		if (workerCap <= 2)
		{
			return false;
		}
		else
		{
			return true;
		}		
	}
	
	public boolean canBeMined ()
	{
		if (patch.getType() == UnitType.Terran_Refinery)
		{
			if (!patch.isBeingConstructed())
			{
				return true;
			}	
			return false;
		}
		return false;
	}
	
	public boolean hasUnit (Unit unitToCheck)
	{
		if (workers.contains(unitToCheck))
		{
			return true;
		}
		return false;
	}
	
	public boolean isVisible ()
	{
		if (patch.isVisible())
		{
			return true;
		}
		return false;
	}
	
	public Position getPosition ()
	{
		return patch.getPosition();
	}
	
	public TilePosition getTile ()
	{
		return patch.getTilePosition();
	}
	
	public Unit getPatch ()
	{
		return patch;
	}
	
	public void assignWorker (Unit unit)
	{
		if (!canBeMined ())
		{
			//System.out.println("Can't assign a worker to the gas patch because refinery hasn't been built!");
			return;
		}
		workers.add(unit);
		unit.rightClick(patch);
	}
	
	public void unAssignWorker (Unit unit)
	{
		unit.rightClick(unit);
		workers.remove(unit);
	}
	
	public void setRefinery ()
	{
		if (hasRefinery)
		{
			//System.out.println("Can't set the refinery, it's already set!");
			return;
		}
		this.hasRefinery = true;
	}
	
	public void update ()
	{
		System.out.println("Gas Patch Updating... Can be Mined: " +canBeMined());
		System.out.println("Gas Patch Unit Type: " +patch.getType());
		List<Unit> deadWorkers = new ArrayList<Unit>();
		for (Unit worker : workers)
		{
			if (worker.getHitPoints() <= 0)
			{
				deadWorkers.add(worker);
			}
		}
		for (Unit deadWorker : deadWorkers)
		{
			workers.remove(deadWorker);
		}
	}
	
	public boolean gasRanOut ()
	{
		if (patch.getResources() <= 0)
		{
			return true;
		}
		return false;
	}
	
}
