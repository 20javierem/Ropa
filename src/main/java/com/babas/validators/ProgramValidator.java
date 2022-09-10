package com.babas.validators;

import com.babas.utilities.Utilities;
import com.moreno.Notify;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

public abstract class ProgramValidator {
    private static ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
    protected static Validator PROGRAMA_VALIDATOR = FACTORY.getValidator();

    public static Set<ConstraintViolation<Object>> loadViolations(Object t) {
        return PROGRAMA_VALIDATOR.validate(t);
    }

    public static void mostrarErrores(Set<ConstraintViolation<Object>> errors){
        Object[] errores=errors.toArray();
        String error = "Verfique el campo: "+((ConstraintViolation<?>) errores[0]).getMessage();
        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR",error);
    }
}
