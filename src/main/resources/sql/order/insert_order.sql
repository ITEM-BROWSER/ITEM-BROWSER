insert into ORDERS( ID
                  , ORDERER_ID
                  , ORDER_STATUS
                  , PAID_DATE
                  , SHIPPING_INFO_ID
                  , CREATED_DATE
                  , UPDATED_DATE
                  , DELETED_DATE)
values ( 1
       , 1
       , 'ACCEPT'
       , now()
       , 1
       , now()
       , null
       , null),
       ( 2
       , 1
       , 'ACCEPT'
       , now()
       , 1
       , now()
       , null
       , null);