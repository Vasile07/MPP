package ro.mpp.objectprotocol;

import ro.mpp.dto.AngajatDTO;

public class LogInRequest implements Request{
    private AngajatDTO angajatDTO;

    public LogInRequest(AngajatDTO angajatDTO) {
        this.angajatDTO = angajatDTO;
    }

    public AngajatDTO getAngajatDTO() {
        return angajatDTO;
    }
}
