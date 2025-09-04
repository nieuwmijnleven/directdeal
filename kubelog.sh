#!/bin/bash

eval $(minikube -p minikube docker-env)

SERVICE_NAME=$1
NAMESPACE=$2

get_running_pod_in_svc() {
  local svc=$1
  local ns=$2
  local namespace_opt="default"

  [[ -n "$ns" ]] && [[ "$ns" != "default" ]] && namespace_opt="-n $ns"

  kubectl get pods -n $namespace_opt 2>/dev/null \
    | grep "$svc" \
    | awk '{print $1}'

    #    | grep "Running" \
}

pod_name=$(get_running_pod_in_svc ${SERVICE_NAME} ${NAMESPACE})
[ -z "$pod_name" ] && echo "${SERVICE_NAME} is not running."
[ -n "$pod_name" ] && kubectl logs ${pod_name}
