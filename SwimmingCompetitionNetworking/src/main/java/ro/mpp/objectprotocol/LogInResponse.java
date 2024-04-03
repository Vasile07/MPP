package ro.mpp.objectprotocol;

import ro.mpp.Domain.Angajat;

public class LogInResponse implements Response{
    private Angajat angajat;

    public LogInResponse(Angajat angajat) {
        this.angajat = angajat;
    }

    public Angajat getAngajat() {
        return angajat;
    }
}
