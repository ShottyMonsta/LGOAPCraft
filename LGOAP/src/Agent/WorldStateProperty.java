package Agent;

//World state property describes any property about the world state
//It uses generics so that a property can be described with any type
public class WorldStateProperty<T>
{
	//The key for accessing the property
	public String key;
	//The value of the property
	public WorldStateValue<T> value;
	
	//Default constructor creates an empty/null property
	public WorldStateProperty ()
	{
		this ("", null);
	}
	
	//Constructor for creating a property with specified key and value
	public WorldStateProperty(String key, WorldStateValue<T> value)
	{
		this.key = key;
		this.value = value;
	}
	
	//Clones this property
	public WorldStateProperty<T> clone ()
	{
		WorldStateProperty<T> newProperty = new WorldStateProperty();
		newProperty.key = new String (key);
		newProperty.value = (WorldStateValue<T>)value.clone();
		return newProperty;
	}
	
	public String toString ()
	{
		return key +", " +value.toString() +".\n";
	}
	
}
