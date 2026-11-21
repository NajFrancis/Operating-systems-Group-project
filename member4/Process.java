//Name: Daynea Chambers
//Id: 2400660
package member4;

import java.util.Random;

public class Process
{
	private int p_id;
	private String resource;
	
	public Process()
	{
	  this.p_id=0;
	}
	public Process(int p_id)
	{
	  this.p_id=p_id;
	
	}
	public String getresourceRequired()
	{
	  return resource;
	}
	public void setresourceRequired(String resource)
	{
	  this.resource=resource;
	
	}
	public int generateProcessID()
	{
	  Random rand=new Random();
	  this.p_id=rand.nextInt();
	  return p_id;
	}
	public static void main(String[] args)
	{
	  Process p=new Process();
	  int process=p.generateProcessID();
	  String resource=p.getresourceRequired();
	  System.out.println("Process ID:" + process + "Resource: "+ resource);
	
	
	}

}
