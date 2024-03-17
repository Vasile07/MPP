package ro.mpp2024.Domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Proba extends Entity<Integer>{
    private Integer distanta;
    private StilInot stil;
    private LocalDateTime dataDesfasurarii;
    private String locatie;

    public Proba(Integer distanta, StilInot stil, LocalDateTime dataDesfasurarii, String locatie) {
        this.distanta = distanta;
        this.stil = stil;
        this.dataDesfasurarii = dataDesfasurarii;
        this.locatie = locatie;
    }

    public Integer getDistanta() {
        return distanta;
    }

    public void setDistanta(Integer distanta) {
        this.distanta = distanta;
    }

    public StilInot getStil() {
        return stil;
    }

    public void setStil(StilInot stil) {
        this.stil = stil;
    }

    public LocalDateTime getDataDesfasurarii() {
        return dataDesfasurarii;
    }

    public void setDataDesfasurarii(LocalDateTime dataDesfasurarii) {
        this.dataDesfasurarii = dataDesfasurarii;
    }

    public String getLocatie() {
        return locatie;
    }

    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Proba proba = (Proba) o;
        return Objects.equals(distanta, proba.distanta) && stil == proba.stil && Objects.equals(dataDesfasurarii, proba.dataDesfasurarii) && Objects.equals(locatie, proba.locatie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), distanta, stil, dataDesfasurarii, locatie);
    }

    @Override
    public String toString() {
        return "Proba{" +
                "distanta=" + distanta +
                ", stil=" + stil +
                ", dataDesfasurarii=" + dataDesfasurarii +
                ", locatie='" + locatie + '\'' +
                '}';
    }
}
