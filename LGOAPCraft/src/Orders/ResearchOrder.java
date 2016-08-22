package Orders;

import java.util.List;
import bwapi.TechType;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;

public class ResearchOrder
{	
	public TechType techType;
	public ResearchOrderStatus status;
	
	public ResearchOrder (TechType techType)
	{
		this.techType = techType;
		status = ResearchOrderStatus.NotStarted;
	}
}
