package com.aaroncarlson.service;

import com.aaroncarlson.exception.BadRequestException;
import com.aaroncarlson.util.AppConstants;

public abstract class PagedResponseService {

    public void validatePageNumberAndSize(final int page, final int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }
        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size cannot be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

}
