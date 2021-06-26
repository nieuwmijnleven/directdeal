#!/bin/sh

KUBERNETES_CONFIG_PATH="./deployment/kubernetes"

eval $(minikube -p minikube docker-env)

echo "Creating Registry..."
#docker run -d -p 5000:5000 --name registry registry:2
kubectl create -f ${KUBERNETES_CONFIG_PATH}/registry.yml

while true
do
    rt=$(minikube ssh curl localhost:5000)
    if [ $? -eq 0 ]; then
    echo "Registry is UP"
    break
    fi
    echo "Registry is not yet reachable;sleep for 10s before retry"
    sleep 10
done

echo "Applying Port Forwarding..."
sh -c "kubectl port-forward --namespace kube-system $(kubectl get po -n kube-system | grep kube-registry-v0 | awk '{print $1;}') 5000:5000 &"

echo "Building Direct-Deal Project..."
./gradlew build
./gradlew build -p direct-deal-gateway
# ./gradlew clean bootJar
# ./gradlew clean bootJar -p direct-deal-gateway

echo "Making Docker Images..."
docker image build -t direct-deal-account-service ./direct-deal-account-service
docker image tag direct-deal-account-service localhost:5000/direct-deal-account-service

docker image build -t direct-deal-sale-service ./direct-deal-sale-service
docker image tag direct-deal-sale-service localhost:5000/direct-deal-sale-service

docker image build -t direct-deal-sale-catalog-service ./direct-deal-sale-catalog-service
docker image tag direct-deal-sale-catalog-service localhost:5000/direct-deal-sale-catalog-service

docker image build -t direct-deal-transaction-history-service ./direct-deal-transaction-history-service
docker image tag direct-deal-transaction-history-service localhost:5000/direct-deal-transaction-history-service

docker image build -t direct-deal-chatting-service ./direct-deal-chatting-service
docker image tag direct-deal-chatting-service localhost:5000/direct-deal-chatting-service

docker image build -t direct-deal-gateway ./direct-deal-gateway
docker image tag direct-deal-gateway localhost:5000/direct-deal-gateway

docker image build -t direct-deal-mysql ./mysql
docker image tag direct-deal-mysql localhost:5000/direct-deal-mysql

echo "Pushing Docker Images To The Registry..."
docker push localhost:5000/direct-deal-account-service
docker push localhost:5000/direct-deal-sale-service
docker push localhost:5000/direct-deal-sale-catalog-service
docker push localhost:5000/direct-deal-transaction-history-service
docker push localhost:5000/direct-deal-chatting-service
docker push localhost:5000/direct-deal-gateway
docker push localhost:5000/direct-deal-mysql

echo "Closing Port Forwarding..."
kill -9 `ps -aux | grep 'kubectl port-forward' | grep -v grep | awk '{print $2;}'`

#curl http://localhost:5000/v2/_catalog
#docker container stop registry && docker container rm -v registry
