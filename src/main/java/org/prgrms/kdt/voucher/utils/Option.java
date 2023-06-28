package org.prgrms.kdt.voucher.utils;

import java.util.Arrays;

public enum Option {
    EXIT, LIST, CREATE;

    public static Option of(String input) {
        return Arrays.stream(values())
                .filter(option -> option.name() == input.toUpperCase())
                .findFirst()
                .orElseThrow(()-> new RuntimeException("입력이 잘못되었습니다."));
    }

}
