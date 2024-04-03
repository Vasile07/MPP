package ro.mpp.objectprotocol;

import ro.mpp.dto.AngajatDTO;

public class LogOutRequest implements Request{
    private AngajatDTO angajatDTO;

    public LogOutRequest(AngajatDTO angajatDTO) {
        this.angajatDTO = angajatDTO;
    }

    public AngajatDTO getAngajatDTO() {
        return angajatDTO;
    }
}
