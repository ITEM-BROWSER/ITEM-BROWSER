package com.psj.itembrowser.cart.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.psj.itembrowser.cart.domain.dto.request.CartProductDeleteRequestDTO;
import com.psj.itembrowser.cart.domain.dto.request.CartProductRequestDTO;
import com.psj.itembrowser.cart.domain.dto.request.CartProductUpdateRequestDTO;
import com.psj.itembrowser.cart.domain.vo.Cart;
import com.psj.itembrowser.cart.domain.vo.CartProductRelation;
import com.psj.itembrowser.product.domain.vo.Product;

/**
 * packageName    : com.psj.itembrowser.test.mapper
 * fileName       : TestMapper
 * author         : ipeac
 * date           : 2023-09-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-27        ipeac       최초 생성
 */
@Mapper
public interface CartMapper {
	@Results(id = "cartResultMap", value = {
		@Result(id = true, property = "id", column = "ID"),
		@Result(property = "userId", column = "USER_ID"),
		@Result(property = "createdDate", column = "CREATED_DATE"),
		@Result(property = "updatedDate", column = "UPDATED_DATE"),
		@Result(property = "cartProductRelations", column = "ID", javaType = List.class, many = @Many(select = "com.psj.itembrowser.cart.mapper.CartMapper.getCartProductRelationsByCartId"))
	})
	@Select("SELECT ID, USER_ID, CREATED_DATE, UPDATED_DATE " +
		"FROM CART  " +
		"WHERE USER_ID = #{userId} " +
		"AND DELETED_DATE is null ")
	Cart getCartByUserId(@Param("userId") String userId);

	@ResultMap("cartResultMap")
	@Select("SELECT ID, USER_ID, CREATED_DATE, UPDATED_DATE " +
		"FROM CART " +
		"WHERE ID = #{cartId} " +
		"AND DELETED_DATE is null")
	Cart getCart(@Param("cartId") Long cartId);

	@ResultMap("cartResultMap")
	@Select("SELECT ID, USER_ID, CREATED_DATE, UPDATED_DATE, DELETED_DATE FROM CART")
	List<Cart> selectAllCarts();

	@Results(id = "cartProductRelationResultMap", value = {
		@Result(id = true, property = "cartId", column = "CART_ID"),
		@Result(property = "productId", column = "PRODUCT_ID"),
		@Result(property = "productQuantity", column = "PRODUCT_QUANTITY"),
		@Result(property = "createdDate", column = "CREATED_DATE"),
		@Result(property = "updatedDate", column = "UPDATED_DATE"),
		@Result(property = "cart", column = "CART_ID", javaType = Cart.class, one = @One(select = "com.psj.itembrowser.cart.mapper.CartMapper.getCart")),
		@Result(property = "product", column = "PRODUCT_ID", javaType = Product.class, one = @One(select = "com.psj.itembrowser.product.mapper.ProductMapper.findProductById"))
	})
	@Select("SELECT CART_ID, PRODUCT_ID, PRODUCT_QUANTITY, CREATED_DATE, UPDATED_DATE " +
		"FROM CART_PRODUCT_RELATION " +
		"WHERE CART_ID = #{cartId} " +
		"AND DELETED_DATE is null")
	List<CartProductRelation> getCartProductRelationsByCartId(@Param("cartId") Long cartId);

	@ResultMap("cartProductRelationResultMap")
	@Select("SELECT CART_ID, PRODUCT_ID, PRODUCT_ID, PRODUCT_QUANTITY, CREATED_DATE, UPDATED_DATE " +
		"FROM CART_PRODUCT_RELATION " +
		"WHERE CART_ID = #{cartId} " +
		"AND PRODUCT_ID = #{productId} " +
		"AND DELETED_DATE is null")
	CartProductRelation getCartProductRelation(@Param("cartId") Long cartId, @Param("productId") Long productId);

	@Insert("INSERT INTO CART (USER_ID, CREATED_DATE) " +
		"VALUES (#{userId}, NOW()) "
	)
	boolean insertCart(@Param("userId") String userId);

	@Insert("INSERT INTO CART_PRODUCT_RELATION (" +
		"CART_ID, PRODUCT_ID, PRODUCT_QUANTITY, CREATED_DATE" +
		") VALUES (" +
		" #{cartId} , #{productId} , #{quantity}, now()" +
		")")
	boolean insertCartProduct(CartProductRequestDTO cartProductRequestDTO);

	@Update("UPDATE CART_PRODUCT_RELATION " +
		"SET PRODUCT_QUANTITY = #{quantity}, UPDATED_DATE = NOW() " +
		"WHERE CART_ID = #{cartId} " +
		"AND PRODUCT_ID = #{productId} " +
		"AND DELETED_DATE is null")
	boolean updateCartProductRelation(CartProductUpdateRequestDTO cartProductUpdateRequestDTO);

	@Update("UPDATE CART_PRODUCT_RELATION " +
		"SET DELETED_DATE = NOW()" +
		"WHERE CART_ID = #{cartId} " +
		"AND PRODUCT_ID = #{productId} " +
		"AND DELETED_DATE is null")
	boolean deleteCartProductRelation(CartProductDeleteRequestDTO cartProductDeleteRequestDTO);
}