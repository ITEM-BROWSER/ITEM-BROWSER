-- insert_cart.sql
INSERT INTO CART_PRODUCT_RELATION ( cart_id
                                  , product_id
                                  , product_quantity
                                  , created_date
                                  , updated_date
                                  , deleted_date)
VALUES ( 1
       , 1
       , 10
       , now()
       , now()
       , NULL),
       ( 1
       , 2
       , 20
       , now()
       , now()
       , NULL);