package it.polito.tdp.network.model;

import java.util.*;
import java.time.LocalDate;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import it.polito.tdp.network.dao.NetworkDAO;


public class Model {
	private double turnover;
	private List<City> cities;
	private List<Store> stores;
	private CityIdMap cityIdMap;
	private StoreIdMap storeIdMap;
	private Graph<Store, DefaultEdge> graph ;
	private List<Store> listStoresForRegion;
	private String provincia;
	private List<Store> ordered;
	private Set<Store> best;
	private List<Store> orderedList;
	private double delta;
	private double effMax;
	
	public Model() {
		super();
		cities = new ArrayList<City>();
		stores = new ArrayList<Store>();
		cityIdMap= new CityIdMap();
		storeIdMap = new StoreIdMap();
		this.turnover=0.0;
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

		this.listStoresForRegion = dao.getStoresForRegion(region, storeIdMap);
		
		return this.listStoresForRegion;
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
	
	public Set<Store> solve(){
		Set<Store> parziale = new LinkedHashSet<Store>();
		best = new LinkedHashSet<Store>();
		orderedList = this.getOrderedListOfStores();
		
		delta = turnover;
		//System.out.println(orderedList.size()+"\n");
		
		effMax = orderedList.get(orderedList.size()-1).getEfficiency_index();
		Store primo = orderedList.get(0);
		
		parziale.add(primo);
		choose(parziale, 0, primo);
		
		return best;
	}

	private void choose(Set<Store> parziale, int step, Store precedente) {
		
		System.out.println(parziale.toString());
				
		//cond terminazione: peso uguale o minore del turnover
		double weight = weight(parziale);
		
		if(weight == turnover){
			best.clear();
			best.addAll(parziale);	
			return;
		}
		
		if( weight < turnover ){
			double diff = turnover- weight;
			
		// max capillarita e minima differenza rispetto al turnover desiderato			
			if( diff < delta && parziale.size()> best.size()){
				delta = diff;
				best.clear();
				best.addAll(parziale);	
			}
		}

		if(parziale.size()>0 && precedente.getEfficiency_index()==effMax)
			return;
	
		if(step==orderedList.size()){
			//ho preso tutti i negozi 
			best.clear();
			best.addAll(parziale);
			
			return;
		}		
		
		for(Store stemp: orderedList){
				//ottimizzo in modo da evitare le permutazioni dei negozi che occupa solo tempo alla ricorsione
			
			if(!parziale.contains(stemp) && stemp.getEfficiency_index() > precedente.getEfficiency_index()){
					
				//aggiungo un filtro per cui se il peso di parziale a cui aggiungo il nuovo negozio supera il turnoverlo ignora
				if(weight(parziale)+ stemp.getFatt_prod() <= turnover){
					parziale.add(stemp);
					choose(parziale, step+1, stemp);					
					parziale.remove(stemp);
				}
			}
		}
	}

	public List<Store> getOrderedListOfStores() {
		ordered= new ArrayList<Store>();
		
	//creo una lista di negozi escludendo quelli con efficienza troppo bassa e troppo alta xk voglio la max capillarita
		
		for(Store s: this.getStoresForDistrinct(provincia)){
			s.setEfficiency_index(this.calculateEfficiency(s.getId_pvd()));
			s.setFatt_prod(this.getFattProducts(s.getId_pvd()));
			ordered.add(s);			
		}		
		int size = ordered.size();
		
		for(int i= 95/100*size; i<ordered.size(); i++){
			ordered.remove(i);
		}
		
		if(ordered.size()>0)
		for(int i =0; i < 5/100*size; i++)
			ordered.remove(i);		
		
		Collections.sort(ordered);		
		return ordered;
	}

	private List<Store> getStoresForDistrinct(String provincia) {
		NetworkDAO dao = new NetworkDAO();

		return dao.getStoresForDistrinct(provincia, storeIdMap);
	}

	private double getFattProducts(String id_pvd) {
		NetworkDAO dao = new NetworkDAO();
		LocalDate ld= LocalDate.of(2017, 06, 01);
		
		//System.out.println(dao.getStoreKpi(id_pvd, ld).toString());
		
		//se il negozio magari perchè appena creato, non ha kpi lo ignoro
		if(dao.getStoreKpi(id_pvd, ld).size()>0){
			return dao.getStoreKpi(id_pvd, ld).get(0);
		}
		return 0;			
	}

	private double calculateEfficiency(String id_pvd) {
		NetworkDAO dao = new NetworkDAO();
		LocalDate ld= LocalDate.of(2017, 06, 01);
		
		//System.out.println(dao.getStoreKpi(id_pvd, ld).toString());
		
		//se il negozio magari perchè appena creato, non ha kpi lo ignoro
		if(dao.getStoreKpi(id_pvd, ld).size()>0){
			double fattprodstore= dao.getStoreKpi(id_pvd, ld).get(0);
			
			double fattprodtot = dao.getFattProdForRegion(provincia, ld);
		
			return fattprodstore/fattprodtot;
		}
		else return 0;		
	}
	
	public double getFattTot(){
		NetworkDAO dao = new NetworkDAO();
		LocalDate ld= LocalDate.of(2017, 06, 01);
		
		return dao.getFattProdForRegion(provincia, ld);
	}

	private double weight(Set<Store> parziale) {
		
		double tot=0.0;
		
		for(Store s: parziale)
			tot +=s.getFatt_prod();
		
		return tot; 
	}

	public void setTurnover(double turnover2) {

		this.turnover= turnover2;
	}

	public void setProvincia(String provincia) {
		this.provincia=provincia;
		
	}

	public List<String> getDistrinctForRegion(String region) {
		NetworkDAO dao = new NetworkDAO();
		return dao.getDistrinctForRegion(region);
	}
	
	
	
	
	

}