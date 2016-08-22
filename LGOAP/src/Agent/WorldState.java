package Agent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import LGOAP.LGOAPAction;


//World state stores a list of facts about the game world in a hashmap
//It describes several operations which manipulate the hashmap
public class WorldState implements Cloneable
{
	//This is the world state map
	private Map<String, WorldStateProperty> properties;
	
	//Default constructor creates a new world state
	public WorldState ()
	{
		properties = new HashMap<String, WorldStateProperty>();
	}
	
	//Constructor that creates a copy of the passed world state
	public WorldState (WorldState other)
	{
		properties = new HashMap<String, WorldStateProperty>();		
		copy(other);
	}
	
	//Returns a copy of this world state
	public WorldState clone ()
	{
		WorldState clone = new WorldState();
		clone.copy(this);
		return clone;
	}
	
	//Returns true if this world state contains the passed key
	public boolean containsKey (String key)
	{
		return properties.containsKey(key);
	}
	
	//Gets this world states properties
	public Map<String, WorldStateProperty> getProperties ()
	{
		return properties;
	}
	
	//Sets a property of this world state
	public void setProperty (String key, WorldStateValue value)
	{
		properties.put(key, new WorldStateProperty(key, value));
	}
	
	//Gets the list of values of this world states properties
	public List<WorldStateProperty> getValues ()
	{
		return new ArrayList<WorldStateProperty>(properties.values());
	}
	
	//Sets the value of one of this world states properties specified by the key
	public <T> void setValue (String key, T value)
	{
		properties.get(key).value.setValue(value);
	}
	
	//Gets the value of the property from the world state given the passed key
	public <T> WorldStateValue<T> getPropertyValue (String key)
	{
		if (!properties.containsKey(key))
		{
			//Handle error
			return new WorldStateValue(false); //This is not correct! Needs fixing later
		}
		return properties.get(key).value;
	}
	
	//Copies the passed world state
	public void copy (WorldState other)
	{
		resetProperties();
		//Iterate through other state and clone it's properties into this states properties
		Collection<WorldStateProperty> otherProperties = other.properties.values();
		for (WorldStateProperty property : otherProperties)
		{
			properties.put(property.key, property.clone());
		}
	}
	
	//Copies the passed world state
	public void partialCopy (WorldState other, LGOAPAction action, boolean isGoal)
	{
		resetProperties();
		//Iterate through other state and clone it's properties into this states properties
		Collection<WorldStateProperty> otherProperties = other.properties.values();
		for (WorldStateProperty property : otherProperties)
		{
			properties.put(property.key, property.clone());
		}
		
		if (isGoal)
		{
			for (WorldStateProperty effectProp : action.effects.properties.values())
			{				
				Object oPropValue = properties.get(effectProp.key).value.getValue();
				Object oActionValue = effectProp.value.getValue();

				int actionValue = ((Integer) oActionValue).intValue();
				int propValue = ((Integer) oPropValue).intValue();	
				int newPropValue = propValue - actionValue;
				
				properties.get(effectProp.key).value = new WorldStateValue(newPropValue);
			}
		}
		else
		{
			for (WorldStateProperty effectProp : action.effects.properties.values())
			{			
				Object oPropValue = properties.get(effectProp.key).value.getValue();
				Object oActionValue = effectProp.value.getValue();			

				int actionValue = ((Integer) oActionValue).intValue();
				int propValue = ((Integer) oPropValue).intValue();	
				int newPropValue = propValue + actionValue;
				
				properties.get(effectProp.key).value = new WorldStateValue(newPropValue);
			}
		}
	}
	
	//Resets the properties of this world state
	public void resetProperties ()
	{
		properties.clear();
	}
	
	//Gets the number of differences between this state and the passed state
	public int getDifferences (WorldState other)
	{
		int differences = 0;
		Collection<WorldStateProperty> thisProperties = properties.values();
		for (WorldStateProperty thisProperty : thisProperties)
		{
			if (!(other.containsKey(thisProperty.key)))
			{
				differences += 1;
			}
			else
			{
				if (!(other.getPropertyValue(thisProperty.key).equals(thisProperty.value)))
				{
					differences += 1;
				}
			}			
		}
		Collection<WorldStateProperty> otherProperties = other.properties.values();
		for (WorldStateProperty otherProperty : otherProperties)
		{
			if (!(properties.containsKey(otherProperty.key)))
			{
				differences += 1;
			}
		}
		
		return differences;
	}

	//Gets the number of unsatisfied properties of this world state given the passed world state???
	public int getUnsatisfiedProperties (WorldState other)
	{
		int unsatisfied = 0;
		Collection<WorldStateProperty> thisProperties = properties.values();
		for (WorldStateProperty thisProperty : thisProperties)
		{
			if (!other.containsKey(thisProperty.key))
			{
				unsatisfied += 1;
			}
			else if (!thisProperty.value.equals(other.getPropertyValue(thisProperty.key)))
			{
				unsatisfied += 1;
			}
		}
		return unsatisfied;
	}
	
	public String toString ()
	{
		String propString = "";
		for (String key : properties.keySet())
		{
		    propString += properties.get(key).toString();
		}
		return propString;
	}
}
