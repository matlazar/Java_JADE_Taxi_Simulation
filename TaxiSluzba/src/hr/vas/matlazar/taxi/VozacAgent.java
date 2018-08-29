package hr.vas.matlazar.taxi;

import static java.lang.Thread.sleep;

import hr.vas.matlazar.entities.Adresa;
import hr.vas.matlazar.entities.Taksista;
import hr.vas.matlazar.utils.DijkstraAlgoritam;
import hr.vas.matlazar.utils.Tra�ilicaAdrese;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class VozacAgent extends Agent {

	Taksista vozac;
	
	protected void setup() {
		
		MessageTemplate query = MessageTemplate.MatchPerformative(ACLMessage.QUERY_REF);
		
		addBehaviour(new OneShotBehaviour() {
			@Override
			public void action() {
				addBehaviour(new JaviSeCentrali());
				
			}
		});
		
		addBehaviour(new CyclicBehaviour(this) {
			@Override
			public void action() {
				ACLMessage msg = receive(query);
				
				if (msg != null) {
						addBehaviour(new VoziMusterije(msg));		
				}
			}
		});
	}
	
	public class JaviSeCentrali extends SequentialBehaviour{

		@Override
		public void onStart() {
			ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
			inicijalizirajVozaca(getAID().getLocalName());
			CentralaAgent.vozaci.add(vozac);
			msg.addReceiver(new AID("Centrala", AID.ISLOCALNAME));
			msg.setContent("Bok centrala, voza� " + getAID().getLocalName() + " do�ao na posao!");
			send(msg);
		}
		
	}
	
	public class VoziMusterije extends SequentialBehaviour{
		ACLMessage msg;
		
		public VoziMusterije(ACLMessage msg) {
			this.msg = msg;
		}

		@Override
		public void onStart() {
				String [] pom = msg.getContent().split(",");
				if(pom.length == 2) {
					String musterija = pom[0];
					String odrediste = pom[1];
					odiPoMusteriju(Tra�ilicaAdrese.dohvatiAdresu(musterija));
					odveziMusteriju(Tra�ilicaAdrese.dohvatiAdresu(odrediste));
				}else if(msg.getContent().equals("Kraj")){
					System.out.println("***************************************************");
					System.out.println("Voza� " + getAID().getLocalName() + " se odjavljuje");
					System.out.println("Vidimo se sutra!!!!");
					System.out.println("***************************************************");
					block();
					doDelete();
					takeDown();
				}

		}
		
	}
	
	public void inicijalizirajVozaca(String naziv) {
		for(Adresa a: CentralaAgent.adrese) {
			if(a.getAdresa().equals("Centrala - Autobusni Kolodvor")) {
				vozac = new Taksista(naziv, a, true);
			}
				
		}
	}
	
	public void odiPoMusteriju(Adresa adresa) {
		System.out.println(getAID().getLocalName() + " ide pokupiti mu�teriju na lokaciji:  " + adresa.getAdresa());
		Adresa polaziste = vozac.getAdresa();
		for(Taksista v: CentralaAgent.vozaci) {
			if(v.getNaziv().equals(vozac.getNaziv())) {
				v.setAdresa(adresa);
				v.setStatus(false);
				break;
			}
		}
		vozac.setAdresa(adresa);
		vozac.setStatus(false);
		voznja(polaziste, vozac.getAdresa());
	}
	
	public void odveziMusteriju(Adresa adresa) {
		System.out.println(getAID().getLocalName() + " pokupio mu�teriju i vozi je na lokaciju:  " + adresa.getAdresa());
		Adresa polaziste = vozac.getAdresa();
		for(Taksista v: CentralaAgent.vozaci) {
			if(v.getNaziv().equals(vozac.getNaziv())) {
				v.setAdresa(adresa);
				break;
			}
		}
		vozac.setAdresa(adresa);
		voznja(polaziste, vozac.getAdresa());
		
		for(Taksista v: CentralaAgent.vozaci) {
			if(v.getNaziv().equals(vozac.getNaziv())) {
				v.setStatus(true);
				break;
			}
		}
		vozac.setStatus(true);
		System.out.println(getAID().getLocalName() +" spreman za dalje :D");
	}
	
	public void voznja(Adresa polaziste, Adresa odrediste) {
		long voznja = 0;
		
		CentralaAgent.graf = DijkstraAlgoritam.izracunajNajmanjiPutOdPolazista(CentralaAgent.graf, polaziste);
		for(Adresa a: CentralaAgent.graf.getMjesta()) {
			if(a.getAdresa().equals(odrediste.getAdresa())) {
				voznja = a.getUdaljenost();
				break;
			}
		}
		try {
			sleep(voznja * 1000);
			System.out.println("---------------------------------------------------");
			System.out.println(getAID().getLocalName() + " to�an ko �vicarski sat");
			System.out.println("---------------------------------------------------");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
	}
	
}
