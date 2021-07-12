package userinput;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TooManyListenersException;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.LineChart.SortingPolicy;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import myconstant.Myconstant;
import toast.MyDialoug;
import toast.Openscreen;
import toast.Toast;
import application.DataStore;
import application.Main;
import application.Myapp;
import application.SerialWriter;
import application.writeFormat;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXToggleButton;

import communicationProtocol.Mycommand;
import data_read_write.CalculatePorometerData;
import data_read_write.CsvWriter;
import de.tesis.dynaware.javafx.fancychart.zoom.Zoom;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.colors.Bright;
import eu.hansolo.tilesfx.colors.Dark;
import extrafont.Myfont;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class NLivetestController implements Initializable {
	@FXML
	StackPane videoanc;

	List<String> bpoints = new ArrayList<String>();
	List<String> bresults = new ArrayList<String>();
	List<String> btime = new ArrayList<String>();
	List<Integer> pressureCounts = new ArrayList<Integer>();
	int pressureindex = 0;

	@FXML
	ScrollPane scrollrecord;

	static SimpleBooleanProperty isRestart;

	List<Double> recorddata, recordtime;

	static File savefile;

	@FXML
	Label lblfilename, lblbpc;

	double stepsizepercentage = 0.2, maxpressureinstepsize = 1, mindelay = 200,
			maxdelay = 2000, minavg = 2, maxavg = 12;

	@FXML
	Rectangle manualblock;

	boolean isAuto = true;

	double readpre = 0, readtime = 0;
	String result = "";

	List<String> bans, tlist;

	int stadycount = 0;

	ListChangeListener<Double> bubblelistener1;
	int skip, skipwet = 0, skipdry = 0;
	MyDialoug mydia;
	// for start popup
	static SimpleBooleanProperty stprop = new SimpleBooleanProperty(false);
	static SimpleBooleanProperty stpropcan = new SimpleBooleanProperty(false);

	static SimpleBooleanProperty isBubbleStart;

	static SimpleBooleanProperty isDryStart;

	AudioClip tones;

	@FXML
	private Button btninfo, btnabr, starttestdry, starttest, starttestwet,
			startautotest, btnfail, btnpass, recordbtn;

	int delayinauto = 2500;

	boolean iswaiting = false;

	@FXML
	Label lbltestnom, lbltestdurationm, status, lblcurranttest;

	public static JFXDialog df;
	public static JFXDialog df1;

	@FXML
	AnchorPane guages, ancregu1, ancregu2, ancpg1, ancpg2, ancpg3, ancpg4;

	@FXML
	AnchorPane root, mainroot;

	private Tile gauge5;

	@FXML
	JFXToggleButton autotest;

	@FXML
	ToggleButton chamberonoff;

	int countbp = 0;
	writeFormat wrd;
	double p1 = 0, bbp, bbf, bbd;

	String typeoftest = "";
	static int i2 = 0;
	boolean both = false, bothbw = false;
	long t1test, t2test;
	boolean once = true;
	int yi = 0;
	static double p_inc = 0.0;
	int data_length = 0;
	double flowint = 0;
	final NumberAxis xAxis = new NumberAxis();
	final NumberAxis yAxis = new NumberAxis();

	LineChart<Number, Number> sc = new LineChart<>(xAxis, yAxis);
	XYChart.Series series2 = new XYChart.Series();

	int testno = 1;

	Myfont f = new Myfont(22);

	double conditionflow, conditionpressure;

	double darcyavg = 0;

	double curpress = 0;

	XYChart.Series flowserireswet = new XYChart.Series();

	long tempt1;

	int testtype = 0; // 0 for bubble 1 for wet 2 for dry
	SerialReader in;

	static SimpleBooleanProperty isSkiptest;

	@FXML
	Label lblresult, lbltesttype;

	boolean isCompletetest = false;

	long changetime = 0;
	int waittime = 0;
	String sampleid = "testing";
	String teststd;

	int chamber = 3;

	@FXML
	private Label ch1name;

	@FXML
	private Label ch2name;

	@FXML
	private Label ch3name;

	@FXML
	private Label ch1status;

	@FXML
	private Label ch2status;

	@FXML
	private Label ch3status;
	Map<String, Object> testdata;
	
	int Delaytime;
	int dropper;

	void setTestStd() {

		ArrayList<String> keys = new ArrayList<>(Myconstant.map.keySet());
		System.out.println("Keys : " + keys);

		teststd = Myconstant.getStd();
		testdata = Myconstant.getCurrentChamberMap();
		// 1 = astm , 2 = iso
		System.out.println("Test data :" + testdata);
		chamber = Integer.parseInt(testdata.get("chamber").toString());
		sampleid = testdata.get("sampleid").toString();

		Myconstant.currentChamberMap = testdata;
		lblfilename.setText(sampleid);

		if (chamber == 1) {
			ch1name.setText(sampleid);
			ch1status.setText("in process");
		} else if (chamber == 2) {
			ch2name.setText(sampleid);
			ch2status.setText("in process");
		} else {
			ch3name.setText(sampleid);
			ch3status.setText("in process");
		}
		Delaytime=120;
		dropper=50;
		setMode();
		
		
		
	System.out.println("Set predefined data");
		
		pressureCounts.clear();
		bpoints.clear();
		bresults.clear();
		btime.clear();

		pressureCounts.add(1146);
		pressureCounts.add(1565);
		pressureCounts.add(2439);
		pressureCounts.add(4114);
		pressureCounts.add(5606);

		bpoints.add("1.75");
		bpoints.add("3.5");
		bpoints.add("7");
		bpoints.add("14");
		bpoints.add("20");

		bresults.add("Pass");
		bresults.add("Pass");
		bresults.add("Pass");
		bresults.add("Pass");
		bresults.add("Pass");

		btime.add(""+Delaytime);
		btime.add(""+Delaytime);
		btime.add(""+Delaytime);
		btime.add(""+Delaytime);
		btime.add(""+Delaytime);

	}

	void setOnce() {

System.out.println("Sample id-------------->"+sampleid);		
		if (!Myconstant.map.containsKey("ch1")) {
			ch1name.setText("--");
			ch1status.setText("--");

		}
		else {
			Map<String, Object> data =  Myconstant.map.get("ch1");
			ch1name.setText(data.get("sampleid").toString());
				
		}
		if (!Myconstant.map.containsKey("ch2")) {
			ch2name.setText("--");
			ch2status.setText("--");

		}
		else {
			Map<String, Object> data =  Myconstant.map.get("ch2");
			ch2name.setText(data.get("sampleid").toString());
			
		}
		if (!Myconstant.map.containsKey("ch3")) {
			ch3name.setText("--");
			ch3status.setText("--");

		}
		else {
			Map<String, Object> data =  Myconstant.map.get("ch3");
			ch3name.setText(data.get("sampleid").toString());
			
		}
		
		
	}

	void setMode() {

		System.out.println("TH type : " + Myconstant.testmode);

		waittime = (int) Double.parseDouble(Myconstant.stepsize);

		waittime = waittime * 1000;
		System.out.println("step time : " + waittime);

	}

	void stopTest2() {

		sendStopCmd();
		recorddata.clear();
		recordtime.clear();
		starttest.setDisable(false);
		status.setText("Test hase been Stop");
		autotest.setDisable(false);

		bans.clear();
		isCompletetest = false;

	}

	// set all shortcut
	void addShortCut() {
		KeyCombination backevent = new KeyCodeCombination(KeyCode.B,
				KeyCombination.CONTROL_ANY);

		mainroot.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {

				if (backevent.match(ke)) {
					testabourd();
				}

				if (ke.getCode() == KeyCode.SPACE) {
					System.out.println("Record");
					recordPressure();

				}

			}
		});

	}

	void recordPressure() {
	

				Toast.makeText(Main.mainstage, "Record Successfully", 500, 100,
						100);
				recorddata.add(readpre);
				recordtime.add(readtime);

				for(int i=pressureindex-1!=0?pressureindex-1:0;i<bresults.size();i++)
				{
				bresults.remove(i);
				bresults.add(i, "Fail");
				}
				
				completeTest();

				//generateList();

		

	}

	void updateResult() {
		bresults.set(pressureindex, "Fail");
	}

	void generateList() {
		String[] suffixes =
		// 0 1 2 3 4 5 6 7 8 9
		{ "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
				// 10 11 12 13 14 15 16 17 18 19
				"th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
				// 20 21 22 23 24 25 26 27 28 29
				"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
				// 30 31
				"th", "st" };

		VBox v = new VBox(5);
		for (int i = 0; i < recorddata.size(); i++) {
			try {
				HBox h = new HBox(3);
				Label l = new Label(
						(i + 1)
								+ suffixes[i + 1]
								+ " bubble : "
								+ Myapp.getRound(DataStore
										.ConvertPressure(recorddata.get(i)), 2)
								+ DataStore.getUnitepressure());
				l.setFont(new Font(15));
				Button del = new Button();
				del.setText("Delete");
				del.setUserData(recordtime.get(i));
				del.setTooltip(new Tooltip(recorddata.get(i) + ""));

				del.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {

						Button b = (Button) arg0.getSource();
						recorddata.remove(Double.parseDouble(b.getTooltip()
								.getText()));
						recordtime.remove(Double.parseDouble(b.getUserData()
								.toString()));
						generateList();
					}
				});
				h.getChildren().add(l);
				h.getChildren().add(del);
				v.getChildren().add(h);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		scrollrecord.setPadding(new Insets(20));
		scrollrecord.setContent(v);
	}

	void generateListforRecords() {

		VBox v = new VBox(5);
		for (int i = 0; i < bpoints.size(); i++) {
			try {
				HBox h = new HBox(3);

				Label l = new Label(bpoints.get(i));
				l.setFont(new Font(15));

				Label l1 = new Label(btime.get(i));
				l1.setFont(new Font(15));

				Label l2 = new Label(bresults.get(i));
				l2.setFont(new Font(15));

				h.getChildren().add(l);
				h.getChildren().add(l1);
				h.getChildren().add(l2);

				v.getChildren().add(h);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		scrollrecord.setPadding(new Insets(20));
		scrollrecord.setContent(v);
	}

	// set hardware connection status.. and if it connected then create
	// communication bridge with it.
	void connectHardware() {
		Myapp.testtrial = "4";
		in = new SerialReader(DataStore.in);

		try {
			DataStore.serialPort.removeEventListener();
			DataStore.serialPort.addEventListener(in);
			DataStore.serialPort.notifyOnDataAvailable(true);
			setTimer();
			status.setText("Hardware Connected");

		} catch (TooManyListenersException e) {

			MyDialoug.showError(102);
			status.setText("Hardware Problem");
		} catch (Exception e) {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					status.setText("Hardware Problem");
					MyDialoug.showError(102);

				}
			});

		}

	}

	// setting all functionality and sequence.

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
System.out.println("Test Data"+Myconstant.map);
System.out.println("chamber"+Myconstant.chambers);
		setTestStd();
		setOnce();
		// Myapp.PrintAll();
		isSkiptest = new SimpleBooleanProperty(false);
		btnfail.setDisable(false);
		tones = new AudioClip(NLivetestController.class.getResource(
				"stoptone.mp3").toString());
		chamberonoff.setVisible(false);

		addShortCut();

		isRestart = new SimpleBooleanProperty(false);
		isSkiptest.set(false);

		DataStore.getconfigdata();
		conditionflow = (double) Double.parseDouble(DataStore.getFc());
		conditionpressure = 3;

		recorddata = new ArrayList<Double>();
		recordtime = new ArrayList<Double>();
		isBubbleStart = new SimpleBooleanProperty(false);
		isDryStart = new SimpleBooleanProperty(false);
	
		setMode();
		lbltesttype.setText("SYNTHETIC BLOOD PENETRATION TEST");
		lbltesttype.setText("ISO-16603");

		connectHardware();
		setButtons();
		setGauges();
		setGraph();

		isSkiptest.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean arg1, Boolean arg2) {

				if (arg2 == true) {
					bans.clear();
					completeTest();
					isSkiptest.set(false);
				}

			}
		});

		recordbtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				recordPressure();
				// savePic(videoanc);

			}
		});
		isBubbleStart.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {

				if (newValue) {
					System.out.println("bubble call");
					bubbleClicknew();
				}

			}
		});

		// setTableList();

		autotest.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				isAuto = autotest.isSelected();
				if (!autotest.isSelected()) {
					manualblock.setVisible(false);
					startautotest.setVisible(false);
				} else {

					manualblock.setVisible(true);
					startautotest.setVisible(true);
				}

			}
		});
		autotest.setSelected(true);

		starttestdry.setDisable(false);
		starttestwet.setDisable(true);

		bans = new ArrayList<>();

		tlist = new ArrayList<>();
		startautotest.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				// starttest.setDisable(true);

				// dryClick(0); //permeability

				isRestart.set(false);
				mydia = new MyDialoug(Main.mainstage,
						"/userinput/Re-testpopup.fxml");
				mydia.showDialoug();

			}
		});

		isRestart.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				restartTest();
			}
		});

		btnpass.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				result = "Pass";
				completeTest();
			}
		});

		btnfail.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				// result="Fail";
				// completeTest();
				mydia = new MyDialoug(Main.mainstage,
						"/userinput/Skiptestpopup.fxml");
				mydia.showDialoug();

			}
		});

	}

	void restartTest() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {

				status.setText("Restarting test...");
				// stopTest();
				showBubblePopup();

			}
		});
	}

	void bubbleClicknew() {
		btnfail.setDisable(false);
		status.setText("Test in Progress..");
		lblcurranttest.setText("Pressure vs Time");
		isCompletetest=false;
		flowserireswet.getData().clear();
		recorddata.clear();
		starttestdry.setDisable(true);
		pressureindex = 0;
		bans.clear();
		tlist.clear();
		recordtime.clear();;

		skip = 0;
		yAxis.setLabel("Pressure (" + DataStore.getUnitepressure() + ")");
		xAxis.setLabel("Time (Seconds)");

		tempt1 = System.currentTimeMillis();

		starttest.setDisable(true);

		testtype = 0;
		countbp = 0;
		// starttest.setVisible(false);

		t2test = System.currentTimeMillis();
		// series1.getData().clear();
		series2.getData().clear();

		// series1.getData().add(new XYChart.Data(0, conditionpressure));
		// series1.getData().add(new XYChart.Data(conditionpressure,
		// conditionpressure));
		changetime = System.currentTimeMillis();

		Toast.makeText(Main.mainstage, "Test is being initialize!", 2400, 200, 200);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				int minde = 540;
				// Mycommand.setDACValue('1', 0, 0);
				// try {
				//
				// Thread.sleep(minde);
				// } catch (Exception e) {
				//
				// }

				Mycommand.setDelay(waittime, 0);
				try {

					Thread.sleep(minde);
				} catch (Exception e) {

				}
				Mycommand.sendAdcEnableBits("001", 0);
				try {

					Thread.sleep(minde);
				} catch (Exception e) {

				}

				startCondition();
				try {

					Thread.sleep(minde);
				} catch (Exception e) {

				}
				Mycommand.startADC(0);
				try {

					Thread.sleep(minde);
				} catch (Exception e) {

				}

			}
		}).start();

	}

	// get differencial time

	void startCondition() {

		System.out.println("Chamber  " + chamber);
		if (chamber == 1) {
			openValve1();
			closeValve2();
			closeValve3();
		} else if (chamber == 2) {
			openValve2();
			closeValve1();
			closeValve3();
		} else if (chamber == 3) {
			openValve3();
			closeValve1();
			closeValve2();
		}

	}

	void endCondition() {

		try {
			Thread.sleep(1000);
		} catch (Exception ew) {

		}
		if (chamber == 1) {
			closeValve1();

		} else if (chamber == 2) {
			closeValve2();

		} else if (chamber == 3) {
			closeValve3();
		}
	}

	void openValve1() {
		System.err.println("Valve opening 1,4");

		// TODO Auto-generated method stub
		try {

			Thread.sleep(1200);
		} catch (Exception e) {

		}
		Mycommand.valveOn('1', 0);

		try {

			Thread.sleep(1200);
		} catch (Exception e) {

		}
		Mycommand.valveOn('4', 0);

		try {

			Thread.sleep(1200);
		} catch (Exception e) {

		}

	}

	void closeValve1() {

		System.err.println("Valve closing 1,4");
		try {

			Thread.sleep(900);
		} catch (Exception e) {

		}
		Mycommand.valveOff('1', 0);
		try {

			Thread.sleep(900);
		} catch (Exception e) {

		}
		Mycommand.valveOff('4', 0);

		try {

			Thread.sleep(1200);
		} catch (Exception e) {

		}

	}

	void openValve2() {
		System.err.println("Valve opening 2,5");

		// TODO Auto-generated method stub
		try {

			Thread.sleep(900);
		} catch (Exception e) {

		}
		Mycommand.valveOn('2', 0);

		try {

			Thread.sleep(900);
		} catch (Exception e) {

		}
		Mycommand.valveOn('5', 0);

		try {

			Thread.sleep(1200);
		} catch (Exception e) {

		}

	}

	void closeValve2() {

		System.err.println("Valve closing 2,5");
		try {

			Thread.sleep(1000);
		} catch (Exception e) {

		}
		Mycommand.valveOff('2', 0);
		try {

			Thread.sleep(1000);
		} catch (Exception e) {

		}
		Mycommand.valveOff('5', 0);

		try {

			Thread.sleep(1200);
		} catch (Exception e) {

		}

	}

	void openValve3() {
		System.err.println("Valve opening 3,6");
		try {

			Thread.sleep(1000);
		} catch (Exception e) {

		}
		Mycommand.valveOn('6', 0);
		try {

			Thread.sleep(1000);
		} catch (Exception e) {

		}
		Mycommand.valveOn('3', 0);
		try {

			Thread.sleep(1000);
		} catch (Exception e) {

		}

	}

	void closeValve3() {

		System.err.println("Valve closing 3,6");
		try {

			Thread.sleep(1000);
		} catch (Exception e) {

		}
		Mycommand.valveOff('6', 0);

		try {

			Thread.sleep(1000);
		} catch (Exception e) {

		}

		Mycommand.valveOff('3', 0);

		try {

			Thread.sleep(1000);
		} catch (Exception e) {

		}

	}

	double getTime() {
		double an = (double) ((System.currentTimeMillis() - tempt1) / 1000);
		return an;
	}

	double getTimeforwait() {
		double an = (double) ((System.currentTimeMillis() - changetime) / 1000);
		// System.out.println("time : " + an);
		return an;
	}

	// set label font type
	void setLabelFont() {
		lbltestdurationm.setFont(f.getM_M());
		lbltestnom.setFont(f.getM_M());
	}

	// find file last added digit
	int findInt(String[] s) {
		try {

			List<String> s1 = Arrays.asList(s);
			ArrayList<String> ss = new ArrayList<String>(s1);

			ArrayList<Integer> ll = new ArrayList<Integer>();
			for (int i = 0; i < ss.size(); i++) {
				// System.out.println(ss.get(i));

				try {
					String temp = ss.get(i).toString()
							.substring(0, ss.get(i).indexOf("."));
					String[] data = temp.split("_");
					System.out.println(temp);

					ll.add(Integer.parseInt(data[data.length - 1]));
				} catch (Exception e) {

				}

			}

			if (ll.size() > 0) {

				return Collections.max(ll) + 1;
			} else {

				return 1;
			}
			// return 0;
		} catch (Exception e) {
			return 1;
		}
	}

	List<Integer> getAdcData(List<Integer> data) {
		List<Integer> d = new ArrayList<Integer>();

		// System.out.println("READ .... ");
		for (int i = 4; i < 49; i = i + 3) {
			d.add(getIntFromBit(data.get(i), data.get(i + 1), data.get(i + 2)));

		}
		// System.out.println("READ DONE ..." + d.size());
		// System.out.println("Adc Data :" + d);
		return d;
	}

	int getIntFromBit(int a1, int a2, int a3) {
		// System.out.println(a1 + " : " + a2 + ": " + a3);
		int a = 0;

		a = a1 << 16;
		a2 = a2 << 8;
		a = a | a2;
		a = a | a3;

		return a;
	}

	int findInt1(String[] s) {
		try {
			Date d1 = new Date();
			SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("ddMMyy");
			String date1 = DATE_FORMAT.format(d1);

			List<String> s1 = Arrays.asList(s);
			ArrayList<String> ss = new ArrayList<String>(s1);

			ArrayList<Integer> ll = new ArrayList<Integer>();
			for (int i = 0; i < ss.size(); i++) {
				if (ss.get(i).contains(date1)) {
					// System.out.println(ss.get(i));

					String temp = ss.get(i).toString()
							.substring(0, ss.get(i).indexOf("."));
					String[] data = temp.split("_");
					System.out.println(temp);

					ll.add(Integer.parseInt(data[2]));

				} else {
					ss.remove(i);
				}
			}

			// return 0;
			return Collections.max(ll) + 1;
		} catch (Exception e) {
			return 1;
		}
	}

	// set round off all points
	public String getRound(Double r, int round) {

		/*
		 * if (round == 2) { r = (double) Math.round(r * 100) / 100; } else if
		 * (round == 3) { r = (double) Math.round(r * 1000) / 1000;
		 * 
		 * } else { r = (double) Math.round(r * 10000) / 10000;
		 * 
		 * }
		 */
		r = (double) Math.round(r * 100) / 100;

		return r + "";

	}

	// set main graphs....
	void setGraph() {
		root.getChildren().add(sc);

		sc.setAxisSortingPolicy(SortingPolicy.Y_AXIS.NONE);
		sc.setAxisSortingPolicy(SortingPolicy.X_AXIS.NONE);

		sc.setAnimated(false);
		sc.setLegendVisible(false);
		yAxis.setLabel("Pressure");
		xAxis.setLabel("Time");
		sc.setCreateSymbols(true);
		// series1.setName("Dry-Test");
		series2.setName("Wet-Test");
		sc.getData().addAll(series2);

		// sc.setTitle("Flow Vs Pressure");

		sc.prefWidthProperty().bind(root.widthProperty());
		sc.prefHeightProperty().bind(root.heightProperty());

		// xAxis.setUpperBound(conditionpressure);
		xAxis.setAutoRanging(true);
		Zoom zoom = new Zoom(sc, root);

	}

	// set pressure and flow gauges
	void setGauges() {
		gauge5 = TileBuilder
				.create()
				.skinType(SkinType.BAR_GAUGE)
				// .prefSize(TILE_WIDTH, TILE_HEIGHT)
				.minValue(0)

				.barBackgroundColor(Color.GRAY)
				.backgroundColor(Color.valueOf("#f1f1f1"))
				.maxValue(conditionpressure)
				.startFromZero(true)
				.thresholdVisible(false)
				.title("Pressure")
				.unit(DataStore.getUnitepressure())

				.textColor(Color.GRAY)
				.unitColor(Color.GRAY)
				.titleColor(Color.GRAY)
				.valueColor(Color.GRAY)
				.gradientStops(new Stop(0, Bright.BLUE),
						new Stop(0.1, Bright.BLUE_GREEN),
						new Stop(0.2, Bright.GREEN),
						new Stop(0.3, Bright.GREEN_YELLOW),
						new Stop(0.4, Bright.YELLOW),
						new Stop(0.5, Bright.YELLOW_ORANGE),
						new Stop(0.6, Bright.ORANGE),
						new Stop(0.7, Bright.ORANGE_RED),
						new Stop(0.8, Bright.RED), new Stop(1.0, Dark.RED))
				.strokeWithGradient(true).animated(true).build();

		gauge5.setMaxValue(DataStore.ConvertPressure(conditionpressure));
		gauge5.prefHeight(guages.getPrefHeight());
		gauge5.prefWidth(guages.getPrefWidth());
		gauge5.maxHeight(guages.getPrefHeight());
		gauge5.maxWidth(guages.getPrefWidth());
		gauge5.minHeight(guages.getPrefHeight());
		gauge5.minWidth(guages.getPrefWidth());
		guages.getChildren().add(gauge5);

		DataStore.livepressure.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {

				String pp = "" + newValue;

				Platform.runLater(new Runnable() {

					@Override
					public void run() {

						gauge5.setValue(DataStore
								.ConvertPressure((double) newValue));

					}
				});

			}

		});

	}

	void setTimer() {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {

				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						showBubblePopup();

						// bubbleClicknew();
					}
				});
			}

		};
		timer.schedule(task, 2000);
	}

	// set all button events
	void setButtons() {
		btninfo.getStyleClass().add("transperant_comm");
		btnabr.getStyleClass().add("transperant_comm");
		startautotest.getStyleClass().add("transperant_comm");
		btnfail.getStyleClass().add("transperant_comm");

		btninfo.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				testinfo();
			}
		});

		btnabr.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				testabourd();
			}
		});

	}

	// set test stop popup
	void testinfo() {

		mydia = new MyDialoug(Main.mainstage, "/userinput/Testinfopopup.fxml");

		mydia.showDialoug();

	}

	// stop test popup
	void testabourd() {

		mydia = new MyDialoug(Main.mainstage, "/userinput/Nabourdpopup.fxml");
		// mydia = new MyDialoug(Main.mainstage, "/userinput/Nresult.fxml");

		mydia.showDialoug();

	}

	// data send to MCU
	public void sendData(writeFormat w) {
		System.out.println("Sending Data......");
		w.showData();
		Thread t = new Thread(new SerialWriter(DataStore.out, w));
		t.start();

	}

	// send data to MCU after some delay
	void sendData(writeFormat w, int slp) {
		System.out.println("Sending Data......");
		w.showData();
		Thread t = new Thread(new SerialWriter(DataStore.out, w, slp));
		try {

			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// set value to ascii and package
	public static List<Integer> getValueList(int val) {
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

	// set all incomming packet event...
	public class SerialReader implements SerialPortEventListener {

		InputStream in;
		int ind = 0;
		List<Integer> readData = new ArrayList<Integer>();

		public SerialReader(InputStream in) {
			this.in = in;
			DataStore.getconfigdata();
		}

		public void serialEvent(SerialPortEvent arg0) {
			int data;
			try {
				int len = 0;
				char prev = '\0';
				// System.out.println("Reading Started:");

				while ((data = in.read()) > -1) {

					if (data == '\n' && prev == 'E') {
						break;
					}
					if (len > 0 || (data == '\r' && prev == '\n')) {
						readData.add(data);

						len++;
					}
					prev = (char) data;
					// System.out.print(prev);

					// System.out.print(new String(buffer,0,len));
				}

				for (int i = 1; i < readData.size(); i++) {

					if (readData.get(i) == 'F'
							&& readData.get(i + 1) == (int) 'M'
							&& readData.get(i + 2) == (int) 'A') {
						double pr = 0, fl = 0;
						List<Integer> reading = getAdcData(readData);

						int maxpre = Integer.parseInt(DataStore.getPg1());
						pr = (double) reading.get(2) * maxpre / 65535;

						// if (DataStore.getUnitepg1().equals("bar")) {
						// pr = DataStore.barToPsi(pr);
						// } else if (DataStore.getUnitepg1().equals("torr")) {
						// pr = DataStore.torrToPsi(pr);
						// }

						// System.out.println("Pr original : " + pr);
						// if (DataStore.isabsolutepg1()) {
						// pr = pr - 14.6;
						// if (pr < 0) {
						// pr = 0;
						// }
						// }

						// System.out.println("Pr after update: " + pr);

						// System.out.println("" + reading);

						// System.out.println("Pr : " + pr);
						DataStore.livepressure.set(pr);
						if (testtype != 5) {
							// setBubblePoints(pr);
							showBubble(pr);
						}

					}

					readData.clear();
					break;

				}

			} catch (IOException e) {
				DataStore.serialPort.removeEventListener();
				MyDialoug.showErrorHome(103);

				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						status.setText("Connection has been lost");
					}
				});
				System.out.println("Live screen error :" + e.getMessage());

			}

		}

	}

	void showBubble(double pr) {

		// System.out.println("Mode 2");
		// System.out.println("pressue  : "+pr);
		readpre = pr;
		readtime = getTime();

		bans.add("" + pr);
		
		tlist.add("" + readtime);

		
		System.out.println("Pressure index : "+pressureindex);
		
		System.out.println("Record size : "+bans.size());
		if (getTimeforwait() >= Delaytime) {

			if (pressureindex == 5) {
				completeTest();
			} else {
				System.out.println("now switching");
				//System.out.println("set Dac to "
				//		+ pressureCounts.get(pressureindex));
				Mycommand.setDACValue('2', pressureCounts.get(pressureindex),
						100);
				pressureindex++;
				changetime = System.currentTimeMillis();
			}
			// openValve24();
		}

		Platform.runLater(new Runnable() {

			@Override
			public void run() {

				series2.getData().add(new XYChart.Data(readtime, ""+DataStore.ConvertPressure(pr)));
			
			}
		});


		

		double diff = (double) curpress * dropper / 100;

		System.out.println("High last : " + curpress);
		 System.out.println("Current  : " + pr);

		 System.out.println("Diff : " + (curpress - diff));
		if (pr < (curpress - diff) && bans.size()>3) {
			System.out.println("Drop");
			isCompletetest = true;
		}

		if (isCompletetest) {
			completeTest();
//			Platform.runLater(new Runnable() {
//
//				@Override
//				public void run() {
//					starttest.setDisable(false);
//				}
//			});
		}

		curpress = pr;
	}

	void completeTest() {
		testtype = 5;
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				sendStopCmd();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				createCsvTableBubble();
			}
		}).start();
	
	

	}

	// csv create function
	void createCsvTableBubble() {

		if (bans.size() != 0) {
			try {

				System.out.println("csv creating........");
				CsvWriter cs = new CsvWriter();

				Date d1 = new Date();
				SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("ddMMyy");
				String date1 = DATE_FORMAT.format(d1);

				File fff = new File("TableCsvs");
				if (!fff.exists()) {
					fff.mkdir();
				}

				File fffff = new File("TableCsvs/" + Myapp.uid);
				if (!fffff.exists()) {
					fffff.mkdir();
				}

				File f = new File(fffff.getPath() + "/" + sampleid);
				if (!f.isDirectory()) {
					f.mkdir();
					System.out.println("Dir csv folder created");
				}

				String[] ff = f.list();

				CalculatePorometerData c = new CalculatePorometerData();

				cs.wtirefile(f.getPath() + "/" + sampleid + "_" + findInt(ff)
						+ ".csv");

				if (recorddata.size() == 0) {
					result = "Pass";
				} else {
					result = "Fail";
				}

				cs.firstLine("blood");
				cs.newLine("testname", "blood");
				cs.newLine("result", result);
				cs.newLine("bpressure", "" + curpress);
				cs.newLine("sample", sampleid);
				//cs.newLine("fluidname", Myapp.fluidname);
				//cs.newLine("fluidvalue", Myapp.fluidvalue);
				//cs.newLine("mode", "" + Myapp.thresold);
				cs.newLineDouble("recordy", recorddata);
				cs.newLineDouble("recordx", recordtime);

				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

				Date date = new Date();
				t1test = System.currentTimeMillis();
				int s = (int) (t1test - t2test) / 1000;

				int hour = (s / 3600);
				int min = (s / 60) % 60;
				int remsec = (s % 60);
				String durr = "";
				if (hour != 0) {
					durr = hour + " hr:" + min + " min:" + remsec + " sec";
				} else {
					durr = min + " min:" + remsec + " sec";
				}

				cs.newLine("duration", durr);
				cs.newLine("durationsecond", s + "");
				cs.newLine("testtime", timeFormat.format(date));
				cs.newLine("testdate", dateFormat.format(date));
				//cs.newLine("customerid", Myapp.uid);

				//cs.newLine("indistry", Myapp.indtype);
				//cs.newLine("application", Myapp.materialapp);
				//cs.newLine("splate", Myapp.splate);

				cs.newLine("ans", bans);
				cs.newLine("t", tlist);

				
				
				
				if(!bpoints.contains("0"))
				{
				btime.add(0, "5");
				bpoints.add(0, "0");
				bresults.add(0, "Pass");
				}
				
				
				cs.newLine("btime", btime);
				cs.newLine("bpoints", bpoints);
				cs.newLine("bresult", bresults);

				savefile = new File(cs.filename);
				cs.closefile();

				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub

						if (chamber == 1) {
							ch1status.setText(result);
						} else if (chamber == 2) {
							ch2status.setText(result);
						} else {
							ch3status.setText(result);
						}

						if (Myconstant.hasMoreChamber()) {
							curpress = 0;
							setTestStd();
							showBubblePopup();
						} else {
							Toast.makeText(Main.mainstage, "Test Completed",
									1000, 200, 200);
							MyDialoug.closeDialoug();
							Openscreen.open("/application/first.fxml");

						}
						// showResultPopup();
					}
				});
				isCompletetest = false;
				System.out.println("csv Created");

			} catch (Exception e) {
				e.printStackTrace();

				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub

						if (chamber == 1) {
							ch1status.setText("cancle");
						} else if (chamber == 2) {
							ch2status.setText("cancle");
						} else {
							ch3status.setText("cancle");
						}

						if (Myconstant.hasMoreChamber()) {
							curpress = 0;
							setTestStd();
							showBubblePopup();
						} else {
							Toast.makeText(Main.mainstage, "Test Completed",
									1000, 200, 200);
							MyDialoug.closeDialoug();
							Openscreen.open("/application/first.fxml");

						}
						// showResultPopup();
					}
				});
			}
		} else {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					if (chamber == 1) {
						ch1status.setText("cancle");
					} else if (chamber == 2) {
						ch2status.setText("cancle");
					} else {
						ch3status.setText("cancle");
					}

					if (Myconstant.hasMoreChamber()) {
						curpress = 0;
						setTestStd();
						showBubblePopup();
					} else {
						Toast.makeText(Main.mainstage, "Test Completed",
								1000, 200, 200);
						MyDialoug.closeDialoug();
						Openscreen.open("/application/first.fxml");

					}
					// showResultPopup();
				}
			});

		}
		// LoadAnchor.LoadCreateTestPage();
		// LoadAnchor.LoadReportPage();
	}

	// show result popup
	void showResultPopup() {
		tones.play();
		mydia = new MyDialoug(Main.mainstage, "/userinput/popupresult.fxml");

		mydia.showDialoug();
		System.out.println("Result show");
	}

	// show start test popup
	void showBubblePopup() {

		mydia = new MyDialoug(Main.mainstage, "/userinput/Wetpopup.fxml");

		mydia.showDialoug();
	}

	// send stop protocol to MCU
	void sendStopCmd() {
		testtype = 5;
		
		Mycommand.setDACValue('2', 0, 0);
		Mycommand.stopADC(1000);
		endCondition();

	}

}
