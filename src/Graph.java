import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Graph {
    private File lignes, troncons;
    Map<String, Station> correspondanceStringStation;
    Map<Station, Set<Troncon>> mapTronconsParStation;
    private Scanner scannerTroncons;

    public Graph(File lignes, File troncons) {
        this.lignes = lignes;
        this.troncons = troncons;

        correspondanceStringStation = new HashMap<>();
        mapTronconsParStation = new HashMap<>();

        try {
            scannerTroncons = new Scanner(troncons);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        String ligneTroncon;

        while (scannerTroncons.hasNext()) {
            ligneTroncon = scannerTroncons.nextLine();
            String[] datas = ligneTroncon.split(",");
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

            Troncon troncon = new Troncon(Integer.parseInt(datas[0]), stationDepart, stationArrivee, Integer.parseInt(datas[3]));
            if (!mapTronconsParStation.containsKey(stationDepart)) {
                mapTronconsParStation.put(stationDepart, new HashSet<>());
            }
            mapTronconsParStation.get(stationDepart).add(troncon);
        }
    }

    public void calculerCheminMinimisantNombreTroncons(String nomStationDepart, String nomStationArrivee) {
        Set<Troncon> ensembleTronconsSortants = mapTronconsParStation.get(correspondanceStringStation.get(nomStationDepart));
        Station stationArrivee = correspondanceStringStation.get(nomStationArrivee);

        /*
        for (Troncon troncon : ensembleTronconsSortants){
            if (troncon.getStationFin().equals(stationArrivee)) break;
        }   // TODO
         */
    }

    public void calculerCheminMinimisantTempsTransport(String stationDepart, String stationArrivee) {
        return;
    }
}
