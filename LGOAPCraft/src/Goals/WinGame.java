package Goals;

import java.util.List;

import Agent.*;
import LGOAP.*;
import bwapi.Game;

//This is an example implementation of a goal
//Must implement the updateRelevance method
//Must have a default relevance
public class WinGame extends LGOAPGoal
{
	private Agent agent;
	private Game game;
	private int maxTime;
	
	//Create an instance of the Kill Target goal
	public WinGame (Agent agent, Game game)
	{
		this.agent = agent;
		this.game = game;
		this.goalState.setProperty("HasLaunchedAttack", new WorldStateValue (true));
		this.relevance = 1.0f;
	}

	//Procedurally update the relevance of this goal based on the game situation
	@Override
	public void updateRelevance() 
	{
		
	}
	
}
