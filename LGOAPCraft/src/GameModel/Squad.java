package GameModel;

import java.util.ArrayList;
import java.util.List;
import CraftPlanner.CraftPlannerBlackboard;
import Orders.MilitaryOrder;
import Orders.MilitaryOrderStatus;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;

public class Squad
{
	public List<CombatUnit> units;
	public SquadStatus status;
	public MilitaryOrder order;

	public Squad (Unit startingUnit, TilePosition startLocation)
	{
		units = new ArrayList<CombatUnit>();
		units.clear();
		units.add(new CombatUnit(startingUnit, startLocation));
		status = SquadStatus.Idle;
	}
	
	public void cancelOrders ()
	{
		if (status != SquadStatus.Dead)
		{
			order.removeSquad(this);
			this.order = null;
			for (CombatUnit unit : units)
			{
				unit.cancelTarget();
			}
		}		
	}
	
	public void update (CraftPlannerBlackboard blackboard)
	{		
		for (CombatUnit unit : units)
		{
			switch (unit.status)
			{						
				case Dead:
					units.remove(unit);
					break;
			}
		}
		if (units.size() == 0)
		{
			cancelOrders();
			status = SquadStatus.Dead;
		}
		
		switch (status)
		{
			case Idle:
				boolean hasOrders = false;
				for (MilitaryOrder order : blackboard.getMilitaryQueue())
				{
					if (order.status != MilitaryOrderStatus.Finished)
					{
						this.order = order;
						for (CombatUnit unit : units)
						{
							unit.setTarget(this.order.target);
						} 
						hasOrders = true;
						this.order.addSquad(this);
						status = SquadStatus.Attacking;
						break;
					}
				}
				if (!hasOrders)
				{
					for (CombatUnit unit : units)
					{
						unit.cancelTarget();
					} 
				}
				break;
				
			case Attacking:
				for (CombatUnit unit : units)
				{
					unit.update();
				}
				this.order.update();
				if (this.order.status == MilitaryOrderStatus.Finished)
				{
					cancelOrders();
					status = SquadStatus.Idle;
				}
				break;
	
		}
	}
	
	public boolean sameType (UnitType typeToCheck)
	{
		if (typeToCheck == this.units.get(0).getUnit().getType())
		{
			return true;
		}
		return false;
	}
	
	public void addUnit (CombatUnit unitToAdd)
	{
		units.add(unitToAdd);
	}
	
	public boolean isFull ()
	{
		if (units.size() >= 9)
		{
			return true;
		}
		return false;
	}
}
