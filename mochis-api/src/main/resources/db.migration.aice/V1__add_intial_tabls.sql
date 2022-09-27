/* Build Table Structures */
CREATE TABLE batches
(
	id SERIAL NOT NULL,
	batch_serial VARCHAR(6) NOT NULL,
	completed BOOL NOT NULL,
	winners INTEGER NOT NULL,
	stubs INTEGER NOT NULL,
	updated TIMESTAMP WITH TIME ZONE NOT NULL,
	created TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE stubs
(
	id SERIAL NOT NULL,
	batch_id INTEGER NOT NULL,
	x INTEGER NOT NULL,
	y INTEGER NOT NULL,
	prize BOOL NOT NULL,
	open BOOL NOT NULL,
	updated TIMESTAMP WITH TIME ZONE NOT NULL,
	created TIMESTAMP WITH TIME ZONE NOT NULL
);

/* Add Primary Key */
ALTER TABLE batches ADD CONSTRAINT pkbatches
	PRIMARY KEY (id);

/* Add Index to batch_serial */
CREATE INDEX "batches_batch_serial_Idx" ON batches (batch_serial);

/* Add Primary Key */
ALTER TABLE stubs ADD CONSTRAINT pkStubs
	PRIMARY KEY (id);

/* Add Foreign Key: fk_stubs_batches */
ALTER TABLE stubs ADD CONSTRAINT fk_stubs_batches
	FOREIGN KEY (batch_id) REFERENCES batches (id)
	ON UPDATE NO ACTION ON DELETE NO ACTION;

/* Add Index to batch_id */
CREATE INDEX "stubs_batch_id_Idx" ON stubs (batch_id);
