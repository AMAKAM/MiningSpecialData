package trial1_File_read;
import java.io.File;
import java.util.Formatter;
import java.util.Random;


public class Filewrite {
	public
	Formatter filer; 
    

	public void filcreate(String file_name) 
    {
	File	path= new File(file_name);
	System.out.println(path.exists());
	//if(!path.exists())
	try{	
   	filer= new Formatter (path);
    System.out.println("yes");
    }
    catch(Exception e)  
    {
    	System.out.println("no");}
    } 
    
	public void adddata(String s)
    {
	
			filer.format("%s\n",s);
    }
	public void adddata_without_new_line(String s)
    {
	
			filer.format("%s",s);
    }

	
	public void adddata()
    {
		
		for(int i=0;i<1000;i=i+8)
			filer.format("%d %f \n",i , i* Math.random() );
       
		
		/*
    	filer.format("%s %s %s \n", "Arjun" ,"21","8.5" );
        filer.format("%s %s %s \n", "Arjunkar" ,"21","7.8" );
        filer.format("%s %s %s \n", "vinoth" ,"21","6.5" );
        filer.format("%s %s %s \n", "karthi" ,"21","7" );
        filer.format("%s %s %s \n", "karthi" ,"21","7" );
       */ 
    }
	public void close()
	{
	filer.close();
	}

}
