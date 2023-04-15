package br.com.mesdra.webfluxproject.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TrimStringValidator implements ConstraintValidator <TrimString, String>{

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if(value == null){
            return true;
        }
        return value.length() == value.trim().length();
    }
}
