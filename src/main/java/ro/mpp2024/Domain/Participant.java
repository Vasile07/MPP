package ro.mpp2024.Domain;

import java.util.Objects;

public class Participant extends Entity<Integer>{
    private String nume;
    private String prenume;
    private Integer varsta;

    public Participant(String nume, String prenume, Integer varsta) {
        this.nume = nume;
        this.prenume = prenume;
        this.varsta = varsta;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Participant that = (Participant) o;
        return Objects.equals(nume, that.nume) && Objects.equals(prenume, that.prenume) && Objects.equals(varsta, that.varsta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nume, prenume, varsta);
    }

    @Override
    public String toString() {
        return "Participant{" +
                "nume='" + nume + '\'' +
                ", prenume='" + prenume + '\'' +
                ", varsta=" + varsta +
                '}';
    }
}
