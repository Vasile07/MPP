package ro.mpp.Domain;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ParticipantDTO implements Serializable {
    private String nume;
    private String prenume;
    private Integer varsta;
    private List<Integer> probe;

    public ParticipantDTO(String nume, String prenume, Integer varsta, List<Integer> probe) {
        this.nume = nume;
        this.prenume = prenume;
        this.varsta = varsta;
        this.probe = probe;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public Integer getVarsta() {
        return varsta;
    }

    public void setVarsta(Integer varsta) {
        this.varsta = varsta;
    }

    public List<Integer> getProbe() {
        return probe;
    }

    public void setProbe(List<Integer> probe) {
        this.probe = probe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipantDTO that = (ParticipantDTO) o;
        return Objects.equals(nume, that.nume) && Objects.equals(prenume, that.prenume) && Objects.equals(varsta, that.varsta) && Objects.equals(probe, that.probe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nume, prenume, varsta, probe);
    }
}
