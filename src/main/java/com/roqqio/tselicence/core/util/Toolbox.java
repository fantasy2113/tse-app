/*
 * Copyright 2020-2020 Futura Retail Solution AG. All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Futura Retail Solution AG ("Confidential Information").
 * You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement
 * you entered into with Futura Retail Solution AG.
 */
package com.roqqio.tselicence.core.util;

/**
 * @author jos
 */
public class Toolbox {

    private Toolbox() {

    }

    public static String trim(String str) {
        return str == null ? str : str.trim();
    }
}
