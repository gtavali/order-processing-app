/* Created by Gabor Tavali */

/* Tables */
CREATE TABLE order_tbl(
 order_id BIGINT(20) NOT NULL,
 buyer_name VARCHAR(40),
 buyer_email VARCHAR(40),
 order_date DATE,
 order_total_value DOUBLE(5,2),
 address VARCHAR(100),
 postcode INT,
 PRIMARY KEY(order_id)
);

CREATE TABLE order_item_tbl(
 order_item_id BIGINT(20) NOT NULL,
 order_id BIGINT(20),
 sale_price DOUBLE(5,2),
 shipping_price DOUBLE(5,2),
 total_item_price DOUBLE(5,2),
 sku VARCHAR(100),
 status ENUM('IN_STOCK','OUT_OF_STOCK'),
 PRIMARY KEY(order_item_id),
 FOREIGN KEY (order_id)
    REFERENCES order_tbl(order_id)
)
