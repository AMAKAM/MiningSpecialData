package trial1_File_read;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Start 
{   
	
	static boolean yep_file_input_recieved=true;
	static float alpha;
	static ArrayList<ArrayList<String>> array= new ArrayList<ArrayList<String>>();
	static ArrayList<ArrayList<String>> data_table= new ArrayList<ArrayList<String>>();
	static ArrayList<ArrayList<Set<Integer>>> attribute_value_sets = new ArrayList <ArrayList<Set<Integer>>>(); 
	static ArrayList<ArrayList<Integer>> cardinality_list = new ArrayList <ArrayList<Integer>>(); 
	static ArrayList<ArrayList<Set<Integer>>> concept_value_sets = new ArrayList <ArrayList<Set<Integer>>>(); 
	static ArrayList<ArrayList<String>> attribute_string_list= new ArrayList<ArrayList<String>>();		
	static ArrayList<ArrayList<String>> concept_string_list= new ArrayList<ArrayList<String>>();		
	
	static ArrayList<Set<Integer>> attribute_star = new ArrayList<Set<Integer>>();
	static ArrayList <Set<Integer>> characteristic_set = new ArrayList<Set<Integer>>();
	static ArrayList <String>  column_heading =new ArrayList <String>();
	
	static ArrayList<ArrayList <ArrayList<Set<Integer>>>> concept_rule_set= new ArrayList<ArrayList<ArrayList<Set<Integer>>>>();
	static ArrayList<ArrayList<ArrayList<String>>> concept_rule_set_string= new  ArrayList<ArrayList<ArrayList<String>>>();
	static ArrayList<ArrayList <Set<Integer>>> concept_intersection_of_rule = new ArrayList<ArrayList<Set<Integer>>>();
	static ArrayList<ArrayList<ArrayList<Integer>>> concept_found_rows = new ArrayList<ArrayList<ArrayList<Integer>>>();
	static ArrayList<ArrayList<ArrayList<Integer>>> concept_found_column = new ArrayList<ArrayList<ArrayList<Integer>>>();	
	static ArrayList<Set<Integer>> concept_could_not_find = new ArrayList<Set<Integer>>();
	static ArrayList<Set<Integer>> concept_found_cases_so_far = new ArrayList<Set<Integer>>();
	
	
	
	static File File_elementary_sets;
	static File File_Rules_set;
	static BufferedWriter bw;
	static FileWriter fw ;
	
	
	static BufferedWriter bw1;
	static FileWriter fw1 ;
	
	Set<Integer> found_cases_so_far = new LinkedHashSet <Integer>();
		
	
	static ArrayList <Boolean> is_column_a_numeric_value = new ArrayList <Boolean>(); 
	static ArrayList <Boolean> is_column_a_symbolic_value = new ArrayList <Boolean>(); 
	static ArrayList<ArrayList<Float>> cutpoints =new ArrayList<ArrayList<Float>>();
	static int no_of_columns;
	static int no_of_rows;	
	static int no_of_attributes;

	
	public static void main(String[] args) throws Exception
	{

    	Fileread f = new Fileread(read_file_name());	
    	File_Rules_set = new File(get_file_name("Rules Set"));
    
									// creating the new output file
		if (!File_Rules_set.exists()) {
			File_Rules_set.createNewFile();
		}
		fw= new FileWriter(File_Rules_set.getAbsoluteFile());
		bw = new BufferedWriter(fw);
		get_elementary_sets();
		System.out.println("The value of alpha less than 1.00");
		boolean matched=false;
		do
		{
			Scanner scaner=new Scanner(System.in);
			String input=scaner.next();
			Pattern p1 = Pattern.compile("(^(((-)|(\\+)){0,1}))(\\d+)(((\\.)?(\\d+)){0,1}$)");
			Matcher match=p1.matcher(input);
			if(match.find())
			{
				if(Float.parseFloat(input)<(float)(1.0))
					matched=true;	
				alpha=Float.parseFloat(input);
			}
			else
				System.out.println("Please enter only a float value");
		}while(!matched);
		
		//Fileread f = new Fileread("t.txt");
		//Fileread f = new Fileread("teat.txt");
		//Fileread f = new Fileread("datafile.txt");		
		array= f.readdata();
		//f.close();
		
		//print_data_from_file_in_array();
		//System.out.println("one down 6 more to go");
		
		calculate_initilaize_table_from_recieved_data();	//print_data_from_file_in_array();
		//print_data_table();
		calculate_numeric_values_and_symbolic_values();
		//print_Numeric_data();	
		//System.out.println("two down five more to go");
		
		calculate_attribute_pairs();						//calculate_attribute_pairs();
		print_attribute_pairs();													
		//System.out.println("three down four more to go");
		
		calculate_cardinality_sets();						//calculate_cardinality_sets();
		//print_cardinality_list();	
		//System.out.println("four down three more to go");
		
		calculate_concept_value_pairs();
		//calculate_concept_value_pairs();
		System.out.println();
		print_concept_value_pairs();
		//System.out.println("five down 2 more to go");
		//calculate_characteristic_sets();
		calculate_A_star();	 							//calculate the set of cases that are indistinguishable  				
		if(yep_file_input_recieved==true)
			print_A_star();

		//calculate_characteristic_sets_and_A_star();					//calculate characteristic sets
		//calculate_A_star();
		//System.out.println("six down 1 more to go");
		//print_characteristic_sets();
		//print_concept_value_pairs();
		for(int i=0;i<concept_value_sets.get(0).size();i++)	
			find_rule(i);
		System.out.println("DONE");
		print_concept_rules();
		prepare_file_write();		
		
		calculate_redundancy();
		System.out.println("halt");	
	 }
	
	public static String read_file_name() throws Exception	
	{	
	String s=null;//test string
	boolean t =true;
	if(t){	
		Scanner scan_string = new Scanner(System.in); //to read a string(file) from user
		boolean file_exists=false; //tells if the string entered is actually a valid file
			while(file_exists!=true)
			{	
				System.out.println("Please Enter the file name ");
				System.out.println("if the textfile is named \"datafile\" enter datafile.txt ");	
				s=scan_string.next();
				File f= new File(s);
				file_exists=f.exists();
				if(file_exists==true)
					System.out.println("The File exits");
				else
					System.out.println("Sorry file does not exist");			
			}	
			//scan_string.close();
			System.out.println("Started computation");
			}	
	return s;
	}	
	
	//print the array table (has heading also has < a a a d > prototype)	
	public static void print_data_from_file_in_array()
	{
	  	for (int i =0;i<array.size();i++)
		{
			for(int j=0;j<array.get(i).size();j++)
				System.out.printf("%-20s", array.get(i).get(j));
			System.out.println();
		}	
	System.out.println();
	System.out.println();
		
	}
	
	
	//data_table has only data  (has heading but not  < a a a d > prototype)
	//so always start with rows =1
	//for loop to assign data_table	
	//no of columns,rows are set here
	public static void calculate_initilaize_table_from_recieved_data()
	{	
		no_of_columns=array.get(0).size();
	  for (int i =1;i<array.size();i++)
			{	
				ArrayList<String> temp= new ArrayList<String>();
				for(int j=0;j<no_of_columns;j++)
				{	
					temp.add(j,array.get(i).get(j).toString());
				}	
			data_table.add(i-1, temp);	
			}		
		no_of_rows=data_table.size();
		no_of_attributes=no_of_columns-1;
		
	}

	
	//print data_table
	//so always start with rows =1
	public static void print_data_table()
	{
	for (int i =1;i<data_table.size();i++)
	{
		for(int j=0;j<data_table.get(i).size();j++)
			System.out.printf("%-20s",data_table.get(i).get(j));
		System.out.println();
	}
	System.out.println();
	System.out.println();
	
	}
	
	
	public static void calculate_numeric_values_and_symbolic_values()
	{
		Pattern p = Pattern.compile("(^(((-)|(\\+)){0,1}))(\\d+)(((\\.)?(\\d+)){0,1}$)");
		boolean for_loop_breaking;
		for(int j=0;j<no_of_attributes;j++)
		{
			for_loop_breaking=false;
			for(int i=1;i<no_of_rows && !for_loop_breaking; i++)
			{
				Matcher match =p.matcher(data_table.get(i).get(j));
				if(!match.find() )
				{
					System.out.println("Column"+i+" "+data_table.get(i).get(j)+" ");
					for_loop_breaking=true;
				}
				else
				{
					
				}
			}
			if(!for_loop_breaking)
			{
				is_column_a_numeric_value.add(j, true);
				is_column_a_symbolic_value.add(j, false);
			}
			else
			{
				is_column_a_numeric_value.add(j, false);
				is_column_a_symbolic_value.add(j, true);
			}
				
		}
		}
	
	
	static void print_Numeric_data() 
	{
	System.out.println("Numeric data");
	for(int j=0;j<is_column_a_numeric_value.size();j++)
		System.out.println("Column: "+j+"\t"+is_column_a_numeric_value.get(j));
	}
	static void print_Symbolic_data() 
	{
	System.out.println("Symbolic data");
	for(int j=0;j<is_column_a_symbolic_value.size();j++)
		System.out.println("Column: "+j+"\t"+is_column_a_symbolic_value.get(j));
		//is_column_a_numeric_value 	
	}
	
	
	
	//Calculate the attribute value pairs
	public static void calculate_attribute_pairs()
	{
		for(int j=0;j<no_of_attributes;j++)
			{	
				if(is_column_a_numeric_value.get(j))
				{
					Set<Float> temp_cut_points =new HashSet<Float>();
					for(int k=1;k<no_of_rows;k++)	
						temp_cut_points.add(Float.parseFloat(data_table.get(k).get(j)));
					ArrayList<Float> floats_in_order=new ArrayList<Float>();
					for(Float w:temp_cut_points)
						floats_in_order.add(w);
					Collections.sort(floats_in_order);
					System.out.println(floats_in_order);
					ArrayList<String> temp_string_list =new ArrayList<String>();
					ArrayList<Float> cut_points_in_order=new ArrayList<Float>();
					if(floats_in_order.size()>1)
						{
						for(int k=0;k<floats_in_order.size()-1;k++)
							cut_points_in_order.add((floats_in_order.get(k)+floats_in_order.get(k+1))/2);
						for(int k=0;k<cut_points_in_order.size();k++)
							{
								temp_string_list.add(floats_in_order.get(0)+".."+cut_points_in_order.get(k));
								temp_string_list.add(cut_points_in_order.get(k)+".."+floats_in_order.get(floats_in_order.size()-1));
							}		
						}
					else
						{
							cut_points_in_order.add(floats_in_order.get(0));
							temp_string_list.add(floats_in_order.get(0).toString());
						}
					
					System.out.println(cut_points_in_order);
					//for(int i=0;i<temp_string_list.size();i++)
						//System.out.println(temp_string_list.get(i));
				
					ArrayList<Set<Integer>> attdribute_value_sets = new ArrayList<Set<Integer>>();					
					ArrayList<Set<Integer>> concept_column= new ArrayList<Set<Integer>>();
					for(int k=0;k<cut_points_in_order.size();k++)
						{
							{
							//System.out.print("<"+cut_points_in_order.get(k)+" ");	
							Set<Integer> concept_set = new LinkedHashSet<Integer>();
								for(int w=1;w<no_of_rows;w++)
									if(Float.parseFloat(data_table.get(w).get(j))<cut_points_in_order.get(k))
										concept_set.add(w);
							concept_column.add(concept_set);				
							attribute_value_sets.add(j,concept_column);
							//System.out.println(concept_column);
							}
							{
							//System.out.print(">"+cut_points_in_order.get(k)+" ");			
							//ArrayList<Set<Integer>> concept_column= new ArrayList<Set<Integer>>();		
							Set<Integer> concept_set = new LinkedHashSet<Integer>();
							for(int w=1;w<no_of_rows;w++)
									if(Float.parseFloat(data_table.get(w).get(j))>cut_points_in_order.get(k))
									{
										concept_set.add(w);
									}
							concept_column.add(concept_set);
							attribute_value_sets.add(j,concept_column);		
							//System.out.println(concept_column);
							}		
						}
						attribute_string_list.add(j,temp_string_list);
						cutpoints.add(j,cut_points_in_order);
				System.out.println(attribute_string_list.get(j)+" "+attribute_value_sets.get(j));
				}
				if(!is_column_a_numeric_value.get(j))
				{
				cutpoints.add(j,null);	
				ArrayList<Set<Integer>> concept_column= new ArrayList<Set<Integer>>();
				Set<String> temporary_list= new HashSet<String>();
					for(int k=1;k<no_of_rows;k++)	
						temporary_list.add(data_table.get(k).get(j).toString());			
				ArrayList<String> temp = new ArrayList<String>();	
				int count=0; 
					for(String w:temporary_list)
						{	
							Set<Integer> concept_set = new LinkedHashSet<Integer>();
								for(int k=1;k<no_of_rows;k++)
										if(w.equalsIgnoreCase(data_table.get(k).get(j)))
											concept_set.add(k);
							concept_column.add(concept_set);
							temp.add(count++,w);	
						}	
				attribute_string_list.add(j,temp);	
				attribute_value_sets.add(j,concept_column);
				}
			}
		for(int j=0;j<no_of_columns;j++)
			column_heading.add(data_table.get(0).get(j));	
	}
    
	
	public static void print_attribute_pairs()
	{
		for(int j=0;j<no_of_attributes;j++)
			{
				System.out.println("Attribute:"+data_table.get(0).get(j));
					for(int k=0;k<attribute_value_sets.get(j).size();k++)
						System.out.printf("%-20s\t%s \n",attribute_string_list.get(j).get(k),attribute_value_sets.get(j).get(k));
				System.out.println();			
			}
	System.out.println();
	System.out.println();
		
	}
	//calculate cardinality list 
	public static void calculate_cardinality_sets()
	{	
		for(int j=0;j<attribute_value_sets.size();j++)
			{	
				ArrayList<Integer> temp = new ArrayList <Integer>();
				for(int k=0;k<attribute_value_sets.get(j).size();k++)
					{	
						temp.add(k, attribute_value_sets.get(j).get(k).size());
					}
				cardinality_list.add(j,temp);
			}	
	}

	

	//	Print the cardinality list
	 public static void print_cardinality_list()
	{
		 for (int j=0;j<no_of_attributes;j++)
		 {	
			 System.out.println("Attribute:"+data_table.get(0).get(j));
			 for(int k=0;k<cardinality_list.get(j).size();k++)
				 System.out.println(attribute_string_list.get(j).get(k)+"\t"+cardinality_list.get(j).get(k));
			 System.out.println();
		 }
	System.out.println();
	System.out.println();
				 
	}
	 
	//Calculate the concept value pairs	
	 public static void calculate_concept_value_pairs()
	 {		
		for(int j=no_of_columns-1;j<no_of_columns;j++)
			{	
			ArrayList<Set<Integer>> concept_column= new ArrayList<Set<Integer>>();
			Set<String> temporary_list= new LinkedHashSet<String>();		
				for(int k=1;k<no_of_rows;k++)	
					temporary_list.add(data_table.get(k).get(j).toString());					
			ArrayList<String> temp = new ArrayList<String>();			
			int count1=0; 	
				for(String w:temporary_list)
					{								
						Set<Integer> concept_set = new LinkedHashSet<Integer>();
							for(int k=1;k<no_of_rows;k++)
								if(w.equals(data_table.get(k).get(j)))
									concept_set.add(k);
						concept_column.add(concept_set);		
						temp.add(count1++, w);
						}
				concept_value_sets.add(0,concept_column);
				concept_string_list.add(0,temp);		
				
			}	
		}
				
	//print the concept value
	public static void print_concept_value_pairs()
		{	
			for(int j=0;j<concept_value_sets.get(0).size();j++)
	 		{
				String s1=concept_string_list.get(0).get(j).toString();
				String s2=concept_value_sets.get(0).get(j).toString();
	 			System.out.println("Concept (" +j+ ")--> "+s1);
	 			System.out.println(s2);
	 		}
	 	System.out.println();
		System.out.println();	
		}
	
	
 public static void debug()
{
Scanner trial = new Scanner(System.in);
String a=trial.next();

}
	
	//calculate characteristic_sets
	/*
	public static void calculate_characteristic_sets_and_A_star()
	{
		Set_Handling set_handle = new Set_Handling();
		characteristic_set.add(0,null);	
		int count=0;
		Set<Integer> no_duplicate = new LinkedHashSet<Integer>();
		for(int i=1;i<no_of_rows;i++)
			{
			ArrayList<Set<Integer>> temp = new ArrayList<Set<Integer>>();
				for(int j=0;j<no_of_attributes;j++)
					{
					int m=-1;	
						for(int x=0;x<attribute_string_list.get(j).size();x++)
							if(data_table.get(i).get(j).equals(attribute_string_list.get(j).get(x).toString()) && m==-1)
								m=x;	
					if(j!=0)
					temp.add(j,set_handle.intersection(temp.get(j-1),attribute_value_sets.get(j).get(m)));
					else
					temp.add(j,attribute_value_sets.get(j).get(m));	
					}
			if(!no_duplicate.contains(i))
				attribute_star.add(count++,temp.get(no_of_attributes-1));
			characteristic_set.add(i,temp.get(no_of_attributes-1));	
			no_duplicate.addAll(temp.get(no_of_attributes-1));
			}
	}
	*/
	
 public static void print_characteristic_sets()
	{
	System.out.println("Print Characteristic set");
		for(int i=1;i<no_of_rows;i++)
		{
			System.out.print("Ka("+ i +"):");
			System.out.println(characteristic_set.get(i));	
		}
	System.out.println();
	System.out.println();			
	}


	//calculate the A_star,i.e the set of all cases that are indistinguishable
	public static void calculate_A_star()
	{
		ArrayList<StringBuffer> row = new ArrayList<StringBuffer>();
		row.add(0, null);
		for(int i=1;i<no_of_rows;i++)
			{	
				StringBuffer s = new StringBuffer();
				for(int j=0;j<no_of_attributes;j++)
						s.append(data_table.get(i).get(j).toString());
				row.add(s);
			}	
		Set<Integer> no_duplicate_a_star= new LinkedHashSet<Integer>();
		for(int i=1;i<row.size();i++)
		{
			Set<Integer> temp = new LinkedHashSet<Integer>();
			if(!no_duplicate_a_star.contains(i))	
			{
			for(int j=i;j<row.size();j++)
				if(row.get(i).toString().equals(row.get(j).toString()))
					{
						temp.add(j);
						no_duplicate_a_star.add(j);
					}
			attribute_star.add(temp);
			}
		}	
		int count =0;
		characteristic_set.add(count++,null);
		for(int i=1;i<no_of_rows;i++)
		{
			boolean break_variable =true;
			for(int x=0;break_variable;x++)
				if(attribute_star.get(x).contains(i))
					{
					characteristic_set.add(count++,attribute_star.get(x));
					break_variable =false;
					}
		}
	 }

	//print A_star
	public static void print_A_star() throws Exception
	{
		
		if (!File_elementary_sets.exists()) {
			File_elementary_sets.createNewFile();
		}

		fw1= new FileWriter(File_elementary_sets.getAbsoluteFile());
		bw1 = new BufferedWriter(fw1);

	bw1.append("Print A star");
	bw1.newLine();
		for(int i=0;i<attribute_star.size();i++)
			bw1.append(attribute_star.get(i).toString());
	bw1.close();
	System.out.println();
	System.out.println();
		
	}


	public static void calculate_characteristic_sets()
	{
		Set_Handling set_handle = new Set_Handling();
		characteristic_set.add(0,null);	
		for(int i=1;i<no_of_rows;i++)
			{
			ArrayList<Set<Integer>> temp = new ArrayList<Set<Integer>>();
				for(int j=0;j<no_of_columns-1;j++)
					{
					int m=-1;	
						for(int x=0;m!=-1;x++)
							if(data_table.get(i).get(j).equals(attribute_string_list.get(j).get(x).toString()) && m==-1)
								m=x;								
					if(j==0)
						temp.set(j,attribute_value_sets.get(j).get(m));	
					else
						temp.set(j,set_handle.intersection(temp.get(j-1),attribute_value_sets.get(j).get(m)));
					}
			System.out.println("no_of_columns"+(no_of_attributes-1));	
			characteristic_set.set(i,temp.get(no_of_attributes-1));	
			}
	}

	
	public static void singleton_approximation()
	{
		
	}	
	
	public static void subset_approximation()
	{
		
	}
	
	public static void find_rule(int a)
	{
	ArrayList <ArrayList<Set<Integer>>> rule_set= new ArrayList <ArrayList<Set<Integer>>>();
	ArrayList<ArrayList<String>> rule_set_string= new ArrayList <ArrayList<String>>();
	ArrayList <Set<Integer>> intersection_of_rule = new ArrayList <Set<Integer>>();
	ArrayList<ArrayList<Integer>> found_rows = new ArrayList<ArrayList<Integer>>();
	ArrayList<ArrayList<Integer>> found_column = new ArrayList<ArrayList<Integer>>();	
	Set<Integer> concept_block = new LinkedHashSet <Integer>();
	Set<Integer> current_goal = new LinkedHashSet<Integer>();	
	Set<Integer> current_intersecting_element = new LinkedHashSet <Integer>();	
	Set<Integer> current_intersection = new LinkedHashSet <Integer>();
	Set<Integer> could_not_find = new LinkedHashSet <Integer>();
	Set<Integer> found_cases_so_far = new LinkedHashSet<Integer>();

	Set_Handling set_handler = new Set_Handling();

	int temp_coll=0;
	int temp_row=0;
	int rules_count=0;
	boolean found_rule=false;
	boolean atleast_one_selection=false;
	
	concept_block.addAll(concept_value_sets.get(0).get(a));
	current_goal.addAll(concept_value_sets.get(0).get(a));
	current_intersection=null;
	
	
	System.out.println("concept_block is "+concept_block);
	do			//check for goal is null or if there is not even one intersection
	{	
	Set<Integer>current_intersection_rule =new LinkedHashSet<Integer>();	
	current_intersection_rule.addAll(concept_block);
	//System.out.println("current_goal is "+current_goal);
	//debug();
	current_intersection=null;
	current_intersecting_element=null;
	ArrayList<Integer>rule_temp_row = new ArrayList<Integer>();
	ArrayList<Integer>rule_temp_column = new ArrayList<Integer>();
	ArrayList<Set<Integer>>possible_rule= new ArrayList<Set<Integer>>();	
	ArrayList<String>temp_string =new ArrayList<String>();	
	//System.out.println("start to find rule again");
	do		//finding t
	{	
		//System.out.println("start to find t again");					
		found_rule=false;
		int temp=0;
		atleast_one_selection=false;
		//System.out.println("current_intersection_rule"+current_intersection_rule);
		current_intersection_rule.retainAll(current_goal);
		//System.out.println("After change current_intersection_rule"+current_intersection_rule);
		int current_max_no_of_intersection=0;
		for(int j=0;j<attribute_value_sets.size();j++)           
		{	
			
					for(int k=0;k<attribute_value_sets.get(j).size();k++)		
					{	
						current_intersecting_element=set_handler.intersection(current_goal,attribute_value_sets.get(j).get(k));   // calculate intersection of Goal and current a,v pair
						//System.out.print(current_intersecting_element+"----->"+ attribute_value_sets.get(j).get(k) + "----->" );
						//System.out.print(!possible_rule.contains(attribute_value_sets.get(j).get(k))+ "----->"+ current_max_no_of_intersection + "----->");
						//System.out.println(possible_rule+"----->"+attribute_value_sets.get(j).get(k));						
						
						if(current_intersecting_element.size()>current_max_no_of_intersection && current_intersecting_element.size()!=0 && (!possible_rule.contains(attribute_value_sets.get(j).get(k))) )       
								{  
									current_max_no_of_intersection=current_intersecting_element.size();
									temp_coll=j;
									temp_row=k; 
									atleast_one_selection=true;
									//System.out.println("good");
								 }	
							else if(current_intersecting_element.size()==current_max_no_of_intersection && current_intersecting_element.size()!=0 && (!possible_rule.contains(attribute_value_sets.get(j).get(k))))
								{	
									if(cardinality_list.get(j).get(k)<cardinality_list.get(temp_coll).get(temp_row))
										{
											current_max_no_of_intersection=current_intersecting_element.size();
											temp_coll=j;
											temp_row=k;
											atleast_one_selection=true;
										//	System.out.println("great");	
										}
								}								
					}		
							
		}	
						
		//debug();
		//System.out.println("The current picked t is ");
		//System.out.println("Column "+temp_coll+"-->"+column_heading.get(temp_coll)+"-->"+attribute_string_list.get(temp_coll).get(temp_row)+"-->"+attribute_value_sets.get(temp_coll).get(temp_row));
		if(atleast_one_selection==true)
		{
		if(current_intersection==null)
					current_intersection=attribute_value_sets.get(temp_coll).get(temp_row);	
			else
					current_intersection=set_handler.intersection(current_intersection, attribute_value_sets.get(temp_coll).get(temp_row));	
		//System.out.println("The current_intersection is " + current_intersection);			
		
		current_goal=set_handler.intersection(current_goal,current_intersection);
		//System.out.println("current_goal is "+current_goal);		
		
		//debug();
		if(set_handler.is_set1_subset_of_set2(current_intersection, concept_block))
				{
				//	System.out.println(concept_block);
				//	System.out.println(set_handler.is_set1_subset_of_set2(current_intersection, concept_block));
				//	System.out.println("the intersection is a subset");
				current_intersection_rule.retainAll(current_intersection);
				rule_temp_row.add(temp, temp_row);
				rule_temp_column.add(temp,temp_coll );
				possible_rule.add(temp, attribute_value_sets.get(temp_coll).get(temp_row));
				temp_string.add(temp++,attribute_string_list.get(temp_coll).get(temp_row));
				//System.out.println("Rule"+possible_rule);
				//System.out.println("String_rule "+temp_string);
				found_rule=true;
				//System.out.println("Found Rule for:"+current_goal);
				//System.out.println("After Adding rule current_intersection_rule"+current_intersection_rule);								
				}
			else
				{	
				//System.out.println("continue");
				rule_temp_row.add(temp, temp_row);
				rule_temp_column.add(temp,temp_coll );
				possible_rule.add(temp, attribute_value_sets.get(temp_coll).get(temp_row));
				temp_string.add(temp++,attribute_string_list.get(temp_coll).get(temp_row));
				current_intersection_rule.retainAll(current_intersection);
				//System.out.println("not found rule yet"+current_intersection_rule);			
				}
		//System.out.print((current_goal.size())+"----->"+(current_intersection.size())+"----->");
		//float f=(float)((float)(current_goal.size())/(float)(current_intersection.size()));
		//System.out.println(f);	
		}
		else
		{
			
			//System.out.println("could not find"+could_not_find);		
			//System.out.println("current_goal is "+current_goal);		
			could_not_find.addAll(current_goal);
			//System.out.println("not found rule yet and no intersection"+current_intersection_rule);			
			//System.out.println("could not find"+could_not_find);		
			//System.out.println("current_goal is "+current_goal);		
			//System.out.println("Current Intersection"+current_intersection_rule);
		}
		//System.out.print( atleast_one_selection +"  "+!found_rule);
		//System.out.print(" .... "+ (atleast_one_selection   &&  !found_rule));		
		//debug();
	}while(atleast_one_selection && !found_rule);   
	if(found_rule==true)
		{

			found_cases_so_far.addAll(current_goal);
			found_rows.add(rules_count,rule_temp_row);
			found_column.add(rules_count,rule_temp_column);
			rule_set.add(rules_count,possible_rule);
			intersection_of_rule.add(current_intersection_rule);
			rule_set_string.add(rules_count++,temp_string);
			current_goal.addAll(concept_block);
			current_goal.removeAll(found_cases_so_far);
			current_goal.removeAll(could_not_find);	
			//System.out.println(intersection_of_rule);
		}
		else if(current_intersection!=null)
		{
			if (((float)((float)current_goal.size()/(float)current_intersection.size()) >(float)alpha))
			{
			found_rule =true;
			found_cases_so_far.addAll(current_goal);
			found_rows.add(rules_count,rule_temp_row);
			found_column.add(rules_count,rule_temp_column);
			rule_set.add(rules_count,possible_rule);
			intersection_of_rule.add(current_intersection_rule);
			rule_set_string.add(rules_count++,temp_string);
			current_goal.addAll(concept_block);
			current_goal.removeAll(found_cases_so_far);
			current_goal.removeAll(could_not_find);			
		//	System.out.println("the intersection "+ current_intersection +" has a error rate less than " + alpha);
			}
		}
		if(!found_rule)
		{
			current_goal.addAll(concept_block);
			current_goal.removeAll(found_cases_so_far);
			current_goal.removeAll(could_not_find);
			 current_goal=set_handler.subtract_setA_from_setB(could_not_find,concept_block);
		}
	}while(current_goal.size()!=0);        
	concept_found_column.add(a,found_column);
	concept_found_rows.add(a,found_rows);
	concept_rule_set_string.add(a,rule_set_string);
	concept_rule_set.add(a,rule_set);
	concept_intersection_of_rule.add(a,intersection_of_rule);
	concept_found_cases_so_far.add(a, found_cases_so_far);
	concept_could_not_find.add(a,could_not_find);
	System.out.println(a);
	}
	
	
static void print_concept_rules() 
	{	
		for(int i=0;i<concept_value_sets.get(0).size();i++)
		{
			System.out.println("Concept"+concept_value_sets.get(0).get(i));
			System.out.println("rule_set"+concept_rule_set.get(i));
			System.out.println("rule_set_string"+concept_rule_set_string.get(i));
			System.out.println("intersection_of_rule"+concept_intersection_of_rule.get(i));
			System.out.println("found_column"+concept_found_column.get(i));
			System.out.println("found_rows"+concept_found_rows.get(i));
			//System.out.println("could_not_find"+concept_could_not_find);
			System.out.println("found_cases_so_far"+concept_found_cases_so_far);
			System.out.println();
			
		}
		System.out.println();
	}
static void print_understand_depth()
{
		System.out.println("Concept"+concept_value_sets);
		System.out.println("rule_set"+concept_rule_set);
		System.out.println("rule_set_string"+concept_rule_set_string);
		System.out.println("intersection_of_rule"+concept_intersection_of_rule);
		System.out.println("found_column"+concept_found_column);
		System.out.println("found_rows"+concept_found_rows);
		System.out.println("found_cases_so_far"+concept_found_cases_so_far);
		System.out.println();
		
}

static void prepare_file_write()
{
	for(int i=0;i<concept_value_sets.get(0).size();i++)
	{
		System.out.println("concept_value_sets"+concept_value_sets.get(0).get(i));
		for(int j=0;j<concept_rule_set.get(i).size();j++)	
		{
		System.out.print("[");			
		for(int k=0;k<concept_rule_set.get(i).get(j).size();k++)
		{
			System.out.print("("+column_heading.get((concept_found_column.get(i).get(j).get(k)))+",");
			System.out.print(attribute_string_list.get(concept_found_column.get(i).get(j).get(k)).get(concept_found_rows.get(i).get(j).get(k))+")");
			if(k+1<concept_rule_set_string.get(i).get(j).size())
				System.out.print(",");
		}
		System.out.print("]");
		System.out.println("-->[("+column_heading.get(no_of_attributes)+"),("+concept_string_list.get(0).get(i)+")]");			
		}
	}
}

static void calculate_redundancy() throws Exception
{
	 ArrayList<ArrayList<ArrayList<Set<Integer>>>> concept_rule_set_dup= new ArrayList<ArrayList<ArrayList<Set<Integer>>>>();
	 ArrayList<ArrayList<ArrayList<String>>> concept_rule_set_string_dup= new  ArrayList<ArrayList<ArrayList<String>>>();
	 ArrayList<ArrayList <Set<Integer>>> concept_intersection_of_rule_dup = new ArrayList<ArrayList<Set<Integer>>>();
	 ArrayList<ArrayList<ArrayList<Integer>>> concept_found_rows_dup = new ArrayList<ArrayList<ArrayList<Integer>>>();
	 ArrayList<ArrayList<ArrayList<Integer>>> concept_found_column_dup = new ArrayList<ArrayList<ArrayList<Integer>>>();	
	 ArrayList<Set<Integer>> concept_could_not_find_dup = new ArrayList<Set<Integer>>();
	 ArrayList<Set<Integer>> concept_found_cases_so_far_dup = new ArrayList<Set<Integer>>();
	 System.out.println();
	 System.out.println();
	 System.out.println();
	 System.out.println();
	 System.out.println();
	 
	 concept_rule_set_dup.addAll(concept_rule_set);
	 concept_rule_set_string_dup.addAll(concept_rule_set_string);
	 concept_found_rows_dup.addAll(concept_found_rows); 
	 concept_found_column_dup.addAll(concept_found_column);
	 concept_intersection_of_rule_dup.addAll(concept_intersection_of_rule);
	 concept_could_not_find_dup.addAll(concept_could_not_find); 
	 concept_found_cases_so_far_dup.addAll(concept_found_cases_so_far);
	 System.out.println("Concept"+concept_value_sets);
	 System.out.println("rule_set"+concept_rule_set_dup);
	 System.out.println("rule_set_string"+concept_rule_set_string_dup);
	 System.out.println("intersection_of_rule"+concept_intersection_of_rule_dup);
	 System.out.println("found_column"+concept_found_column_dup);
	 System.out.println("found_rows"+concept_found_rows_dup);
	 System.out.println("found_cases_so_far"+concept_found_cases_so_far_dup);
	 Set_Handling file_handler =new Set_Handling();
	 boolean break_the_loop=true;
	 do
	 {
	break_the_loop=true;	 
	for(int i=0;i<concept_value_sets.get(0).size() && break_the_loop;i++)
	 {
		 for(int j=0;j<concept_rule_set_dup.get(i).size() && break_the_loop;j++)
		 {
			 for(int k=0;k<concept_rule_set_dup.get(i).get(j).size() && break_the_loop;k++)
			 {
				 Set<Integer> intersection =new LinkedHashSet<Integer>();
					 
				 for(int m=0;m<concept_rule_set_dup.get(i).get(j).size() && break_the_loop;m++)
				 {
					 if(k!=m)
					 {
						 if(intersection.size()==0)
						 intersection=concept_rule_set_dup.get(i).get(j).get(m);
						 else
						 intersection=file_handler.intersection(intersection, concept_rule_set_dup.get(i).get(j).get(m));				 
					 }
				 }
				 if(file_handler.is_set1_subset_of_set2(intersection,concept_value_sets.get(0).get(i)) && intersection.size()>1)
				 	{ 
					 			concept_rule_set_dup.get(i).get(j).remove(k);
					 			concept_intersection_of_rule_dup.get(i).get(j).retainAll(intersection);
					 			concept_found_rows_dup.get(i).get(j).remove(k);
					 			concept_found_column_dup.get(i).get(j).remove(k);
					 			break_the_loop=false;
					 }	
			 }
		 }
	 }
	 }while(!break_the_loop);
	 System.out.println("Concept"+concept_value_sets);
	 System.out.println("rule_set"+concept_rule_set_dup);
	 System.out.println("rule_set_string"+concept_rule_set_string_dup);
	 System.out.println("intersection_of_rule"+concept_intersection_of_rule_dup);
	 System.out.println("found_column"+concept_found_column_dup);
	 System.out.println("found_rows"+concept_found_rows_dup);
	 System.out.println("found_cases_so_far"+concept_found_cases_so_far_dup);
	
	 System.out.println();
	 System.out.println();
	 System.out.println();
	 System.out.println();
	 System.out.println();
	 System.out.println();
	 System.out.println();
	 System.out.println();
	 
	 System.out.println(File_Rules_set);
	 for(int i=0;i<concept_value_sets.get(0).size();i++)
		{
		 bw.append("concept_value_sets"+concept_value_sets.get(0).get(i).toString());
		 
			for(int j=0;j<concept_rule_set_dup.get(i).size();j++)	
			{
				 bw.append(concept_rule_set_dup.get(i).get(j).size()+","+concept_intersection_of_rule_dup.get(i).get(j).size()+","+file_handler.intersection(concept_intersection_of_rule_dup.get(i).get(j),concept_value_sets.get(0).get(i)).size());	
				 bw.newLine();
				 bw.append("[");
				 //System.out.println(concept_rule_set_dup.get(i).get(j).size()+","+concept_intersection_of_rule_dup.get(i).get(j).size()+","+file_handler.intersection(concept_intersection_of_rule_dup.get(i).get(j),concept_value_sets.get(0).get(i)).size());	
				 //System.out.print("[");			
			for(int k=0;k<concept_rule_set_dup.get(i).get(j).size();k++)
			{
				bw.append("("+column_heading.get((concept_found_column_dup.get(i).get(j).get(k)))+",");
				bw.append(attribute_string_list.get(concept_found_column_dup.get(i).get(j).get(k)).get(concept_found_rows_dup.get(i).get(j).get(k))+")");
				//System.out.print("("+column_heading.get((concept_found_column_dup.get(i).get(j).get(k)))+",");
				//System.out.print(attribute_string_list.get(concept_found_column_dup.get(i).get(j).get(k)).get(concept_found_rows_dup.get(i).get(j).get(k))+")");
				if(k+1<concept_rule_set_string_dup.get(i).get(j).size())
					bw.append(",");
				//System.out.print(",");
			}
			bw.append("]");
			//System.out.print("]");
			bw.append("-->[("+column_heading.get(no_of_attributes)+"),("+concept_string_list.get(0).get(i)+")]");
			bw.newLine();
			//System.out.println("-->[("+column_heading.get(no_of_attributes)+"),("+concept_string_list.get(0).get(i)+")]");			
			}
		}
	 bw.close();
}
static String get_file_name(String s)
{
	System.out.println("Enter the file name to wish you write the file of"+s +" to");
	String file_name=null;
	boolean  file_can_be_createn=true;
	String g;
	do
	{
		System.out.println("Please enter only charactrs A-Z and a-z");
		Scanner input_recieve = new Scanner(System.in);
		g=input_recieve.next();
		Pattern p=Pattern.compile("[A-Za-z]*");
		Matcher matcher=p.matcher(g);
		System.out.println(matcher.matches());
		if(matcher.matches())
			file_can_be_createn=false;
	}while(file_can_be_createn);
	
	return g;
}
public static void get_elementary_sets()

{
	boolean loop_variable=true;
do
{
	System.out.println("Do you wish to create a file for elementary sets");
	Scanner choice=new Scanner(System.in);
	System.out.println("If yes type Yes else No");
	String s=choice.next();
	if(s.equalsIgnoreCase("Yes"))
		{	
		File_elementary_sets=(new File(get_file_name("Elementary sets")));
		yep_file_input_recieved=true;
		loop_variable=false;
		}
	else if(s.equalsIgnoreCase("no"))
		{
		yep_file_input_recieved=false;
		loop_variable=false;
		}
	else
		{
		System.out.println("Wrong Choice not YES or NO");	
		yep_file_input_recieved=false;

		}
}while(loop_variable);
}
}
	












