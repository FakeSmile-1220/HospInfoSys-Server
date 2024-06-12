package org.bedrock.hospinfosysserver;

public record RegistInfo(Integer id, String realName, String gender, String cardNumber, String birthdate, Integer age,
                         String homeAddress, String deptName, String doctorName, String registLevel, String isBook,
                         Double registFee, String registDate, String diagnosis, String prescription, Double drugPrice,
                         int visitState) {
}
