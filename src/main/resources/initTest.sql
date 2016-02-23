-- 测试使用的初始化脚本

-- Oracle 脚本
create table test_jdbc(
t_int number(5),
t_double number(5,2),
t_string varchar2(100),
t_date date,
t_blob blob,
t_clob clob);

create or replace procedure proc_t_jdbc(p_1 in number, p_2 in out number, p_3 out date)   
  as
  begin
    select (p_1 + p_2) into p_2 from dual;
    select sysdate into p_3 from dual;
  end;

insert into test_jdbc values (1, 1.1, 'this is string', sysdate, to_blob('11111000011111'), to_clob('this is clob'));
insert into test_jdbc values (2, 555.52, 'this is string 1', sysdate, to_blob('10101010100101'), to_clob('this is clob 1'));
insert into test_jdbc values (3, 3.1, 'this is string 3', sysdate, to_blob('111111'), to_clob('this is clob 3'));
insert into test_jdbc values (4, 35.1, 'this is string 4', sysdate, to_blob('11111'), to_clob('this is clob 4'));
insert into test_jdbc values (5, 56.1, 'this is string 5', sysdate, to_blob('1110110000101111'), to_clob('this is clob 5'));
insert into test_jdbc values (6, 66.1, 'this is string 6', sysdate, to_blob('1010110001111'), to_clob('this is clob 6'));

--MySQL 脚本
create table test_jdbc(
t_int int,
t_double double,
t_string varchar(100),
t_date date,
t_blob blob,
t_clob text);

insert into test_jdbc values (1, 1.1, 'this is string', now(), null, null);
insert into test_jdbc values (2, 555.52, 'this is string 1', now(), null, null);
insert into test_jdbc values (3, 3.1, 'this is string 3', now(), null, null);
insert into test_jdbc values (4, 35.1, 'this is string 4', now(), null, null);
insert into test_jdbc values (5, 56.1, 'this is string 5', now(), null, null);
insert into test_jdbc values (6, 66.1, 'this is string 6', now(), null, null);