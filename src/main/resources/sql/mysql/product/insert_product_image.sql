-- PRODUCT_IMAGE-data.sql
INSERT INTO product_image (PRODUCT_ID, FILE_NAME, FILE_PATH, TYPE, SIZE)
VALUES ( 1
       , 'image1.jpg'
       , '/path/to/image1.jpg'
       , 'image/jpeg'
       , 1024),
       ( 1
       , 'image2.jpg'
       , '/path/to/image2.jpg'
       , 'image/jpeg'
       , 2048),
       ( 1
       , 'image3.jpg'
       , '/path/to/image3.jpg'
       , 'image/jpeg'
       , 2048);