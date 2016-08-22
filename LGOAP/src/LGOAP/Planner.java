package LGOAP;

import Agent.*;

//This is a planner, it is layered to create an LGOAP
public class Planner
{
	public LGOAPUtils utils;
	public LGOAPEngine engine;
	public Agent agent;
	public LGOAPActionManager actionManager;
	int layer;
	
	//Creates a new planner
	public Planner (Agent agent, int layer)
	{
		this.agent = agent;
		this.layer = layer;
		utils = new LGOAPUtils(agent);
		engine = new LGOAPEngine (utils, agent);
		actionManager = new LGOAPActionManager();		
	}
	
	public LGOAPActionManager getActionManager ()
	{
		return actionManager;
	}
	
	public void addAction (LGOAPAction actionToAdd)
	{
		actionManager.addAction(actionToAdd);
	}
	
	//Returns a plan given the passed constraints
	public Plan getPlan (LGOAPGoal constraint)
	{
		utils.resetLists();
		utils.setGoal(constraint);
		Plan plan = (Plan) engine.makePath(new LGOAPNode(agent, this));
		
		return plan;
	}
}
