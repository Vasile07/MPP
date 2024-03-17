package ro.mpp2024.Validator;

import ro.mpp2024.Domain.Entity;

public interface AbstractValidator<E>{
    public void validate(E e);
}
