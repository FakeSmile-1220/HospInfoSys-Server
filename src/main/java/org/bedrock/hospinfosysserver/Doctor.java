package org.bedrock.hospinfosysserver;

public class Doctor extends User {
    private final String deptName;
    private final String registLevel;
    private final Double registFee;

    public Doctor(String id, String realName, String password, String type, String deptName, String registLevel, Double registFee) {
        super(id, realName, password, type);
        this.deptName = deptName;
        this.registLevel = registLevel;
        this.registFee = registFee;
    }

    public String getDeptName() {
        return deptName;
    }

    public String getRegistLevel() {
        return registLevel;
    }

    public Double getRegistFee() {
        return registFee;
    }
}