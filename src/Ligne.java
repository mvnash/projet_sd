public class Ligne {
    private int id, numeroTransport, tempsTrajets;
    private Station stationDepart, stationArrivee;
    private String transport;

    public Ligne(int id, int numero, int tempsTrajets, Station stationDepart, Station stationArrivee, String transport) {
        this.id = id;
        this.numeroTransport = numero;
        this.tempsTrajets = tempsTrajets;
        this.stationDepart = stationDepart;
        this.stationArrivee = stationArrivee;
        this.transport = transport;
    }
}
