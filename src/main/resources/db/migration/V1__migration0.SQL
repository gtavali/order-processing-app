/* Created by Gabor Tavali */

/* Tables */
CREATE TABLE order_tbl (
 order_id BIGINT(20) NOT NULL AUTO_INCREMENT,
 buyer_name VARCHAR(40),
 buyer_email VARCHAR(40),
 order_date DATE,
 order_total_value INT,
 address VARCHAR(100),
 postcode INT,
 PRIMARY KEY(order_id)
);

CREATE TABLE order_item_tbl (
 order_item_id BIGINT(20) NOT NULL AUTO_INCREMENT,
 order_id BIGINT(20),
 sale_price INT,
 PRIMARY KEY(order_item_id),
 FOREIGN KEY (order_id)
    REFERENCES order_tbl(order_id)
);