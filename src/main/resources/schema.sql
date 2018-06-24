CREATE TABLE transactions(

  id VARCHAR not null,
  amount DECIMAL not null,
  transaction_time TIMESTAMP,
  PRIMARY KEY(id)
);

CREATE INDEX transaction_time_index
ON transactions (transaction_time);