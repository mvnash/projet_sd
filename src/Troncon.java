public class Troncon {
    private int identifiant_ligne;
    private Station stationDepart;
    private Station stationFin;
    private int duree;

    public Troncon(int identifiant_ligne, Station stationDepart, Station stationFin, int duree) {
        this.identifiant_ligne = identifiant_ligne;
        this.stationDepart = stationDepart;
        this.stationFin = stationFin;
        this.duree = duree;
    }

    public int getIdentifiant_ligne() {
        return identifiant_ligne;
    }

    public Station getStationDepart() {
        return stationDepart;
    }

    public Station getStationFin() {
        return stationFin;
    }

    public int getDuree() {
        return duree;
    }

    public void setIdentifiant_ligne(int identifiant_ligne) {
        this.identifiant_ligne = identifiant_ligne;
    }

    public void setStationDepart(Station stationDepart) {
        this.stationDepart = stationDepart;
    }

    public void setStationFin(Station stationFin) {
        this.stationFin = stationFin;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }
}
