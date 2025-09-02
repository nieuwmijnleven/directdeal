#!/bin/bash

KUBERNETES_CONFIG_PATH="./deployment/kubernetes"

eval $(minikube -p minikube docker-env)

echo "Creating Registry..."
#docker run -d -p 5000:5000 --name registry registry:2
kubectl create -f ${KUBERNETES_CONFIG_PATH}/registry.yml

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

echo "Applying Port Forwarding..."
sh -c "kubectl port-forward --namespace kube-system $(kubectl get po -n kube-system | grep kube-registry-v0 | awk '{print $1;}') 5000:5000 &"

echo "Building Direct-Deal Project..."
./gradlew clean build
./gradlew clean build -p direct-deal-gateway
# ./gradlew clean bootJar
# ./gradlew clean bootJar -p direct-deal-gateway

echo "Making Docker Images..."
docker image build -t direct-deal-account-service ./direct-deal-account-service
docker image tag direct-deal-account-service localhost:5000/direct-deal-account-service

docker image build -t direct-deal-sale-service ./direct-deal-sale-service
docker image tag direct-deal-sale-service localhost:5000/direct-deal-sale-service

docker image build -t direct-deal-sale-catalog-service-webflux ./direct-deal-sale-catalog-service-webflux
docker image tag direct-deal-sale-catalog-service-webflux localhost:5000/direct-deal-sale-catalog-service-webflux

docker image build -t direct-deal-transaction-history-service ./direct-deal-transaction-history-service
docker image tag direct-deal-transaction-history-service localhost:5000/direct-deal-transaction-history-service

docker image build -t direct-deal-chatting-service ./direct-deal-chatting-service
docker image tag direct-deal-chatting-service localhost:5000/direct-deal-chatting-service

docker image build -t direct-deal-gateway ./direct-deal-gateway
docker image tag direct-deal-gateway localhost:5000/direct-deal-gateway

docker image build -t direct-deal-mysql ./mysql
docker image tag direct-deal-mysql localhost:5000/direct-deal-mysql

docker image build -t direct-deal-nginx ./direct-deal-ui
docker image tag direct-deal-nginx localhost:5000/direct-deal-nginx

echo "Pushing Docker Images To The Registry..."
docker push localhost:5000/direct-deal-account-service
docker push localhost:5000/direct-deal-sale-service
# docker push localhost:5000/direct-deal-sale-catalog-service
docker push localhost:5000/direct-deal-sale-catalog-service-webflux
docker push localhost:5000/direct-deal-transaction-history-service
docker push localhost:5000/direct-deal-chatting-service
docker push localhost:5000/direct-deal-gateway
docker push localhost:5000/direct-deal-mysql
docker push localhost:5000/direct-deal-nginx

echo "Closing Port Forwarding..."
kill -9 `ps -aux | grep 'kubectl port-forward' | grep -v grep | awk '{print $2;}'`

#curl http://localhost:5000/v2/_catalog
#docker container stop registry && docker container rm -v registry
