package GameModel;

import java.util.ArrayList;
import java.util.List;

import bwapi.Position;
import bwapi.Unit;

public class MineralPatch
{
	private Unit patch;
	private List<Unit> workers;
	
	public MineralPatch (Unit patch)
	{
		this.patch = patch;
		this.workers = new ArrayList<Unit>();
		this.workers.clear();
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
	
	public void assignWorker (Unit unit)
	{
		if (isFull())
		{
			//System.out.println("The mineral patch is either empty or already has maximum workers assigned!");
			return;
		}
		//System.out.println("Worker is being assigned to a mineral patch...");
		workers.add(unit);
		//System.out.println("Patch = " +patch);
		unit.rightClick(patch);
		//System.out.println("Worker assigned, minerals will now be mined!");
	}
	
	public void unAssignWorker (Unit unit)
	{
		unit.rightClick(unit);
		workers.remove(unit);
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
	
	public Unit getPatch ()
	{
		return patch;
	}
	
	public void update ()
	{
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
	
	public boolean mineralsRanOut ()
	{
		if (patch.getResources() <= 0)
		{
			return true;
		}
		return false;
	}
}
