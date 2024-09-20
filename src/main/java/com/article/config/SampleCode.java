package com.article.config;

import org.springframework.util.ObjectUtils;

import java.util.List;

public class SampleCode {

    public boolean isTerminationCompany (String status, String termination) {
        List<String> rightTermination = rightCompanyTermination();
        List<String> falseTermination = falseCompanyTermination();
        List<String> choiceTermination = choiceCompanyTermination();
        List<String> rightStatus = companyStatus();
        List<String> falseCompanyStats = companyStatus();

        for (String terminationValue : rightTermination) {
            if (termination.equals(terminationValue)) {
                return true;
            }
        }

        for (String closeTermination : falseTermination) {
            if (termination.equals(closeTermination)) {
                return false;
            }
        }

        for (String terminationValue : choiceTermination) {
            for (String statusValue : rightStatus) {
                if (terminationValue.equals(termination) && statusValue.equals(status)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> rightCompanyTermination () {

        rightCompanyTermination().add("TAXPAYER");
        rightCompanyTermination().add("SIMPLYFIED TAXPAYER");
        rightCompanyTermination().add("DUTY_FREE");
        rightCompanyTermination().add("ETC");

        return rightCompanyTermination();
    }

    public List<String> falseCompanyTermination () {
        falseCompanyTermination().add("CLOSED");
        return falseCompanyTermination();
    }

    public List<String> choiceCompanyTermination () {

        choiceCompanyTermination().add("CLOSE_TEMPORARILY");
        choiceCompanyTermination().add("NO_DATA");
        return choiceCompanyTermination();
    }

    public List<String> companyStatus () {
        companyStatus().add("분할합병해산");
        companyStatus().add("합병해산");
        companyStatus().add("회생철차");
        companyStatus().add("보전관리");
        companyStatus().add("조직변경해산");
        companyStatus().add("살아있는등기");

        return companyStatus();
    }

    public List<String> falseCompanyStats () {

        falseCompanyStats().add("기타폐쇄");
        falseCompanyStats().add("청산종결간주");
        falseCompanyStats().add("파산");
        falseCompanyStats().add("해산");
        falseCompanyStats().add("청산종결");
        falseCompanyStats().add("해산간주");
        return falseCompanyStats();
    }
}
