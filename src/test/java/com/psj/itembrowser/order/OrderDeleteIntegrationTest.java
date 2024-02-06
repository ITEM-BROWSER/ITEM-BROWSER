package com.psj.itembrowser.order;

import static java.util.concurrent.TimeUnit.MINUTES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.psj.itembrowser.order.service.OrderService;
import com.psj.itembrowser.security.common.exception.BadRequestException;
import com.psj.itembrowser.security.common.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.AUTO_CONFIGURED)
@ActiveProfiles("test")
public class OrderDeleteIntegrationTest {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Value("${customed.table.schema}")
    private String tableSchema;
    
    //TODO @sql 을 JPA 로 변경
    @Test
    @Sql(scripts = {"classpath:drop-table.sql", "classpath:schema.sql",
        "classpath:/sql/h2/member/insert_member.sql",
        "classpath:/sql/h2/shippinginfo/insert_shipping_info.sql", "classpath:sql/h2/product"
        +
        "/insert_product.sql", "classpath:sql/h2/order/insert_order_product.sql",
        "classpath:sql" + "/order/insert_order.sql"})
    @DisplayName("5개의 스레드로 동시에 주문 취소를 수행하는 경우, 1개의 스레드는 성공하고 나머지는 실패해야 합니다.")
    public void When_5ThreadsDeleteSameOrder_Expect_OnlyOneSuccessAnd()
        throws InterruptedException {
        // given - @Sql
        int threadCount = 5;
        int expectedSuccessCount = 1;
        int expectedFailCount = threadCount - expectedSuccessCount;
        
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger();
        List<Future<?>> futures = new ArrayList<>();
        
        // when
        for (int i = 0; i < threadCount; i++) {
            futures.add(executorService.submit(() -> {
                orderService.removeOrder(1L);
                successCount.incrementAndGet();
            }));
        }
        
        executorService.shutdown();
        boolean didAllThreadsTerminate = executorService.awaitTermination(1, MINUTES);
        
        // then
        // 스레드가 모두 1분안에 수행된 경우에만 성공
        assertThat(didAllThreadsTerminate).isTrue();
        // 하나만 성공하는지 확인
        assertThat(successCount.get()).isEqualTo(expectedSuccessCount);
        // 스레드의 개수와 future 가 동일한지 확인
        assertThat(futures.size()).isEqualTo(threadCount);
        
        // 실패한 테스트의 경우 CustomIllegalStateException 이 발생하므로, 이를 통해 실패한 테스트를 확인
        futures.stream()
            .filter(Future::isDone)
            .forEach(future -> {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    // CustomIllegalStateException 이 발생해야함 - 주문이 존재하지 않음
                    assertThatCode(() -> {
                        throw e.getCause();
                    }).isInstanceOf(BadRequestException.class)
                        .hasMessage(ErrorCode.ORDER_NOT_CANCELABLE.getMessage());
                    
                    // 실패 카운트 증대
                    failCount.getAndIncrement();
                }
            });
        
        // 실패카운트가 4개인지 확인
        assertThat(failCount.get()).isEqualTo(expectedFailCount);
    }
    
    //TODO @sql 을 JPA 로 변경시 테스트 데이터 삭제 구현필
    @AfterEach
    void cleanUpAfterEachTest() {
        String selectAllTableName = String.format(
            "SELECT Concat('TRUNCATE TABLE ', TABLE_NAME, ';') " +
                "FROM INFORMATION_SCHEMA.TABLES " +
                "WHERE table_schema = '%s' " +
                "AND table_type = 'BASE TABLE' ", tableSchema);
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0;");
        List<String> truncateTableSqls = jdbcTemplate.queryForList(selectAllTableName,
            String.class);
        truncateTableSqls.forEach(jdbcTemplate::execute);
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1;");
    }
}