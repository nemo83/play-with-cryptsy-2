# --- !Ups
CREATE TABLE transactions (
id bigint AUTO_INCREMENT PRIMARY KEY
);

# --- !Downs
DROP TABLE IF EXISTS transactions;