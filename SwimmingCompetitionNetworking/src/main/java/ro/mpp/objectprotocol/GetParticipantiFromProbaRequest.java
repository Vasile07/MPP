package ro.mpp.objectprotocol;

public class GetParticipantiFromProbaRequest implements Request{
    private Integer idProba;

    public GetParticipantiFromProbaRequest(Integer idProba) {
        this.idProba = idProba;
    }

    public Integer getIdProba() {
        return idProba;
    }
}
