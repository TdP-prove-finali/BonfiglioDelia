package it.polito.tdp.network.model;

import java.util.HashMap;
import java.util.Map;

public class StoreIdMap {

	private Map<String, Store> map;
	
	public StoreIdMap(){
		map = new HashMap<String, Store>();
	}
	
	public Store get(String string){
		return map.get(string);
	}
	
	public Store put(Store value){
		Store old = map.get(value);
		if(old == null){
			map.put(value.getId_pvd(), value);
			return value;
		} else 
			return old;
	}

	public void remove(String id_pvd) {
		map.remove(id_pvd);
	}
	
}
