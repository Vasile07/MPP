package ro.mpp.Validator;


import ro.mpp.Domain.Angajat;

public class AngajatValidator implements AbstractValidator<Angajat> {
    @Override
    public void validate(Angajat angajat) {
        String errors = "";

        if(angajat.getNume().isEmpty())
            errors += "Nume invalid!\n";
        if(angajat.getPrenume().isEmpty())
            errors += "Prenume invalid!\n";
        if(angajat.getParola().isEmpty())
            errors += "Parola invalida!\n";
        if(angajat.getLocatie().isEmpty())
            errors += "Locatie invalida!\n";
        if(!errors.isEmpty())
            throw new RuntimeException(errors);
    }
}
