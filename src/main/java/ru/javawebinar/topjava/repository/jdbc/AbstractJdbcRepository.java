package ru.javawebinar.topjava.repository.jdbc;

import javax.validation.Validation;
import javax.validation.Validator;

public abstract class AbstractJdbcRepository {
    protected final Validator validator;

    public AbstractJdbcRepository() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}
