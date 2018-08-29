package hr.vas.matlazar.entities;

import java.io.Serializable;

public class Taksista implements Serializable{
	
	private String naziv;
	private Adresa adresa;
	private boolean status;
	private int trenutnoMinVrijeme;
	
	public Taksista(String naziv, Adresa adresa, boolean status) {
		this.naziv = naziv;
		this.adresa = adresa;
		this.status = status;
	}
	
	public int getTrenutnoMinVrijeme() {
		return trenutnoMinVrijeme;
	}
	public void setTrenutnoMinVrijeme(int trenutnoMinVrijeme) {
		this.trenutnoMinVrijeme = trenutnoMinVrijeme;
	}
	
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	public Adresa getAdresa() {
		return adresa;
	}
	public void setAdresa(Adresa adresa) {
		this.adresa = adresa;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
	
}
