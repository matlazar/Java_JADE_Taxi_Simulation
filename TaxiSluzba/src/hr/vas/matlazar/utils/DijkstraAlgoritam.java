package hr.vas.matlazar.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;


import java.util.Set;

import hr.vas.matlazar.entities.Adresa;
import hr.vas.matlazar.entities.Graf;

public class DijkstraAlgoritam {
	
	public static Graf izracunajNajmanjiPutOdPolazista(Graf graf, Adresa adresa) {
		adresa.setUdaljenost(0);
		
		Set<Adresa> prodjenaAdresa = new HashSet<>();
		Set<Adresa> neProdjenaAdresa = new HashSet<>();
		
		neProdjenaAdresa.add(adresa);
		
		while (neProdjenaAdresa.size() != 0) {
			Adresa trenutnaAdresa = najnizaUdaljenost(neProdjenaAdresa);
			neProdjenaAdresa.remove(trenutnaAdresa);
			for(Entry<Adresa, Integer> susjed : trenutnaAdresa.getSusjedi().entrySet() ) {
				Adresa susjednaAdresa = susjed.getKey();
				Integer udaljenost = susjed.getValue();
				if(!prodjenaAdresa.contains(susjednaAdresa)) {
					izracunajMinUdaljenost(susjednaAdresa, udaljenost, trenutnaAdresa);
					neProdjenaAdresa.add(susjednaAdresa);
				}
			}
			prodjenaAdresa.add(trenutnaAdresa);
		}
		
		
		return graf;
	}
	
	
	private static Adresa najnizaUdaljenost(Set<Adresa> nesredjenaAdresa) {
		Adresa najnizaUdaljenostAdrese = null;
		int najnizaUdaljnost = Integer.MAX_VALUE;
		for(Adresa adresa : nesredjenaAdresa) {
			int udaljenostAdrese = adresa.getUdaljenost();
			if(udaljenostAdrese < najnizaUdaljnost) {
				najnizaUdaljnost = udaljenostAdrese;
				najnizaUdaljenostAdrese = adresa;
			}
		}
		return najnizaUdaljenostAdrese;
	}
	
	private static void izracunajMinUdaljenost(Adresa destinacija, Integer udaljenost, Adresa polaziste) {
		Integer udaljenostPolazista = polaziste.getUdaljenost();
		if(udaljenostPolazista + udaljenost < destinacija.getUdaljenost()) {
			destinacija.setUdaljenost(udaljenostPolazista + udaljenost);
			List<Adresa> najkraciPut = new ArrayList<>(polaziste.getNajkracaUdaljenost());
			najkraciPut.add(polaziste);
			destinacija.setNajkracaUdaljenost(najkraciPut);
		}
	}
	
}
