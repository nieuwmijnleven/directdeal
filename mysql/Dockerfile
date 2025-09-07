FROM mysql:5.7.41
LABEL image.authors="Jeon, Cheol"
ADD direct-deal-my.cnf /etc/mysql/conf.d/custom.cnf
ADD direct-deal-init.sql /docker-entrypoint-initdb.d/direct-deal-init.sql
EXPOSE 3306