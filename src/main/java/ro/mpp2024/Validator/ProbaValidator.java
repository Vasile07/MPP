package ro.mpp2024.Validator;

import ro.mpp2024.Domain.Proba;

public class ProbaValidator implements AbstractValidator<Proba> {
    @Override
    public void validate(Proba proba) {
        String errors = "";

        if(proba.getDistanta() != 50 && proba.getDistanta() != 200 && proba.getDistanta() != 800 && proba.getDistanta() != 1500)
            errors += "Distanta invalida!\n";
        if(proba.getLocatie().isEmpty())
            errors += "Locatie invalida!\n";

        if(!errors.isEmpty())
            throw new RuntimeException(errors);
    }
}
