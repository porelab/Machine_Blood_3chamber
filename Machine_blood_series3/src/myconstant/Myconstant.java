package myconstant;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.Database;
import data_read_write.CsvWriter;
import data_read_write.DatareadN;

public class Myconstant 
{
	
	public static String sampleid,testmode,stepsize,sampletype,lotno,supportplate;
	public static List<String> cknames;
	

	public static Map<String,Object> map=new HashMap<>();
	
	public static DatareadN dr;
	public static File f;
	
	public Myconstant() {

		f=new File("information/info.csv");
		dr=new DatareadN();
		dr.fileRead(f);

		
		
	Map<String,Object> ch1data=new HashMap<>();
	ch1data.put("sampleid", "sample1");
	ch1data.put("type", "garment");
	ch1data.put("samplearea", "area1");
	ch1data.put("lotno", "lot1");
	
	map.put("ch1", ch1data);
	
	
	Map<String,Object> ch2data=new HashMap<>();
	ch2data.put("sampleid", "sample2");
	ch2data.put("type", "garment");
	ch2data.put("samplearea", "area2");
	ch2data.put("lotno", "lot2");
	
	map.put("ch2", ch2data);
	
	Map<String,Object> ch3data=new HashMap<>();
	ch3data.put("sampleid", "sample3");
	ch3data.put("type", "roll");
	ch3data.put("samplearea", "");
	ch3data.put("lotno", "lot3");
	
	map.put("ch3", ch3data);
		
	}
	
	public static void inIt()
	{
		f=new File("information/info.csv");
		dr=new DatareadN();
		dr.fileRead(f);
	}
	
	public static String getStd()
	{
		String std="1";
		//1 for Astm and 2 for iso
		try {
			std=dr.data.get("std").toString();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return std;
	}
	

	
	
	public static void setStd(String std)
	{
     	
		try {
			dr.data.replace("std", std);
			updateInfo();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void updateInfo()
	{
		try {
			CsvWriter c=new CsvWriter();
			c.wtirefile(f.getPath());
			c.firstLine("information");
			
			dr.data.forEach((k, v) -> {
			
				c.newLine(k, v.toString());
				
			});
				
			c.closefile();
			System.out.println("Updated");
		}
		catch(Exception e)
		{
			
		}
	}

	public static String getipaddress()
	{
		String ip="";
		Database db=new Database();		
		List<List<String>> ll=db.getData("select ip from connection");
		ip =(ll.get(0).get(0));

		return ip;
	}
}


