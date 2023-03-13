import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Graph {
    private File lignes, troncons;
    Map<String, Station> correspondanceStringStation;
    Map<Station, Set<Troncon>> mapTronconsParStation;
    Map<Integer, Ligne> correspondanceIdLigneVersLigne;
    private Scanner scannerTroncons, scannerLigne;

    public Graph(File lignes, File troncons) {
        this.lignes = lignes;
        this.troncons = troncons;

        correspondanceStringStation = new HashMap<>();
        mapTronconsParStation = new HashMap<>();
        correspondanceIdLigneVersLigne = new HashMap<>();

        try {
            scannerLigne = new Scanner(lignes);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        String ligneFichierLignes;

        while (scannerLigne.hasNext()) {
            ligneFichierLignes = scannerLigne.nextLine();
            String[] datas = ligneFichierLignes.split(",");
            Integer identifiantLigne = Integer.parseInt(datas[0]);
            String numeroTransport = datas[1];
            Station stationDepart = correspondanceStringStation.get(datas[2]); // TODO UTILISER DES STRINGS A LA PLACE DE STATION
            Station stationArrivee = correspondanceStringStation.get(datas[3]);
            String typeTransport = datas[4];
            Integer attenteEstimee = Integer.parseInt(datas[5]);

            Ligne nouvelleLigne = new Ligne(identifiantLigne, attenteEstimee, stationDepart, stationArrivee, typeTransport, numeroTransport);
            correspondanceIdLigneVersLigne.put(identifiantLigne, nouvelleLigne);
        }

        try {
            scannerTroncons = new Scanner(troncons);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
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

        Station stationArrivee = correspondanceStringStation.get(nomStationArrivee);
        Station stationDepart = correspondanceStringStation.get(nomStationDepart);
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

        Station retracageStation = stationArrivee;
        while (!retracageStation.equals(stationDepart)) {
            Troncon tronconRetracage = parcoursDesStations.get(retracageStation);
            if (tronconRetracage==null) break;
            System.out.println(tronconRetracage);
            retracageStation = tronconRetracage.getStationDepart();
        }

    }

    public void calculerCheminMinimisantTempsTransport(String stationDepart, String stationArrivee) {
        return;
    }
}
