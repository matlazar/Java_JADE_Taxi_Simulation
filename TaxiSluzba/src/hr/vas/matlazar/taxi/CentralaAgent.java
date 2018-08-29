package hr.vas.matlazar.taxi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import hr.vas.matlazar.entities.Adresa;
import hr.vas.matlazar.entities.Graf;
import hr.vas.matlazar.entities.Taksista;
import hr.vas.matlazar.utils.DijkstraAlgoritam;
import hr.vas.matlazar.utils.TražilicaAdrese;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import static java.lang.Thread.sleep;

public class CentralaAgent extends Agent{
	
	public static List<Taksista> vozaci = new ArrayList<>();
	public static Graf graf = new Graf();
	public static List<Adresa> adrese = new ArrayList<>();
	public static int radnoVrijeme = 0;
	Random rand = new Random();
	Scanner scanner = new Scanner(System.in);
	
	protected void setup() {
		MessageTemplate query = MessageTemplate.MatchPerformative(ACLMessage.QUERY_REF);
		inicijalizirajGrad();
		System.out.println("++++++++++++++++++++++++++++++++++++++++");
		System.out.println("+++++++Popis stanica u Varaždinu++++++++");
		System.out.println("++++++++++++++++++++++++++++++++++++++++");
		System.out.println("Centrala - Autobusni Kolodvor");
		System.out.println("Korzo");
		System.out.println("McDonalds");
		System.out.println("Studenski dom");
		System.out.println("Skate park");
		System.out.println("Stadion NK Varaždin");
		System.out.println("Banfica");
		System.out.println("++++++++++++++++++++++++++++++++++++++++");
		System.out.println("Unesite rando vrijeme: ");
		radnoVrijeme = scanner.nextInt();
		System.out.println("++++++++++++++++++++++++++++++++++++++++");
		try {
			sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		int brojac = 0;
		while (brojac < 10) {
			ACLMessage msg = receive(query);
			if (msg != null) {
				System.out.println(msg.getContent());
			}
			
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			brojac++;

		}
		System.out.println("*****************************************");
		System.out.println("**********Poèetna pozicija-**************");
		System.out.println("*****************************************");
		System.out.println("******Centrala - Autobusni Kolodvor******");
		System.out.println("*****************************************");
		
		 addBehaviour(new CyclicBehaviour(this) {
			@Override
			public void action() {
					addBehaviour(new ObaviNarudzbu());
			}
		 });
	}
	
	public class ObaviNarudzbu extends SequentialBehaviour{
		
		ACLMessage poruka = new ACLMessage(ACLMessage.QUERY_REF);
		List<Taksista> slobodniVozaci = new ArrayList<>();
		ACLMessage poruka2 = new ACLMessage(ACLMessage.QUERY_REF);
		public void onStart() {
			String unos = simulirajPoziv(rand);
			System.out.println("Pristigla narudžba: " + " " + unos);	
			String [] addresses = unos.split(",");
			String adresa = addresses[0];
			int min = Integer.MAX_VALUE;
			Taksista pozitivniVozac = null;
			if(unos.equals("Kraj")) {
				for(Taksista t:vozaci) {
					System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
					System.out.println("Radno vrijeme je završilo " + t.getNaziv()+ "! Vidimo se sutra!");
					System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
					poruka.addReceiver(new AID(t.getNaziv(), AID.ISLOCALNAME));
					poruka.setContent(unos);
					send(poruka);
					block();
					doDelete();
					takeDown();
				}
			}else {
				for(Taksista v:vozaci) {
					if(v.isStatus()) {
						slobodniVozaci.add(v);
					}
				}
				
				if(!slobodniVozaci.isEmpty()) {
					for(Taksista v:slobodniVozaci) {
						int vrijeme = putDoIshodista(v.getAdresa(), TražilicaAdrese.dohvatiAdresu(adresa));
						if(min >= vrijeme) {
							pozitivniVozac = v;
							min = vrijeme;
						}
					}
					poruka.addReceiver(new AID(pozitivniVozac.getNaziv(), AID.ISLOCALNAME));
					poruka.setContent(unos);
					send(poruka);
				}else {
					System.out.println("Nažalost, nemam slobodnih automobila, javite se kasnije");
				}
			}
			
		}
	}
	
	public int putDoIshodista(Adresa polaziste, Adresa odrediste) {
		int voznja = 0;
		Graf pomGraf = new Graf();
		pomGraf = DijkstraAlgoritam.izracunajNajmanjiPutOdPolazista(graf, polaziste);
		for(Adresa a : pomGraf.getMjesta())  {
			if(a.getAdresa().equals(odrediste)) {
				voznja = a.getUdaljenost();
				break;
			}
		}
		return voznja;
	}
	
	public static String simulirajPoziv(Random rand) {
		long frekvencija = rand.nextInt(10)+1;
		radnoVrijeme -= frekvencija;
		int pocetna = rand.nextInt(adrese.size());
		int odrediste = rand.nextInt(adrese.size());
		while(pocetna == odrediste) {
			odrediste = rand.nextInt(adrese.size());
		}
		try {
			sleep(frekvencija * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(radnoVrijeme <= 0) {
			return "Kraj";
		}
		
		
		return adrese.get(pocetna).getAdresa() + ","+adrese.get(odrediste).getAdresa();
	}
	
	public static void inicijalizirajGrad(){
		Adresa centrala = new Adresa("Centrala - Autobusni Kolodvor");
		adrese.add(centrala);
		Adresa korzo = new Adresa("Korzo");
		adrese.add(korzo);
		Adresa mcDonalds = new Adresa("McDonalds");
		adrese.add(mcDonalds);
		Adresa studenskiDom = new Adresa("Studenski dom"); 
		adrese.add(studenskiDom);
		Adresa skate = new Adresa("Skate park");
		adrese.add(skate);
		Adresa stadion = new Adresa("Stadion NK Varaždin");
		adrese.add(stadion);
		Adresa banfica = new Adresa("Banfica");
		adrese.add(banfica);
		
		
		for(Adresa a: adrese) {
			switch(a.getAdresa()) {
				case "Centrala - Autobusni Kolodvor":
					centrala.dodajDestinaciju(korzo, 12);
					centrala.dodajDestinaciju(mcDonalds, 14);
					centrala.dodajDestinaciju(studenskiDom, 18);
					centrala.dodajDestinaciju(stadion, 16);
					centrala.dodajDestinaciju(skate, 12);
					centrala.dodajDestinaciju(banfica, 19);
					break;
				case "Korzo":
					korzo.dodajDestinaciju(centrala, 12);
					korzo.dodajDestinaciju(studenskiDom, 13);
					korzo.dodajDestinaciju(mcDonalds, 15);
					break;
				case "McDonalds":
					mcDonalds.dodajDestinaciju(korzo, 15 );
					mcDonalds.dodajDestinaciju(centrala, 14);
					mcDonalds.dodajDestinaciju(skate, 10);
					break;
				case "Studenski dom":
					studenskiDom.dodajDestinaciju(korzo, 13);
					studenskiDom.dodajDestinaciju(centrala, 18);
					studenskiDom.dodajDestinaciju(stadion, 11);
					break;
				case "Skate park":
					skate.dodajDestinaciju(centrala, 12);
					skate.dodajDestinaciju(mcDonalds, 10);
					skate.dodajDestinaciju(banfica, 15);
					break;
				case "Stadion NK Varaždin":
					stadion.dodajDestinaciju(centrala, 16);
					stadion.dodajDestinaciju(studenskiDom, 11);
					stadion.dodajDestinaciju(banfica, 13);
					break;
				case "Banfica":
					banfica.dodajDestinaciju(centrala, 19);
					banfica.dodajDestinaciju(skate, 15);
					banfica.dodajDestinaciju(stadion, 11);
			}
		}
		
		graf.dodajMjesto(centrala);
		graf.dodajMjesto(korzo);
		graf.dodajMjesto(mcDonalds);
		graf.dodajMjesto(studenskiDom);
		graf.dodajMjesto(skate);
		graf.dodajMjesto(stadion);
		graf.dodajMjesto(banfica);
	}
	
	
}
