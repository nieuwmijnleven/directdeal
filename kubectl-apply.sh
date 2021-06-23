#!/bin/bash

KUBERNETES_CONFIG_PATH="./deployment/kubernetes"

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

kubectl apply -f ${KUBERNETES_CONFIG_PATH}/zookeeper-service.yml
kubectl apply -f ${KUBERNETES_CONFIG_PATH}/mysql-service.yml
kubectl apply -f ${KUBERNETES_CONFIG_PATH}/mongo-service.yml
kubectl apply -f ${KUBERNETES_CONFIG_PATH}/redis-service.yml
kubectl apply -f ${KUBERNETES_CONFIG_PATH}/kafka-service.yml
kubectl apply -f ${KUBERNETES_CONFIG_PATH}/direct-deal-account-service.yml
kubectl apply -f ${KUBERNETES_CONFIG_PATH}/direct-deal-chatting-service.yml
kubectl apply -f ${KUBERNETES_CONFIG_PATH}/direct-deal-sale-service.yml
kubectl apply -f ${KUBERNETES_CONFIG_PATH}/direct-deal-sale-catalog-service.yml
kubectl apply -f ${KUBERNETES_CONFIG_PATH}/direct-deal-transaction-history-service.yml
kubectl apply -f ${KUBERNETES_CONFIG_PATH}/direct-deal-gateway-service.yml

