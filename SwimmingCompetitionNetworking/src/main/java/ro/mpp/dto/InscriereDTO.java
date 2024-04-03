package ro.mpp.dto;

import java.io.Serializable;
import java.util.List;

public class InscriereDTO implements Serializable {
    String firstname;String lastname;
    List<Integer> probe;

    public InscriereDTO(String firstname, String lastname, List<Integer> probe) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.probe = probe;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public List<Integer> getProbe() {
        return probe;
    }
}
