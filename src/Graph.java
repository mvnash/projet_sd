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

            Station stationDepart = new Station(nomStationDepart);
            Station stationArrivee = new Station(nomStationArrivee);

            if (!correspondanceStringStation.containsKey(nomStationDepart)) {
                correspondanceStringStation.put(nomStationDepart, stationDepart);
            }

            if (!correspondanceStringStation.containsKey(nomStationArrivee)) {
                correspondanceStringStation.put(nomStationArrivee, stationArrivee);
            }

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

                if (troncon.getStationFin().equals(stationArrivee)) {
                    trouve = true;
                    break;
                }
            }
        }
        int dureeTotale = 0;
        int dureeTransport = 0;
        int nbTroncons = 0;
        Station retracageStation = stationArrivee;
        ArrayList<Troncon> tracageParcours = new ArrayList<>();
        HashSet<Ligne> lignesParcourues = new HashSet();
        while (!retracageStation.equals(stationDepart)) {
            nbTroncons++;
            Troncon tronconRetracage = parcoursDesStations.get(retracageStation);
            if (tronconRetracage==null) {
                break;
            }
            lignesParcourues.add(tronconRetracage.getLigne());
            tracageParcours.add(tronconRetracage);
            dureeTransport += tronconRetracage.getDuree();
            retracageStation = tronconRetracage.getStationDepart();
            Troncon nvTroncon = parcoursDesStations.get(retracageStation);
            if (nvTroncon==null) {
                break;
            }

        }
        dureeTotale += dureeTransport;
        for (Ligne lignesParcourue : lignesParcourues) {
            dureeTotale += lignesParcourue.getAttenteMoyenne();
        }

        Collections.reverse(tracageParcours);
        for (Troncon troncon : tracageParcours) {
            System.out.println(troncon);
        }

        System.out.println("NbrTroncons="+nbTroncons);
        System.out.println("dureeTransport="+dureeTransport + "  dureeTotale=" + dureeTotale );
    }

    public void calculerCheminMinimisantTempsTransport(String stationDepart, String stationArrivee) {
        return;
    }
}
