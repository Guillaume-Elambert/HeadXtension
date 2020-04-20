------------------------------------------------------------
-- Table: Credentials
------------------------------------------------------------
CREATE TABLE Credentials(
	id           INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT ,
	name         TEXT NOT NULL ,
	url          TEXT NOT NULL ,
	username     TEXT NOT NULL ,
	password     INTEGER NOT NULL ,
	id_Folder    INTEGER

	,CONSTRAINT Credentials_Folder_FK FOREIGN KEY (id_Folder) REFERENCES Folder(id)
);