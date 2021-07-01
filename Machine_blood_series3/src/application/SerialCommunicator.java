package application;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import toast.MyDialoug;
import ConfigurationPart.SkadaController;
import application.SerialCommunicator.SerialReader;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// for communication of PC and MCU

public class SerialCommunicator {

	int ind = 0;

	public SerialCommunicator() {
		DataStore.listOfHeads.add('P');
		DataStore.listOfHeads.add('T');
		DataStore.listOfHeads.add('H');
		DataStore.listOfHeads.add('F');
		DataStore.listOfHeads.add('L');
		DataStore.listOfHeads.add('S');
		DataStore.listOfHeads.add('V');
		
		// listOfHeads.add('D');
		// listOfHeads.add('C');
		DataStore.intList.put("80", FXCollections.observableArrayList()); // for
																			// P
		DataStore.intList.put("70", FXCollections.observableArrayList()); // for
																			// F
		DataStore.intList.put("84", FXCollections.observableArrayList()); // for
																			// T
		DataStore.intList.put("72", FXCollections.observableArrayList()); // for
																			// H
		DataStore.intList.put("76", FXCollections.observableArrayList());
	}

	public void connect(String portName) throws Exception {
		CommPortIdentifier portIdentifier = CommPortIdentifier
				.getPortIdentifier(portName);
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use1");
			
			if(DataStore.commPort!=null)
			{
				DataStore.commPort.close();
				
				
			}
			
		} 
		setUpConnect(portIdentifier);
		
	}
	void setUpConnect(CommPortIdentifier portIdentifier)
	{
	
		try {
			DataStore.commPort = portIdentifier.open(this.getClass().getName(),
					10000);
		if (DataStore.commPort instanceof SerialPort) {
			DataStore.serialPort = (SerialPort) DataStore.commPort;
			DataStore.serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			
		DataStore.in = DataStore.serialPort.getInputStream();
			DataStore.out = DataStore.serialPort.getOutputStream();
			DataStore.sr=new SerialReader(DataStore.in);
		
			DataStore.serialPort.addEventListener(DataStore.sr);
			DataStore.serialPort.notifyOnDataAvailable(true);
			
		//	DataStore.hardReset();

		} else {
			System.out
					.println("Error: Only serial ports are handled by this example.");
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Error in serial communicator : "+e.getMessage());
			
			
			
			if(DataStore.commPort!=null)
			{
				System.out.println("error it is not null");
				
				DataStore.commPort.close();
				setUpConnect(portIdentifier);
				
			}
			else
			{
				System.out.println("error but it is null");
			}
			
		}
	}

	/*  */
	public static class SerialReader implements SerialPortEventListener {
	
	
		   InputStream in;
	       int ind=0;
	        List<Integer> readData= new ArrayList<Integer>();
	        
	        public SerialReader ( InputStream in )
	        {
	            this.in = in;
	            DataStore.getconfigdata();
	        }
	       
	        public void serialEvent(SerialPortEvent arg0) {
	            int data;
	            try
	            {
	                int len = 0;
	                char prev='\0';
	               // System.out.println("Reading Started:");
	                
	                while ( ( data = in.read()) > -1 )
	                {
	                	
	                    if ( data == '\n'&& prev == 'E') {
	                       break;
	                    }
	                    if(len>0 || (data=='\r'&& prev=='\n') )
	                    {
	                    	readData.add(data);
	                    
	                    	len++;
	                    }
	                    prev = (char) data; 
	                  System.out.print(prev);
	                    
	                    //System.out.print(new String(buffer,0,len));
	                }
	        		
	                for(int i=1;i<readData.size();)
	                {
	                	//System.out.println("Head: "+readData.get(i));
	                	int ss=readData.get(i);
	                	char ss1=(char)ss;
	                
	                
	                	
	                	if(readData.get(i)==(int)'O'&&readData.get(i+1)==(int)'F')
	                	{
	                		// Pressure Gauge 1
	                		
	                		int a = 0, a1, a2, a3;
							a1 = readData.get(i + 2);
							a2 = readData.get(i + 3);
							a3 = readData.get(i + 4);

							System.out.println("PG1 Offset Bit : " + a1 + ":"
									+ a2 + ":" + a3);

							a = a1 << 16;
							a2 = a2 << 8;
							a = a | a2;
							a = a | a3;
							int maxpre = Integer.parseInt(DataStore
									.getPg1());
							double b = (double) a * maxpre / 65535;
							System.out
							.println("PG1 offset data ..... : "
									+ a);
							System.out
							.println("PG1 Error data ..... : "
									+ b);
							Myapp.pg1offset.set(b);
						
							// Pressure Gauge 2
							
							a1 = readData.get(i + 5);
							a2 = readData.get(i + 6);
							a3 = readData.get(i + 7);
							
							System.out.println("PG2 Offset Bit : " + a1 + ":"
									+ a2 + ":" + a3);
							
							a = a1 << 16;
							a2 = a2 << 8;
							a = a | a2;
							a = a | a3;
							System.out
							.println("PG2 Original Offset Data ..... : "
									+ a);
							int maxpre1 = Integer.parseInt(DataStore
									.getPg2());
							double b1 = (double) a * maxpre1 / 65535;
							System.out
							.println("PG2 Error data ..... : "
									+ b1);
							Myapp.pg2offset.set(b1);
							
							
							// Flowmeter 1
							
							a1 = readData.get(i + 8);
							a2 = readData.get(i + 9);
							a3 = readData.get(i + 10);
							
							System.out.println("FM1 Offset Bit : " + a1 + ":"
									+ a2 + ":" + a3);
							
							a = a1 << 16;
							a2 = a2 << 8;
							a = a | a2;
							a = a | a3;
							System.out
							.println("FM1 Original Offset Data ..... : "
									+ a);
							int maxpre2 = Integer.parseInt(DataStore
									.getFm1());
							double b2 = (double) a * maxpre2 / 65535;
							System.out
							.println("FM1 Error data ..... : "
									+ b2);
							Myapp.fm1offset.set(b2);
							
							// Flowmeter 2
							
							a1 = readData.get(i + 11);
							a2 = readData.get(i + 12);
							a3 = readData.get(i + 13);
							
							System.out.println("FM2 Offset Bit : " + a1 + ":"
									+ a2 + ":" + a3);
							
							a = a1 << 16;
							a2 = a2 << 8;
							a = a | a2;
							a = a | a3;
							System.out
							.println("FM2 Original Offset Data ..... : "
									+ a);
							int maxpre3 = Integer.parseInt(DataStore
									.getFm2());
							double b3 = (double) a * maxpre3 / 65535;
							System.out
							.println("FM2 Error data ..... : "
									+ b3);
							Myapp.fm2offset.set(b3);

							i=i+13;
	                	}
	        			
	                	else if (readData.get(i) == 'O'
								&& readData.get(i + 1) == 'P') {

							int a = 0, a1, a2, a3;
							a1 = readData.get(i + 3);
							a2 = readData.get(i + 4);
							a3 = readData.get(i + 5);

							System.out.println("PG1 Offset Bit : " + a1 + ":"
									+ a2 + ":" + a3);

							a = a1 << 16;
							a2 = a2 << 8;
							a = a | a2;
							a = a | a3;
							System.out
							.println("PG1 Original Offset Data ..... : "
									+ a);
							int maxpre = Integer.parseInt(DataStore
									.getPr());
							double b = (double) a * maxpre / 65535;
							System.out
							.println("PG1 Error data ..... : "
									+ b);
							Myapp.pg1offset.set(b);
							
							
							
							a1 = readData.get(i + 9);
							a2 = readData.get(i + 10);
							a3 = readData.get(i + 11);
							
							System.out.println("PG2 Offset Bit : " + a1 + ":"
									+ a2 + ":" + a3);
							
							a = a1 << 16;
							a2 = a2 << 8;
							a = a | a2;
							a = a | a3;
							System.out
							.println("PG2 Original Offset Data ..... : "
									+ a);
							int maxpre1 = Integer.parseInt(DataStore
									.getPr());
							double b1 = (double) a * maxpre1 / 65535;
							System.out
							.println("PG2 Error data ..... : "
									+ b1);
							Myapp.pg2offset.set(b1);


							
							i = i + 11;
						}

	                	
	                	else if(readData.get(i)==80&&readData.get(i+5)==70)
	                			{  	
	                				//System.out.println("Pressure: "+Integer.parseInt(Integer.toHexString(readData.get(i+1))+""+Integer.toHexString(readData.get(i+2))+""+Integer.toHexString(readData.get(i+3)),16));
	                				int a=0,a1,a2,a3;
	                				a1=readData.get(i+2);
	                				a2=readData.get(i+3);
	                				a3=readData.get(i+4);
	                				 System.out.println("...........................................................\n\nOriginal Pressure Data BIT :"+a1+" : "+a2+" : "+a3); 
	                     			
	                				
	                				a=a1<<16;
	                				   a2=a2<<8;
	                				   a = a|a2;
	                				   a = a|a3;
	                					 System.out.println("Original Pressure Data ..... : "+a); 
	                				 //  double b=(double)a*110/16777215;
	                					 
	                			
	                      				
	                					 //   b = Math.round(b*10000)/10000D;
	                					  if(readData.get(i+1)==49)
	                					  {
	                					int maxpre=Integer.parseInt(DataStore.getPr());
	                       				 double b=(double)a*maxpre/65535;
	                       				 
	                       				 
	                       				 
	                				 System.out.println(" Pressure Regulator..... : "+b);  
	                				 DataStore.livepressure.set(b);
	                				 DataStore.intList.get("80").add(b);
	                				 DataStore.spr.set(b);
	                				 
	                					  }
	                					  else if(readData.get(i+1)==50)
	                					  {
	                					int maxpre=Integer.parseInt(DataStore.getPg1());
	                       				 double b=(double)a*maxpre/65535;
	                       				 
	                       				 b=b-Myapp.pg1offset.get();
	                       				 
	                				 System.out.println(" Pressure guage1 ..... : "+b);  
	                				 DataStore.livepressure.set(b);
	                				 DataStore.intList.get("80").add(b);
	                				 DataStore.spg1.set(b);
	                				 
	                				 DataStore.sv3.set(true);
	                				 
	                					  }
	                					  else if(readData.get(i+1)==51)
	                					  {
	                					int maxpre=Integer.parseInt(DataStore.getPg2());
	                					System.out.println("Pressure Gauge2 Org Data "+a);
	                       				 double b=(double)a*maxpre/65535;
	                       				 
	                       				 b=b-Myapp.pg2offset.get();
	                				 System.out.println(" Pressure gauge2 ..... : "+b);  
	                				 DataStore.livepressure.set(b);
	                				 DataStore.intList.get("80").add(b);
	                				 DataStore.spg2.set(b);
	                				 DataStore.sv3.set(false);
	                					  }	  
	                					  else if(readData.get(i+1)==52)
	                					  {
	                						  double b=(double)a*Double.parseDouble(DataStore.pg1)/65535;
	                						  System.out.println(" Leak pressure ..... : "+b); 
	                          				 DataStore.livepressure.set(b);
	                          				 DataStore.intList.get("76").add(b);  
	                					  }
	                					  else if(readData.get(i+1)==53)
	                					  {
	                						  double b=(double)a*Double.parseDouble(DataStore.pg2)/65535;
	                						  System.out.println(" Leak pressure ..... : "+b); 
	                          				 DataStore.livepressure.set(b);
	                          				 DataStore.intList.get("76").add(b);  
	                					  }
	                				
	                			
	                			
	                			
	                			
	                				a1=readData.get(i+7);
	                				a2=readData.get(i+8);
	                				a3=readData.get(i+9);
	                				

	               				 System.out.println("Original Flow Data BIT :"+a1+" : "+a2+" : "+a3);
	                				a=a1<<16;
	                				   a2=a2<<8;
	                				   a = a|a2;
	                				   a = a|a3;
	                					 System.out.println("Original Flow Data ..... : "+a);
	                					  double b=0;
	                					  //System.out.println("BIT VALUE:"+readData.get(i+1));
	                					if(readData.get(i+6)==49)
	                					{
	                						b=(double)a*Integer.parseInt(DataStore.getFc())/65535;
	                					System.out.println("Flow Controller :  ... :"+b);
	                					DataStore.sfc.set((int)b);
	                					
	                					}
	                					else if(readData.get(i+6)==50)
	                					{
	                						b=(double)a*Integer.parseInt(DataStore.getFm1())/65535;
	                						//b=(double)a*8000/65535;
	                					System.out.println("Flow Meter 1 : ... :"+b);	
	                					DataStore.sfm1.set((int)b);
	                					DataStore.sv1.set(true);
	                					DataStore.sv2.set(false);
	                					}
	                					else if(readData.get(i+6)==51)
	                					{
	                						b=(double)a*Integer.parseInt(DataStore.getFm2())/65535;
	                					//	b=(double)a*200000/65535;
	                					System.out.println("Flow Meter 2 : ... :"+b);	
	                					DataStore.sfm2.set((int)b);
	                					DataStore.sv1.set(false);
	                					DataStore.sv2.set(true);
	                					}
	                					

	                 				   DataStore.liveflow.set((double)b);
	                				   DataStore.intList.get("70").add(b);
	                				   
	                				 
	                				
	                			}
	                			
	                		
	                			
	                			
	                		readData.clear();
	                		break;
	                		
	                	}
	                	
	                
	               
	                DataStore.allDataRead.add(readData);
	                readData.clear();
	                
	            }
	            catch ( IOException e )
	            {
	            	try{
						DataStore.serialPort.removeEventListener();
						DataStore.serialPort.close();
		            	}
		            	catch(Exception es)
		            	{
		            		System.out.println("serial error : "+es.getMessage());
		            	}
						MyDialoug.showError(103);
						Platform.runLater(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub

								DataStore.connect_hardware.set(false);
							}
						});
			    }             
	
	}

		private static String asciiToHex(String asciiValue) {
			char[] chars = asciiValue.toCharArray();
			StringBuffer hex = new StringBuffer();
			for (int i = 0; i < chars.length; i++) {
				hex.append(Integer.toHexString((int) chars[i]));
			}
			return hex.toString();
		}

	}


	public static void writeDataToDevice(writeFormat wrt) {
		for (Integer dout : wrt.wData) {
			try {
				DataStore.out.write(dout);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}

// model class to write data to machine
