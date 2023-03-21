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

        String ligneFichierLignes;

        while (scannerLigne.hasNext()) {
            ligneFichierLignes = scannerLigne.nextLine();
            String[] datas = ligneFichierLignes.split(",");
            int identifiantLigne = Integer.parseInt(datas[0]);
            String numeroTransport = datas[1];
            String stationDepart = datas[2];
            String stationArrivee = datas[3];
            String typeTransport = datas[4];
            int attenteEstimee = Integer.parseInt(datas[5]);

            Ligne nouvelleLigne = new Ligne(identifiantLigne, attenteEstimee, stationDepart, stationArrivee, typeTransport, numeroTransport);
            correspondanceIdLigneVersLigne.put(identifiantLigne, nouvelleLigne);
        }


        String ligneFichierTroncons;

        while (scannerTroncons.hasNext()) {
            ligneFichierTroncons = scannerTroncons.nextLine();
            String[] datas = ligneFichierTroncons.split(",");
            String nomStationDepart = datas[1];
            String nomStationArrivee = datas[2];

            if (!correspondanceStringStation.containsKey(nomStationDepart)) {
                Station stationDepart = new Station(nomStationDepart);
                correspondanceStringStation.put(nomStationDepart, stationDepart);
            }
            Station stationDepart = correspondanceStringStation.get(nomStationDepart);

            if (!correspondanceStringStation.containsKey(nomStationArrivee)) {
                Station stationArrivee = new Station(nomStationArrivee);
                correspondanceStringStation.put(nomStationArrivee, stationArrivee);
            }
            Station stationArrivee = correspondanceStringStation.get(nomStationArrivee);

            Troncon troncon = new Troncon(correspondanceIdLigneVersLigne.get(Integer.parseInt(datas[0])), stationDepart, stationArrivee, Integer.parseInt(datas[3]));
            if (!mapTronconsParStation.containsKey(stationDepart)) {
                mapTronconsParStation.put(stationDepart, new HashSet<>());
            }
            mapTronconsParStation.get(stationDepart).add(troncon);
        }
    }

    public void calculerCheminMinimisantNombreTroncons(String nomStationDepart, String nomStationArrivee) {
        HashMap<Station, Troncon> parcoursDesStations = new HashMap<>();
        HashSet<Station> stationsParcourues = new HashSet<>();
        Queue<Station> fileBFS = new ArrayDeque<>();

        Station stationDepart = correspondanceStringStation.get(nomStationDepart);
        Station stationArrivee = correspondanceStringStation.get(nomStationArrivee);
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
        TreeSet<Station> treeSetStation = new TreeSet<>(Comparator.comparing(Station::getTempsPourArriver).thenComparing(Station::getNomStation));
        HashMap<Station, Troncon> parcoursDesStations = new HashMap<>();
        HashSet<Station> definitives=new HashSet<>();

        Station depart = correspondanceStringStation.get(stationDepart);
        Station arrivee = correspondanceStringStation.get(stationArrivee);

        depart.setTempsPourArriver(0);
        treeSetStation.add(depart);

        while (!treeSetStation.isEmpty()) {
            Station stationParcourue = treeSetStation.pollFirst();
            definitives.add(stationParcourue);
            if (stationParcourue.equals(arrivee)) break;

            Set<Troncon> ensembleTronconsSortant = mapTronconsParStation.get(stationParcourue);
            for (Troncon troncon : ensembleTronconsSortant) {
                Station stationArriveeTronconCourant = troncon.getStationFin();
                int tempsPourSuivant = stationParcourue.getTempsPourArriver() + troncon.getDuree();
                if (!definitives.contains(stationArriveeTronconCourant) && (!treeSetStation.contains(stationArriveeTronconCourant) || tempsPourSuivant < stationArriveeTronconCourant.getTempsPourArriver())) {
                    treeSetStation.remove(stationArriveeTronconCourant);
                    stationArriveeTronconCourant.setTempsPourArriver(tempsPourSuivant);
                    treeSetStation.add(stationArriveeTronconCourant);
                    parcoursDesStations.put(stationArriveeTronconCourant, troncon);
                }
            }
        }
        affichage(depart, arrivee, parcoursDesStations);
    }

    public void affichage(Station stationDepart, Station arrivee, HashMap<Station, Troncon> parcoursDesStations) {
        int dureeTotale = 0;
        int dureeTransport = 0;
        int nbTroncons = 0;
        Station retracageStation = arrivee;
        Troncon tronconPrecedent = null;
        ArrayList<Troncon> tracageParcours = new ArrayList<>();
        while (!retracageStation.equals(stationDepart)) {
            Troncon tronconRetracage = parcoursDesStations.get(retracageStation);
            if (tronconRetracage == null) {
                break;
            }
            nbTroncons++;
            if (tronconPrecedent == null) {
                dureeTotale += tronconRetracage.getLigne().getAttenteMoyenne();
            } else {
                if (tronconPrecedent.getLigne().getId() != tronconRetracage.getLigne().getId()) {
                    dureeTotale += tronconRetracage.getLigne().getAttenteMoyenne();
                }
            }
            tronconPrecedent = tronconRetracage;
            tracageParcours.add(tronconRetracage);
            dureeTransport += tronconRetracage.getDuree();
            retracageStation = tronconRetracage.getStationDepart();
        }
        dureeTotale += dureeTransport;

        Collections.reverse(tracageParcours);
        for (Troncon troncon : tracageParcours) {
            System.out.println(troncon);
        }

        System.out.println("NbrTroncons=" + nbTroncons);
        System.out.println("dureeTransport=" + dureeTransport + "  dureeTotale=" + dureeTotale);
    }
}
