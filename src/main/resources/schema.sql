-- 테이블 순서는 관계를 고려하여 한 번에 실행해도 에러가 발생하지 않게 정렬되었습니다.

-- member Table Create SQL
-- 테이블 생성 SQL - member
CREATE TABLE member
(
    `MEMBER_NO`       bigint       NOT NULL AUTO_INCREMENT COMMENT '멤버고유번호',
    `EMAIL`           varchar(255) NOT NULL COMMENT '아이디',
    `PASSWORD`        varchar(500) NOT NULL COMMENT '패스워드',
    `FIRST_NAME`      varchar(255) NOT NULL COMMENT '이름',
    `LAST_NAME`       varchar(255) NOT NULL COMMENT '성',
    `GENDER`          varchar(30)  NOT NULL COMMENT '성별',
    `PHONE_NUMBER`    varchar(45)  NOT NULL COMMENT '휴대폰번호. 휴대폰번호 (-) 가 없어야함',
    `BIRTHDAY`        date         NOT NULL COMMENT '생년월일. 생년월일',
    `ROLE`            varchar(100) NOT NULL COMMENT '권한. USER -> 일반 사용자, ADMIN -> 관리자',
    `STATUS`          varchar(100) NOT NULL COMMENT '회원 상태. ACTIVE -> 활성화, READY -> 대기, DISABLED -> 비활성화',
    `ADDRESS_MAIN`    varchar(500) NULL DEFAULT NULL COMMENT '주소',
    `ADDRESS_SUB`     varchar(500) NULL DEFAULT NULL COMMENT '주소 상세',
    `ZIP_CODE`        varchar(30)  NULL DEFAULT NULL COMMENT '우편번호',
    `CREATED_DATE`    timestamp    NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    `UPDATED_DATE`    timestamp    NULL DEFAULT CURRENT_TIMESTAMP COMMENT '업데이트일',
    `DELETED_DATE`    timestamp    NULL DEFAULT NULL COMMENT '삭제일',
    `LAST_LOGIN_DATE` timestamp    NULL DEFAULT NULL COMMENT '최종 로그인 일시',
    PRIMARY KEY (MEMBER_NO)
);

-- Unique Index 설정 SQL - member(EMAIL)
CREATE UNIQUE INDEX UQ_member_1
    ON member (EMAIL);


-- product Table Create SQL
-- 테이블 생성 SQL - product
CREATE TABLE product
(
    `ID`                    bigint                                                                    NOT NULL AUTO_INCREMENT COMMENT 'pk값',
    `NAME`                  varchar(500)                                                              NULL DEFAULT NULL COMMENT '상품명',
    `CATEGORY`              int                                                                       NULL DEFAULT NULL COMMENT '상품카테고리. CATEGORY 테이블 참조',
    `DETAIL`                varchar(45)                                                               NULL DEFAULT NULL COMMENT '상품설명',
    `STATUS`                varchar(200)                                                              NULL DEFAULT NULL COMMENT '상품상태. 심사중임시저장승인대기승인완료부분승인완료승인반려상품삭제품절',
    `QUANTITY`              int                                                                       NULL DEFAULT NULL COMMENT '상품 재고. 상품의 재고',
    `SELLER_ID`             varchar(45)                                                               NULL DEFAULT NULL COMMENT '판매자ID',
    `SELL_START_DATETIME`   datetime                                                                  NULL DEFAULT NULL COMMENT '판매시작일시',
    `SELL_END_DATETIME`     datetime                                                                  NULL DEFAULT NULL COMMENT '판매종료일시',
    `DISPLAY_NAME`          varchar(500)                                                              NULL DEFAULT NULL COMMENT '노출상품명. 실제노출되는 상품명',
    `UNIT_PRICE`            int                                                                       NULL DEFAULT NULL COMMENT '상품기본단가. 상품단가',
    `BRAND`                 varchar(300)                                                              NULL DEFAULT NULL COMMENT '브랜드',
    `DELIVERY_FEE_TYPE`     enum ('FREE','NOT_FREE','CHARGE_RECEIVED','CONDITIONAL_FREE')             NULL DEFAULT NULL COMMENT '배송비타입. 배송비타입',
    `DELIVERY_METHOD`       enum ('SEQUENCIAL','COLD_FRESH','MAKE_ORDER','AGENT_BUY','VENDOR_DIRECT') NULL DEFAULT NULL COMMENT '배송방법. 배송방법',
    `DELIVERY_DEFAULT_FEE`  int                                                                       NULL DEFAULT NULL COMMENT '기본배송비. 기본 배송',
    `FREE_SHIP_OVER_AMOUNT` int                                                                       NULL DEFAULT NULL COMMENT '무료배송금액. 무료 배송 기준 금액',
    `RETURN_CENTER_CODE`    varchar(255)                                                              NULL DEFAULT NULL COMMENT '반품지 센터 코드. CENTER 테이블 참조',
    `CREATED_DATE`          timestamp                                                                 NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    `UPDATED_DATE`          timestamp                                                                 NULL DEFAULT NULL COMMENT '업데이트일',
    `DELETED_DATE`          timestamp                                                                 NULL DEFAULT NULL COMMENT '삭제일',
    PRIMARY KEY (ID)
);

-- product_image Table Create SQL
-- 테이블 생성 SQL - product_image
CREATE TABLE product_image
(
    `ID`           bigint       NOT NULL AUTO_INCREMENT COMMENT '이미지 pk값',
    `PRODUCT_ID`   bigint       NOT NULL COMMENT '상품ID, product 테이블의 ID 참조',
    `FILE_NAME`    varchar(255) NOT NULL COMMENT '파일 이름',
    `FILE_PATH`    varchar(255) NOT NULL COMMENT '파일 경로',
    `TYPE`         varchar(50)  NOT NULL COMMENT 'MIME 타입',
    `SIZE`         bigint       NOT NULL COMMENT '파일 크기',
    `CREATED_DATE` timestamp    NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    `UPDATED_DATE` timestamp    NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '업데이트일',
    `DELETED_DATE` timestamp    NULL DEFAULT NULL COMMENT '삭제일',
    PRIMARY KEY (ID),
    FOREIGN KEY (PRODUCT_ID) REFERENCES product (ID) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Index 설정 SQL - product_image(PRODUCT_ID)
CREATE INDEX IDX_PRODUCT_IMAGE_PRODUCT_ID
    ON product_image (PRODUCT_ID);


-- orders_product_relation Table Create SQL
-- 테이블 생성 SQL - orders_product_relation
CREATE TABLE orders_product_relation
(
    `GROUP_ID`         bigint    NOT NULL COMMENT '주문그룹ID',
    `PRODUCT_ID`       bigint    NOT NULL COMMENT '상품ID',
    `PRODUCT_QUANTITY` int       NULL DEFAULT NULL COMMENT '주문한 상품 수. 주문한 상품 수',
    `CREATED_DATE`     timestamp NULL DEFAULT NULL COMMENT '생성일',
    `UPDATED_DATE`     timestamp NULL DEFAULT NULL COMMENT '수정일',
    `DELETED_DATE`     timestamp NULL DEFAULT NULL COMMENT '삭제일'
);

-- Index 설정 SQL - orders_product_relation(PRODUCT_ID)
CREATE INDEX FK_ORDERS_PRODUCT_RELATION_PRODUCT_ID_PRODUCT_ID
    ON orders_product_relation (PRODUCT_ID);

-- Unique Index 설정 SQL - orders_product_relation(GROUP_ID, PRODUCT_ID)
CREATE UNIQUE INDEX UQ_orders_product_relation_1
    ON orders_product_relation (GROUP_ID, PRODUCT_ID);

-- Foreign Key 설정 SQL - orders_product_relation(PRODUCT_ID) -> product(ID)
ALTER TABLE orders_product_relation
    ADD CONSTRAINT FOREIGN KEY (PRODUCT_ID)
        REFERENCES product (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - orders_product_relation(PRODUCT_ID)
-- ALTER TABLE orders_product_relation
-- DROP FOREIGN KEY CHARSET=utf8mb4;


-- shipping_infos Table Create SQL
-- 테이블 생성 SQL - shipping_infos
CREATE TABLE shipping_infos
(
    `ID`                   bigint       NOT NULL AUTO_INCREMENT COMMENT '배송지 PK',
    `MEMBER_EMAIL`         varchar(255) NULL DEFAULT NULL COMMENT '유저 아이디(이메일)',
    `RECEIVER`             varchar(45)  NULL DEFAULT NULL COMMENT '수취인',
    `MAIN_ADDRESS`         varchar(45)  NULL DEFAULT NULL COMMENT '메인주소',
    `SUB_ADDRESS`          varchar(45)  NULL DEFAULT NULL COMMENT '상세주소',
    `PHONE_NUMBER`         varchar(45)  NULL DEFAULT NULL COMMENT '휴대폰 번호. 휴대폰번호 (-) 가 없어야함',
    `ALTERNATIVE_NUMBER`   int          NULL DEFAULT NULL COMMENT '대안 연락처. 번호 (-) 가 없어야함',
    `SHIPPING_REQUEST_MSG` varchar(200) NULL DEFAULT NULL COMMENT '배송요청사항',
    `CREATED_DATE`         timestamp    NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    `UPDATED_DATE`         timestamp    NULL DEFAULT NULL COMMENT '업데이트일',
    `DELETED_DATE`         timestamp    NULL DEFAULT NULL COMMENT '삭제일',
    PRIMARY KEY (ID)
);

-- Index 설정 SQL - shipping_infos(MEMBER_EMAIL)
CREATE INDEX FK_SHIPPING_INFOS_USER_ID_MEMBER_NO
    ON shipping_infos (MEMBER_EMAIL);

-- Foreign Key 설정 SQL - shipping_infos(MEMBER_EMAIL) -> member(EMAIL)
ALTER TABLE shipping_infos
    ADD CONSTRAINT FK_shipping_infos_MEMBER_EMAIL_member_EMAIL FOREIGN KEY (MEMBER_EMAIL)
        REFERENCES member (EMAIL) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - shipping_infos(MEMBER_EMAIL)
-- ALTER TABLE shipping_infos
-- DROP FOREIGN KEY FK_shipping_infos_MEMBER_EMAIL_member_EMAIL;


-- cart Table Create SQL
-- 테이블 생성 SQL - cart
CREATE TABLE cart
(
    `ID`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'pk값',
    `USER_ID`      varchar(255) NOT NULL COMMENT '사용자 이메일 ID',
    `CREATED_DATE` timestamp    NULL DEFAULT CURRENT_TIMESTAMP,
    `UPDATED_DATE` timestamp    NULL DEFAULT CURRENT_TIMESTAMP,
    `DELETED_DATE` timestamp    NULL,
    PRIMARY KEY (ID)
);

-- 테이블 Comment 설정 SQL - cart
ALTER TABLE cart
    COMMENT '장바구니';

-- Foreign Key 설정 SQL - cart(USER_ID) -> member(EMAIL)
ALTER TABLE cart
    ADD CONSTRAINT FK_cart_USER_ID_member_EMAIL FOREIGN KEY (USER_ID)
        REFERENCES member (EMAIL) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Unique Index 설정 SQL - CART(USER_ID)
CREATE UNIQUE INDEX UQ_CART_1
    ON CART (USER_ID);

-- Foreign Key 삭제 SQL - cart(USER_ID)
-- ALTER TABLE cart
-- DROP FOREIGN KEY FK_cart_USER_ID_member_EMAIL;


-- cart_product_relation Table Create SQL
-- 테이블 생성 SQL - cart_product_relation
CREATE TABLE cart_product_relation
(
    `CART_ID`          bigint    NOT NULL COMMENT '장바구니ID',
    `PRODUCT_ID`       bigint    NOT NULL COMMENT '상품ID',
    `PRODUCT_QUANTITY` bigint    NULL DEFAULT NULL COMMENT '장바구니 상품 수량',
    `CREATED_DATE`     timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    `UPDATED_DATE`     timestamp NULL DEFAULT NULL COMMENT '수정일',
    `DELETED_DATE`     timestamp NULL DEFAULT NULL COMMENT '삭제일'
);

-- Index 설정 SQL - cart_product_relation(PRODUCT_ID)
CREATE INDEX FK_CART_PRODUCT_RELATION_PRODUCT_ID_PRODUCT_ID
    ON cart_product_relation (PRODUCT_ID);

-- Unique Index 설정 SQL - cart_product_relation(CART_ID, PRODUCT_ID)
CREATE UNIQUE INDEX UK_CART_PRODUCT_RELATION
    ON cart_product_relation (CART_ID, PRODUCT_ID);

-- Foreign Key 설정 SQL - cart_product_relation(PRODUCT_ID) -> product(ID)
ALTER TABLE cart_product_relation
    ADD CONSTRAINT FK_CART_PRODUCT_RELATION_PRODUCT_ID_PRODUCT_ID FOREIGN KEY (PRODUCT_ID)
        REFERENCES product (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - cart_product_relation(PRODUCT_ID)
-- ALTER TABLE cart_product_relation
-- DROP FOREIGN KEY FK_CART_PRODUCT_RELATION_PRODUCT_ID_PRODUCT_ID;


-- category Table Create SQL
-- 테이블 생성 SQL - category
CREATE TABLE category
(
    `ID`           bigint                             NOT NULL AUTO_INCREMENT COMMENT '본인 상품 ID',
    `NAME`         varchar(255)                       NOT NULL,
    `STATUS`       enum ('ACTIVE','READY','DISABLED') NOT NULL COMMENT '상품 카테고리 상태코드',
    `PARENT_ID`    bigint                             NULL DEFAULT NULL COMMENT '부모 상품 ID',
    `CREATED_DATE` timestamp                          NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    `UPDATED_DATE` timestamp                          NULL DEFAULT NULL COMMENT '업데이트일',
    PRIMARY KEY (ID)
);

-- Index 설정 SQL - category(PARENT_ID)
CREATE INDEX FK_CATEGORY_PARENT_ID_CATEGORY_ID
    ON category (PARENT_ID);

-- Foreign Key 설정 SQL - category(PARENT_ID) -> category(ID)
ALTER TABLE category
    ADD CONSTRAINT FK_CATEGORY_PARENT_ID_CATEGORY_ID FOREIGN KEY (PARENT_ID)
        REFERENCES category (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - category(PARENT_ID)
-- ALTER TABLE category
-- DROP FOREIGN KEY FK_CATEGORY_PARENT_ID_CATEGORY_ID;


-- center Table Create SQL
-- 테이블 생성 SQL - center
CREATE TABLE center
(
    `CENTER_CODE`            varchar(255) NOT NULL COMMENT '센터코드',
    `MANAGER_NAME`           varchar(255) NOT NULL COMMENT '담당자명',
    `COMPANY_CONTACT_NUMBER` varchar(255) NOT NULL COMMENT '연락처',
    `ZIP_CODE`               varchar(30)  NOT NULL COMMENT '우편번호',
    `ADDRESS`                varchar(500) NOT NULL COMMENT '주소',
    `ADDRESS_DETAIL`         varchar(500) NOT NULL COMMENT '주소 상세',
    PRIMARY KEY (CENTER_CODE)
);


-- orders Table Create SQL
-- 테이블 생성 SQL - orders
CREATE TABLE orders
(
    `ID`               bigint       NOT NULL AUTO_INCREMENT COMMENT '주문ID. 주문번호',
    ORDERER_NUMBER       varchar(200) NULL DEFAULT NULL COMMENT '주문자. 주문자 ( 현재 사용자 정보 호출)',
    `ORDER_STATUS`     varchar(200) NULL DEFAULT NULL COMMENT '주문상태. ACCEPT 결제완료 | INSTRUCT 상품준비중 | DEPARTURE 배송지시 | DELIVERING 배송중 | FINAL_DELIVERY 배송완료 | NONE_TRACKING 업체 직접 배송(배송 추적X)',
    `PAID_DATE`        timestamp    NULL DEFAULT NULL COMMENT '결제일시',
    `SHIPPING_INFO_ID` bigint       NULL DEFAULT NULL COMMENT '배송지정보ID. 배송정보 ID',
    `CREATED_DATE`     timestamp    NULL DEFAULT CURRENT_TIMESTAMP COMMENT '주문생성일. 주문일시',
    `UPDATED_DATE`     timestamp    NULL DEFAULT NULL COMMENT '주문수정일. 주문변경일시',
    `DELETED_DATE`     timestamp    NULL DEFAULT NULL COMMENT '주문취소일. 주문취소일시',
    PRIMARY KEY (ID)
);

-- Index 설정 SQL - orders(SHIPPING_INFO_ID)
CREATE INDEX FK_ORDERS_SHIPPING_INFO_ID_SHIPPING_INFOS_ID
    ON orders (SHIPPING_INFO_ID);

-- Foreign Key 설정 SQL - orders(ID) -> orders_product_relation(GROUP_ID)
ALTER TABLE orders
    ADD CONSTRAINT FK_ORDERS_ID_ORDERS_PRODUCT_RELATION_GROUP_ID FOREIGN KEY (ID)
        REFERENCES orders_product_relation (GROUP_ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - orders(ID)
-- ALTER TABLE orders
-- DROP FOREIGN KEY FK_ORDERS_ID_ORDERS_PRODUCT_RELATION_GROUP_ID;

-- Foreign Key 설정 SQL - orders(SHIPPING_INFO_ID) -> shipping_infos(ID)
ALTER TABLE orders
    ADD CONSTRAINT FK_ORDERS_SHIPPING_INFO_ID_SHIPPING_INFOS_ID FOREIGN KEY (SHIPPING_INFO_ID)
        REFERENCES shipping_infos (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - orders(SHIPPING_INFO_ID)
-- ALTER TABLE orders
-- DROP FOREIGN KEY FK_ORDERS_SHIPPING_INFO_ID_SHIPPING_INFOS_ID;


-- member_refresh_token Table Create SQL
-- 테이블 생성 SQL - member_refresh_token
CREATE TABLE member_refresh_token
(
    `ID`            BIGINT       NOT NULL AUTO_INCREMENT,
    `EMAIL`         varchar(255) NOT NULL,
    `REFRESH_TOKEN` LONGTEXT     NULL,
    `CREATED_DATE`  TIMESTAMP    NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (ID)
);

-- 테이블 Comment 설정 SQL - member_refresh_token
ALTER TABLE member_refresh_token
    COMMENT '회원의 리프레시 토큰 테이블';

-- Unique Index 설정 SQL - member_refresh_token(EMAIL)
CREATE UNIQUE INDEX UQ_member_refresh_token_1
    ON member_refresh_token (EMAIL);

-- Foreign Key 설정 SQL - member_refresh_token(EMAIL) -> member(EMAIL)
ALTER TABLE member_refresh_token
    ADD CONSTRAINT FK_member_refresh_token_EMAIL_member_EMAIL FOREIGN KEY (EMAIL)
        REFERENCES member (EMAIL) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - member_refresh_token(EMAIL)
-- ALTER TABLE member_refresh_token
-- DROP FOREIGN KEY FK_member_refresh_token_EMAIL_member_EMAIL;