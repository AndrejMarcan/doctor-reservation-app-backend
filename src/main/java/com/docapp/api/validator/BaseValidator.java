package com.docapp.api.validator;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.docapp.api.exception.BadRequestException;

public class BaseValidator {
	/** See {@link Objects#isNull(Object)} */
    protected void validateObjectIsNotNull(Object object, String message)
            throws BadRequestException {
        if (Objects.isNull(object)) {
            throw new BadRequestException(message);
        }
    }

    /** See {@link StringUtils#isBlank(CharSequence)} */
    protected void validateStringIsNotEmpty(String inputString, String message)
            throws BadRequestException {
        if (StringUtils.isBlank(inputString)) {
            throw new BadRequestException(message);
        }
    }
}
