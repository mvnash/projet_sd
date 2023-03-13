public class Ligne {
    private int id, attenteMoyenne;
    private Station stationDepart, stationArrivee;
    private String typeTransport,numeroTransport;

    public Ligne(int id, int attenteMoyenne, Station stationDepart, Station stationArrivee, String typeTransport, String numeroTransport) {
        this.id = id;
        this.attenteMoyenne = attenteMoyenne;
        this.stationDepart = stationDepart;
        this.stationArrivee = stationArrivee;
        this.typeTransport = typeTransport;
        this.numeroTransport = numeroTransport;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAttenteMoyenne(int attenteMoyenne) {
        this.attenteMoyenne = attenteMoyenne;
    }

    public void setStationDepart(Station stationDepart) {
        this.stationDepart = stationDepart;
    }

    public void setStationArrivee(Station stationArrivee) {
        this.stationArrivee = stationArrivee;
    }

    public void setTypeTransport(String typeTransport) {
        this.typeTransport = typeTransport;
    }

    public void setNumeroTransport(String numeroTransport) {
        this.numeroTransport = numeroTransport;
    }

    public int getId() {
        return id;
    }

    public int getAttenteMoyenne() {
        return attenteMoyenne;
    }

    public Station getStationDepart() {
        return stationDepart;
    }

    public Station getStationArrivee() {
        return stationArrivee;
    }

    public String getTypeTransport() {
        return typeTransport;
    }

    public String getNumeroTransport() {
        return numeroTransport;
    }

    @Override
    public String toString() {
        return
                "id=" + id +
                ", nom=" + numeroTransport +
                ", source=" + stationDepart +
                ", destination=" + stationArrivee +
                ", type='" + typeTransport + '\'' +
                ", attente='" + attenteMoyenne + '\'' +
                '}';
    }
}
