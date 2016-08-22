package LGOAP;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Metrics extends Frame
{
	private Panel panel;
	private TextArea planArea;
	private TextArea executionArea;
	private TextArea resourceArea;
	private TextArea statsArea;
	private String plan;
	private String planExecution;
	private int unitsCreated;
	private String uCreated;
	private int unitsKilled;
	private String uKilled;
	private int unitsLost;
	private String uLost;
	private int buildingsCreated;
	private String bCreated;
	private int buildingsDestroyed;
	private String bDestroyed;
	private int buildingsLost;
	private String bLost;
	private int mineralsCollected;
	private String mCollected;
	private int gasCollected;
	private String gCollected;
	
	public Metrics ()
	{
		setLayout(new FlowLayout());
		
		plan = "";
		planExecution = "";
		unitsCreated = 0;
		uCreated = "Units created: " +unitsCreated +"\n";
		unitsKilled = 0;
		uKilled = "Units killed: " +unitsKilled +"\n";
		unitsLost = 0;
		uLost = "Units lost: " +unitsLost +"\n";
		buildingsCreated = 0;
		bCreated = "Buildings created: " +buildingsCreated +"\n";
		buildingsDestroyed = 0;
		bDestroyed = "Buildings destroyed: " +buildingsDestroyed +"\n";
		buildingsLost = 0;
		bLost = "Buildings lost: " +buildingsLost +"\n";
		mineralsCollected = 0;
		mCollected = "Minerals collected: " +mineralsCollected +"\n";
		gasCollected = 0;
		gCollected = "Gas collected: " +gasCollected +"\n";
		
		planArea = new TextArea(plan);
		executionArea = new TextArea(planExecution);
		resourceArea = new TextArea(mCollected +gCollected);
		statsArea = new TextArea(uCreated +uLost +uKilled +bDestroyed +bLost);
		
		Label planTitle = new Label("Planner: ");
		add(planTitle);
		add(planArea);
		Label executionTitle = new Label("Plan Execution: ");
		add(executionTitle);
		add(executionArea);
		Label resourceTitle = new Label("Resources: ");
		add(resourceTitle);
		add(resourceArea);
		Label statsTitle = new Label("Statistics: ");
		add(statsTitle);
		add(statsArea);
		
		
		setTitle("Metrics");
		setSize(520, 870);
		setVisible(true);
	}
	
	public void createBuilding ()
	{
		buildingsCreated += 1;
		bCreated = "Buildings created: " +buildingsCreated +"\n";
		statsArea.setText(uCreated +uLost +uKilled +bCreated +bDestroyed +bLost);
	}
	
	public void destroyBuilding ()
	{
		buildingsDestroyed += 1;
		bDestroyed = "Buildings destroyed: " +buildingsDestroyed +"\n";
		statsArea.setText(uCreated +uLost +uKilled +bCreated +bDestroyed +bLost);
	}
	
	public void loseBuilding ()
	{
		buildingsLost += 1;
		bLost = "Buildings lost: " +buildingsLost +"\n";
		statsArea.setText(uCreated +uLost +uKilled +bCreated +bDestroyed +bLost);
	}
	
	public void createUnit ()
	{
		unitsCreated += 1;
		uCreated = "Units created: " +unitsCreated +"\n";
		statsArea.setText(uCreated +uLost +uKilled +bCreated +bDestroyed +bLost);
	}
	
	public void killUnit ()
	{
		unitsKilled += 1;
		uKilled = "Units killed: " +unitsKilled +"\n";
		statsArea.setText(uCreated +uLost +uKilled +bCreated +bDestroyed +bLost);
	}
	
	public void loseUnit ()
	{
		unitsLost += 1;
		uLost = "Units lost: " +unitsLost +"\n";
		statsArea.setText(uCreated +uLost +uKilled +bCreated +bDestroyed +bLost);
	}
	
	public void updateResources (int minerals, int gas)
	{
		gasCollected = gas;
		mineralsCollected = minerals;
		gCollected = "Gas collected: " +gasCollected +"\n";
		mCollected = "Minerals collected: " +mineralsCollected +"\n";
		resourceArea.setText(mCollected +gCollected);
	}
	
	public void addToPlan (String planToAdd)
	{
		plan += planToAdd;
		planArea.setText(plan);
	}
	
	public void addToExecution (String stepToAdd)
	{
		planExecution += stepToAdd;
		executionArea.setText(planExecution);
	}
	
	public void makeOutput()
	{
		String fileExtension = "_";
		int newFileIndex = 0;
		
		File folder = new File("C:/Users/ShottyMonsta/Desktop/CraftPlanner/Output/Plans");
		File[] listOfFiles = folder.listFiles();
	    for (int i = 0; i < listOfFiles.length; i++)
	    {
	    	if (listOfFiles[i].isFile())
			{
	    		newFileIndex += 1;
			}
	    }
	    fileExtension += newFileIndex;
		
		PrintWriter writer;
		try
		{			
			writer = new PrintWriter("C:/Users/ShottyMonsta/Desktop/CraftPlanner/Output/Plans/plan" +fileExtension +".txt", "UTF-8");
			for (String part : plan.split("\n"))
			{
				writer.println(part);
			}			
			writer.close();
			
			writer = new PrintWriter("C:/Users/ShottyMonsta/Desktop/CraftPlanner/Output/Execution/execution" +fileExtension +".txt", "UTF-8");
			for (String part : planExecution.split("\n"))
			{
				writer.println(part);
			}	
			writer.close();
			
			writer = new PrintWriter("C:/Users/ShottyMonsta/Desktop/CraftPlanner/Output/Resources/resources" +fileExtension +".txt", "UTF-8");
			for (String part : resourceArea.getText().split("\n"))
			{
				writer.println(part);
			}	
			writer.close();
			
			writer = new PrintWriter("C:/Users/ShottyMonsta/Desktop/CraftPlanner/Output/Stats/stats" +fileExtension +".txt", "UTF-8");
			for (String part : statsArea.getText().split("\n"))
			{
				writer.println(part);
			}	
			writer.close();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
