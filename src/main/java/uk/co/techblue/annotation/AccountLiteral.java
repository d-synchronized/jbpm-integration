/*******************************************************************************
 * Copyright 2013, Barbon. All Rights Reserved. 
 * No part of this content may be used without Barbon's express consent.
 ******************************************************************************/
package uk.co.techblue.annotation;

import javax.enterprise.util.AnnotationLiteral;

import uk.co.techblue.dto.AccountType;

/**
 * The Class AccountLiteral.
 */
public class AccountLiteral extends AnnotationLiteral<Account> implements Account {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2514922023705276966L;

    /** The values. */
    private final AccountType values;

    /** The description. */
    private final String description;

    public AccountLiteral(final AccountType values, final String description) {
        this.values = values;
        this.description = description;
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.co.techblue.annotation.Account#values()
     */
    @Override
    public AccountType values() {
        return values;
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.co.techblue.annotation.Account#description()
     */
    @Override
    public String description() {
        return description;
    }

}
