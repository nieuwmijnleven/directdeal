#!/bin/bash

# Usage: ./redeploy-service.sh [service-name]
# Example: ./redeploy-service.sh account-service
#          ./redeploy-service.sh sale-service
#          ./redeploy-service.sh chatting-service

SERVICE_NAME=$1
COMMAND=$2 #configmap

KUBERNETES_CONFIG_PATH="./deployment/kubernetes"

declare -A manifests
declare -A configmaps
declare -A dockerfiles

manifests[account-service]="./deployment/kubernetes/direct-deal-account-service.yml"
manifests[chatting-service]="./deployment/kubernetes/direct-deal-chatting-service.yml"
manifests[gateway]="./deployment/kubernetes/direct-deal-gateway-service.yml"
manifests[sale-catalog-service-webflux]="./deployment/kubernetes/direct-deal-sale-catalog-service-webflux.yml"
manifests[sale-service]="./deployment/kubernetes/direct-deal-sale-service.yml"
manifests[transaction-history-service]="./deployment/kubernetes/direct-deal-transaction-history-service.yml"
manifests[kafka]="./deployment/kubernetes/kafka-service.yml"
manifests[mongo]="./deployment/kubernetes/mongo-service.yml"
manifests[mysql]="./deployment/kubernetes/mysql-service.yml"
manifests[nginx]="./deployment/kubernetes/nginx-service.yml"
manifests[redis]="./deployment/kubernetes/redis-service.yml"
manifests[zookeeper]="./deployment/kubernetes/zookeeper-service.yml"

configmaps[account-service]="./deployment/kubernetes/configmap/direct-deal-account-configmap.yml"
configmaps[chatting-service]="./deployment/kubernetes/configmap/direct-deal-chatting-configmap.yml"
configmaps[gateway]="./deployment/kubernetes/configmap/direct-deal-gateway-configmap.yml"
configmaps[sale-catalog-service-webflux]="./deployment/kubernetes/configmap/direct-deal-sale-catalog-webflux-configmap.yml"
configmaps[sale-service]="./deployment/kubernetes/configmap/direct-deal-sale-configmap.yml"
configmaps[transaction-history-service]="./deployment/kubernetes/configmap/direct-deal-transaction-history-configmap.yml"

dockerfiles[account-service]="./direct-deal-account-service"
dockerfiles[chatting-service]="./direct-deal-chatting-service"
dockerfiles[gateway]="./direct-deal-gateway"
dockerfiles[sale-catalog-service-webflux]="./direct-deal-sale-catalog-service-webflux"
dockerfiles[sale-service]="./direct-deal-sale-service"
dockerfiles[transaction-history-service]="./direct-deal-transaction-history-service"
dockerfiles[mysql]="./mysql"
dockerfiles[nginx]="./direct-deal-ui"

if [ -z "$SERVICE_NAME" ]; then
  echo "❌ Please provide a service name. (ex: ./redeploy-service.sh account-service)"
  exit 1
fi

# Minikube 상태 확인
status=$(minikube status --format='{{.Host}}')

if [ "$status" != "Running" ]; then
  echo "🚀 Minikube가 실행 중이 아닙니다. 클러스터를 시작합니다..."
  #minikube delete --all
  minikube start --memory 8192 --cpus 2
else
  echo "✅ Minikube가 이미 실행 중입니다."
fi

echo "----------------------------------------"
echo "🔽 Stopping $SERVICE_NAME ..."
# kubectl delete -f ${configmaps[$SERVICE_NAME]}.yml
# kubectl delete -f ${manifests[$SERVICE_NAME]}.yml
[ -n "${configmaps[$SERVICE_NAME]}" ] \
  && echo "Deleting configmap: ${configmaps[$SERVICE_NAME]}" \
  && kubectl delete -f "${configmaps[$SERVICE_NAME]}"
[ -n "${manifests[$SERVICE_NAME]}" ] \
  && echo "Deleting manifest: ${manifests[$SERVICE_NAME]}" \
  && kubectl delete -f "${manifests[$SERVICE_NAME]}"

if [ -n "${dockerfiles[$SERVICE_NAME]}" ] && ([ -z "${COMMAND}" ] || [ "${COMMAND}" != "configmap" ]); then

  echo "----------------------------------------"
  echo "🛠️ Rebuilding $SERVICE_NAME ..."
  [ -f "${dockerfiles[$SERVICE_NAME]}/build.gradle" ] \
    && ./gradlew clean build -p "${dockerfiles[$SERVICE_NAME]}"

  echo "----------------------------------------"
  echo "🛠️ Creating Registry ..."
  kubectl apply -f ${KUBERNETES_CONFIG_PATH}/registry.yml

  first_time_fail=true
  spinstr='|/-\\'
  spin_index=0

  while true
  do
      rt=$(minikube ssh curl localhost:5000 > /dev/null 2>&1)

      if [ $? -eq 0 ]; then
          echo -e "\nRegistry is UP"
          break
      fi

      if [ "$first_time_fail" = true ]; then
          echo -n "Waiting until the Registry is ready... "
          first_time_fail=false
      fi

      # 커서 맨 끝에 스피너 출력
      printf "\b%s" "${spinstr:spin_index:1}"
      ((spin_index=(spin_index+1)%${#spinstr}))

      sleep 2
  done
  printf "\n"


  echo "----------------------------------------"
  echo "📡 Starting Port Forwarding for Registry ..."
  kubectl port-forward --namespace kube-system $(kubectl get po -n kube-system | grep kube-registry-v0 | awk '{print $1;}') 5000:5000 >/dev/null 2>&1 &
  PORT_FORWARD_PID=$!
  sleep 2  # 잠깐 대기 (포트포워딩 준비시간)

  eval $(minikube -p minikube docker-env)

  echo "🐳 Rebuilding Docker Image for $SERVICE_NAME ..."
  docker image build -t direct-deal-${SERVICE_NAME} ${dockerfiles[$SERVICE_NAME]}
  docker image tag direct-deal-${SERVICE_NAME} localhost:5000/direct-deal-${SERVICE_NAME}

  echo "📤 Pushing Docker Image to Registry ..."
  docker push localhost:5000/direct-deal-${SERVICE_NAME}

  echo "🛑 Closing Port Forwarding ..."
  kill -9 $PORT_FORWARD_PID && wait $PID 2>/dev/null

fi

echo "----------------------------------------"
echo "🚀 Restarting $SERVICE_NAME on Kubernetes ..."
#kubectl create -f ${configmaps[$SERVICE_NAME]}.yml
#kubectl create -f ${manifests[$SERVICE_NAME]}.yml
kubectl apply -f "${KUBERNETES_CONFIG_PATH}/spring-cloud-kubernetes-config.yml"
[ -n "${configmaps[$SERVICE_NAME]}" ] \
  && echo "create configmap: ${configmaps[$SERVICE_NAME]}" \
  && kubectl create -f "${configmaps[$SERVICE_NAME]}"
[ -n "${manifests[$SERVICE_NAME]}" ] \
  && echo "create manifest: ${manifests[$SERVICE_NAME]}" \
  && kubectl create -f "${manifests[$SERVICE_NAME]}"


echo "----------------------------------------"
echo "✅ $SERVICE_NAME has been successfully redeployed!"