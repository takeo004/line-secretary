CREATE SCHEMA line_secretary;

CREATE USER username WITH PASSWORD 'userpassword';
GRANT ALL ON SCHEMA line_secretary TO admin;
GRANT ALL PRIVILEGES ON DATABASE line_secretary TO admin;