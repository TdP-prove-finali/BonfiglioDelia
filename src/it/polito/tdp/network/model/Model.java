package it.polito.tdp.network.model;

import java.util.*;
import java.time.LocalDate;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import it.polito.tdp.network.dao.NetworkDAO;

public class Model {
	
	private List<City> cities;
	private List<Store> stores;
	private CityIdMap cityIdMap;
	private StoreIdMap storeIdMap;
	private Graph<Store, DefaultEdge> graph ;

	public Model() {
		super();
		cities = new ArrayList<City>();
		stores = new ArrayList<Store>();
		cityIdMap= new CityIdMap();
		storeIdMap = new StoreIdMap();
	}

	public List<City> getCityList(){
		if(cities.isEmpty()){
			NetworkDAO dao = new NetworkDAO();
			cities = dao.getCities(cityIdMap);
		}
		return cities;
	}
	
	public List<String> getRegions(){
		NetworkDAO dao = new NetworkDAO();
		return dao.getAllRegions();
	}

	public List<Store> getStoresForRegion(String region) {
		NetworkDAO dao = new NetworkDAO();

		return dao.getStoresForRegion(region, storeIdMap);
	}
	
	public List<Store> getStores(){
		if(stores.isEmpty()){
			NetworkDAO dao = new NetworkDAO();
			stores = dao.getStores(storeIdMap);
			
		}
		return stores;
	}

	public List<Store> getStoresForCity(String city_name) {
		List<Store> sl= new ArrayList<Store>();
		
		for(Store stemp: this.getStores()){
			if(stemp.getCity_name().contains(city_name))
				sl.add(stemp);
		}
		return sl;
	}

	public City getCity(String city_name) {
		for(City ctemp: this.getCityList())
			if(ctemp.getCity_name().equals(city_name.toUpperCase())){
				
				return ctemp;
			}
		return null;
	}

	public void createGraph() {
		//if(graph==null)  poichè ci sono modifiche al db è meglio creare il grafo ogni volta che chiamo il metodo
			graph = new SimpleGraph<Store, DefaultEdge>(DefaultEdge.class);
		
		NetworkDAO dao = new NetworkDAO();
		
		//add all stores as verticles
		Graphs.addAllVertices(graph, this.getStores());
		
		for(Store stemp: graph.vertexSet()){
			for(Store neighbour: dao.getNeighbours(stemp.getCity_name(), storeIdMap)){
				if(!stemp.equals(neighbour))
					graph.addEdge(stemp, neighbour);
			}
		}		
	}

	public List<Store> getNeighbours(Store stemp) {
		return Graphs.neighborListOf(graph, stemp);
	}
	
	public int insertNewPvd(Store s) {
	
		NetworkDAO dao = new NetworkDAO();

		boolean b = false;
		boolean c = false;
		
		if(storeIdMap.get(s.getId_pvd())== null){
		//pvd non presente nel db posso crearlo
			
			//PRIMA CONTROLLA CHE LA RAGONE SOCIALE e la partita IVA sono DIVERSE
			if(!equalBusinessName(s) && !equalIva(s)){
				
				s.setIva(s.getIva());
				stores.add(s);
				storeIdMap.put(s);
				
				if(dao.insertNew(s, storeIdMap))
					return 0;
			}
			return -1;
		}
		else{
			// pvd già presente con questo id nel db quindi lo si sta aggiornando!
			Store toupdate = storeIdMap.get(s.getId_pvd());
					
			if(!toupdate.getRag_social().equals(s.getRag_social()))
				return -2;
			
			if(	!toupdate.getCity_name().equals(s.getCity_name())){//aggiorno citta e id istat
					b= dao.updateOldCity(s, storeIdMap);
			}			
			if ( !toupdate.getAddress().equals(s.getAddress())){
					c= dao.updateOldAddress(s, storeIdMap);
			}
			
			if(b||c)				
				return 1;
			}
		
		return -2;
	}

	private boolean equalIva(Store s) {
		for(Store stemp:this.getStores())
			if(stemp.getIva().equals(s.getIva())){
				return true;
			}
		return false;
	}

	private boolean equalBusinessName(Store s) {
		for(Store stemp: this.getStores())
			if(stemp.getRag_social().equals(s.getRag_social().toUpperCase()))
				return true;
		return false;
	}

	public boolean removePvd(String pvd) {
		Store toremove= storeIdMap.get(pvd);
	//remove the store from the DB and the storeidmap
			NetworkDAO dao = new NetworkDAO();
				
		if(dao.remove(toremove, storeIdMap)){
	//remove the store from the list of stores			
		stores.remove(stores.indexOf(toremove));
		
	//remove the store as vertex from the Graph
		graph.removeVertex(toremove);
			
			return true;
		} else
			return false;		
	}

	public List<Double> getSumKpiOfMonth(LocalDate data) {
		NetworkDAO dao = new NetworkDAO();
		
		return dao.getSumKpi(data);
	}	
}