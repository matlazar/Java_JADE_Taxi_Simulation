package hr.vas.matlazar.taxi;

import static java.lang.Thread.sleep;

import java.util.Random;

import hr.vas.matlazar.entities.Adresa;
import hr.vas.matlazar.utils.DijkstraAlgoritam;

public class VozacUrani extends VozacAgent{
	
	Random rand = new Random();

	@Override
	public void voznja(Adresa polaziste, Adresa odrediste) {
		long voznja = 0;
		long ranije = rand.nextInt(10)+1;
		CentralaAgent.graf = DijkstraAlgoritam.izracunajNajmanjiPutOdPolazista(CentralaAgent.graf, polaziste);
		for (Adresa a : CentralaAgent.graf.getMjesta()) {
			if (a.getAdresa().equals(odrediste.getAdresa())) {
				voznja = a.getUdaljenost();
				break;
			}
		}
		try {
			if(voznja < ranije) {
				ranije = 0;
			}
			sleep((voznja-ranije) * 1000);
			if(ranije == 0) {
				System.out.println("---------------------------------------------------");
				System.out.println(getAID().getLocalName() +" ipak stigano tocno na vrijeme");
				System.out.println("---------------------------------------------------");
			}else {
				System.out.println("---------------------------------------------------");
				System.out.println(getAID().getLocalName() +" uranio " + ranije + " sekundi!");
				System.out.println("---------------------------------------------------");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
