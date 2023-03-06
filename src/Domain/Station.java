package Domain;

import java.util.Objects;

public class Station {

    private final String nomStation;

    public Station(String nomStation) {
        this.nomStation = nomStation;
    }


    public String getNomStation() {
        return nomStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(nomStation, station.nomStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomStation);
    }

    @Override
    public String toString() {
        return "Domain.Station{" +
                "nomStation='" + nomStation + '\'' +
                '}';
    }
}
