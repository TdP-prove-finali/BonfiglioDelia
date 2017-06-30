package it.polito.tdp.network;

/**
 * Sample Skeleton for 'Network.fxml' Controller Class
 */

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import it.polito.tdp.network.dao.NetworkDAO;
import it.polito.tdp.network.model.City;
import it.polito.tdp.network.model.Model;
import it.polito.tdp.network.model.Store;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;


public class NetworkController {
	private Model model;

    @FXML
    private ComboBox<Integer> boxMonth;
    
    @FXML // fx:id="btnStoreKpi"
    private Button btnStoreKpi; // Value injected by FXMLLoader
	
    @FXML // fx:id="btnKpi"
    private Button btnKpi; // Value injected by FXMLLoader
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML
    private TextField txtDistrict;

    @FXML
    private TextField txtId_Istat;
    
    @FXML // fx:id="boxRegion"
    private ComboBox<String> boxRegion; // Value injected by FXMLLoader

    @FXML // fx:id="btnSearchStore"
    private Button btnSearchStore; // Value injected by FXMLLoader

    @FXML // fx:id="txtCityName"
    private TextField txtCityName; // Value injected by FXMLLoader

    @FXML // fx:id="btnStoreIn"
    private Button btnStoreIn; // Value injected by FXMLLoader

    @FXML // fx:id="id_pvd"
    private TextField id_pvd; // Value injected by FXMLLoader

    @FXML // fx:id="btnCompleteStore"
    private Button btnCompleteStore; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML // fx:id="citystore"
    private TextField citystore; // Value injected by FXMLLoader

    @FXML // fx:id="addresspvd"
    private TextField addresspvd; // Value injected by FXMLLoader

    @FXML // fx:id="btnInsert"
    private Button btnInsert; // Value injected by FXMLLoader

    @FXML // fx:id="btnDelete"
    private Button btnDelete; // Value injected by FXMLLoader

    @FXML // fx:id="txtbrand"
    private TextField txtbrand; // Value injected by FXMLLoader

    @FXML // fx:id="txtiva"
    private TextField txtiva; // Value injected by FXMLLoader
    
    @FXML // fx:id="storebox"
    private HBox storebox; // Value injected by FXMLLoader
    
    @FXML // fx:id="kpiStoreChart"
    private BarChart<String, Double> kpiStoreChart; // Value injected by FXMLLoader

    @FXML // fx:id="x"
    private CategoryAxis x; // Value injected by FXMLLoader

    @FXML // fx:id="y"
    private NumberAxis y; // Value injected by FXMLLoader
    
    @FXML
    void completeStore(ActionEvent event) {
        	txtResult.clear();
        	storebox.setVisible(false);
        	btnInsert.setVisible(false);
        	btnDelete.setVisible(false);
        	btnStoreKpi.setVisible(false);
        	
        	String pvd= id_pvd.getText();
        	
        	String[] st= pvd.split(" ");
        	
        	if(st[0].length()==10 && pvd.matches("[0-9 ]+") && st[1].length()<=6 && st[1].length()>=5){
        		
	        	List<Store> storest= new ArrayList<Store>(model.getStores());
	        	boolean ok= false;
	        	storebox.setVisible(true);
	        	btnInsert.setVisible(true);
	        	btnDelete.setVisible(true);
	        	btnStoreKpi.setVisible(true);
	        	boxMonth.setVisible(true);
	        	
	        	for(Store stemp: storest){
	        		if (stemp.getId_pvd().equals(pvd)){
	        			ok=true;
	        			
	        			model.createGraph();
	        			
	        			txtbrand.setText(stemp.getRag_social());
	        			txtiva.setText(stemp.getIva());
	        			citystore.setText(stemp.getCity_name());
	        			addresspvd.setText(stemp.getAddress());
	        			
	        			if(model.getNeighbours(stemp).size()!= 0){
	        				txtResult.appendText("Neighbours of the specified store: \n");
			        			for(Store a : model.getNeighbours(stemp))
			        				txtResult.appendText(a.toString()+"\n");
	        			} else {
	        				txtResult.appendText("This is the only store in the city\n");
	        			}
	        	    }
	        	}
	        
	        	if(ok==false){//non ho trovato il pvd con questo id
	        		model.createGraph();
	        		
	        		txtResult.appendText("The store with the id specified is not in the DB.\n");
	        		storebox.setVisible(true);
	        		btnInsert.setVisible(true);
		        	btnDelete.setVisible(false);
		        	btnStoreKpi.setVisible(false);
		        	boxMonth.setVisible(false);
		        	txtbrand.clear();
		        	txtiva.clear();
		        	citystore.clear();
		        	addresspvd.clear();
	        		}
	        } else{
	        	model.createGraph();
	        	
	        	txtResult.appendText("Please, insert 10 digits, a space and other 6 digits\n");
	        	storebox.setVisible(false);
	        	btnInsert.setVisible(false);
	        	btnDelete.setVisible(false);
	        	btnStoreKpi.setVisible(false);
	        	boxMonth.setVisible(false);
	        }
    }	


    @FXML
    void doSearchStoreForRegion(ActionEvent event) {

    	if(boxRegion.getValue() ==null){
    		txtResult.setText("Please, select a region\n");
    		return;
    	}
    	txtResult.clear();
    	String region=boxRegion.getValue();
    	
    	List<Store> stores= new ArrayList<Store>(model.getStoresForRegion(region));
	    
	    	for(Store stemp: stores)
		    		txtResult.appendText(stemp.toString()+"\n");  
	    
    }

    @FXML
    void doSearchStoreInCity(ActionEvent event) {
    	txtDistrict.clear();
    	txtId_Istat.clear();
    	txtResult.clear();
    	
    	String city_name = txtCityName.getText().toUpperCase();
    	
    	if(!city_name.matches("[a-zA-Z ]+")){
    		txtResult.setText("Please insert a City name, with only alphabetic letters\n");
    		return;
    	}
    	List<Store> stores= new ArrayList<Store>(model.getStoresForCity(city_name));
    	if(stores.size()!=0){		
	    	for(Store stemp: stores)
		    		txtResult.appendText(stemp.toString()+"\n"); 
	     } else 
	    	 txtResult.appendText("There are not shops in the city selected\n");
	   
	    City c = model.getCity(city_name);	
	    if(c!=null){
		    txtDistrict.setText(c.getDistrict_name());
		    txtId_Istat.setText(c.getId_istat());
	    } else{
	    	txtResult.setText("The city written is absent in DB");
	    }
    }

    @FXML
    void doDelete(ActionEvent event) {
    	String pvd= id_pvd.getText();
    	txtResult.clear();
    	
    	if(model.removePvd(pvd)){
    		txtResult.setText("Store removed successfully from the DB\n");
    		storebox.setVisible(false);
        	btnInsert.setVisible(false);
        	btnDelete.setVisible(false);
        	
    	} 	else
    		txtResult.setText("Something went wrong\n");
    }

    @FXML
    void doInsert(ActionEvent event) {
    	txtResult.clear();
    	
    	String pvd= id_pvd.getText();
    	String brand = txtbrand.getText();
    	String iva = txtiva.getText();
    	String city_name = citystore.getText();
    	String add = addresspvd.getText();
    	
    	if(!brand.equals("") && !iva.equals("") && !city_name.equals("") && !add.equals("")){
    		//presenti tutti i campi
    		if(check(brand.toUpperCase(), iva, city_name.toUpperCase(), add.toUpperCase())){
				    	
				    Store s = new Store(pvd, brand.toUpperCase(), iva, model.getCity(city_name).getCity_name(), model.getCity(city_name).getId_istat(), add.toUpperCase(), "");
				    int result= model.insertNewPvd(s);
				    
						   if(result == 0)
						    	txtResult.appendText("Insert done successfully!\n");
						   else if(result == 1)
							   txtResult.appendText("Update done successfully!\n");
						   else if(result == -2)
						    	txtResult.appendText("We are sorry, you can not change the Business Name or the IVA of an existing store, you can only change the City and the Address\nOtherwise you can delete the store and insert it again with the Brand and the IVA you deserve.\n");
						   else if(result == -1)
							   txtResult.appendText("We are sorry you can not use the Business Name or the IVA of a store already existing in the DB.\nOtherwise you can delete the store and insert it again with the Brand and the IVA you deserve.\n");
    		} else 
    			txtResult.appendText("Please modify the wrong gaps.\n");
    	
    	} else 
    		txtResult.appendText("Please complete all the gaps refering the store.\n");
    	
    }
    
    private boolean check(String brand, String iva, String city_name, String address){
		boolean c=true;
		boolean a = true;
		boolean b= true;
		boolean i = true;
		
    	if(!brand.matches("[0-9a-zA-Z .,&]*")){
			txtResult.appendText("Error in the gap brand: insert the Business name\n");
    			b = false;
    	}
    		
    	if(!iva.matches("[0-9]*")|| iva.length()>10  ){
			txtResult.appendText("Error in the gap IVA: insert only 10 digits\n");
			i = false;
    	}
    	if(!city_name.matches("[a-zA-Z ]+")){
			txtResult.appendText("Error in the gap City name: insert only alphabetic characters and spaces\n");
			c=false;
		}
    	
    	if(model.getCity(city_name.toUpperCase())==null){
    		txtResult.appendText("Error in the gap City name: absent in the DB\n");
    		c= false;
    	}
    	
		if(!address.matches("[a-zA-Z0-9 .?,?/?]*")){
	    	txtResult.appendText("Error in the gap address: insert only alphanumeric characters, spaces,./\n");
			a= false;
		} 
		if(a && c && b && i)
			return true;
		
		return false;
		
    }
    

    @FXML
    void doKPIGraph(ActionEvent event) {
    	
    	final String kpip = "products";
        final String kpis = "services";
        final String kpia = "accessories";
        final String kpir = "recharges";
        		
    	XYChart.Series<String, Double> set1 = new XYChart.Series<>();
    	
    	NetworkDAO dao = new NetworkDAO();
    	
    	LocalDate ld1 =  LocalDate.of(2017, 02, 01);
    	List<Double> kpi = dao.getSumKpi(ld1);
    	
    	set1.setName("January");
    	set1.getData().add(new XYChart.Data<String,Double>(kpip, kpi.get(0)));
    	set1.getData().add(new XYChart.Data<String,Double>(kpis, kpi.get(1)));
    	set1.getData().add(new XYChart.Data<String,Double>(kpia, kpi.get(2)));
    	set1.getData().add(new XYChart.Data<String,Double>(kpir, kpi.get(3)));

    	
    	XYChart.Series<String, Double> set2 = new XYChart.Series<>();
    	LocalDate ld2 =  LocalDate.of(2017, 03, 01);
    	List<Double> kpi2 = dao.getSumKpi(ld2);

    	set2.getData().add(new XYChart.Data<String,Double>(kpip, kpi2.get(0)));
    	set2.getData().add(new XYChart.Data<String,Double>(kpis, kpi2.get(1)));
    	set2.getData().add(new XYChart.Data<String,Double>(kpia, kpi2.get(2)));
    	set2.getData().add(new XYChart.Data<String,Double>(kpir, kpi2.get(3)));
    	
    	set2.setName("February");
   
    	
    	XYChart.Series<String, Double> set3 = new XYChart.Series<>();
    	LocalDate ld3 =  LocalDate.of(2017, 04, 01);
    	List<Double> kpi3 = dao.getSumKpi(ld3);

    	set3.getData().add(new XYChart.Data<String,Double>(kpip, kpi3.get(0)));
    	set3.getData().add(new XYChart.Data<String,Double>(kpis, kpi3.get(1)));
    	set3.getData().add(new XYChart.Data<String,Double>(kpia, kpi3.get(2)));
    	set3.getData().add(new XYChart.Data<String,Double>(kpir, kpi3.get(3)));
    	
    	set3.setName("March");
    	
    	XYChart.Series<String, Double> set4 = new XYChart.Series<>();
    	LocalDate ld4 =  LocalDate.of(2017, 05, 01);
    	List<Double> kpi4 = dao.getSumKpi(ld4);

    	set4.getData().add(new XYChart.Data<String,Double>(kpip, kpi4.get(0)));
    	set4.getData().add(new XYChart.Data<String,Double>(kpis, kpi4.get(1)));
    	set4.getData().add(new XYChart.Data<String,Double>(kpia, kpi4.get(2)));
    	set4.getData().add(new XYChart.Data<String,Double>(kpir, kpi4.get(3)));
    	set4.setName("April");
    	
    	XYChart.Series<String, Double> set5 = new XYChart.Series<>();
    	LocalDate ld5 =  LocalDate.of(2017, 06, 01);
    	List<Double> kpi5 = dao.getSumKpi(ld5);

    	set5.getData().add(new XYChart.Data<String,Double>(kpip, kpi5.get(0)));
    	set5.getData().add(new XYChart.Data<String,Double>(kpis, kpi5.get(1)));
    	set5.getData().add(new XYChart.Data<String,Double>(kpia, kpi5.get(2)));
    	set5.getData().add(new XYChart.Data<String,Double>(kpir, kpi5.get(3)));
    	
    	set5.setName("May");
    	kpiStoreChart.autosize();
    	kpiStoreChart.getData().add(set1);
    	kpiStoreChart.getData().add(set2);
    	kpiStoreChart.getData().add(set3);
    	kpiStoreChart.getData().add(set4);
    	kpiStoreChart.getData().add(set5);
    	
    }
    @FXML
    void doStoreKpi(ActionEvent event) {
    	int mese = boxMonth.getValue();
    	
    	NetworkDAO dao = new NetworkDAO();
    	XYChart.Series<String, Double> setMonth = new XYChart.Series<>();
    	LocalDate ld =  LocalDate.of(2017, 06, 01);
    	
    	List<Double> kpiMonth = dao.getAvgKpi(ld);

    	setMonth.getData().add(new XYChart.Data<String,Double>("products", kpiMonth.get(0)));
    	setMonth.getData().add(new XYChart.Data<String,Double>("services", kpiMonth.get(1)));
    	setMonth.getData().add(new XYChart.Data<String,Double>("accessories", kpiMonth.get(2)));
    	setMonth.getData().add(new XYChart.Data<String,Double>("recharges", kpiMonth.get(3)));
    	
    	setMonth.setName("Month "+mese);
    	kpiStoreChart.autosize();
    	kpiStoreChart.getData().add(setMonth);
    	
    	
    	XYChart.Series<String, Double> setStore = new XYChart.Series<>();
    	LocalDate ld5 =  LocalDate.of(2017, 06, 01);
    	
    	List<Double> kpi5 = dao.getStoreKpi(id_pvd, ld5);

    	setStore.getData().add(new XYChart.Data<String,Double>("products", kpi5.get(0)));
    	setStore.getData().add(new XYChart.Data<String,Double>("services", kpi5.get(1)));
    	setStore.getData().add(new XYChart.Data<String,Double>("accessories", kpi5.get(2)));
    	setStore.getData().add(new XYChart.Data<String,Double>("recharges", kpi5.get(3)));
    	
    	setStore.setName(txtbrand.getText());
    	kpiStoreChart.autosize();
    	kpiStoreChart.getData().add(setStore);

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxRegion != null : "fx:id=\"boxRegion\" was not injected: check your FXML file 'Network.fxml'.";
        assert btnSearchStore != null : "fx:id=\"btnSearchStore\" was not injected: check your FXML file 'Network.fxml'.";
        assert btnKpi != null : "fx:id=\"btnKpi\" was not injected: check your FXML file 'Network.fxml'.";
        assert txtCityName != null : "fx:id=\"txtCityName\" was not injected: check your FXML file 'Network.fxml'.";
        assert btnStoreIn != null : "fx:id=\"btnStoreIn\" was not injected: check your FXML file 'Network.fxml'.";
        assert txtDistrict != null : "fx:id=\"txtDistrict\" was not injected: check your FXML file 'Network.fxml'.";
        assert txtId_Istat != null : "fx:id=\"txtId_Istat\" was not injected: check your FXML file 'Network.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Network.fxml'.";
        assert storebox != null : "fx:id=\"storebox\" was not injected: check your FXML file 'Network.fxml'.";
        assert id_pvd != null : "fx:id=\"id_pvd\" was not injected: check your FXML file 'Network.fxml'.";
        assert btnCompleteStore != null : "fx:id=\"btnCompleteStore\" was not injected: check your FXML file 'Network.fxml'.";
        assert btnInsert != null : "fx:id=\"btnInsert\" was not injected: check your FXML file 'Network.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'Network.fxml'.";
        assert btnStoreKpi != null : "fx:id=\"btnStoreKpi\" was not injected: check your FXML file 'Network.fxml'.";
        assert txtbrand != null : "fx:id=\"txtbrand\" was not injected: check your FXML file 'Network.fxml'.";
        assert txtiva != null : "fx:id=\"txtiva\" was not injected: check your FXML file 'Network.fxml'.";
        assert citystore != null : "fx:id=\"citystore\" was not injected: check your FXML file 'Network.fxml'.";
        assert addresspvd != null : "fx:id=\"addresspvd\" was not injected: check your FXML file 'Network.fxml'.";
        assert kpiStoreChart != null : "fx:id=\"kpiStoreChart\" was not injected: check your FXML file 'Network.fxml'.";
        assert x != null : "fx:id=\"x\" was not injected: check your FXML file 'Network.fxml'.";
        assert y != null : "fx:id=\"y\" was not injected: check your FXML file 'Network.fxml'.";
        assert boxMonth != null : "fx:id=\"boxMonth\" was not injected: check your FXML file 'Network.fxml'.";

    }

	public void setModel(Model model2) {
		this.model=model2;		
		boxRegion.getItems().addAll(model.getRegions());
		
		for(int i =1; i<6; i++)
			boxMonth.getItems().add(i);
	}
}