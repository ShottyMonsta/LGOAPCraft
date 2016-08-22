package searchEngine;

import LGOAP.LGOAPNode;

public abstract class Engine
{
	public Utils utils;
	
	public Engine (Utils utils)
	{
		this.utils = utils;
	}	
		
	//TODO make the A* algorithm more efficient
	//Currently a bit of a mess!
	public Path makePath (Node start)
	{			
		start.g = 0;
		start.h = utils.getHeuristicDistance(start, true);
		start.f = start.g + start.h;
		utils.addOpenList(start);
		Node current = utils.getCheapestOpen();
		
		LGOAPNode LCurrent = (LGOAPNode) current;
		
		while (!utils.isFinished(current))
		{
			current = utils.getCheapestOpen();
			//System.out.println("Current action taken: " +((LGOAPNode)current).actionTaken);
			//System.out.println("Current = " +current);
			//END CASE
			if (utils.isReached(current))
			{
				return createPlan(current);
			}
			//NORMAL CASE
			else
			{
				//System.out.println("Goal was not reached!");
				utils.removeOpenList(current);
				utils.addClosedList(current);
				for (Node neighbour : current.getNeighbours())
				{
					if (utils.inClosedList(neighbour))
					{
						continue;
					}
					else
					{
						//GOAL CODE NEEDS CHECKING!!!! 
						float tentativeGScore = utils.getEdgeCost(neighbour);
						
						if (!utils.inOpenList(neighbour))
						{
							utils.addOpenList(neighbour);
						}
						else if (tentativeGScore >= neighbour.g)
						{
							continue;
						}
						
						neighbour.parent = current;
						neighbour.g = tentativeGScore;
						neighbour.h = utils.getHeuristicDistance(neighbour, false);
						neighbour.f = neighbour.g + neighbour.h;
					}
				}
			}
		}		
		//FAILED TO FIND PATH!! HANDLE ERROR
		//System.out.println("Failed to find a valid plan..");
		return null;
	}
	
	public abstract Path createPlan (Node goal);
	
	
}
