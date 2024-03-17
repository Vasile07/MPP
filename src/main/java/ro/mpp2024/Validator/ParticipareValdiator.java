package ro.mpp2024.Validator;

import ro.mpp2024.Domain.Participare;

public class ParticipareValdiator implements AbstractValidator<Participare> {
    @Override
    public void validate(Participare participare) {
        String errors = "";

        if(!errors.isEmpty())
            throw new RuntimeException(errors);
    }
}
