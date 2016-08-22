package Orders;

import java.util.ArrayList;
import java.util.List;

import GameModel.CombatUnit;
import GameModel.Squad;
import bwapi.TilePosition;
import bwapi.Unit;
import bwta.BWTA;

public class MilitaryOrder
{
	public TilePosition target;
	public MilitaryOrderStatus status;
	public List<Squad> squads;
	
	public MilitaryOrder (TilePosition target)
	{		
		this.target = target;
		this.status = MilitaryOrderStatus.Uncompleted;
		squads = new ArrayList<Squad>();
		squads.clear();
	}
	
	public void update ()
	{
		switch (status)
		{
			case Uncompleted:
				int squadsAtLocation = 0;
				for (Squad squad : squads)
				{
					int combatUnitsAtLocation = 0;
					for (CombatUnit unit : squad.units)
					{
						if (unit.getUnit().getDistance(target.toPosition()) <= 250)
						{
							combatUnitsAtLocation += 1;
						}
					}
					if (combatUnitsAtLocation == squad.units.size())
					{
						squadsAtLocation += 1;
					}
				}
				if (squadsAtLocation == squads.size())
				{
					status = MilitaryOrderStatus.Finished;
				}
		}
	}	
	
	public void addSquad (Squad squadToAdd)
	{
		squads.add(squadToAdd);
	}
	
	public void removeSquad (Squad squadToRemove)
	{
		squads.remove(squadToRemove);
	}
}
