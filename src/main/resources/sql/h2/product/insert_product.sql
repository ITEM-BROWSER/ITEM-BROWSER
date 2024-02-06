-- insert_product.sql
INSERT INTO PRODUCT ( ID
                    , NAME
                    , CATEGORY
                    , DETAIL
                    , STATUS
                    , QUANTITY
                    , SELLER_ID
                    , SELL_START_DATETIME
                    , SELL_END_DATETIME
                    , DISPLAY_NAME
                    , UNIT_PRICE
                    , BRAND
                    , DELIVERY_FEE_TYPE
                    , DELIVERY_METHOD
                    , DELIVERY_DEFAULT_FEE
                    , CREATED_DATE
                    , UPDATED_DATE)
VALUES ( 1
       , 'Product A'
       , 1
       , 'This is product A'
       , 'APPROVED'
       , 100
       , 'seller1'
       , NOW()
       , DATE_ADD(NOW(), INTERVAL 30 DAY)
       , 'Product A'
       , 10000
       , 'BrandA'
       , 'FREE'
       , 'SEQUENCIAL'
       , 0
       , NOW()
       , NOW()),
       ( 2
       , 'Product B'
       , 2
       , 'This is product B'
       , 'APPROVED'
       , 50
       , 'seller2'
       , NOW()
       , DATE_ADD(NOW(), INTERVAL 30 DAY)
       , 'Product B'
       , 15000
       , 'BrandB'
       , 'CHARGE_RECEIVED'
       , 'COLD_FRESH'
       , 3000
       , NOW()
       , NOW());