package searchEngine;

import java.util.List;

public abstract class Node
{
	//Costs
	public float g;
	public float h;
	public float f;
	
	//Parent node
	public Node parent;	
	
	public Node ()
	{
		g = 0;
		h = 0;
		f = 0;
		parent = null;
	}
	
	public abstract List<Node> getNeighbours ();
	
}
