package communicationProtocol;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TooManyListenersException;

import javafx.application.Platform;
import javafx.scene.control.Label;
import toast.Toast;
import application.DataStore;
import application.Main;
import application.SerialWriter;
import application.writeFormat;

public class Mycommand {

	
	public static void setLacthing(String ss,int slp)
    {
    	writeFormat wr=new writeFormat();
		wr.addChar('L');
		wr.addChar('V');
		
		for(int i=0;i<14;i++)
		{
			wr.addChar(ss.charAt(i));
		}
		wr.addLast();
		sendData(wr, slp);
		
    }
	
	
	public static void setStability(int no,int val,int delay)
	{
		writeFormat wr=new writeFormat();
		wr.addChar('F');
		wr.addChar('M');
		wr.addChar('T');
		wr.addInt(no);
		wr.addInt(val);
		wr.addLast();
		sendData(wr, delay);
	}
	public static  void systemReset()
	{
    	writeFormat wr=new writeFormat();
		wr.addChar('C');
		wr.addChar('V');
		wr.addChar('D');
		wr.addChar('1');
		wr.addChar('1');
		wr.addChar('1');
		wr.addChar('1');
		wr.addChar('0');
		wr.addChar('0');
		wr.addChar('0');
		wr.addChar('0');
		wr.addChar('0');
		wr.addChar('0');
		wr.addChar('0');
		wr.addChar('0');
		wr.addChar('0');
		wr.addChar('0');
		//wr.addData1(getValueList(2000));
		wr.addLast();
		sendData(wr, 200);
	}
	
	public static void valveOn(int no,int delay)
	{

		writeFormat wrD = new writeFormat();
		wrD.addChar('V');
		wrD.addInt(no);
		wrD.addChar('1');
		wrD.addInt(0);
		wrD.addInt(0);
		wrD.addLast();
		sendData(wrD,delay);
		
	
	}
	public static void valveOff(int no,int delay)
	{

		writeFormat wrD = new writeFormat();
		wrD.addChar('V');
		wrD.addInt(no);
		wrD.addChar('0');
		wrD.addInt(0);
		wrD.addInt(0);
		wrD.addLast();
		sendData(wrD,delay);
		
	
	}
	public static void valveOn(char no,int delay)
	{

		
		
		writeFormat wrD = new writeFormat();
		wrD.addChar('V');
		wrD.addChar(no);
		wrD.addChar('1');	
		wrD.addInt(0);
		wrD.addInt(0);
		wrD.addLast();
		sendData(wrD,delay);
		
	
	}
	public static void valveOff(char no,int delay)
	{

		writeFormat wrD = new writeFormat();
		wrD.addChar('V');
		wrD.addChar(no);
		wrD.addChar('0');
		wrD.addInt(0);
		wrD.addInt(0);
		wrD.addLast();
		sendData(wrD,delay);
		
		
	}	
	 public static  void getADCSingleVal(int adcno,int delay)
	    {
	    	writeFormat wr=new writeFormat();
			wr.addChar('R');
			wr.addChar('S');
			wr.addChar('A');
			wr.addInt(adcno);
			wr.addBlank(1);
			wr.addLast();
			sendData(wr, delay);
	    }

   
    public static  void setDACIncrement(int v1,int v2,int v3,int v4,int delay)
    {
    	writeFormat wr=new writeFormat();
		wr.addChar('F');
		wr.addChar('M');
		wr.addChar('D');
		wr.addChar('I');
		wr.addData1(getBitFromInt(v1));
		wr.addData1(getBitFromInt(v2));
		wr.addData1(getBitFromInt(v3));
		wr.addData1(getBitFromInt(v4));
		
		wr.addLast();
		sendData(wr, delay);
    }
    
    
    public static  void setDACValue(char dacno,int val,int delay)
    {
    	writeFormat wr=new writeFormat();
		wr.addChar('F');
		wr.addChar('M');
		wr.addChar('D');
		wr.addChar('S');
		wr.addChar(dacno);
		wr.addData1(getBitFromInt(val));
		wr.addLast();
		sendData(wr, delay);
    }
    
	public static  void sendData(writeFormat w, int slp) {
	
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {

				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						
						Thread t = new Thread(new SerialWriter(DataStore.out, w, slp));
						try {
							t.start();
						} catch (Exception e) {
							
							e.printStackTrace();
						}
					}
				});
			}

		};
		timer.schedule(task, slp);
		
	

	}
	
    
  
    
    
   public static List<Integer> getAdcData(List<Integer> data,boolean print)
	{
		List<Integer> d=new ArrayList<Integer>();
		
		for(int i=4;i<49;i=i+3)
		{
		d.add(getIntFromBit(data.get(i), data.get(i+1), data.get(i+2)));
	
		}
	 
		if(print)
		{
		System.out.println("\nAdc Data : "+d);
		}
		
		return d;
	}
	
	public static  void startADC(int delay)
	{
		writeFormat wr=new writeFormat();
		wr.addChar('F');
		wr.addChar('M');
		wr.addChar('S');
		wr.addBlank(2);
		wr.addLast();
		sendData(wr, delay);
		
		
	}
	public static  void stopADC(int delay)
	{
		writeFormat wr=new writeFormat();
		wr.addChar('F');
		wr.addChar('M');
		wr.addChar('X');
		wr.addBlank(2);
		wr.addLast();
		sendData(wr, delay);
	}
	public static void setDelay(int miliseconds,int delay)
	{
		writeFormat wr=new writeFormat();
		wr.addChar('F');
		wr.addChar('M');
		wr.addChar('L');
		wr.addData1(getBitFromInt(miliseconds));
		wr.addLast();
		sendData(wr, delay);
		
		
	}
	public  static  List<Integer> getBitFromInt(int val) {
		String pad = "000000";
		String st = "" + Integer.toHexString(val);
		String st1 = (pad + st).substring(st.length());
		List<Integer> ls = new ArrayList<Integer>();

		int n = (int) Long.parseLong(st1.substring(0, 2), 16);
		int n1 = (int) Long.parseLong(st1.substring(2, 4), 16);
		int n2 = (int) Long.parseLong(st1.substring(4, 6), 16);
		ls.add(n);
		ls.add(n1);
		ls.add(n2);

		return ls;
	}
	public static  void sendAdcEnableBits(String bit,int delay)
	{
		writeFormat wr=new writeFormat();
		wr.addChar('F');
		wr.addChar('M');
		wr.addChar('A');
		wr.addChar('E');
		for(int i=0;i<15;i++)
		{
			try {
			wr.addChar(bit.charAt(i));
			}
			catch(Exception e)
			{
				wr.addChar('0');
			}
		}
	
		wr.addLast();
		sendData(wr, delay);
	}
	
	public static  void sendAdcEnableBits(char ch1,char ch2,char ch3,char ch4,char ch5,char ch6,char ch7,char ch8,char ch9,char ch10,char ch11,char ch12,char ch13,char ch14,char ch15,int delay)
	{
		writeFormat wr=new writeFormat();
		wr.addChar('F');
		wr.addChar('M');
		wr.addChar('A');
		wr.addChar('E');
		wr.addChar(ch1);
		wr.addChar(ch2);
		wr.addChar(ch3);
		wr.addChar(ch4);
		wr.addChar(ch5);
		wr.addChar(ch6);
		wr.addChar(ch7);
		wr.addChar(ch8);
		wr.addChar(ch9);
		wr.addChar(ch10);
		wr.addChar(ch11);
		wr.addChar(ch12);
		wr.addChar(ch13);
		wr.addChar(ch14);
		wr.addChar(ch15);
		wr.addLast();
		sendData(wr, delay);
	}
	
	public static int getIntFromBit(int a1,int a2,int a3)
	{
		System.out.println(a1 + " : "+a2+ ": "+a3);
		int a=0;
		
		a=a1<<16;
		   a2=a2<<8;
		   a = a|a2;
		   a = a|a3;
		   
		   return a;
	}
	
	
	
	
}
