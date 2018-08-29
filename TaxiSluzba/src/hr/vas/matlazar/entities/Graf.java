package hr.vas.matlazar.entities;

import java.util.HashSet;
import java.util.Set;

public class Graf {
	
	private Set<Adresa> mjesta = new HashSet<>();
	
	public void dodajMjesto(Adresa adresa) {
		mjesta.add(adresa);
	}

	public Set<Adresa> getMjesta() {
		return mjesta;
	}

	public void setMjesta(Set<Adresa> mjesta) {
		this.mjesta = mjesta;
	}
	
}
