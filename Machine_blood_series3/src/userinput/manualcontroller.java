
package userinput;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TooManyListenersException;

import ConfigurationPart.NConfigurePageController;
import application.DataStore;
import application.Database;
import application.Main;
import application.Myapp;
import application.SerialWriter;
import application.writeFormat;
import communicationProtocol.Mycommand;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Gauge.SkinType;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.Section;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import toast.MyDialoug;
import toast.Openscreen;

public class manualcontroller implements Initializable {

	public static SimpleStringProperty fm1, fm2, pg1, pg2;
	public static SimpleStringProperty fm1count, fm2count, pg1count, pg2count;
	int a = 0;
	double b ;
	@FXML
	ToggleButton recordbtn;

	@FXML
	AnchorPane root;

	@FXML
	Label lblfm1, lblfm2, pg1value, labp1, labp2, lblfm1max, lblfm2max,
			lblpg1max, lblpg2max;

	@FXML
	Label lblanc, lblanc2, lblconnection, lblrealeas;

	@FXML
	AnchorPane ap, ap1, ap2, ap3, ap4, ap5, ap6, ap7;

	@FXML
	Button setpr, setfc, btnback, btnreconnect;

	@FXML
	TextField pr, fc;

	@FXML
	ImageView v1, v2, v3, v4, v5new, 
			 imgv5, onoffimg;

	@FXML
	ToggleButton valve1, valve2, valve3, valve4, valve5new, valveonoff;

	public static ToggleButton valve1s, valve2s, valve3s, valve4s;

	public Gauge gauge23, gauge23c, gauge20, gauge12, gauge12c, gauge12c1,
			gauge12c2;
	writeFormat wrD;

	@FXML
	Circle concircle;

	SerialReader in;
	@FXML
	private Label lblpg1c, lblpg2c, lblfm1c, lblfm2c, lblpg1o, lblpg2o,
			lblfm1o, lblfm2o;
	

    @FXML
    private Rectangle rectmain;
	

	final BooleanProperty spacePressed = new SimpleBooleanProperty(false);
	final BooleanProperty rightPressed = new SimpleBooleanProperty(false);
	final BooleanBinding spaceAndRightPressed = spacePressed.and(rightPressed);

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	void connectHardware() {
		if (DataStore.connect_hardware.get()) {
			valveonoff.setDisable(false);
			lblconnection.setText("Connected with  : " + DataStore.getCom());
			lblconnection.setTextFill(Paint.valueOf("#2A91D8"));
			concircle.setStyle("-fx-fill: #2A91D8;");
			
			try {
				in = new SerialReader(DataStore.in);

				DataStore.serialPort.removeEventListener();
				DataStore.serialPort.addEventListener(in);
				DataStore.serialPort.notifyOnDataAvailable(true);

			} catch (TooManyListenersException e) {

				MyDialoug.showError(102);

			} catch (Exception e) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub

						MyDialoug.showError(102);

					}
				});

			}

		} else {
			valveonoff.setDisable(true);
			lblconnection.setText("Not Connected");
			lblconnection.setTextFill(Paint.valueOf("#939598"));
			concircle.setStyle("-fx-fill: #939598;");
			
			

			MyDialoug.showError(102);

		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		
		
		setkeyboardmode();

		if (NConfigurePageController.bolkey) {
			setClickkeyboard();
			System.out.println("Trure");

		} else {
			System.out.println("False");
		
		}
		
		
		/* Manual Controller */
		
		addShortCut();

		fm1 = new SimpleStringProperty("0");
		fm2 = new SimpleStringProperty("0");
		pg1 = new SimpleStringProperty("0");
		pg2 = new SimpleStringProperty("0");
		fm1count = new SimpleStringProperty("0");
		fm2count = new SimpleStringProperty("0");
		pg1count = new SimpleStringProperty("0");
		pg2count = new SimpleStringProperty("0");

		
		recordbtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				if (recordbtn.isSelected()) {
					recordbtn.setText("Pause");
				} else {

					recordbtn.setText("Record");
				}

			}
		});

		//connectHardware();

		// addDataToTable();
		Image image1 = new Image(this.getClass().getResourceAsStream(
				"/userinput/btnimg.png"));
		setpr.setGraphic(new ImageView(image1));
		setfc.setGraphic(new ImageView(image1));

		Image image = new Image(this.getClass().getResourceAsStream(
				"/userinput/valve OFF.png"));

		valveonoff.setText("START");
		recordbtn.setText("Record");
		valveonoff.setStyle("-fx-background-color:  #2A91D8;");
		Image image2 = new Image(this.getClass().getResourceAsStream(
				"/userinput/starticon.png"));
		valveonoff.setGraphic(new ImageView(image2));

		v1.setImage(image);
		v2.setImage(image);
		v3.setImage(image);
		v4.setImage(image);
		v5new.setImage(image);

		DataStore.getconfigdata();

		valve1s = valve1;
		valve2s = valve2;
		valve3s = valve3;
		valve4s = valve4;

		pg1count.addListener(new ChangeListener() {

			@Override
			public void changed(ObservableValue observable, Object oldValue,
					Object newValue) {

				javafx.application.Platform.runLater(new Runnable() {

					@Override
					public void run() {
						lblpg1c.setText(pg1count.get());
					
					}
				});
			}
		});
		pg2count.addListener(new ChangeListener() {

			@Override
			public void changed(ObservableValue observable, Object oldValue,
					Object newValue) {

				javafx.application.Platform.runLater(new Runnable() {

					@Override
					public void run() {
						lblpg2c.setText(pg2count.get());
					
					
					}
				});
			}
		});
		fm1count.addListener(new ChangeListener() {

			@Override
			public void changed(ObservableValue observable, Object oldValue,
					Object newValue) {

				javafx.application.Platform.runLater(new Runnable() {

					@Override
					public void run() {
						lblfm1c.setText(fm1count.get());
					
					}
				});
			}
		});
		fm2count.addListener(new ChangeListener() {

			@Override
			public void changed(ObservableValue observable, Object oldValue,
					Object newValue) {

				javafx.application.Platform.runLater(new Runnable() {

					@Override
					public void run() {
					lblfm2c.setText(fm2count.get());
						
					
					}
				});
			}
		});
		DataStore.spg2.addListener(new ChangeListener() {

			@Override
			public void changed(ObservableValue observable, Object oldValue,
					Object newValue) {

				javafx.application.Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						labp2.setText("" + round(DataStore.spg2.get(), 2));
						lblpg2o.setText("" + round(DataStore.spg2.get(), 2));
					}
				});
			}
		});
		DataStore.spg1.addListener(new ChangeListener() {

			@Override
			public void changed(ObservableValue observable, Object oldValue,
					Object newValue) {

				javafx.application.Platform.runLater(new Runnable() {

					@Override
					public void run() 
					{
						// TODO Auto-generated method stub
						labp1.setText("" + round(DataStore.spg1.get(), 2));
						lblpg1o.setText("" + round(DataStore.spg1.get(), 2));

					}
				});
			}
		});

		DataStore.sfm1.addListener(new ChangeListener() {

			@Override
			public void changed(ObservableValue observable, Object oldValue,
					Object newValue) {

				javafx.application.Platform.runLater(new Runnable() {

					@Override
					public void run() 
					{
						// TODO Auto-generated method stub
						int v = (int) DataStore.sfm1.get();
						lblfm1.setText("" + v);

						lblfm1o.setText("" + v);
					}
				});
			}
		});

		DataStore.sfm2.addListener(new ChangeListener() {

			@Override
			public void changed(ObservableValue observable, Object oldValue,
					Object newValue) {

				javafx.application.Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						int v1 = (int) DataStore.sfm2.get();
						lblfm2.setText("" + v1);
						lblfm2o.setText("" + v1);
						
					}
				});
			}
		});

		// valve1.selectedProperty().bind(DataStore.sv1);
		// valve2.selectedProperty().bind(DataStore.sv2);
		// valve3.selectedProperty().bind(DataStore.sv3);
		// valve4.selectedProperty().bind(DataStore.sv4);

		DataStore.connect_hardware.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				// TODO Auto-generated method stub

				if (newValue) {
					lblconnection.setText("Connected with  : "
							+ DataStore.getCom());
					lblconnection.setTextFill(Paint.valueOf("#00ff00"));
				} else {
					lblconnection.setText("Not Connected");
					lblconnection.setTextFill(Paint.valueOf("#ff0000"));
					MyDialoug.showError(102);
				}

			}
		});

		btnback.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub

				Openscreen.open("/application/first.fxml");

			}
		});

		btnreconnect.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				connectHardware(DataStore.getCom());

			}
		});

		valve1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				if (valve1.isSelected()) {
					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/valve ON.png"));
					v1.setImage(image);


					
					Mycommand.valveOn('6', 0);
					
				

				} else {


					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/valve OFF.png"));
					v1.setImage(image);
					Mycommand.valveOff('6', 0);
				}

			}
		});

		valve2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				if (valve2.isSelected()) {
					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/valve ON.png"));

					v2.setImage(image);

					
					Mycommand.valveOn('3', 0);
				
				} else {

					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/valve OFF.png"));
					v2.setImage(image);
					Mycommand.valveOff('3', 0);
				}

			}
		});

		valve3.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				if (valve3.isSelected()) {
					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/valve ON.png"));
					v3.setImage(image);

					

					Mycommand.valveOn('4', 0);
				} else {
					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/valve OFF.png"));
					v3.setImage(image);

					


					Mycommand.valveOff('4', 0);
				}

			}
		});
		valve4.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				if (valve4.isSelected()) {
					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/valve ON.png"));
					v4.setImage(image);
					lblrealeas.setVisible(true);
					imgv5.setVisible(true);


					Mycommand.valveOn('5', 0);
				} else {
					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/valve OFF.png"));
					v4.setImage(image);
					lblrealeas.setVisible(false);
					imgv5.setVisible(false);


					Mycommand.valveOff('5', 0);
				}

			}
		});

		valve5new.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				if (valve5new.isSelected()) {

					Mycommand.valveOn('2', 0);
					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/valve ON.png"));
					v5new.setImage(image);

				} else {

				

					Mycommand.valveOff('2', 0);
					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/valve OFF.png"));
					v5new.setImage(image);

				}

			}
		});

		valveonoff.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				if (valveonoff.isSelected()) {

					rectmain.setVisible(false);
					valveonoff.setText("STOP");
					valveonoff.setStyle("-fx-background-color: #939598;");
					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/stopicon.png"));

					valveonoff.setGraphic(new ImageView(image));

					wrD = new writeFormat();
					wrD.addChar('S');
					wrD.addChar('M');
					wrD.addBlank(3);
					wrD.addLast();
					sendData(wrD);

				} else {
					rectmain.setVisible(true);
					valveonoff.setText("START");
					valveonoff.setStyle("-fx-background-color:#2A91D8");
					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/starticon.png"));
					valveonoff.setGraphic(new ImageView(image));

					

					imgv5.setVisible(false);

					wrD = new writeFormat();
					wrD.addChar('X');
					wrD.addChar('M');
					wrD.addBlank(3);
					// wrD.stopTN();
					wrD.addLast();
					sendData(wrD);

				}

			}
		});

		int prval = Integer.parseInt(DataStore.getPr());

		List<Double> da = new ArrayList<>();
		for (int j = 1; j <= 6; j++) {
			double d = prval / 6;
			da.add(d * j);
		}

		Gauge gauge1 = GaugeBuilder
				.create()
				.skinType(SkinType.SIMPLE)
				.needleColor(Color.WHITE)
				// Dial Color
				.needleBorderColor(Color.web("#228CCC"))
				// Dial Border
				.foregroundBaseColor(Color.WHITE)
				// Value Color
				.sections(
						new Section(0, da.get(0), "0", Color.web("#E7E7E8")),
						new Section(da.get(0), da.get(1), "1", Color
								.web("#C4D0E2")),
						new Section(da.get(1), da.get(2), "2", Color
								.web("#A4BBDB")),
						new Section(da.get(2), da.get(3), "3", Color
								.web("#83A9D6")),
						new Section(da.get(3), da.get(4), "4", Color
								.web("#5F99D0")),
						new Section(da.get(4), prval, "5", Color.web("#228CCC")))
				// .title("700")
				// .value(20)
				.maxValue(prval).animated(true).build();

		int fcval = Integer.parseInt(DataStore.getFc());

		List<Double> dfc = new ArrayList<>();
		for (int j = 1; j <= 6; j++) {
			double d = fcval / 6;
			dfc.add(d * j);
		}
		gauge1.setPrefSize(ap.getPrefWidth(), ap.getPrefHeight());
		ap.getChildren().add(gauge1);

		Gauge gauge2 = GaugeBuilder.create().skinType(SkinType.SIMPLE)
				.skinType(SkinType.SIMPLE)
				.needleColor(Color.WHITE)
				// Dial Color
				.needleBorderColor(Color.web("#228CCC"))
				// Dial Border
				.foregroundBaseColor(Color.WHITE)
				// Value Color
				.maxValue(fcval)

				.sections(
						new Section(0, dfc.get(0), "0", Color.web("#E7E7E8")),
						new Section(dfc.get(0), dfc.get(1), "1", Color
								.web("#C4D0E2")),
						new Section(dfc.get(1), dfc.get(2), "2", Color
								.web("#A4BBDB")),
						new Section(dfc.get(2), dfc.get(3), "3", Color
								.web("#83A9D6")),
						new Section(dfc.get(3), dfc.get(4), "4", Color
								.web("#5F99D0")),
						new Section(dfc.get(4), fcval, "5", Color
								.web("#228CCC")))

				.animated(true).build();
		gauge2.setPrefSize(ap2.getPrefWidth(), ap2.getPrefHeight());
		ap2.getChildren().add(gauge2);

		Gauge gauge3 = GaugeBuilder
				.create()
				.skinType(SkinType.DASHBOARD)
				.animated(true)
				// .title("Pressure")
				.barColor(Color.CRIMSON)
				.valueColor(Color.WHITE)
				.titleColor(Color.GRAY)
				.unitColor(Color.GRAY)
				.thresholdVisible(false)
				.shadowsEnabled(true)
				.gradientBarEnabled(true)
				.gradientBarStops(new Stop(0.00, Color.BLUE),
						new Stop(0.25, Color.CYAN), new Stop(0.50, Color.LIME),
						new Stop(0.75, Color.YELLOW), new Stop(1.00, Color.RED))
				.build();

		gauge3.setPrefSize(ap3.getPrefWidth(), ap3.getPrefHeight());
		
		gauge3.setMaxValue(Double.parseDouble(DataStore.getFm1()));
		gauge3.valueProperty().bind(DataStore.sfm1);

		ap3.getChildren().add(gauge3);

		// PG-2

		Gauge gauge4 = GaugeBuilder
				.create()
				.skinType(SkinType.DASHBOARD)
				.animated(true)
				// .title("Pressure")
				.barColor(Color.CRIMSON)
				.valueColor(Color.WHITE)
				.titleColor(Color.GRAY)
				.unitColor(Color.GRAY)
				.thresholdVisible(false)
				.threshold(35)
				.shadowsEnabled(true)
				.gradientBarEnabled(true)

				.gradientBarStops(new Stop(0.00, Color.BLUE),
						new Stop(0.25, Color.CYAN), new Stop(0.50, Color.LIME),
						new Stop(0.75, Color.YELLOW), new Stop(1.00, Color.RED))
				.build();

		gauge4.setMaxValue(Double.parseDouble(DataStore.getFm2()));
		gauge4.valueProperty().bind(DataStore.sfm2);
		gauge4.setPrefSize(ap4.getPrefWidth(), ap4.getPrefHeight());
		ap4.getChildren().add(gauge4);

		// PG3........................
		Gauge gauge5 = GaugeBuilder
				.create()
				.skinType(SkinType.DASHBOARD)
				.animated(true)
				// .title("Pressure")
				.unit("\u00B0C")
				// .value(0.50)
				// .maxValue(40)
				.barColor(Color.CRIMSON)
				.valueColor(Color.WHITE)
				.titleColor(Color.GRAY)
				.unitColor(Color.GRAY)
				.thresholdVisible(false)
				.threshold(35)
				.shadowsEnabled(true)
				.gradientBarEnabled(true)
				.gradientBarStops(new Stop(0.00, Color.BLUE),
						new Stop(0.25, Color.CYAN), new Stop(0.50, Color.LIME),
						new Stop(0.75, Color.YELLOW), new Stop(1.00, Color.RED))
				.build();

		gauge5.setMaxValue(Integer.parseInt(DataStore.getPg2()));
		gauge5.valueProperty().bind(DataStore.spg2);
		lblpg2max.setText("PG2" + "\n" + Integer.parseInt(DataStore.getPg2())
				+ " ("+DataStore.getUnitepg2()+")");
		
		gauge5.setPrefSize(ap6.getPrefWidth(), ap6.getPrefHeight());
		ap6.getChildren().add(gauge5);

		// PG-4

		Gauge gauge6 = GaugeBuilder
				.create()
				.skinType(SkinType.DASHBOARD)
				.animated(true)

				.barColor(Color.CRIMSON)
				.valueColor(Color.WHITE)
				.titleColor(Color.GRAY)
				.unitColor(Color.GRAY)
				.thresholdVisible(false)
				.threshold(35)
				.shadowsEnabled(true)
				.gradientBarEnabled(true)
				.gradientBarStops(new Stop(0.00, Color.BLUE),
						new Stop(0.25, Color.CYAN), new Stop(0.50, Color.LIME),
						new Stop(0.75, Color.YELLOW), new Stop(1.00, Color.RED))
				.maxValue(Integer.parseInt(DataStore.getPg1())).build();

		gauge6.setMaxValue(Integer.parseInt(DataStore.getPg1()));
		lblpg1max.setText("PG1" + "\n" + Integer.parseInt(DataStore.getPg1())
				+ " ("+DataStore.getUnitepg2()+")");
		gauge6.setPrefSize(ap5.getPrefWidth(), ap5.getPrefHeight());
		gauge6.valueProperty().bind(DataStore.spg1);
		// pg2value.textProperty().bind(DataStore.sspg2);

		// labp1.textProperty().bind(DataStore.sspg1);
		// labp2.textProperty().bind(DataStore.sspg2);

		ap5.getChildren().add(gauge6);

		pr.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				setPR(gauge1);
			}
		});

		fc.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				setFC(gauge2);

			}
		});

		setpr.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				setPR(gauge1);

			}
		});
		setfc.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub

				setFC(gauge2);
			}
		});
		
		
		
		
		
		
		
		
	}
	
	/* Virtual Keyboard Textbox Click Event. */
	void setClickkeyboard() {
	pr.setOnMouseClicked(new EventHandler<Event>() {

		@Override
		public void handle(Event event) {
			// TODO Auto-generated method stub
			setVitual(pr, null, "Pressure Regulator", null);
		}
	});
	
	fc.setOnMouseClicked(new EventHandler<Event>() {

		@Override
		public void handle(Event event) {
			// TODO Auto-generated method stub
			setVitual(fc, null, "Flow controller", null);
		}
	});
	}
	
	

	void addShortCut() {
		KeyCombination backevent = new KeyCodeCombination(KeyCode.B,
				KeyCombination.CONTROL_ANY);

		root.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {

				if (backevent.match(ke)) {

					Openscreen.open("/application/first.fxml");
				}

			}
		});

	}

	void setPR(Gauge gauge1) {

		try {
			double d1 = Double.parseDouble(pr.getText());
			double d = (double) 65535 * d1
					/ Integer.parseInt(DataStore.getPr());
			System.out.println("Sending  : " + (int) d);
			List<Integer> ss = getValueList((int) d);
			wrD = new writeFormat();
			wrD.addChar('P');
			wrD.addChar('R'); 
			wrD.addData1(ss);
			wrD.addLast();
			sendData(wrD);

			labp1.setText("" + d1);
			gauge1.setValue(d1);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void setFC(Gauge gauge2) {
		try {
		
			double d1 = Double.parseDouble(fc.getText());
			// double d=Double.parseDouble(pr.getText());

			double d = (double) 65535 * d1
					/ Integer.parseInt(DataStore.getFc());
			System.out.println("Sending  : " + (int) d);

			List<Integer> ss = getValueList((int) d);
			wrD = new writeFormat();
			wrD.addChar('F');
			wrD.addChar('C');
			wrD.addData1(ss);
			wrD.addChkSm();
			wrD.addLast();
			sendData(wrD);

			gauge2.setValue(d1);
			lblanc2.setText("" + d1);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendData(writeFormat w) {
		System.out.println("Sending Data......");
		w.showData();
		Thread t = new Thread(new SerialWriter(DataStore.out, w));
		t.start();
		// start.setDisable(true);
	}

	public boolean connectHardware(String st) {

		boolean bol = false;

		// sendDataToWeb();
		Enumeration pList = CommPortIdentifier.getPortIdentifiers();

		int count = 0;

		while (pList.hasMoreElements()) {

			CommPortIdentifier cpi = (CommPortIdentifier) pList.nextElement();
			System.out.print("Port " + cpi.getName() + " " + cpi.getPortType());
			if (cpi.getName().equals(st)) {
				DataStore.connect_hardware.set(true);
				try {

					DataStore.sc.connect(st);
					bol = true;
					Myapp.hb.set(false);
					writeFormat wrD = new writeFormat();
					wrD.stopTN();
					wrD.addLast();

					sendData(wrD);

				} catch (Exception e) {

					e.printStackTrace();
				}

				break;
			}

			System.out.println("PORT :" + cpi.getName());
			count++;
		}

		DataStore.connect_hardware.set(bol);
		if (bol == false) {
			// Toast.makeText(Main.mainstage,
			// "Hardware not connected please plugout and plugin", 200, 200,
			// 3000);
		} else {
			// Toast.makeText(Main.mainstage, "Successfully Connected", 200,
			// 200, 3000);

		}

		return bol;
	}

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
					System.out.print(prev);

					// System.out.print(new String(buffer,0,len));
				}

				for (int i = 1; i < readData.size(); i++) {
				
					if (readData.get(i) == 83 && readData.get(i + 1) == (int) 'C')
					{
						// .. for fm1

						int a1, a2, a3;
						a1 = readData.get(i + 2);
						a2 = readData.get(i + 3);
						a3 = readData.get(i + 4);
						System.out.println("\nFlow Meter 1 - >  bits  : " + a1
								+ " : " + a2 + " : " + a3);

						a = a1 << 16;
						a2 = a2 << 8;
						a = a | a2;
						a = a | a3;

						// System.out.println("Flow Meter 1 :  ... :"+DataStore.getFm1());
						b=(double) a * Integer.parseInt(DataStore.getFm1()) / 65535;
					
						
						System.out.println("Flow Meter 1 :  ... :" + b);
						DataStore.sfm1.set(b);
		
						fm1count.set("" + a);

							
						if (a > 62200) {
							// SkadaController.valve1s.selectedProperty().bind(DataStore.sv1);
							System.out.println("Max flow reach to fm1");
						}
						// .... for fm2

						a = 0;
						a1 = readData.get(i + 5);
						a2 = readData.get(i + 6);
						a3 = readData.get(i + 7);

						System.out.println("\nFlow Meter 2 - >  bits  : " + a1
								+ " : " + a2 + " : " + a3);

						a = a1 << 16;
						a2 = a2 << 8;
						a = a | a2;
						a = a | a3;

						b = (double) a * Integer.parseInt(DataStore.getFm2())
								/ 65535;
						
					
						
						System.out.println("Flow Meter 2 :  ... :" + b);
						DataStore.sfm2.set(b);
					
						fm2count.set("" + a);

					

						// .... for pg1

						a = 0;
						a1 = readData.get(i + 8);
						a2 = readData.get(i + 9);
						a3 = readData.get(i + 10);
						System.out.println("\nPressure G1  -> Bit : " + a1
								+ " : " + a2 + " : " + a3);

						a = a1 << 16;
						a2 = a2 << 8;
						a = a | a2;
						a = a | a3;

						b = (double) a * Integer.parseInt(DataStore.getPg1())
								/ 65535;
						
					
						/*if(DataStore.isabsolutepg1())
						{
							b=b-14.6;
							if(b<0)
							{
								b=0;
							}
						}
						*/
				
						System.out.println("Pressure Gauge 1 :  ... :" + b);
						DataStore.spg1.set(b);
					    pg1count.set("" + a);

								
						if (a > 62200) {
							// SkadaController.valve3s.selectedProperty().bind(DataStore.sv3);
							System.out.println("Max pressure reach to PG1");
						}

						// .... for pg2

						a = 0;
						a1 = readData.get(i + 11);
						a2 = readData.get(i + 12);
						a3 = readData.get(i + 13);
						System.out.println("\nPressure G2  -> Bit : " + a1
								+ " : " + a2 + " : " + a3);

						a = a1 << 16;
						a2 = a2 << 8;
						a = a | a2;
						a = a | a3;
						b = (double) a * Integer.parseInt(DataStore.getPg2())
								/ 65535;
						
					
						
						/*if(DataStore.isabsolutepg2())
						{
							b=b-14.6;
							if(b<0)
							{
								b=0;
							}
						}
						*/
						System.out
								.println("Pressure Gauge 2 :  original   reading  : "
										+ a + "... :" + b);
						
						
						
						DataStore.spg2.set(b);

						pg2count.set("" + a);

							
						i = i + 16;

					}

					readData.clear();
					break;

				}

			} catch (IOException e) {

				DataStore.serialPort.removeEventListener();
				MyDialoug.showErrorHome(103);

				System.out.println("Live screen error :" + e.getMessage());
				e.printStackTrace();
			}

		}

	}
	
	
	/* Keyboard Mode Checking */
	void setkeyboardmode() {

		Database db = new Database();

		List<List<String>> ll = db.getData("select * from keyboardmode");
		String mode = (ll.get(0).get(0));

		if (mode.equals("true")) {

			NConfigurePageController.bolkey = true;
			System.out.println("Bol True");
			
		} else {

			NConfigurePageController.bolkey = false;
			System.out.println("Bol False");
		}
		javafx.application.Platform.runLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				try {
					Myapp.virtualStage = null;
					if (Myapp.virtualStage == null) {

						Parent root2;
						root2 = FXMLLoader.load(getClass().getResource(
								"/application/keyboard.fxml"));
						Myapp.virtualStage = new Stage();
						Myapp.virtualStage.setTitle("Keyboard");
						Myapp.virtualStage.initStyle(StageStyle.UTILITY);
						Myapp.virtualStage.initOwner(Main.mainstage);
						Myapp.virtualStage.initModality(Modality.WINDOW_MODAL);
						Myapp.virtualStage.setScene(new Scene(root2, 805, 350));
						Myapp.virtualStage.setResizable(false);

						Myapp.virtualStage
								.setOnShown(new EventHandler<WindowEvent>() {

									@Override
									public void handle(WindowEvent event) {
										// TODO Auto-generated method stub
										Myapp.keyboardinput
												.setText(Myapp.currTf.getText());
									}
								});

						Myapp.virtualStage
								.setOnCloseRequest(new EventHandler<WindowEvent>() {

									@Override
									public void handle(WindowEvent arg0) {

										Myapp.currTf
												.setText(Myapp.keyboardinput
														.getText());
									}
								});

					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}
	
	
	/* Open Keyboard Scren stage. */
	public static void setVitual(TextField tf, TextField nextTf, String lbl,
			String nextlbl) {
		try {
			// root2 =
			// FXMLLoader.load(getClass().getClassLoader().getResource("/application/keyboard.fxml"));
			Myapp.currTf = tf;
			Myapp.nextTf = nextTf;
			Myapp.nextlbl = nextlbl;
			Myapp.lblkeyboard.setText(lbl);
			Myapp.virtualStage.show();

			// Hide this current window (if this is what you want)
			// ((Node)(event.getSource())).getScene().getWindow().hide();

			// Set position of second window, related to primary window.
			// stage1.setX(primaryStage.getX() + 200);
			// stage1.setY(primaryStage.getY() + 100);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	
}
