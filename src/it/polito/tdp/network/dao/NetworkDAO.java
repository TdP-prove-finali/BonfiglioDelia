package it.polito.tdp.network.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import it.polito.tdp.network.model.City;
import it.polito.tdp.network.model.CityIdMap;
import it.polito.tdp.network.model.Store;
import it.polito.tdp.network.model.StoreIdMap;
import javafx.scene.control.TextField;

public class NetworkDAO {

	public List<String> getAllRegions() {
		final String sql = "SELECT DISTINCT region FROM cities ORDER BY region ";
		List<String> regions= new ArrayList<String>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {	
				regions.add(rs.getString("region"));
			}	
			
			//System.out.println(regions.toString());
			conn.close();
			return regions;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	public List<City> getCities(CityIdMap cityIdMap) {
		final String sql = "SELECT * FROM cities ";
		
		List<City> cities = new ArrayList<City>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				City ctemp = cityIdMap.get(rs.getString("id_istat"));
				
				if(ctemp ==null){
					ctemp = new City(rs.getString("id_istat"),rs.getString("city_name").toUpperCase(),
						rs.getString("district_name"), rs.getString("region"), rs.getString("district_abb"),
						rs.getInt("inhabitants"), rs.getInt("PIL"));
				
					ctemp = cityIdMap.put(ctemp);
				}
				cities.add(ctemp);
			}
			
			conn.close();
			return cities;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	public List<Store> getStoresForRegion(String region, StoreIdMap storeIdMap) {

		final String sql = "select n.*"+
							"from network as n, cities as c "+
							"where c.id_istat=n.id_istat and c.region= ?";
		
		List<Store> stores= new ArrayList<Store>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, region);
			
			ResultSet rs = st.executeQuery();
			
		while (rs.next()) {
				Store stemp = storeIdMap.get(rs.getString("id_pvd"));
				
				if(stemp ==null){
					stemp = new Store(rs.getString("id_pvd"), rs.getString("rag_social"), rs.getString("iva"),
							rs.getString("city").toUpperCase(), rs.getString("id_istat"),rs.getString("address"), rs.getString("comm_state")); 
				
					stemp = storeIdMap.put(stemp);
				}
				stores.add(stemp);
			}
			//System.out.println(stores.size());
			conn.close();
			return stores;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	public List<Store> getStores(StoreIdMap storeIdMap) {
		final String sql = "select * "+
							"from network ";

		List<Store> stores= new ArrayList<Store>();		
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Store stemp = storeIdMap.get(rs.getString("id_pvd"));
				
				if(stemp ==null){
					stemp = new Store(rs.getString("id_pvd"), rs.getString("rag_social"), rs.getString("iva"),
							rs.getString("city").toUpperCase(), rs.getString("id_istat"),rs.getString("address"), rs.getString("comm_state")); 
				
					stemp = storeIdMap.put(stemp);
				}
				stores.add(stemp);
			}
			//System.out.println(stores.size());
			conn.close();
			return stores;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	public List<Store> getNeighbours(String city_name, StoreIdMap storeIdMap) {
		final String sql = "select n.*"+
				"from network as n, cities as c "+
				"where c.id_istat=n.id_istat and c.city_name= ?";

		List<Store> stores= new ArrayList<Store>();
		
		try {
		Connection conn = DBConnect.getConnection();
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1, city_name);
		
		ResultSet rs = st.executeQuery();
		
		while (rs.next()) {
				Store stemp = storeIdMap.get(rs.getString("id_pvd"));
				
				if(stemp ==null){
					stemp = new Store(rs.getString("id_pvd"), rs.getString("rag_social"), rs.getString("iva"),
							rs.getString("city").toUpperCase(), rs.getString("id_istat"),rs.getString("address"), rs.getString("comm_state")); 
				
					stemp = storeIdMap.put(stemp);
				}
				stores.add(stemp);
			}
			//System.out.println(stores.size());
			conn.close();
			return stores;
		
		} catch (SQLException e) {
		 e.printStackTrace();
		throw new RuntimeException("Errore Db");
		}
		}

	public boolean insertNew(Store s, StoreIdMap storeIdMap) {
		final String sql = "INSERT INTO network "+
							"VALUES (?, ?, ?, ?, ?, ?, ?) ";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, s.getId_pvd());
			st.setString(2, s.getRag_social());
			st.setString(3, s.getIva());
			st.setString(4, s.getCity_name());
			st.setString(5, s.getId_istat());
			st.setString(6, s.getAddress());
			st.setString(7, "");
			
			int rs = st.executeUpdate();
			
			if(rs==1) {
				Store stemp = storeIdMap.get(s.getId_pvd());
					
					if(stemp !=null){	// c'è qualcosa che non va e va rimosso!	
						storeIdMap.remove(stemp.getId_pvd());
					}
			//creo il negozio e lo metto nella mappa
				Store snew = new Store(s.getId_pvd(),s.getRag_social(),s.getIva(), s.getCity_name(), s.getId_istat(), s.getAddress(), s.getCommercial_state());
			
				snew = storeIdMap.put(snew);
				
				conn.close();
				return true;
				
			}	else 	{	
				conn.close();
			
				return false;
			}
			
		} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("Errore Db");
			}
	}

	public boolean remove(Store s, StoreIdMap storeIdMap) {
		
		final String sql = "delete e.*, n.* "+
							"from economics as e, network as n "+
							"where n.id_pvd=e.id_pvd and e.id_pvd=? ";

		try {
		Connection conn = DBConnect.getConnection();
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1, s.getId_pvd());
		
		int rs = st.executeUpdate();
		
		if(rs==1){
				Store stemp = storeIdMap.get(s.getId_pvd());
				
				storeIdMap.remove(stemp.getId_pvd());
				conn.close();
			return true;
		} else{
			
			conn.close();
			return false;
		
		}
		
		} catch (SQLException e) {
			e.printStackTrace();
		 	throw new RuntimeException("Errore Db");
		}
		}

	public boolean updateOldAddress(Store s, StoreIdMap storeIdMap) {
		final String sql = "UPDATE network "+
				"SET address = ? "+
				"WHERE id_pvd = ? ";
							    

		try {
		Connection conn = DBConnect.getConnection();
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1, s.getAddress());
		st.setString(2, s.getId_pvd());
		
		int rs = st.executeUpdate();
		
		if (rs==1) {
			Store stemp = storeIdMap.get(s.getId_pvd());
				
			stemp.setAddress(s.getAddress());	
			
			conn.close();
			return true;
		}
		
			conn.close();
			return false;
		
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	public boolean updateOldCity(Store s, StoreIdMap storeIdMap) {
		final String sql = "UPDATE network "+
							"SET city = ? , id_istat = ?"+
							"WHERE id_pvd = ? ";
				    
			
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			//System.out.println(s.getCity_name());
			
			st.setString(1, s.getCity_name());
			st.setString(2, s.getId_istat());
			st.setString(3, s.getId_pvd());
			
			int rs = st.executeUpdate();
			
			if (rs ==1) {
			Store stemp = storeIdMap.get(s.getId_pvd());
				
				stemp.setCity_name(s.getCity_name());	
				stemp.setId_istat(s.getId_istat());
				
				conn.close();
				return true;
				
			}
			
			conn.close();
			return false;
			
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("Errore Db");
			}
		}

	public List<Double> getSumKpi(LocalDate data) {
		final String sql = "select sum(fatt_products) as fp, sum(fatt_services) as fs, sum(fatt_accessories)as fa, sum(fatt_ricariche) as fr "+
							"from economics	"+
							"where data = ? ";
		
		List<Double> kpi = new ArrayList<Double>() ;
		
		try {
		Connection conn = DBConnect.getConnection();
		PreparedStatement st = conn.prepareStatement(sql);
		st.setDate(1, Date.valueOf(data));
		
		ResultSet rs = st.executeQuery();
		
		rs.next();
		
				kpi.add(rs.getDouble("fp"));
				kpi.add(rs.getDouble("fs"));
				kpi.add(rs.getDouble("fa"));
				kpi.add(rs.getDouble("fr"));


			conn.close();
		
			return kpi ;
		
		} catch (SQLException e) {
		 e.printStackTrace();
		throw new RuntimeException("Errore Db");
		}
	}

	public List<Double> getStoreKpi(String id_pvd, LocalDate ld5) {
		final String sql = "select e.fatt_products as fp, e.fatt_services as fs, e.fatt_accessories as fa, e.fatt_ricariche as fr "+
							"from economics	as e "+
							"where e.id_pvd= ? and e.`data` = ? ";

		List<Double> kpi = new ArrayList<Double>() ;
		
		try {
		Connection conn = DBConnect.getConnection();
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1, id_pvd);
		st.setDate(2, Date.valueOf(ld5));
		
		ResultSet rs = st.executeQuery();
		
		if(rs.next()){
		
			kpi.add(rs.getDouble("fp"));
			kpi.add(rs.getDouble("fs"));
			kpi.add(rs.getDouble("fa"));
			kpi.add(rs.getDouble("fr"));
		}
		
		//System.out.println(kpi.toString());
		conn.close();
		
		return kpi ;
		
		} catch (SQLException e) {
		e.printStackTrace();
		throw new RuntimeException("Errore Db");
		}
		}

	public List<Double> getAvgKpi(LocalDate ld) {
		final String sql = 
				"select avg(fatt_products) as afp, avg(fatt_services) as afs, avg(fatt_accessories)as afa, avg(fatt_ricariche) as afr "+
				"from economics	"+
				"where data = ? ";

		List<Double> kpi = new ArrayList<Double>() ;
		
		try {
		Connection conn = DBConnect.getConnection();
		PreparedStatement st = conn.prepareStatement(sql);
		st.setDate(1, Date.valueOf(ld));
		
		ResultSet rs = st.executeQuery();
		
		rs.next();
		
			kpi.add(rs.getDouble("afp"));
			kpi.add(rs.getDouble("afs"));
			kpi.add(rs.getDouble("afa"));
			kpi.add(rs.getDouble("afr"));
		
		
		conn.close();
		
		return kpi ;
		
		} catch (SQLException e) {
		e.printStackTrace();
		throw new RuntimeException("Errore Db");
		}
		}

	public double getFattProdForRegion(String provincia, LocalDate ld) {
		final String sql = "select sum(fatt_products) as fp "+
							"from economics	as e "+
							"where e.`data`=? and e.id_pvd in ( 	select n.id_pvd "+
																"from network as n, cities as c "+
																"where n.id_istat=c.id_istat and c.district_name= ? "+
															")";

			double fatt_prod = 0.0;
			
			try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDate(1, Date.valueOf(ld));
			st.setString(2, provincia);
			
			ResultSet rs = st.executeQuery();
			
			rs.next();
			
				fatt_prod= rs.getDouble("fp");			
				//System.out.println(fatt_prod);
			conn.close();
			
			return fatt_prod ;
			
			} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore Db");
			}
			}

	public List<Store> getStoresForDistrinct(String provincia, StoreIdMap storeIdMap) {
		final String sql = "select n.*"+
				"from network as n, cities as c "+
				"where c.id_istat=n.id_istat and c.district_name= ? and n.comm_state<>'no' ";

		List<Store> stores= new ArrayList<Store>();

		try {
		Connection conn = DBConnect.getConnection();
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1, provincia);
		
		ResultSet rs = st.executeQuery();
		
		while (rs.next()) {
			Store stemp = storeIdMap.get(rs.getString("id_pvd"));
			
			if(stemp ==null){
				stemp = new Store(rs.getString("id_pvd"), rs.getString("rag_social"), rs.getString("iva"),
						rs.getString("city").toUpperCase(), rs.getString("id_istat"),rs.getString("address"), rs.getString("comm_state")); 
			
				stemp = storeIdMap.put(stemp);
			}
			stores.add(stemp);
		}
		//System.out.println(stores.size());
		conn.close();
		return stores;
		
		} catch (SQLException e) {
		 e.printStackTrace();
		throw new RuntimeException("Errore Db");
		}
		}

	public List<String> getDistrinctForRegion(String region) {
		final String sql = "select distinct c.district_name as provincia "+
							"from cities as c "+
							"where c.region = ? "+ 
							"order by provincia ";

		List<String> result= new ArrayList<String>();

		try {
		Connection conn = DBConnect.getConnection();
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1, region);
		
		ResultSet rs = st.executeQuery();
		
		while (rs.next()) {
			result.add(rs.getString("provincia"));
		}
		//System.out.println(stores.size());
		conn.close();
		return result;
		
		} catch (SQLException e) {
		 e.printStackTrace();
		throw new RuntimeException("Errore Db");
		}
		}
	
}