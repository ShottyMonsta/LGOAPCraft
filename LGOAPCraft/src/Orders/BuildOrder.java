package Orders;

import Actuators.ResourceActuator;
import GameModel.Worker;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;

public class BuildOrder
{
	private Worker worker;
	private TilePosition buildPosition;
	public BuildOrderStatus status;
	private UnitType buildingType;
	private ResourceActuator resourceActuator;
	public boolean hasWorker;
	private Unit currentBuilding;

	public BuildOrder (UnitType buildingType, ResourceActuator resourceActuator)
	{
		this.buildingType = buildingType;
		this.resourceActuator = resourceActuator;
		this.hasWorker = false;
		this.currentBuilding = null;
		this.status = BuildOrderStatus.Idle;
	}
	
	public boolean canAfford ()
	{
		if (resourceActuator.canAfford(buildingType))
		{
			return true;
		}
		return false;
	}
	
	public void setWorker (Worker worker)
	{
		this.worker = worker;
	}
	public void removeWorker ()
	{
		this.worker = null;
	}
	
	public void update ()
	{
		switch (status)
		{
			case Idle:
				//System.out.println("Worker " +worker +"'s build order " +buildingType +" is idle!");	
				if (canAfford())
				{
					resourceActuator.spendMinerals(buildingType.mineralPrice());
					resourceActuator.spendGas(buildingType.gasPrice());
					buildPosition = worker.getBase().getBestBuildPosition(worker, buildingType);
					worker.getUnit().build(buildingType, buildPosition);
					//System.out.println("Worker " +worker +"'s build order " +buildingType +" is waiting for construction!");
					status = BuildOrderStatus.WaitingForConstruction;
				}
				break;
			
			case WaitingForConstruction:
				if (!worker.getUnit().canBuild(buildingType, buildPosition))
				{
					if (worker.getUnit().isConstructing())
					{
						if (worker.getUnit().getBuildUnit().isBeingConstructed())
						{
							resourceActuator.refundMinerals(buildingType.mineralPrice());
							resourceActuator.refundGas(buildingType.gasPrice());
							currentBuilding = worker.getUnit().getBuildUnit();
							//System.out.println("Worker " +worker +"'s build order " +buildingType +" has begun construction!");
							status = BuildOrderStatus.UnderConstruction;
						}
					}
					else
					{
						resourceActuator.refundMinerals(buildingType.mineralPrice());
						resourceActuator.refundGas(buildingType.gasPrice());
						status = BuildOrderStatus.Idle;
					}
				}
				
				break;
				
			case UnderConstruction:
				if (!worker.getUnit().isConstructing())
				{
					//System.out.println("Worker " +worker +"'s build order " +buildingType +" has finished!");
					status = BuildOrderStatus.Finished;				
				}	
				break;
		}
	}
}
