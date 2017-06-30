package it.polito.tdp.network.model;

public class Store {
	private String id_pvd;
	private String rag_social;
	private String iva;
	private String city_name;
	private String id_istat;
	private String address;
	private String commercial_state;
	
	public Store(String id_pvd, String rag_social, String iva, String city_name, String id_istat, String address,
			String commercial_state) {
		super();
		this.id_pvd = id_pvd;
		this.rag_social = rag_social;
		this.iva = iva;
		this.city_name = city_name;
		this.id_istat = id_istat;
		this.address = address;
		this.commercial_state = commercial_state;
	}

	/**
	 * @return the id_pvd
	 */
	public String getId_pvd() {
		return id_pvd;
	}

	/**
	 * @param id_pvd the id_pvd to set
	 */
	public void setId_pvd(String id_pvd) {
		this.id_pvd = id_pvd;
	}

	/**
	 * @return the rag_social
	 */
	public String getRag_social() {
		return rag_social;
	}

	/**
	 * @param rag_social the rag_social to set
	 */
	public void setRag_social(String rag_social) {
		this.rag_social = rag_social;
	}

	/**
	 * @return the iva
	 */
	public String getIva() {
		return iva;
	}

	/**
	 * @param iva the iva to set
	 */
	public void setIva(String iva) {
		this.iva = iva;
	}

	/**
	 * @return the city_name
	 */
	public String getCity_name() {
		return city_name;
	}

	/**
	 * @param city_name the city_name to set
	 */
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	/**
	 * @return the id_istat
	 */
	public String getId_istat() {
		return id_istat;
	}

	/**
	 * @param id_istat the id_istat to set
	 */
	public void setId_istat(String id_istat) {
		this.id_istat = id_istat;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the commercial_state
	 */
	public String getCommercial_state() {
		
			return commercial_state;
		
	}

	/**
	 * @param commercial_state the commercial_state to set
	 */
	public void setCommercial_state(String cs) {
			this.commercial_state=cs;
	}

	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id_pvd == null) ? 0 : id_pvd.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Store other = (Store) obj;
		if (id_pvd == null) {
			if (other.id_pvd != null)
				return false;
		} else if (!id_pvd.equals(other.id_pvd))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Id store: "+id_pvd.trim() + ", Brand: "+rag_social.trim()+ ", Closed to public "+this.getCommercial_state();
	}
	
	
	
	
}