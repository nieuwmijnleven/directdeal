#!/bin/bash

# Usage: ./redeploy-service.sh [service-name]
# Example: ./redeploy-service.sh account-service
#          ./redeploy-service.sh sale-service
#          ./redeploy-service.sh chatting-service

SERVICE_NAME=$1
KUBERNETES_CONFIG_PATH="./deployment/kubernetes"

if [ -z "$SERVICE_NAME" ]; then
  echo "❌ Please provide a service name. (ex: ./redeploy-service.sh account-service)"
  exit 1
fi

echo "----------------------------------------"
echo "🔽 Stopping $SERVICE_NAME ..."
kubectl delete -f ${KUBERNETES_CONFIG_PATH}/direct-deal-${SERVICE_NAME}.yml

echo "----------------------------------------"
echo "🛠️ Rebuilding $SERVICE_NAME ..."
./gradlew clean build -p direct-deal-${SERVICE_NAME}

echo "----------------------------------------"
echo "📡 Starting Port Forwarding for Registry ..."
kubectl port-forward --namespace kube-system $(kubectl get po -n kube-system | grep kube-registry-v0 | awk '{print $1;}') 5000:5000 >/dev/null 2>&1 &
PORT_FORWARD_PID=$!
sleep 2  # 잠깐 대기 (포트포워딩 준비시간)

echo "🐳 Rebuilding Docker Image for $SERVICE_NAME ..."
docker image build -t direct-deal-${SERVICE_NAME} ./direct-deal-${SERVICE_NAME}
docker image tag direct-deal-${SERVICE_NAME} localhost:5000/direct-deal-${SERVICE_NAME}

echo "📤 Pushing Docker Image to Registry ..."
docker push localhost:5000/direct-deal-${SERVICE_NAME}

echo "🛑 Closing Port Forwarding ..."
kill -9 $PORT_FORWARD_PID && wait $PID 2>/dev/null

echo "----------------------------------------"
echo "🚀 Restarting $SERVICE_NAME on Kubernetes ..."
kubectl create -f ${KUBERNETES_CONFIG_PATH}/direct-deal-${SERVICE_NAME}.yml

echo "----------------------------------------"
echo "✅ $SERVICE_NAME has been successfully redeployed!"