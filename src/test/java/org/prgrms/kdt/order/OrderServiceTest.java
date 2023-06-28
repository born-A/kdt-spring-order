package org.prgrms.kdt.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.kdt.voucher.domain.FixedAmountVoucher;
import org.prgrms.kdt.voucher.repository.MemoryVoucherRepository;
import org.prgrms.kdt.voucher.service.VoucherService;


import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    class OrderRepositoryStub implements OrderRepository{

        @Override
        public Order insert(Order order) {
            return null;
        }
    }

    @Test
    void createOrder() {
        //given
        var voucherRepository = new MemoryVoucherRepository();
        var fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(),100);
        voucherRepository.insert(fixedAmountVoucher);
        var sut = new OrderService(new VoucherService(voucherRepository), new OrderRepositoryStub());

        //when
        var order = sut.createOrder(UUID.randomUUID(), List.of(new OrderItem(UUID.randomUUID(), 200,1)),fixedAmountVoucher.getVoucherId());

        //then
        assertThat(order.totalAmount(), is(100L));
        assertThat(order.getVoucher().isEmpty(), is(false));
        assertThat(order.getVoucher().get().getVoucherId(), is(fixedAmountVoucher.getVoucherId()));
        assertThat(order.getOrderStatus(), is(OrderStatus.ACCEPTED));

    }

    @Test
    @DisplayName("오더가 생성돼야한다. (mock)")
    void createOrderByMock() {
        //given
        var voucherServiceMock= mock(VoucherService.class);
        var orderRepositoryMock = mock(OrderRepository.class);
        var fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 100);
        when(voucherServiceMock.getVoucher(fixedAmountVoucher.getVoucherId())).thenReturn(fixedAmountVoucher);

        var sut = new OrderService(voucherServiceMock,orderRepositoryMock);

        //when
        var order = sut.createOrder(
                UUID.randomUUID(),
                List.of(new OrderItem(UUID.randomUUID(),200,1)),
                fixedAmountVoucher.getVoucherId());

        //then
        assertThat(order.totalAmount(), is(100L));
        assertThat(order.getVoucher().isEmpty(), is(false));
        var inOrder = inOrder(voucherServiceMock,orderRepositoryMock);
        verify(voucherServiceMock).getVoucher(fixedAmountVoucher.getVoucherId());
        verify(orderRepositoryMock).insert(order);
        verify(voucherServiceMock).useVoucher(fixedAmountVoucher);
    }

}