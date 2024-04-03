package ro.mpp.dto;

import java.io.Serializable;
import java.util.Objects;

public class AngajatDTO implements Serializable {
    String firstname;
    String lastname;
    String password;

    public AngajatDTO(String firstname, String lastname, String password) {

        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
    }


    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AngajatDTO that = (AngajatDTO) o;
        return Objects.equals(firstname, that.firstname) && Objects.equals(lastname, that.lastname) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname, password);
    }
}
