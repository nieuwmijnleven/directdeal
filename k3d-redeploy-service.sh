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

manifests[account-service]="./deployment/k3d/direct-deal-account-service.yml"
manifests[chatting-service]="./deployment/k3d/direct-deal-chatting-service.yml"
manifests[gateway]="./deployment/k3d/direct-deal-gateway-service.yml"
manifests[sale-catalog-service-webflux]="./deployment/k3d/direct-deal-sale-catalog-service-webflux.yml"
manifests[sale-service]="./deployment/k3d/direct-deal-sale-service.yml"
manifests[transaction-history-service]="./deployment/k3d/direct-deal-transaction-history-service.yml"
manifests[kafka]="./deployment/k3d/kafka-service.yml"
manifests[mongo]="./deployment/k3d/mongo-service.yml"
manifests[mysql]="./deployment/k3d/mysql-service.yml"
manifests[nginx]="./deployment/k3d/nginx-service.yml"
manifests[redis]="./deployment/k3d/redis-service.yml"
manifests[zookeeper]="./deployment/k3d/zookeeper-service.yml"

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

# 2️⃣ k3d 클러스터 확인
if ! sudo k3d cluster list | grep -q 'directdeal'; then
  echo "🚀 k3d 클러스터 'directdeal'이 존재하지 않습니다. 생성 중..."
  sudo k3d cluster create directdeal --no-lb --k3s-arg "--kubelet-arg=feature-gates=KubeletInUserNamespace=true@server:0" --registry-create directdeal-registry.localhost:5000
  k3d kubeconfig merge directdeal --kubeconfig-merge-default --kubeconfig-switch-context
else
  echo "✅ k3d 클러스터 'directdeal'이 실행 중입니다."
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
    && ./gradlew clean bootJar -p "${dockerfiles[$SERVICE_NAME]}"

#  echo "----------------------------------------"
#  echo "🛠️ Creating Registry ..."
#  kubectl apply -f ${KUBERNETES_CONFIG_PATH}/registry.yml
#
#  first_time_fail=true
#  spinstr='|/-\\'
#  spin_index=0
#
#  while true
#  do
#      # k3d는 로컬 도커를 사용하므로 localhost:5000 접근 가능
#      curl --silent --max-time 2 http://localhost:5000/v2/_catalog > /dev/null 2>&1
#
#      if [ $? -eq 0 ]; then
#          echo -e "\nRegistry is UP"
#          break
#      fi
#
#      if [ "$first_time_fail" = true ]; then
#          echo -n "Waiting until the Registry is ready... "
#          first_time_fail=false
#      fi
#
#      # 커서 맨 끝에 스피너 출력
#      printf "\b%s" "${spinstr:spin_index:1}"
#      ((spin_index=(spin_index+1)%${#spinstr}))
#
#      sleep 2
#  done
#  printf "\n"
#
#
#
#  echo "----------------------------------------"
#  echo "📡 Starting Port Forwarding for Registry ..."
#  kubectl port-forward --namespace kube-system $(kubectl get po -n kube-system | grep kube-registry-v0 | awk '{print $1;}') 5000:5000 >/dev/null 2>&1 &
#  PORT_FORWARD_PID=$!
#  sleep 2  # 잠깐 대기 (포트포워딩 준비시간)

  #eval $(minikube -p minikube docker-env)

  echo "🐳 Rebuilding Docker Image for $SERVICE_NAME ..."
  sudo docker image build -f ${dockerfiles[$SERVICE_NAME]}/Dockerfile.k3d -t direct-deal-${SERVICE_NAME} ${dockerfiles[$SERVICE_NAME]}
  sudo docker image tag direct-deal-${SERVICE_NAME} localhost:5000/direct-deal-${SERVICE_NAME}

  echo "📤 Pushing Docker Image to Registry ..."
  sudo docker push localhost:5000/direct-deal-${SERVICE_NAME}

#  echo "🛑 Closing Port Forwarding ..."
#  kill -9 $PORT_FORWARD_PID && wait $PID 2>/dev/null

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