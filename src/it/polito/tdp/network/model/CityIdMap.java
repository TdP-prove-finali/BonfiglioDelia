package it.polito.tdp.network.model;

import java.util.*;


public class CityIdMap {

	private Map<String, City> map;
	
	public CityIdMap(){
		map = new HashMap<String, City>();
	}
	
	public City get(String id){
		return map.get(id);
	}
	
	public City put(City value){
		City old = map.get(value);
		if(old == null){
			map.put(value.getId_istat(), value);
			return value;
		} else 
			return old;
	}
	
}
