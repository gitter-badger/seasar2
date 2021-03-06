create table ADDRESS (ID int not null, CITY varchar(255), constraint ADDRESS_PK primary key(ID));
create table BIG_DECIMAL_ENTITY (BIG_DECIMAL_PROPERTY decimal(19,2));
create table BIG_INTEGER_ENTITY (BIG_INTEGER_PROPERTY bigint);
create table BOOLEAN_ENTITY (BOOLEAN_PROPERTY bit);
create table BYTE_ARRAY_ENTITY (BYTE_ARRAY_PROPERTY varbinary(255), BLOB_PROPERTY varbinary(max));
create table BYTE_ENTITY (BYTE_PROPERTY smallint);
create table CALENDAR_ENTITY (DATE_PROPERTY datetime, TIME_PROPERTY datetime, TIMESTAMP_PROPERTY datetime);
create table CHARACTER_ENTITY (CHARACTER_PROPERTY char(1));
create table DATE_ENTITY (DATE_PROPERTY datetime, TIME_PROPERTY datetime, TIMESTAMP_PROPERTY datetime);
create table DEPARTMENT (ID int identity not null, NAME varchar(255), constraint DEPARTMENT_PK primary key(ID));
create table DOUBLE_ENTITY (DOUBLE1_PROPERTY double precision);
create table EMPLOYEE (ID int identity not null, FIRST_NAME varchar(255) not null, LAST_NAME varchar(255) not null, DEPARTMENT_ID int, ADDRESS_ID int, constraint EMPLOYEE_PK primary key(ID));
create table ENUM_ENTITY (ENUM_PROPERTY int);
create table FLOAT_ENTITY (FLOAT_PROPERTY float);
create table INTEGER_ENTITY (INTEGER_PROPERTY int);
create table LONG_ENTITY (LONG_PROPERTY bigint);
create table SERIALIZABLE_ENTITY (SERIALIZABLE_PROPERTY varbinary(255), BLOB_PROPERTY varbinary(max));
create table SHORT_ENTITY (SHORT_PROPERTY smallint);
create table SQL_DATE_ENTITY (SQL_DATE_PROPERTY datetime);
create table STRING_ENTITY (STRING_PROPERTY varchar(255), CLOB_PROPERTY varchar(max));
create table TIME_ENTITY (TIME_PROPERTY datetime);
create table TIMESTAMP_ENTITY (TIMESTAMP_PROPERTY datetime);