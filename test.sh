#!/bin/bash

set -e
declare -A manifests
declare -A images

manifests[account-service]="./deployment/kubernetes/direct-deal-account-service.yml"
manifests[chatting-service]="./deployment/kubernetes/direct-deal-chatting-service.yml"
manifests[gateway]="./deployment/kubernetes/direct-deal-gateway-service.yml"
manifests[sale-catalog-service]="./deployment/kubernetes/direct-deal-sale-catalog-service.yml"
manifests[sale-service]="./deployment/kubernetes/direct-deal-sale-service-webflux.yml"
manifests[transaction-history-service]="./deployment/kubernetes/direct-deal-transaction-history-service.yml"
manifests[kafka]="./deployment/kubernetes/kafka-service.yml"
manifests[mongo]="./deployment/kubernetes/mongo-service.yml"
manifests[mysql]="./deployment/kubernetes/mysql-service.yml"
manifests[nginx]="./deployment/kubernetes/nginx-service.yml"
manifests[redis]="./deployment/kubernetes/redis-service.yml"
manifests[zookeeper]="./deployment/kubernetes/zookeeper-service.yml"

images[account-service]="gcr.io/$PROJECT_ID/direct-deal-account-service:$REVISION_ID"
images[chatting-service]="gcr.io/$PROJECT_ID/direct-deal-chatting-service:$REVISION_ID"
images[gateway]="gcr.io/$PROJECT_ID/direct-deal-gateway:$REVISION_ID"
images[sale-catalog-service]="gcr.io/$PROJECT_ID/direct-deal-sale-catalog-service:$REVISION_ID"
images[sale-service]="gcr.io/$PROJECT_ID/direct-deal-sale-service-webflux:$REVISION_ID"
images[transaction-history-service]="gcr.io/$PROJECT_ID/direct-deal-transaction-history-service:$REVISION_ID"
images[kafka]="wurstmeister/kafka:2.12-2.4.0"
images[mongo]="mongo:4.0.25"
images[mysql]="gcr.io/$PROJECT_ID/direct-deal-mysql:$REVISION_ID"
images[nginx]="gcr.io/$PROJECT_ID/direct-deal-nginx:$REVISION_ID"
images[redis]="redis:6.2.4-alpine3.13"
images[zookeeper]="wurstmeister/zookeeper"

for deploy in "${!manifests[@]}"; do
    if ! kubectl get deployment $deploy -n default > /dev/null 2>&1; then
        echo "Creating deployment for $deploy"
        envsubst < "${manifests[$deploy]}" | kubectl apply -n default -f -
        # Wait for the deployment to be ready
        # kubectl rollout status deployment/$deploy -n default
    fi
    echo "Updating image for $deploy"
    kubectl set image deployment/$deploy $deploy=${images[$deploy]} -n default
done
