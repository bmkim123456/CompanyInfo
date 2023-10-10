package com.article.article.exception;

public class SearchParamException extends RuntimeException{

    private final int idSeq;
    private final String companyName;

    public SearchParamException (String message, int idSeq, String companyName) {
        super(message + idSeq + " " + companyName);
        this.idSeq = idSeq;
        this.companyName = companyName;
    }

    public int getIdSeq() {
        return idSeq;
    }

    public String getCompanyName() {
        return companyName;
    }
}
