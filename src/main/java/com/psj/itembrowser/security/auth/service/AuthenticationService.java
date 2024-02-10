package com.psj.itembrowser.security.auth.service;

import com.psj.itembrowser.member.annotation.CurrentUser;
import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.order.domain.vo.Order;
import java.util.List;
import lombok.NonNull;
import org.springframework.stereotype.Service;

/**
 * packageName    : com.psj.itembrowser.order.service.impl fileName       : AuthenticationService author         : ipeac date           : 2024-02-05 description    : =========================================================== DATE              AUTHOR NOTE ----------------------------------------------------------- 2024-02-05        ipeac       최초 생성
 */
@Service
public interface AuthenticationService {
    
    void authorizeOrdersWhenCustomer(@NonNull List<Order> orders, @CurrentUser Member jwt);
}