package ro.mpp.Domain;

import java.util.Objects;

public class Angajat extends Entity<Integer>{
    private String nume;
    private String prenume;
    private String locatie;
    private String parola;

    public Angajat(String nume, String prenume, String locatie, String parola) {
        this.nume = nume;
        this.prenume = prenume;
        this.locatie = locatie;
        this.parola = parola;
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

    public String getLocatie() {
        return locatie;
    }

    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Angajat angajat = (Angajat) o;
        return Objects.equals(nume, angajat.nume) && Objects.equals(prenume, angajat.prenume) && Objects.equals(locatie, angajat.locatie) && Objects.equals(parola, angajat.parola);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nume, prenume, locatie, parola);
    }

    @Override
    public String toString() {
        return "Angajat{" +
                "nume='" + nume + '\'' +
                ", prenume='" + prenume + '\'' +
                ", locatie='" + locatie + '\'' +
                ", parola='" + parola + '\'' +
                '}';
    }
}
