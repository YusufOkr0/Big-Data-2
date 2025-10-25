SET FOREIGN_KEY_CHECKS=0;

-- Drop tables if exist (opsiyonel)
DROP TABLE IF EXISTS emp;
DROP TABLE IF EXISTS dept;
DROP TABLE IF EXISTS salgrade;

-- DEPT table
CREATE TABLE dept (
                      deptno INT PRIMARY KEY,
                      dname VARCHAR(50),
                      loc VARCHAR(50)
);

-- EMP table
CREATE TABLE emp (
                     empno INT AUTO_INCREMENT PRIMARY KEY,
                     ename VARCHAR(50),
                     job VARCHAR(50),
                     mgr INT,
                     hiredate DATE,
                     sal INT,
                     comm INT,
                     deptno INT,
                     image_url VARCHAR(255),
                     FOREIGN KEY (mgr) REFERENCES emp(empno),
                     FOREIGN KEY (deptno) REFERENCES dept(deptno)
);

-- SALGRADE table
CREATE TABLE salgrade (
                          grade INT PRIMARY KEY,
                          losal INT,
                          hisal INT
);

-- Insert DEPT
INSERT INTO dept VALUES (10,'ACCOUNTING','NEW YORK');
INSERT INTO dept VALUES (20,'RESEARCH','DALLAS');
INSERT INTO dept VALUES (30,'SALES','CHICAGO');
INSERT INTO dept VALUES (40,'OPERATIONS','BOSTON');

-- Insert SALGRADE
INSERT INTO salgrade VALUES (1,700,1200);
INSERT INTO salgrade VALUES (2,1201,1400);
INSERT INTO salgrade VALUES (3,1401,2000);
INSERT INTO salgrade VALUES (4,2001,3000);
INSERT INTO salgrade VALUES (5,3001,9999);

-- EMP insert sıralaması: önce mgr=NULL olan
INSERT INTO emp (empno, ename, job, mgr, hiredate, sal, comm, deptno, image_url)
VALUES (7839,'KING','PRESIDENT',7839,'1981-11-17',5000,NULL,10,'king.jpg');

-- Diğer EMP kayıtları
INSERT INTO emp (empno, ename, job, mgr, hiredate, sal, comm, deptno, image_url) VALUES
                                                                                     (7566,'JONES','MANAGER',7839,'1981-04-02',2975,NULL,20,'jones.jpg'),
                                                                                     (7782,'CLARK','MANAGER',7839,'1981-06-09',2450,NULL,10,'clark.jpg'),
                                                                                     (7698,'BLAKE','MANAGER',7839,'1981-05-01',2850,NULL,30,'blake.jpg'),
                                                                                     (7788,'SCOTT','ANALYST',7566,'1982-12-09',3000,NULL,20,'scott.jpg'),
                                                                                     (7902,'FORD','ANALYST',7566,'1981-12-03',3000,NULL,20,'ford.jpg'),
                                                                                     (7369,'SMITH','CLERK',7902,'1980-12-17',800,NULL,20,'smith.jpg'),
                                                                                     (7876,'ADAMS','CLERK',7788,'1983-01-12',1100,NULL,20,'adams.jpg'),
                                                                                     (7521,'WARD','SALESMAN',7698,'1981-02-22',1250,500,30,'ward.jpg'),
                                                                                     (7499,'ALLEN','SALESMAN',7698,'1981-02-20',1600,300,30,'allen.jpg'),
                                                                                     (7654,'MARTIN','SALESMAN',7698,'1981-09-28',1250,1400,30,'martin.jpg'),
                                                                                     (7844,'TURNER','SALESMAN',7698,'1981-09-08',1500,0,30,'turner.jpg'),
                                                                                     (7900,'JAMES','CLERK',7698,'1981-12-03',950,NULL,30,'james.jpg'),
                                                                                     (7934,'MILLER','CLERK',7782,'1982-01-23',1300,NULL,10,'miller.jpg');

SET FOREIGN_KEY_CHECKS=1;
