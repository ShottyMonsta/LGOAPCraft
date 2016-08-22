package Agent;

//This is an atomic fact about the game world, they are aggregated in the working memory class
public class WorkingMemoryFact <T>
{
	private FactType type;
	private T fact;
	
	//Creates a working memory fact
	public WorkingMemoryFact (FactType type, T fact)
	{
		this.type = type;
		this.fact = fact;
	}
	
	//Returns the type of fact
	public FactType getType ()
	{
		return type;
	}
	
	//Returns the fact
	public T getFact ()
	{
		return fact;
	}
}
