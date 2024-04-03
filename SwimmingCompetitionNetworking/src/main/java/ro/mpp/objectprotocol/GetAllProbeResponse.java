package ro.mpp.objectprotocol;

import ro.mpp.Domain.ProbaDTO;

import java.util.List;

public class GetAllProbeResponse implements Response{
    List<ProbaDTO> probaDTOS;

    public GetAllProbeResponse(List<ProbaDTO> probaDTOS) {
        this.probaDTOS = probaDTOS;
    }

    public List<ProbaDTO> getProbaDTOS() {
        return probaDTOS;
    }
}
