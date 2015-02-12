package trial1_File_read;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;


public class Fileread 
{
	private Scanner scan;

	
public Fileread(String file) throws Exception
{
	 scan = new Scanner(new File(file)); 	
}
	



public ArrayList<ArrayList<String>> readdata() throws Exception
{	
int j,i=0;
ArrayList<ArrayList<String>> array= new ArrayList<ArrayList<String>>(); 
{
StringBuffer buffer=null;
buffer = new StringBuffer(scan.nextLine());
Scanner trial = new Scanner(buffer.toString());
j=0;
ArrayList<String> col= new ArrayList<String>();
	while(trial.hasNext())
	{
	String s = trial.next();
	if(i<2)
	if((!s.equalsIgnoreCase("<")) && (!s.equalsIgnoreCase(">")) && (!s.equalsIgnoreCase("[")) && (!s.equalsIgnoreCase("]")) )
	col.add(j++, s);		
	}
	//trial.close();
	array.add(i++, col);
	}
	{
	StringBuffer buffer=null;
	buffer = new StringBuffer(scan.nextLine());
	Scanner trial = new Scanner(buffer.toString());
	j=0;
	ArrayList<String> col= new ArrayList<String>();
	while(trial.hasNext())
	{
	String s = trial.next();
	if((!s.equalsIgnoreCase("<")) && (!s.equalsIgnoreCase(">")) && (!s.equalsIgnoreCase("[")) && (!s.equalsIgnoreCase("]")) )
	{		
	col.add(j++, s);	
	}
		
	}
	//trial.close();
	array.add(i++, col);
	}
while(scan.hasNextLine())
{
	StringBuffer buffer=null;
	buffer = new StringBuffer(scan.nextLine());
	Scanner trial = new Scanner(buffer.toString());
	j=0;
	ArrayList<String> col= new ArrayList<String>();
	while(trial.hasNext())
	{
		String s = trial.next();
		col.add(j++, s);	
			
	}
	trial.close();
	array.add(i++, col);
}
return array;
}

public void close()
{
//scan.close();
}

}

