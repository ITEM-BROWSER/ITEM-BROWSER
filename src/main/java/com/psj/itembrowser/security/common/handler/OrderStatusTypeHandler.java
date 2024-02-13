package com.psj.itembrowser.security.common.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import com.psj.itembrowser.order.domain.vo.Order;

public class OrderStatusTypeHandler<E extends Enum<E>> implements TypeHandler<Order.OrderStatus> {
	private Class<E> type;

	public OrderStatusTypeHandler(Class<E> type) {
		this.type = type;
	}

	public void setParameter(PreparedStatement preparedStatement, int i, Order.OrderStatus status, JdbcType jdbcType) throws
		SQLException {
		preparedStatement.setString(i, status.getValue());
	}

	@Override
	public Order.OrderStatus getResult(ResultSet resultSet, String s) throws SQLException {
		String statusCode = resultSet.getString(s);
		return getStatus(statusCode);
	}

	@Override
	public Order.OrderStatus getResult(ResultSet resultSet, int i) throws SQLException {
		String statusCode = resultSet.getString(i);
		return getStatus(statusCode);
	}

	@Override
	public Order.OrderStatus getResult(CallableStatement callableStatement, int i) throws SQLException {
		String statusCode = callableStatement.getString(i);
		return getStatus(statusCode);
	}

	private Order.OrderStatus getStatus(String statusCode) {
		try {
			Order.OrderStatus[] enumConstants = (Order.OrderStatus[])type.getEnumConstants();
			for (Order.OrderStatus status : enumConstants) {
				if (Objects.equals(status.getValue(), statusCode)) {
					return status;
				}
			}
			return null;
		} catch (Exception exception) {
			throw new TypeException("Can't make enum object '" + type + "'", exception);
		}
	}
}