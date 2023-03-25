import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Graph {

  private Map<String, Station> correspondanceStringStation;
  private Map<Station, Set<Troncon>> mapTronconsParStation;
  private Map<Integer, Ligne> correspondanceIdLigneVersLigne;
  private Scanner scannerTroncons, scannerLigne;

  public Graph(File lignes, File troncons) {
    correspondanceStringStation = new HashMap<>();
    mapTronconsParStation = new HashMap<>();
    correspondanceIdLigneVersLigne = new HashMap<>();

    try {
      scannerLigne = new Scanner(lignes);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
    try {
      scannerTroncons = new Scanner(troncons);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }

    while (scannerLigne.hasNext()) {
      creerLigne(scannerLigne.nextLine());
    }

    while (scannerTroncons.hasNext()) {
      creerTroncon(scannerTroncons.nextLine());
    }
  }

  public void calculerCheminMinimisantNombreTroncons(String nomStationDepart,
      String nomStationArrivee) {
    HashMap<Station, Troncon> parcoursDesStations = new HashMap<>();
    HashSet<Station> stationsParcourues = new HashSet<>();
    Queue<Station> fileBFS = new ArrayDeque<>();

    Station stationDepart = correspondanceStringStation.get(nomStationDepart);
    if (stationDepart == null) {
      throw new IllegalArgumentException(
          "calculerCheminMinimisantNombreTroncons=> Station de départ inconnue");
    }
    Station stationArrivee = correspondanceStringStation.get(nomStationArrivee);
    if (stationArrivee == null) {
      throw new IllegalArgumentException(
          "calculerCheminMinimisantNombreTroncons=> Station d'arrivée inconnue");
    }
    stationsParcourues.add(stationDepart);
    fileBFS.add(stationDepart);
    boolean trouve = false;

    while (!fileBFS.isEmpty() && !trouve) {
      Station stationParcourue = fileBFS.poll();
      Set<Troncon> ensembleTronconsSortant = mapTronconsParStation.get(stationParcourue);
      for (Troncon troncon : ensembleTronconsSortant) {
        if (!stationsParcourues.contains(troncon.getStationFin())) {
          fileBFS.add(troncon.getStationFin());
          parcoursDesStations.put(troncon.getStationFin(), troncon);
          stationsParcourues.add(troncon.getStationFin());
        }

        trouve = troncon.getStationFin().equals(stationArrivee);
      }
    }
    affichage(stationDepart, stationArrivee, parcoursDesStations);
  }

  public void calculerCheminMinimisantTempsTransport(String stationDepart, String stationArrivee) {
    TreeSet<Station> treeSetStation = new TreeSet<>(
        Comparator.comparing(Station::getTempsPourArriver).thenComparing(Station::getNomStation));
    HashMap<Station, Troncon> parcoursDesStations = new HashMap<>();
    HashSet<Station> definitives = new HashSet<>();

    Station depart = correspondanceStringStation.get(stationDepart);
    if (depart == null) {
      throw new IllegalArgumentException(
          "calculerCheminMinimisantTempsTransport=> Station de départ inconnue");
    }
    Station arrivee = correspondanceStringStation.get(stationArrivee);
    if (arrivee == null) {
      throw new IllegalArgumentException(
          "calculerCheminMinimisantTempsTransport=> Station d'arrivée inconnue");
    }

    depart.setTempsPourArriver(0);
    treeSetStation.add(depart);

    while (!treeSetStation.isEmpty()) {
      Station stationParcourue = treeSetStation.pollFirst();
      definitives.add(stationParcourue);
      if (stationParcourue.equals(arrivee)) {
        break;
      }

      Set<Troncon> ensembleTronconsSortant = mapTronconsParStation.get(stationParcourue);
      for (Troncon troncon : ensembleTronconsSortant) {
        Station stationArriveeTronconCourant = troncon.getStationFin();
        int tempsPourSuivant = stationParcourue.getTempsPourArriver() + troncon.getDuree();
        if (!definitives.contains(stationArriveeTronconCourant) && (
            !treeSetStation.contains(stationArriveeTronconCourant)
                || tempsPourSuivant < stationArriveeTronconCourant.getTempsPourArriver())) {
          treeSetStation.remove(stationArriveeTronconCourant);
          stationArriveeTronconCourant.setTempsPourArriver(tempsPourSuivant);
          treeSetStation.add(stationArriveeTronconCourant);
          parcoursDesStations.put(stationArriveeTronconCourant, troncon);
        }
      }
    }
    affichage(depart, arrivee, parcoursDesStations);
  }

  public void affichage(Station stationDepart, Station arrivee,
      HashMap<Station, Troncon> parcoursDesStations) {
    int dureeTotale = 0;
    int dureeTransport = 0;
    int nbTroncons = 0;
    Station retracageStation = arrivee;
    ArrayList<Troncon> tracageParcours = new ArrayList<>();
    while (!retracageStation.equals(stationDepart)) {
      Troncon tronconRetracage = parcoursDesStations.get(retracageStation);
      Troncon tronconSuivant = parcoursDesStations.get(tronconRetracage.getStationDepart());
      Troncon tronconGroupe = new Troncon(tronconRetracage.getLigne(),
          tronconRetracage.getStationDepart(), tronconRetracage.getStationFin(),
          tronconRetracage.getDuree());
      int idLigne = tronconRetracage.getLigne().getId();

      dureeTotale += tronconRetracage.getLigne().getAttenteMoyenne();

      if (tronconSuivant != null && tronconSuivant.getLigne().getId() == idLigne) {
        tronconGroupe.setDuree(0);
        while (idLigne == tronconSuivant.getLigne().getId()) {
          tronconGroupe.setStationDepart(tronconRetracage.getStationDepart());
          tronconGroupe.setDuree(tronconGroupe.getDuree() + tronconRetracage.getDuree());
          tronconSuivant = parcoursDesStations.get(tronconRetracage.getStationDepart());
          tronconRetracage = tronconSuivant;
          nbTroncons++;
        }
      } else {
        nbTroncons++;
      }

      tracageParcours.add(tronconGroupe);
      dureeTransport += tronconGroupe.getDuree();
      retracageStation = tronconGroupe.getStationDepart();
    }
    dureeTotale += dureeTransport;

    Collections.reverse(tracageParcours);
    for (Troncon troncon : tracageParcours) {
      System.out.println(troncon);
    }

    System.out.println("NbrTroncons=" + nbTroncons);
    System.out.println("dureeTransport=" + dureeTransport + "  dureeTotale=" + dureeTotale);
  }

  private void creerLigne(String stringLigneACreer) {
    String[] datas = stringLigneACreer.split(",");
    int identifiantLigne = Integer.parseInt(datas[0]);
    String numeroTransport = datas[1];
    String stationDepart = datas[2];
    String stationArrivee = datas[3];
    String typeTransport = datas[4];
    int attenteEstimee = Integer.parseInt(datas[5]);

    Ligne nouvelleLigne = new Ligne(identifiantLigne, attenteEstimee, stationDepart, stationArrivee,
        typeTransport, numeroTransport);
    correspondanceIdLigneVersLigne.put(identifiantLigne, nouvelleLigne);
  }

  private void creerStation(String nomStation) {
    Station station = new Station(nomStation);
    correspondanceStringStation.put(nomStation, station);
  }

  private void creerTroncon(String stringTronconACreer) {
    String[] datas = stringTronconACreer.split(",");
    String nomStationDepart = datas[1];
    String nomStationArrivee = datas[2];

    if (!correspondanceStringStation.containsKey(nomStationDepart)) {
      creerStation(nomStationDepart);
    }
    Station stationDepart = correspondanceStringStation.get(nomStationDepart);

    if (!correspondanceStringStation.containsKey(nomStationArrivee)) {
      creerStation(nomStationArrivee);
    }
    Station stationArrivee = correspondanceStringStation.get(nomStationArrivee);

    Troncon troncon = new Troncon(correspondanceIdLigneVersLigne.get(Integer.parseInt(datas[0])),
        stationDepart, stationArrivee, Integer.parseInt(datas[3]));
    if (!mapTronconsParStation.containsKey(stationDepart)) {
      mapTronconsParStation.put(stationDepart, new HashSet<>());
    }
    mapTronconsParStation.get(stationDepart).add(troncon);
  }
}
