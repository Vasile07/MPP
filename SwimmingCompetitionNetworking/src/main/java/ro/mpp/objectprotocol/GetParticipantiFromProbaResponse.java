package ro.mpp.objectprotocol;

import ro.mpp.Domain.ParticipantDTO;

import java.util.List;

public class GetParticipantiFromProbaResponse implements Response{
    List<ParticipantDTO> participantDTOS;

    public GetParticipantiFromProbaResponse(List<ParticipantDTO> participantDTOS) {
        this.participantDTOS = participantDTOS;
    }

    public List<ParticipantDTO> getParticipantDTOS() {
        return participantDTOS;
    }
}
