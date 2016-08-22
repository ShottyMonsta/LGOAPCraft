package GameModel;

import CraftPlanner.CraftPlannerBlackboard;
import Orders.BuildOrder;
import Orders.BuildOrderStatus;
import bwapi.Unit;

public class Worker
{
	private Unit unit;
	private BuildOrder buildOrder;
	private Base base;
	private MineralPatch mineralPatch;
	private GasPatch gasPatch;
	public WorkerStatus status;

	public Worker (Unit unit, Base base)
	{
		this.base = base;
		this.unit = unit;
		this.status = WorkerStatus.Idle;
	}	
	
	public void cancelOrders ()
	{
		switch (status)
		{
			case MineGas:
				gasPatch.unAssignWorker(unit);
				this.gasPatch = null;
				break;
			case MineMinerals:
				mineralPatch.unAssignWorker(unit);
				this.mineralPatch = null;
				break;
			case Build:
				this.buildOrder.removeWorker();
				this.buildOrder = null;	
				break;
		}
		//System.out.println("Worker " +this +", who was " +status +", had their orders cancelled!");
		this.status = WorkerStatus.Idle;
	}
	
	public void update (CraftPlannerBlackboard blackboard)
	{
		if (unit.getHitPoints() <= 0)
		{
			cancelOrders ();
			status = WorkerStatus.Dead;
		}
		
		switch (status)
		{
			case Idle:
				//System.out.println("Worker " +this +" idle...");
				if (base.getFreeGasPatch() != null)
				{
					gasPatch = base.getFreeGasPatch();
					gasPatch.assignWorker(unit);
					status = WorkerStatus.MineGas;						
				}
				else if (base.getFreeMineralPatch() != null)
				{
					mineralPatch = base.getFreeMineralPatch();
					mineralPatch.assignWorker(unit);
					//System.out.println("Worker " +this +" mining minerals...");
					status = WorkerStatus.MineMinerals;
				}
				break;
							
			case MineMinerals:
				if (mineralPatch.mineralsRanOut())
				{
					cancelOrders ();
				}
				else if (!unit.isGatheringMinerals())
				{
					unit.rightClick(mineralPatch.getPatch());
				}
				for (BuildOrder buildOrder : blackboard.getBuildQueue())
				{
					if (buildOrder.canAfford())
					{
						if (buildOrder.status != BuildOrderStatus.Finished && !buildOrder.hasWorker)
						{
							cancelOrders();
							this.buildOrder = buildOrder;
							this.buildOrder.hasWorker = true;
							this.buildOrder.setWorker(this);
							//System.out.println("Worker " +this +" building...");
							status = WorkerStatus.Build;
							break;
						}
					}					
				}
				break;
				
			case MineGas:
				if (gasPatch.gasRanOut())
				{
					cancelOrders ();
				}
				else if (!unit.isGatheringGas())
				{
					unit.rightClick(gasPatch.getPatch());
				}
				for (BuildOrder buildOrder : blackboard.getBuildQueue())
				{
					if (buildOrder.canAfford())
					{
						if (buildOrder.status != BuildOrderStatus.Finished && !buildOrder.hasWorker)
						{
							cancelOrders();
							this.buildOrder = buildOrder;
							this.buildOrder.hasWorker = true;
							this.buildOrder.setWorker(this);
							//System.out.println("Worker " +this +" building...");
							status = WorkerStatus.Build;
							break;
						}
					}					
				}
				break;
				
			case Build:
				buildOrder.update();
				if(buildOrder.status == BuildOrderStatus.Finished)
				{
					cancelOrders();
				}
				break;
		}		
	}
	
	public Base getBase ()
	{
		return base;
	}
	
	public void setBase (Base newBase)
	{
		this.base = newBase;
	}
	
	public Unit getUnit ()
	{
		return unit;
	}
}
