package it.polito.tdp.network.model;

public class City {
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */

	private String id_istat;
	private String city_name;
	private String district_name;
	private String region;
	private String district_abb;
	private int inhabitants;
	private int pil;


	public City(String id_istat, String city_name, String district_name, String region, String district_abb,
			int inhabitants, int pil) {
		super();
		this.id_istat = id_istat;
		this.city_name = city_name;
		this.district_name = district_name;
		this.region = region;
		this.district_abb = district_abb;
		this.inhabitants = inhabitants;
		this.pil = pil;
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
	 * @return the district_name
	 */
	public String getDistrict_name() {
		return district_name;
	}

	/**
	 * @param district_name the district_name to set
	 */
	public void setDistrict_name(String district_name) {
		this.district_name = district_name;
	}

	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * @return the inhabitants
	 */
	public int getInhabitants() {
		return inhabitants;
	}

	/**
	 * @param inhabitants the inhabitants to set
	 */
	public void setInhabitants(int inhabitants) {
		this.inhabitants = inhabitants;
	}

	/**
	 * @return the pil
	 */
	public int getPil() {
		return pil;
	}

	/**
	 * @param pil the pil to set
	 */
	public void setPil(int pil) {
		this.pil = pil;
	}
	/**
	 * @return the district_abb
	 */
	public String getDistrict_abb() {
		return district_abb;
	}

	/**
	 * @param district_abb the district_abb to set
	 */
	public void setDistrict_abb(String district_abb) {
		this.district_abb = district_abb;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id_istat == null) ? 0 : id_istat.hashCode());
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
		City other = (City) obj;
		if (id_istat == null) {
			if (other.id_istat != null)
				return false;
		} else if (!id_istat.equals(other.id_istat))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "City :" + id_istat + ", " + city_name + ", " + district_name
				+ ", " + region + ", inhabitants=" + inhabitants + ", pil=" + pil + "]";
	}
	
}
