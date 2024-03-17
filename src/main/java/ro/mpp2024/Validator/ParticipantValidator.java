package ro.mpp2024.Validator;

import ro.mpp2024.Domain.Participant;

public class ParticipantValidator implements AbstractValidator<Participant> {
    @Override
    public void validate(Participant participant) {
        String errors = "";
        if(participant.getNume().isEmpty())
            errors += "Nume invalid!\n";
        if(participant.getPrenume().isEmpty())
            errors += "Prenume invalid!\n";
        if(participant.getVarsta() < 18)
            errors += "Varsta invalida!\n";
        if(!errors.isEmpty())
            throw new RuntimeException(errors);
    }
}
