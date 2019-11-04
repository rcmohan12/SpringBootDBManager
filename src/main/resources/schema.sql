DROP TABLE IF EXISTS connections;

CREATE TABLE connections (
   			  id SERIAL,
    		  hostname VARCHAR(255), 
              port SMALLINT, 
    		  dbname VARCHAR(255), 
              username VARCHAR(255), 
    		  password CHAR(32)
);
DROP TABLE IF EXISTS userDetails;

CREATE TABLE userDetails (
   			  id SERIAL,
    		  name VARCHAR(255),  
    		  city VARCHAR(255),
    		  country VARCHAR(255)
);
INSERT INTO userDetails(name, city, country)
VALUES('A', 'Blr', 'Ind'),
('B', 'Miami', 'USA'),
('C', 'Biejing', 'China'),
('D', 'Prague', 'CZ'),
('E', 'Rome', 'Italy');