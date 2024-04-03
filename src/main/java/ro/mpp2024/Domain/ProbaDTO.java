package ro.mpp2024.Domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class ProbaDTO {
    private Integer idProba;
    private Integer distanta;
    private StilInot stil;
    private LocalDateTime dataDesfasurarii;
    private String locatie;
    private Integer nrInscrieri;

    public ProbaDTO(Integer idProba, Integer distanta, StilInot stil, LocalDateTime dataDesfasurarii, String locatie, Integer nrInscrieri) {
        this.idProba = idProba;
        this.distanta = distanta;
        this.stil = stil;
        this.dataDesfasurarii = dataDesfasurarii;
        this.locatie = locatie;
        this.nrInscrieri = nrInscrieri;
    }

    public Integer getIdProba() {
        return idProba;
    }

    public void setIdProba(Integer idProba) {
        this.idProba = idProba;
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

    public Integer getNrInscrieri() {
        return nrInscrieri;
    }

    public void setNrInscrieri(Integer nrInscrieri) {
        this.nrInscrieri = nrInscrieri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProbaDTO probaDTO = (ProbaDTO) o;
        return Objects.equals(idProba, probaDTO.idProba) && Objects.equals(distanta, probaDTO.distanta) && stil == probaDTO.stil && Objects.equals(dataDesfasurarii, probaDTO.dataDesfasurarii) && Objects.equals(locatie, probaDTO.locatie) && Objects.equals(nrInscrieri, probaDTO.nrInscrieri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProba, distanta, stil, dataDesfasurarii, locatie, nrInscrieri);
    }
}
