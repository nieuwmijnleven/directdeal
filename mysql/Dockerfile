FROM mysql:5.7.34
LABEL image.authors="Jeon, Cheol <real.overcomer.jc@gmail.com>"
ADD direct-deal-my.cnf /etc/mysql/conf.d/custom.cnf
ADD direct-deal-init.sql /docker-entrypoint-initdb.d/direct-deal-init.sql
EXPOSE 3306