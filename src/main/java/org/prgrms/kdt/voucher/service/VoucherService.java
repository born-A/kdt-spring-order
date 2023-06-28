package org.prgrms.kdt.voucher.service;

import org.prgrms.kdt.voucher.domain.FixedAmountVoucher;
import org.prgrms.kdt.voucher.domain.PercentDiscountVoucher;
import org.prgrms.kdt.voucher.domain.Voucher;
import org.prgrms.kdt.voucher.utils.VoucherType;
import org.prgrms.kdt.voucher.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class VoucherService {

    @Qualifier("file")
    private final VoucherRepository voucherRepository;
    public VoucherService(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    public Voucher getVoucher(UUID voucherId) {
        return voucherRepository
                .findById(voucherId)
                .orElseThrow(() -> new RuntimeException("can not find a voucher for" + voucherId));
    }

    public List<Voucher> getVouchers() throws IOException, ClassNotFoundException {
        return voucherRepository.findAll();
    }


    public Voucher save(VoucherType type, Long discount) throws IOException {
        VoucherType voucherType = type;
        switch (voucherType){
            case FIXED_AMOUNT_VOUCHER, PERCENT_DISCOUNT_VOUCHER -> {
                System.out.println("voucherType = " + voucherType);
                return voucherRepository.insert(voucherType.makeVoucher(discount));
            }
            default -> throw new RuntimeException("해당 바우처는 발급 불가능합니다");
        }

    }

    public Voucher useVoucher(Voucher voucher) {
        return voucher;
    }
}
