BEGIN TRANSACTION;

DROP TABLE IF EXISTS transfer;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS users;


CREATE TABLE users (
	user_id serial NOT NULL,
	username varchar(50) UNIQUE NOT NULL,
	password_hash varchar(200) NOT NULL,
	role varchar(20),
	CONSTRAINT pk_users PRIMARY KEY (user_id),
	CONSTRAINT uq_username UNIQUE (username)
);

CREATE TABLE transfer (
	transfer_id serial NOT NULL,
	transfer_type varchar(7) NOT NULL,
	transfer_status varchar(8) NOT NULL,
	user_from int NOT NULL,
	user_to int NOT NULL,
	amount decimal(10, 2) NOT NULL,
	
	CONSTRAINT pk_transfer PRIMARY KEY (transfer_id),
	CONSTRAINT fk_transfer_from FOREIGN KEY (user_from) REFERENCES users (user_id),
	CONSTRAINT fk_transfer_to FOREIGN KEY (user_to) REFERENCES users (user_id)
);

CREATE TABLE account (
	account_id serial NOT NULL,
	user_id int NOT NULL,
	balance decimal(10, 2) DEFAULT(1000),
	
	CONSTRAINT pk_account PRIMARY KEY (account_id),
	CONSTRAINT fk_account_users FOREIGN KEY (user_id) REFERENCES users (user_id)
);
--rollback
COMMIT TRANSACTION;


