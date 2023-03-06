package Graph;

import Domain.Station;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Graph {
    private File lignes, troncons;
    Map<String, Station> correspondanceStringStation;
    private Scanner scannerTroncons;

    public Graph(File lignes, File troncons) {
        this.lignes = lignes;
        this.troncons = troncons;

        correspondanceStringStation = new HashMap<>();

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

            if (!correspondanceStringStation.containsKey(nomStationDepart)) {
                Station stationDepart = new Station(nomStationDepart);
                correspondanceStringStation.put(nomStationDepart, stationDepart);
            }

            if (!correspondanceStringStation.containsKey(nomStationArrivee)) {
                Station stationArrivee = new Station(nomStationArrivee);
                correspondanceStringStation.put(nomStationArrivee, stationArrivee);
            }
        }
    }

    public Station creeStationDepuisTroncons(String troncon) {
        return null;
    }

    public void calculerCheminMinimisantNombreTroncons(String stationDepart, String stationArrivee) {
        return;
    }

    public void calculerCheminMinimisantTempsTransport(String stationDepart, String stationArrivee) {
        return;
    }
}
