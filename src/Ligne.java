public class Ligne {
    private int id, attenteMoyenne;
    private String stationDepart, stationArrivee;
    private String typeTransport,numeroTransport;

    public Ligne(int id, int attenteMoyenne, String stationDepart, String stationArrivee, String typeTransport, String numeroTransport) {
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

    public void setStationDepart(String stationDepart) {
        this.stationDepart = stationDepart;
    }

    public void setStationArrivee(String stationArrivee) {
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

    public String getStationDepart() {
        return stationDepart;
    }

    public String getStationArrivee() {
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
        return "id=" + id +
                ", nom=" + numeroTransport +
                ", source=" + stationDepart +
                ", destination=" + stationArrivee +
                ", type='" + typeTransport + '\'' +
                ", attente='" + attenteMoyenne + '\'';
    }
}
