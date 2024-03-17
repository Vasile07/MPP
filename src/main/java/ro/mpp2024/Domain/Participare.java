package ro.mpp2024.Domain;

import java.time.LocalDate;
import java.util.Objects;

public class Participare extends Entity<Pair<Integer,Integer>>{
    private LocalDate dataInscrierii;

    public Participare(LocalDate dataInscrierii) {
        this.dataInscrierii = dataInscrierii;
    }

    public LocalDate getDataInscrierii() {
        return dataInscrierii;
    }

    public void setDataInscrierii(LocalDate dataInscrierii) {
        this.dataInscrierii = dataInscrierii;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Participare that = (Participare) o;
        return Objects.equals(dataInscrierii, that.dataInscrierii);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dataInscrierii);
    }

    @Override
    public String toString() {
        return "ParticipareValdiator{" +
                getId().toString() +
                ", dataInscrierii=" + dataInscrierii +
                '}';
    }
}
