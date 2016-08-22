package Agent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Comparator;

//Working memory represents an agents memory and is used to store facts about the world
//These facts are then used to create world states for the state space search
public class WorkingMemory
{
	//List of working memory facts
	private List<WorkingMemoryFact> facts;
	
	//Creates a working memory
	public WorkingMemory ()
	{
		this.facts = new ArrayList<WorkingMemoryFact>();
	}
	
	//Adds the passed fact to the working memory
	public void addFact (WorkingMemoryFact fact)
	{
		facts.add(fact);
	}
	
	//Removes the passed fact from the working memory
	public void removeFact (WorkingMemoryFact fact)
	{
		facts.remove(fact);
	}
	
	//Returns a sorted array of working memory facts, comparator implemented in extension
	public WorkingMemoryFact[] getFactsByType (FactType type, Comparator<WorkingMemoryFact> sortBy)
	{
		List<WorkingMemoryFact> matchingFacts = new ArrayList<WorkingMemoryFact>();
		for (WorkingMemoryFact fact : facts)
		{
			if (fact.getType() == type)
			{
				matchingFacts.add(fact);
			}
		}
		WorkingMemoryFact[] matchingFactArr = new WorkingMemoryFact[matchingFacts.size()];
		for (int i = 0; i < matchingFacts.size(); i++)
		{
			matchingFactArr[i] = matchingFacts.get(i);
		}
		if (sortBy != null)
		{
			Arrays.sort(matchingFactArr, sortBy);
		}
		return matchingFactArr;
	}
	
	//Returns a specific fact with the type
	public <T> WorkingMemoryFact getSpecificFact (FactType type, T gameObject)
	{
		for (WorkingMemoryFact fact : facts)
		{
			if (fact == gameObject)
			{
				return fact;
			}
		}
		return null;
	}
	
	//Removes all facts of the given type from the working memory
	public void clearFactsByType (FactType type)
	{
		List<WorkingMemoryFact> matchingFacts = new ArrayList<WorkingMemoryFact>();
		for (WorkingMemoryFact fact : facts)
		{
			if (fact.getType() == type)
			{
				matchingFacts.add(fact);
			}
		}
		for (WorkingMemoryFact fact : matchingFacts)
		{
			facts.remove(fact);
		}
	}
}
