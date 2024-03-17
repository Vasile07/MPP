package ro.mpp2024.Domain;

import java.util.Objects;

public class Pair<Id1,Id2> {
    private Id1 id1;
    private Id2 id2;

    public Pair(Id1 id1, Id2 id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    public Id1 getId1() {
        return id1;
    }

    public void setId1(Id1 id1) {
        this.id1 = id1;
    }

    public Id2 getId2() {
        return id2;
    }

    public void setId2(Id2 id2) {
        this.id2 = id2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(id1, pair.id1) && Objects.equals(id2, pair.id2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id1, id2);
    }

    @Override
    public String toString() {
        return "Pair{" +
                "id1=" + id1 +
                ", id2=" + id2 +
                '}';
    }
}
