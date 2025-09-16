base_services=(
"kafka"
"zookeeper"
"mongo"
#"mysql"
)

docker run -e MYSQL_ROOT_PASSWORD=secret --name mysql -d --network directdeal direct-deal-mysql

for service in "${base_services[@]}"; do
  docker start ${service}
done

docker run -it --network directdeal busybox /bin/sh -c \
'while true; do \
  rt=$(nc -z -w 1 mysql 3306 && nc -z -w 1 mongo 27017 && nc -z -w 1 kafka 9092 && nc -z -w 1 zookeeper 2181); \
  if [ $? -eq 0 ]; then \
    echo "DB is ready"; \
    break; \
  fi; \
  echo "DB is not yet reachable;sleep for 9s before retry"; \
  sleep 9; \
done'

services=(
"account-service"
"chatting-service"
"sale-service"
"sale-catalog-service"
"transaction-history-service"
"gateway"
)

for service in "${services[@]}"; do
  docker start ${service}
done