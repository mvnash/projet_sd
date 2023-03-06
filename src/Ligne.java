public class Ligne {
    private int id, duree;
    private Station stationDepart, stationArrivee;

    public Ligne(int id, int duree, Station stationDepart, Station stationArrivee) {
        this.id = id;
        this.duree = duree;
        this.stationDepart = stationDepart;
        this.stationArrivee = stationArrivee;
    }

    public int getId() {
        return id;
    }

    public int getDuree() {
        return duree;
    }

    public Station getStationDepart() {
        return stationDepart;
    }

    public Station getStationArrivee() {
        return stationArrivee;
    }
}
