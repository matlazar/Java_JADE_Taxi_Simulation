package hr.vas.matlazar.utils;

import hr.vas.matlazar.entities.Adresa;
import hr.vas.matlazar.taxi.CentralaAgent;

public class TrailicaAdrese {
	
	public static Adresa dohvatiAdresu(String adresa) {
		Adresa addr = null;
		for(Adresa adress: CentralaAgent.adrese) {
			if(adress.getAdresa().equals(adresa)) {
				addr = adress;
			}
		}
		return addr;
	}
}
