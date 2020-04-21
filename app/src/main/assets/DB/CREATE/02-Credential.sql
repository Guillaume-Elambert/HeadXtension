------------------------------------------------------------
-- Table: Credentials
------------------------------------------------------------
CREATE TABLE Credential(
	id           INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT ,
	name         TEXT NOT NULL ,
	url          TEXT NOT NULL ,
	username     TEXT NOT NULL ,
	password     INTEGER NOT NULL ,
	id_Folder    INTEGER

	,CONSTRAINT Credential_Folder_FK FOREIGN KEY (id_Folder) REFERENCES Folder(id)
);