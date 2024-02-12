package com.psj.itembrowser.order.service.impl;

import org.springframework.stereotype.Service;

import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.product.domain.vo.Product;

/**
 *packageName    : com.psj.itembrowser.order.service.impl
 * fileName       : DiscountService
 * author         : ipeac
 * date           : 2024-02-12
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-02-12        ipeac       최초 생성
 */
@Service
public interface DiscountService {
	double calculateDiscount(Product product, Member member);
}