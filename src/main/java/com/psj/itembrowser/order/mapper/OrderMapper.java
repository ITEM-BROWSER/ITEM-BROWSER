package com.psj.itembrowser.order.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.psj.itembrowser.order.domain.dto.request.OrderPageRequestDTO;
import com.psj.itembrowser.order.domain.vo.Order;
import com.psj.itembrowser.order.domain.vo.OrdersProductRelation;

@Mapper
public interface OrderMapper {

	/**
	 * 주문에 대한 삭제일 업데이트
	 *
	 * @param requestDTO 주문 삭제 요청 DTO
	 */
	@Update("UPDATE ORDERS SET ORDER_STATUS =  #{orderStatus}, DELETED_DATE = NOW() WHERE id = #{id}")
	void deleteSoftly(OrderDeleteRequestDTO requestDTO);

	Order selectOrderWithNoCondition(@Param("id") long id);

	@Update("UPDATE ORDERS_PRODUCT_RELATION SET DELETED_DATE = NOW() WHERE GROUP_ID = #{orderId}")
	void deleteSoftlyOrderProducts(@Param("orderId") long orderId);

	@Results(id = "orderProductResultMap", value = {
		@Result(property = "groupId", column = "GROUP_ID"),
		@Result(property = "productId", column = "PRODUCT_ID"),
		@Result(property = "productQuantity", column = "PRODUCT_QUANTITY"),
		@Result(property = "createdDate", column = "CREATED_DATE"),
		@Result(property = "updatedDate", column = "UPDATED_DATE"),
		@Result(property = "deletedDate", column = "DELETED_DATE")
	})
	@Select(
		"SELECT GROUP_ID, PRODUCT_ID, PRODUCT_QUANTITY, CREATED_DATE, UPDATED_DATE, DELETED_DATE " +
			" FROM ORDERS_PRODUCT_RELATION " +
			"WHERE GROUP_ID = #{orderId}")
	List<OrdersProductRelation> selectOrderProductRelations(@Param("orderId") long orderId);

	@ResultMap("orderResultMap")
	@Select(
		"SELECT ID, ORDER_STATUS, CREATED_DATE, UPDATED_DATE, DELETED_DATE " +
			"FROM ORDERS " +
			"WHERE ID = #{orderId} " +
			"for update"
	)
	Order selectOrderWithPessimissticLock(@Param("orderId") long orderId);

	Order selectOrderWithNotDeleted(@Param("id") Long id);

	List<Order> selectOrdersWithPaginationAndNoCondition(
		@Param("orderPageRequestDTO") OrderPageRequestDTO requestDTO);

	List<Order> selectOrdersWithPaginationAndNotDeleted(
		@Param("orderPageRequestDTO") OrderPageRequestDTO requestDTO);

	List<OrdersProductRelation> selectOrderRelation(@Param("orderId") long orderId);

	void createOrder(Order order);

	void createOrderProducts(List<OrdersProductRelation> products);
}