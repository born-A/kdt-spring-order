package org.prgrms.kdt.voucher.utils;

import org.prgrms.kdt.voucher.domain.FixedAmountVoucher;
import org.prgrms.kdt.voucher.domain.PercentDiscountVoucher;
import org.prgrms.kdt.voucher.domain.Voucher;

import java.util.UUID;
import java.util.function.Function;

public enum VoucherType {
    FIXED_AMOUNT_VOUCHER(1, value->new FixedAmountVoucher(UUID.randomUUID(), value)),
    PERCENT_DISCOUNT_VOUCHER(2, value->new PercentDiscountVoucher(UUID.randomUUID(), value));

    private final int matchNum;
    private final Function<Long, Voucher> expression;
    VoucherType(int matchNum,Function<Long, Voucher> expression) {
        this.matchNum = matchNum;
        this.expression = expression;
    }

    public static VoucherType of(String input) {
        return java.util.Arrays.stream(values())
                .filter(voucherType -> voucherType.matchNum == Integer.parseInt(input))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("입력이 잘못되었습니다."));
    }

    public Voucher makeVoucher(long discount){
        return expression.apply(discount);
    }
}
