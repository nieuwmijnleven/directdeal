services=(
"account-service"
"chatting-service"
"sale-service"
"sale-catalog-service"
"transaction-history-service"
"gateway"
"kafka"
"zookeeper"
"mysql"
"mongo"
)

for service in "${services[@]}"; do
  docker stop ${service}
done

docker rm mysql