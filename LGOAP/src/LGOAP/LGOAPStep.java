package LGOAP;

import Agent.*;
import searchEngine.Step;

//LGOAPStep represents a single step in a plan
public class LGOAPStep extends Step
{
	public WorldState stepState;

	public WorldState getWorldState() {
		return stepState;
	}
}
