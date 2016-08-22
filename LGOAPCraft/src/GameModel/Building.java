package GameModel;

import java.util.ArrayList;
import java.util.List;

import CraftPlanner.CraftPlannerBlackboard;
import Orders.BuildOrderStatus;
import Orders.ResearchOrder;
import Orders.ResearchOrderStatus;
import Orders.TrainingOrder;
import Orders.TrainingOrderStatus;
import bwapi.Game;
import bwapi.Unit;
import bwapi.UnitType;

public class Building
{
	private Unit building;
	public TrainingOrder trainingOrder;
	public ResearchOrder researchOrder;
	public BuildingStatus status;
	
	public Building (Unit building)
	{
		this.building = building;
		this.status = BuildingStatus.Idle;
		this.trainingOrder = null;
	}
	
	public boolean isComplete ()
	{
		if (building.isBeingConstructed())
		{
			return false;
		}
		else if (building.getRemainingBuildTime() > 0)
		{
			return false;
		}
		return true;
	}
	
	public void cancelOrders ()
	{
		this.trainingOrder = null;
		this.researchOrder = null;
	}
	
	public int getHP ()
	{
		return building.getHitPoints();
	}
	
	public UnitType getBuildingType ()
	{
		return building.getType();
	}
	
	public Unit getBuilding ()
	{
		return building;
	}
	
	public boolean isDead ()
	{
		if (building.getHitPoints() <= 0)
		{
			return true;
		}
		return false;
	}
	
	public void update (CraftPlannerBlackboard blackboard)
	{
		switch (status)
		{
			case Idle:
				for (TrainingOrder trainingOrder : blackboard.getTrainingQueue())
				{
					if (trainingOrder.status == TrainingOrderStatus.Finished)
					{
						continue;
					}
					if (building.canTrain(trainingOrder.getUnitType()))
					{
						if (!trainingOrder.hasBuilding)
						{
							//System.out.println(building.getType() +" found a training order for " +trainingOrder.getTrainCount() +" " +trainingOrder.getUnitType() +"'s!");
							this.trainingOrder = trainingOrder;
							trainingOrder.setBuilding(this);
							status = BuildingStatus.Training;
							break;
						}
					}
				}
				//TODO Handle research orders
//				for (ResearchOrder researchOrder : blackboard.getReseachQueue())
//				{
//					if (building.canResearch(researchOrder.techType))
//					{
//						if (!researchOrder.hasBuilding)
//						{
//							trainingOrder.setBuilding(this);
//							status = BuildingStatus.Training;
//							break;
//						}
//					}
//				}
				break;
			
			case Training:
				trainingOrder.update();
				if (trainingOrder.status == TrainingOrderStatus.Finished)
				{
					//System.out.println("Training order finished!");
					trainingOrder.removeBuilding();
					trainingOrder = null;
					status = BuildingStatus.Idle;
				}
				break;
				
			//TODO handle research orders
			case Researching:
				break;
		}		
	}
}
