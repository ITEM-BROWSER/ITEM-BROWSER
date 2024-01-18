INSERT INTO ORDERS ( ORDERER_ID
                   , ORDER_STATUS
                   , PAID_DATE
                   , SHIPPING_INFO_ID
                   , CREATED_DATE
                   , UPDATED_DATE
                   , DELETED_DATE)
VALUES ( 'user1'
       , 'ACCEPT'
       , now()
       , 1
       , now()
       , null
       , null);