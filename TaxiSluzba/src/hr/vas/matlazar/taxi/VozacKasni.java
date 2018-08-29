package hr.vas.matlazar.taxi;

import static java.lang.Thread.sleep;

import java.util.Random;

import hr.vas.matlazar.entities.Adresa;
import hr.vas.matlazar.utils.DijkstraAlgoritam;

public class VozacKasni extends VozacAgent {

	Random rand = new Random();
	
	@Override
	public void voznja(Adresa polaziste, Adresa odrediste) {
		long voznja = 0;
		long kasnjenje = rand.nextInt(10)+1;
		CentralaAgent.graf = DijkstraAlgoritam.izracunajNajmanjiPutOdPolazista(CentralaAgent.graf, polaziste);
		for (Adresa a : CentralaAgent.graf.getMjesta()) {
			if (a.getAdresa().equals(odrediste.getAdresa())) {
				voznja = a.getUdaljenost();
				break;
			}
		}
		try {
			sleep((voznja + kasnjenje) * 1000);
			System.out.println("---------------------------------------------------");
			System.out.println(getAID().getLocalName() + " kasni " + kasnjenje + " sekunda");
			System.out.println("---------------------------------------------------");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
