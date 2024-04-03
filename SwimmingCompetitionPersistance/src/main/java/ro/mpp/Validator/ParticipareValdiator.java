package ro.mpp.Validator;


import ro.mpp.Domain.Participare;

public class ParticipareValdiator implements AbstractValidator<Participare> {
    @Override
    public void validate(Participare participare) {
        String errors = "";

        if(!errors.isEmpty())
            throw new RuntimeException(errors);
    }
}
