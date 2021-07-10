package application;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXSlider;

import data_read_write.CsvWriter;
import data_read_write.DatareadN;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import myconstant.Myconstant;
import toast.MyDialoug;
import toast.Openscreen;

public class QuicktestController1 implements Initializable {

	String path = "information/recipie/";

	@FXML
	private ImageView imgdownarrow111;

	@FXML
	private ComboBox<String> cmbsampleid;

	@FXML
	private RadioButton rdmanual;

	@FXML
	private RadioButton rdautometed;

	@FXML
	private Label lblnote, lblerror;

	@FXML
	private JFXSlider stepsizeslider;

	@FXML
	private TextField txtlotno;

	@FXML
	private CheckBox cksupport;

	@FXML
	private RadioButton rdfull;

	@FXML
	private RadioButton rdsingle;

	@FXML
	private CheckBox ck1;

	@FXML
	private CheckBox ck2;

	@FXML
	private CheckBox ck3;

	@FXML
	private CheckBox ck4;

	@FXML
	private CheckBox ck5;

	@FXML
	private CheckBox ck6;

	@FXML
	private CheckBox ck7;

	@FXML
	private CheckBox ck8;

	@FXML
	Rectangle fullenable;

	List<CheckBox> allcks = new ArrayList<CheckBox>();

	static ToggleGroup tgb1, tgb2, tgbch1, tgbch2, tgbch3;

	static String ch1selectedrad = "", ch2selectedrad = "", ch3selectedrad = "";

	@FXML
	Button starttest, btncancel;

	@FXML
	AnchorPane ancaddsamplearea;

	@FXML
	private Rectangle ch1rec, ch2rec, ch3rec;

	@FXML
	private CheckBox chamer1, chamer2, chamer3;

	@FXML
	private RadioButton rdch2roll;

	@FXML
	private RadioButton rdch2garment;

	@FXML
	private RadioButton rdch3roll;

	@FXML
	private RadioButton rdch3garment;

	@FXML
	private ComboBox<String> cmbsampleid3, cmbsampleid2;

	@FXML
	private TextField txtlotno2;

	@FXML
	private TextField txtlotno3;

	@FXML
	private Rectangle recloc1;

	@FXML
	private TextField txtarea;

	@FXML
	private Rectangle recloc2;

	@FXML
	private TextField txtarea2;

	@FXML
	private Rectangle recloc3;

	@FXML
	private TextField txtarea3;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		chamer1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if (chamer1.isSelected()) {
					ch1rec.setVisible(false);

				} else {
					ch1rec.setVisible(true);
				}
			}
		});

		chamer2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if (chamer2.isSelected()) {
					ch2rec.setVisible(false);
				} else {
					ch2rec.setVisible(true);
				}

			}
		});

		chamer3.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if (chamer3.isSelected()) {
					ch3rec.setVisible(false);
				} else {
					ch3rec.setVisible(true);
				}

			}
		});

		stepsizeslider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

				double v = (double) newValue;
				Myconstant.stepsize = "" + (int) v;
				lblnote.setText("Note : This will plot " + (60 / (int) v) + " Readings in 1 Minutes.");

			}

		});

		setRdch1garment();
		setRdch2garment();
		setRdch3garment();
		// setTestMode();
		setSampleType();

		allcks.add(ck1);
		allcks.add(ck2);
		allcks.add(ck3);
		allcks.add(ck4);
		allcks.add(ck5);
		allcks.add(ck6);
		allcks.add(ck7);
		allcks.add(ck8);

		btncancel.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub

				MyDialoug.closeDialoug();

			}
		});

		starttest.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub

				teststart();

			}
		});

		cmbsampleid.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {

				loadFile(arg2);

			}
		});

		getAllFiles();
	}

	void loadFile(String file) {
		File f = new File(path + file + ".csv");
		if (f.exists()) {
			DatareadN dr = new DatareadN();
			dr.fileRead(f);
			System.out.println(dr.data);

			String testtype = dr.data.get("testmode").toString();
			String mtype = dr.data.get("mtype").toString();
			String lotno = dr.data.get("lotno").toString();
			List<String> ck = dr.getValuesOf(dr.data.get("ck").toString());
			List<String> ckname = dr.getValuesOf(dr.data.get("ckname").toString());

			String supportplate = dr.data.get("supportplate").toString();
			String stepsize = dr.data.get("stepsize").toString();

			txtlotno.setText(lotno);

			if (supportplate.equals("true")) {
				cksupport.setSelected(true);
			} else {
				cksupport.setSelected(false);
			}

			if (testtype.endsWith("1")) {
				rdmanual.setSelected(true);
			} else {
				rdautometed.setSelected(true);
			}
			if (mtype.endsWith("1")) {
				rdsingle.setSelected(true);
			} else {

				rdfull.setSelected(true);
			}

			for (int i = 0; i < allcks.size(); i++) {
				if (ck.contains(allcks.get(i).getId())) {
					allcks.get(i).setSelected(true);
				} else {

					allcks.get(i).setSelected(false);
				}

			}

			stepsizeslider.setValue(Double.parseDouble(stepsize));

		}

	}

	void setTestMode() {

		tgb1 = new ToggleGroup();

		rdmanual.setToggleGroup(tgb1);
		rdmanual.setUserData("1");
		rdautometed.setToggleGroup(tgb1);
		rdautometed.setUserData("2");

	}

	void setRdch1garment() {

		tgbch1 = new ToggleGroup();

		rdmanual.setToggleGroup(tgbch1);
		rdmanual.setUserData("1");
		rdautometed.setToggleGroup(tgbch1);
		rdautometed.setUserData("2");

		ch1selectedrad = "1";


		txtarea.setVisible(false);
		recloc1.setVisible(false);

		tgbch1.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> arg0, Toggle arg1, Toggle arg2) {
				if (arg2 == null)
					arg1.setSelected(true);
				ch1selectedrad = arg2.getUserData().toString();

				if (ch1selectedrad.equals("1")) {

					txtarea.setVisible(false);
					recloc1.setVisible(false);

				} else {
					txtarea.setVisible(true);
					recloc1.setVisible(true);
				}
			}
		});

	}

	void setRdch2garment() {

		tgbch2 = new ToggleGroup();

		rdch2roll.setToggleGroup(tgbch2);
		rdch2roll.setUserData("1");
		rdch2garment.setToggleGroup(tgbch2);
		rdch2roll.setUserData("2");

		ch2selectedrad = "1";

		txtarea2.setVisible(false);
		recloc2.setVisible(false);

		tgbch2.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> arg0, Toggle arg1, Toggle arg2) {
				if (arg2 == null)
					arg1.setSelected(true);
				ch2selectedrad = arg2.getUserData().toString();

				if (ch2selectedrad.equals("1")) {

					txtarea2.setVisible(false);
					recloc2.setVisible(false);

				} else {
					txtarea2.setVisible(true);
					recloc2.setVisible(true);
				}
			}
		});

	}

	void setRdch3garment() {

		tgbch3 = new ToggleGroup();

		rdch3roll.setToggleGroup(tgbch3);
		rdch3roll.setUserData("1");
		rdch3garment.setToggleGroup(tgbch3);
		rdch3roll.setUserData("2");

		ch3selectedrad = "1";

		txtarea3.setVisible(false);
		recloc3.setVisible(false);

		tgbch3.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> arg0, Toggle arg1, Toggle arg2) {
				if (arg2 == null)
					arg1.setSelected(true);
				ch3selectedrad = arg2.getUserData().toString();

				if (ch3selectedrad.equals("1")) {

					txtarea3.setVisible(false);
					recloc3.setVisible(false);

				} else {
					txtarea3.setVisible(true);
					recloc3.setVisible(true);
				}
			}
		});

	}

	void setSampleType() {

		tgb2 = new ToggleGroup();

		rdsingle.setToggleGroup(tgb2);
		rdsingle.setUserData("1");
		rdfull.setToggleGroup(tgb2);
		rdfull.setUserData("2");

		rdfull.setSelected(false);
		tgb2.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> arg0, Toggle arg1, Toggle arg2) {

				System.out.println("Selection changed");
				Myconstant.sampletype = arg2.getUserData().toString();

				if (Myconstant.sampletype.equals("2")) {
					fullenable.setVisible(false);

					stepsizeslider.setMax(60);
					stepsizeslider.setMin(15);
					stepsizeslider.setBlockIncrement(15);
					stepsizeslider.setMajorTickUnit(15);

					stepsizeslider.setValue(15);

				} else {
					stepsizeslider.setMax(20);
					stepsizeslider.setMin(2);
					stepsizeslider.setBlockIncrement(2);
					stepsizeslider.setMajorTickUnit(2);

					stepsizeslider.setValue(2);
					fullenable.setVisible(true);
				}

			}
		});

		rdfull.setSelected(true);
	}

	void teststart() {
Myconstant.map.clear();
Myconstant.chambers.clear();
//		Myconstant.sampleid = "" + cmbsampleid.getValue();
//		Myconstant.lotno= ""+txtlotno.getText();
//		MyDialoug.closeDialoug();
//		Openscreen.open("/userinput/Nlivetest.fxml");
//		

		List<String> cks = new ArrayList<String>();
		List<String> ckname = new ArrayList<String>();

		for (int i = 0; i < allcks.size(); i++) {
			if (allcks.get(i).isSelected()) {
				cks.add(allcks.get(i).getId());
				ckname.add(allcks.get(i).getText());
			}
		}

		String sampleid = cmbsampleid.getValue();
		String lotno = txtlotno.getText();
		String sampletype = tgb2.getSelectedToggle().getUserData().toString();
//		String testmode = tgb1.getSelectedToggle().getUserData().toString();
		

		String ch1garment = tgbch1.getSelectedToggle().getUserData().toString();
		String ch2garment = tgbch2.getSelectedToggle().getUserData().toString();
		String ch3garment = tgbch3.getSelectedToggle().getUserData().toString();

		String sampleid1 = cmbsampleid.getValue();
		String sampleid2 = cmbsampleid2.getValue();
		String sampleid3 = cmbsampleid3.getValue();
		
		
		String lotno1 = txtlotno.getText();
		String lotno2 = txtlotno2.getText();
		String lotno3 = txtlotno3.getText();
		
		boolean flag1 = true;
		boolean flag2 = true;
		boolean flag3 = true;

		if (chamer1.isSelected()) {
			if (sampleid1.isEmpty() || lotno1.isEmpty()) {
				flag1 = false;
				lblerror.setText("Please add sampleid and lotno");
			} else {

				Map<String,Object> ch1data=new HashMap<>();
				ch1data.put("sampleid", cmbsampleid.getValue());
				ch1data.put("type", ch1garment);
				ch1data.put("samplearea", txtarea.getText());
				ch1data.put("lotno", txtlotno.getText());
				ch1data.put("chamber", 1);
				Myconstant.map.put("ch1", ch1data);
				flag1 = true;
			}
		}
		
		if (chamer2.isSelected()) {
			if (sampleid2.isEmpty() || lotno2.isEmpty()) {
				flag2 = false;
				lblerror.setText("Please add sampleid and lotno");
			} else {

				Map<String,Object> ch2data=new HashMap<>();
				ch2data.put("sampleid", cmbsampleid2.getValue());
				ch2data.put("type", ch2garment);
				ch2data.put("samplearea", txtarea2.getText());
				ch2data.put("lotno", txtlotno2.getText());
				ch2data.put("chamber", 2);
				Myconstant.map.put("ch2", ch2data);
				flag2 = true;
			}
		}
		
		if (chamer3.isSelected()) {
			if (sampleid3.isEmpty() || lotno3.isEmpty()) {
				flag3 = false;
				lblerror.setText("Please add sampleid and lotno");
			} else {

				Map<String,Object> ch3data=new HashMap<>();
				ch3data.put("sampleid", cmbsampleid3.getValue());
				ch3data.put("type", ch3garment);
				ch3data.put("samplearea", txtarea3.getText());
				ch3data.put("lotno", txtlotno3.getText());
				ch3data.put("chamber", 3);
				Myconstant.map.put("ch3", ch3data);
				flag3 = true;
			}
		}
		

		if (flag1 && flag2 && flag3) {
			
			Myconstant.setDummyData();
			Openscreen.open("/userinput/Nlivetest.fxml");
			MyDialoug.closeDialoug();
		}
		/*
		 * if (flag == true) { lblerror.setText("");
		 * 
		 * if (filesave(sampleid, testmode, sampletype, lotno, cks, ckname)) {
		 * lblerror.setText("saved"); Openscreen.open("/userinput/Nlivetest.fxml");
		 * MyDialoug.closeDialoug();
		 * 
		 * } else {
		 * 
		 * lblerror.setText("not saved"); }
		 */
		// MyDialoug.closeDialoug();
//			Openscreen.open("/userinput/Nlivetest.fxml");

	}

	boolean filesave(String sampleid, String testtype, String mtype, String lotno, List<String> ck,
			List<String> ckname) {

		try {

			File check = new File(path);
			if (!check.exists()) {
				check.mkdirs();
			}

			File f = new File(path + sampleid + ".csv");
			CsvWriter cw = new CsvWriter();
			cw.wtirefile(f.getPath());
			cw.firstLine(sampleid);
			cw.newLine("testmode", testtype);
			cw.newLine("mtype", mtype);
			cw.newLine("ck", ck);
			cw.newLine("lotno", lotno);
			cw.newLine("ckname", ckname);
			cw.newLine("supportplate", cksupport.isSelected() + "");
			cw.newLine("stepsize", stepsizeslider.getValue() + "");
			cw.closefile();

			Myconstant.lotno = lotno;
			Myconstant.sampleid = sampleid;
			Myconstant.sampletype = mtype;
			Myconstant.testmode = testtype;
			Myconstant.supportplate = cksupport.isSelected() + "";
			Myconstant.stepsize = stepsizeslider.getValue() + "";

			if (mtype.equals("1")) {
				Myconstant.cknames = new ArrayList<String>();
				Myconstant.cknames.add("single");
			} else {
				Myconstant.cknames = ckname;
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	void getAllFiles() {
		try {
			File[] f = new File(path).listFiles();

			Arrays.sort(f, new Comparator<File>() {
				public int compare(File f1, File f2) {
					return Long.compare(f2.lastModified(), f1.lastModified());
				}
			});

			List<String> fname = new ArrayList<String>();
			int lastPeriodPos;

			for (int i = 0; i < f.length; i++) {

				lastPeriodPos = f[i].getName().lastIndexOf('.');
				if (lastPeriodPos > 0) {
					fname.add(f[i].getName().substring(0, lastPeriodPos));
				}

			}

			cmbsampleid.getItems().addAll(fname);

			if (fname.size() > 0) {
				cmbsampleid.getSelectionModel().select(0);
			}

		} catch (Exception e) {
			System.out.println("No file found");
		}
	}

	void saveData() {

	}

}
