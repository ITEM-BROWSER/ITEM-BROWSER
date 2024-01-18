-- ORDERS_PRODUCT_RELATION-data.sql
INSERT INTO ORDERS_PRODUCT_RELATION ( GROUP_ID
                                    , PRODUCT_ID
                                    , PRODUCT_QUANTITY
                                    , CREATED_DATE)
VALUES ( 1
       , 1
       , 1
       , now()),
       ( 1
       , 2
       , 2
       , now()),
       ( 2
       , 1
       , 1
       , now()),
       ( 2
       , 2
       , 2
       , now());