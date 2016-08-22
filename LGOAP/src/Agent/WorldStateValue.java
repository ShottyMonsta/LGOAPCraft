package Agent;

//World state value describes the actual value of a world state property
//It uses generics so that the value can be of any type
//For instance health of an enemy may be an integer, whereas the state of a building may be a bool
public class WorldStateValue<T> implements Cloneable
{
	protected T value;
	private Class<T> type;
	
	//Default constructor creates a null value
	public WorldStateValue ()
	{
		this(null);
	}
	
	//Constructor creates a value with the passed value
	public WorldStateValue(T value)
	{
		this.value = value;
	}
	
	//Gets the value
	public T getValue ()
	{
		return this.value;
	}

	//Sets the value
	public void setValue (T value)
	{
		this.value = value;
	}
	
	//Returns true if the passed object has equality to this object
	public boolean equals (Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		WorldStateValue other = (WorldStateValue) obj;
		if (value == null)
		{
			if (other.value != null)
			{
				return false;
			}
		}
		else if (!value.equals(other.value))
		{
			return false;
		}
		return true;
	}
	
	public boolean isSameType (Object obj)
	{
		if (obj.getClass() == value.getClass())
		{
			return true;
		}
		return false;
	}
	
	//Clones this object, throws exception if not possible
	public Object clone ()
	{
		try 
		{
			return super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			System.out.println("Clone not supported!");
		}
		return null;
	}
	
	public String toString ()
	{
		Object val = (Object) value;
		return val.toString();
	}
}
