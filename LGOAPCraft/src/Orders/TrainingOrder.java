package Orders;

import java.util.ArrayList;
import java.util.List;

import Actuators.ResourceActuator;
import Actuators.SupplyActuator;
import GameModel.Building;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;

public class TrainingOrder
{
	private int trainCount;
	private UnitType unitType;
	public TrainingOrderStatus status;
	private Building building;
	public boolean hasBuilding;
	private ResourceActuator resourceActuator;
	private SupplyActuator supplyActuator;
	
	public TrainingOrder (int trainCount, UnitType unitType, ResourceActuator resourceActuator, SupplyActuator supplyActuator)
	{
		this.trainCount = trainCount;
		this.unitType = unitType;
		this.status = TrainingOrderStatus.Idle;
		this.resourceActuator = resourceActuator;
		this.supplyActuator = supplyActuator;
	}
	
	public UnitType getUnitType ()
	{
		return unitType;
	}
	
	public int getTrainCount ()
	{
		return trainCount;
	}

	public void setBuilding (Building building)
	{
		hasBuilding = true;
		this.building = building;
	}
	
	public void removeBuilding ()
	{
		hasBuilding = false;
		this.building = null;
	}
	
	private boolean canAfford ()
	{
		if (resourceActuator.canAfford(unitType))
		{
			if (supplyActuator.getUseableSupply() > unitType.supplyRequired())
			{
				return true;
			}			
		}
		return false;
	}
	
	public void update ()
	{
		switch (status)
		{
			case Idle:
				if (canAfford())
				{
					building.getBuilding().train(unitType);
					status = TrainingOrderStatus.Training;
				}
				break;
			
			case Training:
				if (!building.getBuilding().isTraining())
				{
					trainCount -= 1;
					
					if (trainCount > 0)
					{
						status = TrainingOrderStatus.Idle;
					}
					else
					{
						status = TrainingOrderStatus.Finished;
					}
				}				
				break;
		}
	}
}
