package org.bedrock.hospinfosysserver;

public record Doctor(String id, String realName, String password, String deptName, String registLevel,
                     Double registFee) {
}
