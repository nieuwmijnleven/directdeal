#!/bin/bash

set -e  # 오류 발생 시 스크립트 종료

log() {
  echo -e "[`date '+%Y-%m-%d %H:%M:%S'`] $*"
}

error_exit() {
  echo -e "[ERROR] $*" >&2
  exit 1
}

base_services=(
  "zookeeper"
  "kafka"
  "mongo"
  # "mysql"
)

app_services=(
  "account-service"
  "chatting-service"
  "sale-service"
  "sale-catalog-service"
  "transaction-history-service"
  "gateway"
)

all_services=(
  "${app_services[@]}"
  "${base_services[@]}"
  "mysql"
)

declare -A svc_create_cmd

svc_create_cmd[account-service]="docker run -e SPRING_PROFILES_ACTIVE=dev --name account-service -d -p 8080:8080 --network directdeal  direct-deal-account-service"
svc_create_cmd[chatting-service]="docker run -e SPRING_PROFILES_ACTIVE=dev --name chatting-service -d -p 8085:8085 --network directdeal  direct-deal-chatting-service"
svc_create_cmd[sale-service]="docker run -e SPRING_PROFILES_ACTIVE=dev --name sale-service -d -p 8081:8081 --network directdeal  direct-deal-sale-service"
svc_create_cmd[sale-catalog-service]="docker run -e SPRING_PROFILES_ACTIVE=dev --name sale-catalog-service -d -p 8082:8082 --network directdeal  direct-deal-sale-catalog-service-webflux"
svc_create_cmd[transaction-history-service]="docker run -e SPRING_PROFILES_ACTIVE=dev --name transaction-history-service -d -p 8083:8083 --network directdeal  direct-deal-transaction-history-service"
svc_create_cmd[gateway]="docker run -e SPRING_PROFILES_ACTIVE=dev --name gateway -d --network directdeal -p 8084:8084 direct-deal-gateway"
svc_create_cmd[kafka]="docker run --name kafka -e KAFKA_BROKER_ID=1 -e KAFKA_PORT=9092 -e KAFKA_ADVERTISED_HOST_NAME=kafka -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://kafka:9092 -e KAFKA_LISTENERS=PLAINTEXT://:9092 -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 -e KAFKA_AUTO_CREATE_TOPICS_ENABLE=true -e KAFKA_CREATE_TOPICS=local.event:1:1 -d --network directdeal wurstmeister/kafka:2.12-2.4.1"
svc_create_cmd[mysql]="docker run -e MYSQL_ROOT_PASSWORD=secret --name mysql -d --network directdeal direct-deal-mysql"
svc_create_cmd[zookeeper]="docker run --name zookeeper -d --network directdeal zookeeper:3.9.1"
svc_create_cmd[mongo]='docker run --name mongo -e MONGO_INITDB_ROOT_USERNAME=root -e MONGO_INITDB_ROOT_PASSWORD=secret -d --network directdeal mongo:4.0.25'

NETWORK="directdeal"
MYSQL_CONTAINER="mysql"
MYSQL_IMAGE="direct-deal-mysql"
MYSQL_ROOT_PASSWORD="secret"

LOCAL_REGISTRY="localhost:5000"

remove_container() {
  prev_container="${container}"
  container="$1"
  log "Removing ${container} container..."
  if ! docker rm -v "${container}"; then
    log "Warning: Failed to remove ${container} container, it may not exist."
  fi
  log "${container} container removed."
  container="${prev_container}"
}

# 전체 서비스 시작
start_services() {
  log "Check if ${NETWORK} network is or not..."
  if ! docker network ls --format '{{.Name}}' | grep -q "^${NETWORK}$"; then
    if ! docker network create directdeal; then
      error_exit "Failed to create directdeal network."
    fi
  fi

  log "Starting MySQL container..."

  if docker ps --format '{{.Names}}' | grep -q "^${MYSQL_CONTAINER}$"; then
    stop_service "${MYSQL_CONTAINER}"
  fi

  if docker ps -a --format '{{.Names}}' | grep -q "^${MYSQL_CONTAINER}$"; then
    remove_container "${MYSQL_CONTAINER}"
  fi

  if ! docker run -e MYSQL_ROOT_PASSWORD="${MYSQL_ROOT_PASSWORD}" --name "${MYSQL_CONTAINER}" -d --network "${NETWORK}" "${MYSQL_IMAGE}"; then
    error_exit "Failed to run MySQL container."
  fi

  log "Starting base services..."
  for service in "${base_services[@]}"; do
    log "Starting ${service}..."
    if docker ps --format '{{.Names}}' | grep -q "^${service}$"; then
      stop_service "${service}"
    fi

    if ! docker start "${service}"; then
      if ! ${svc_create_cmd[$service]}; then
        error_exit "Failed to start ${service} container."
      fi
    fi
    log "✅ Container ${service} started."
  done

  log "Waiting for DB services to become available..."
  if ! docker run -it --rm --network "${NETWORK}" busybox /bin/sh -c '
    while true; do
      nc -z -w 1 mysql 3306 &&
      nc -z -w 1 mongo 27017 &&
      nc -z -w 1 kafka 9092 &&
      nc -z -w 1 zookeeper 2181

      if [ $? -eq 0 ]; then
        echo "✅ All DB services are ready."
        break
      fi

      echo "⏳ DB is not yet reachable; sleeping 9s before retry..."
      sleep 9
    done
  '; then
    error_exit "DB readiness check failed."
  fi

  log "Starting application services..."
  for service in "${app_services[@]}"; do
    log "Starting ${service}..."
    if docker ps --format '{{.Names}}' | grep -q "^${service}$"; then
      stop_service "${service}"
    fi

    if ! docker start "${service}"; then
      if ! ${svc_create_cmd[$service]}; then
        error_exit "Failed to start ${service} container."
      fi
    fi
    log "✅ Container ${service} started."
  done

  log "🚀 All services started successfully."
}

# 전체 서비스 정지
stop_services() {
  log "Stopping all services..."
  for service in "${all_services[@]}"; do
    log "Stopping ${service}..."
    if ! docker stop "${service}"; then
      log "Warning: Failed to stop ${service} or it may not be running."
    fi
  done

  log "Removing MySQL container..."
  if ! docker rm "${MYSQL_CONTAINER}"; then
    log "Warning: Failed to remove MySQL container, it may not exist."
  fi

  log "All services stopped and MySQL container removed."
}

# 이미지 푸시 함수
push_images_to_local_registry() {
  images=(
    "minidocks/java:17-jre-nogui"
    "wurstmeister/kafka:2.12-2.4.1"
    "node:20"
    "mongo:4.0.25"
    "mysql:8.0.41"
    "busybox:latest"
    "zookeeper:3.9.1"
  )

  for image in "${images[@]}"; do
    log "Pulling image from Docker Hub: ${image}"
    if ! sudo docker pull "${image}"; then
      error_exit "Failed to pull image ${image}"
    fi

    local_tag="${LOCAL_REGISTRY}/${image}"

    log "Tagging image ${image} as ${local_tag}"
    if ! sudo docker tag "${image}" "${local_tag}"; then
      error_exit "Failed to tag image ${image}"
    fi

    log "Pushing image ${local_tag} to local registry"
    if ! sudo docker push "${local_tag}"; then
      error_exit "Failed to push image ${local_tag}"
    fi
  done
}

# 특정 컨테이너 시작
start_service() {
  container="$1"
  if [ -z "$container" ]; then
    error_exit "Usage: $0 start-service <container-name>"
  fi

  log "Starting container: $container"
  if [ "${container}" = "account-service" ]; then
    if docker ps --format '{{.Names}}' | grep -q "^${MYSQL_CONTAINER}$"; then
      stop_service "${MYSQL_CONTAINER}"
    fi

    if docker ps -a --format '{{.Names}}' | grep -q "^${MYSQL_CONTAINER}$"; then
      remove_container "${MYSQL_CONTAINER}"
    fi

    if ! docker run -e MYSQL_ROOT_PASSWORD="${MYSQL_ROOT_PASSWORD}" --name "${MYSQL_CONTAINER}" -d --network "${NETWORK}" "${MYSQL_IMAGE}"; then
      error_exit "Failed to run MySQL container."
    fi

    log "Waiting for mysql services to become available..."
    if ! docker run -it --rm --network "${NETWORK}" busybox /bin/sh -c '
      while true; do
        nc -z -w 1 mysql 3306

        if [ $? -eq 0 ]; then
          echo "✅ mysql is ready."
          break
        fi

        echo "⏳ DB is not yet reachable; sleeping 9s before retry..."
        sleep 9
      done
    '; then
      error_exit "mysql readiness check failed."
    fi
  fi

  if ! docker start "$container"; then
    if ! "${svc_create_cmd[$container]}"; then
      error_exit "Failed to start container: $container"
    fi
  fi

  log "✅ Container $container started."
}

# 특정 컨테이너 정지
stop_service() {
  prev_container="${container}"
  container="$1"
  if [ -z "$container" ]; then
    error_exit "Usage: $0 stop-service <container-name>"
  fi
  log "Stopping container: $container"
  if ! docker stop "$container"; then
    error_exit "Failed to stop container: $container"
  fi
  log "✅ Container $container stopped."
  container="${prev_container}"
}

# 특정 컨테이너 상태 확인
status_service() {
  container="$1"
  if [ -z "$container" ]; then
    error_exit "Usage: $0 status-service <container-name>"
  fi

  if ! docker inspect "$container" &> /dev/null; then
    log "❌ Container \"$container\" not found."
    return
  fi

  status=$(docker inspect -f '{{.State.Status}}' "$container")

  case "$status" in
    running)
      log "✅ Container \"$container\" is running."
      ;;
    exited)
      log "⏸️  Container \"$container\" is stopped."
      ;;
    *)
      log "ℹ️  Container \"$container\" is in \"$status\" state."
      ;;
  esac
}

# 명령 분기 처리
if [ $# -eq 0 ]; then
  echo "Usage: $0 {start|stop|push|start-service <container>|stop-service <container>}"
  exit 1
fi

case "$1" in
  start)
    start_services
    ;;
  stop)
    stop_services
    ;;
  push)
    push_images_to_local_registry
    ;;
  start-service)
    start_service "$2"
    ;;
  stop-service)
    stop_service "$2"
    ;;
  status-service)
    status_service "$2"
    ;;
  *)
    echo "Invalid argument: $1"
    echo "Usage: $0 {start|stop|push|start-service <container>|stop-service <container>}"
    exit 1
    ;;
esac
