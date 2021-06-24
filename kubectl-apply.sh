#!/bin/bash

minikube ssh sudo ip link set docker0 promisc on

KUBERNETES_CONFIG_PATH="./deployment/kubernetes"

eval $(minikube -p minikube docker-env)

# kubectl delete -f ${KUBERNETES_CONFIG_PATH}/registry.yml
kubectl delete -f ${KUBERNETES_CONFIG_PATH}/log-elasticsearch.yml
kubectl delete -f ${KUBERNETES_CONFIG_PATH}/log-kibana.yml
kubectl delete -f ${KUBERNETES_CONFIG_PATH}/log-fluentd.yml
kubectl delete -f ${KUBERNETES_CONFIG_PATH}/zookeeper-service.yml
kubectl delete -f ${KUBERNETES_CONFIG_PATH}/mysql-service.yml
kubectl delete -f ${KUBERNETES_CONFIG_PATH}/mongo-service.yml
kubectl delete -f ${KUBERNETES_CONFIG_PATH}/redis-service.yml
kubectl delete -f ${KUBERNETES_CONFIG_PATH}/kafka-service.yml
kubectl delete -f ${KUBERNETES_CONFIG_PATH}/direct-deal-account-service.yml
kubectl delete -f ${KUBERNETES_CONFIG_PATH}/direct-deal-chatting-service.yml
kubectl delete -f ${KUBERNETES_CONFIG_PATH}/direct-deal-sale-service.yml
kubectl delete -f ${KUBERNETES_CONFIG_PATH}/direct-deal-sale-catalog-service.yml
kubectl delete -f ${KUBERNETES_CONFIG_PATH}/direct-deal-transaction-history-service.yml
kubectl delete -f ${KUBERNETES_CONFIG_PATH}/direct-deal-gateway-service.yml

# kubectl create -f ${KUBERNETES_CONFIG_PATH}/registry.yml
kubectl create -f ${KUBERNETES_CONFIG_PATH}/log-elasticsearch.yml
kubectl create -f ${KUBERNETES_CONFIG_PATH}/log-kibana.yml
kubectl create -f ${KUBERNETES_CONFIG_PATH}/log-fluentd.yml
kubectl create -f ${KUBERNETES_CONFIG_PATH}/zookeeper-service.yml
kubectl create -f ${KUBERNETES_CONFIG_PATH}/mysql-service.yml
kubectl create -f ${KUBERNETES_CONFIG_PATH}/mongo-service.yml
kubectl create -f ${KUBERNETES_CONFIG_PATH}/redis-service.yml
kubectl create -f ${KUBERNETES_CONFIG_PATH}/kafka-service.yml
kubectl create -f ${KUBERNETES_CONFIG_PATH}/direct-deal-account-service.yml
kubectl create -f ${KUBERNETES_CONFIG_PATH}/direct-deal-chatting-service.yml
kubectl create -f ${KUBERNETES_CONFIG_PATH}/direct-deal-sale-service.yml
kubectl create -f ${KUBERNETES_CONFIG_PATH}/direct-deal-sale-catalog-service.yml
kubectl create -f ${KUBERNETES_CONFIG_PATH}/direct-deal-transaction-history-service.yml
kubectl create -f ${KUBERNETES_CONFIG_PATH}/direct-deal-gateway-service.yml

