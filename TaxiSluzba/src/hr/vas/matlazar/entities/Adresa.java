package hr.vas.matlazar.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adresa {
	
	private String adresa;
	private Integer udaljenost = Integer.MAX_VALUE;
	private List<Adresa> najkracaUdaljenost = new ArrayList<>();
	Map<Adresa, Integer> susjedi = new HashMap<>();
	
	public void dodajDestinaciju(Adresa destinacija, int udaljenost) {
		susjedi.put(destinacija, udaljenost);
	}
	
	
	public Adresa(String adresa) {
		this.adresa = adresa;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public Integer getUdaljenost() {
		return udaljenost;
	}

	public void setUdaljenost(Integer udaljenost) {
		this.udaljenost = udaljenost;
	}

	public List<Adresa> getNajkracaUdaljenost() {
		return najkracaUdaljenost;
	}

	public void setNajkracaUdaljenost(List<Adresa> najkracaUdaljenost) {
		this.najkracaUdaljenost = najkracaUdaljenost;
	}

	public Map<Adresa, Integer> getSusjedi() {
		return susjedi;
	}

	public void setSusjedi(Map<Adresa, Integer> susjedi) {
		this.susjedi = susjedi;
	}
	
	
	
	
}
