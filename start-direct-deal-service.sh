#!/bin/bash

echo "Starting Direct-Deal Service..."

echo "Starting Minikube..."
sh -c "minikube stop"
sh -c "minikube delete"
sh -c "minikube start --memory 8192 --cpus 2"

echo "Building Images..."
sh -c "./build-images.sh"

echo "Staring Services..."
sh -c "./kubectl-apply.sh"

echo "Waiting for all services to be up...(Maybe it takes up to about 5 minutes)"

# [service-name namespace] array definition (namespace can be omitted if it's default)
services=(
  "account-service default"
  "sale-service default"
  "sale-catalog-service default"
  "transaction-history-service default"
  "chatting-service default"
  "gateway default"
#   "kibana kube-system"
#   "jenkins jenkins"
)

# Function to check if the pod is Running
check_pod_running() {
  local svc=$1
  local ns=$2
  local namespace_opt=""

  [[ "$ns" != "default" ]] && namespace_opt="-n $ns"

  kubectl get pods $namespace_opt 2>/dev/null \
    | grep "$svc" \
    | grep "Running" \
    | awk '{print $2}' \
    | awk -F '/' '{print $1}' \
    | awk '{sum+=$1} END {print sum}'
}

# Function to remove printed lines (to refresh the output)
remove_lines() {
  local lines_to_remove=$1
  for (( i=0; i<lines_to_remove; i++ )); do
    echo -ne "\x1b[2K"   # clear current line
    echo -e "\x1b[1A"    # move cursor up one line
  done
}

while true; do
  all_up=true
  output=()

  output+=("Checking service statuses...")

  for entry in "${services[@]}"; do
    read -r svc ns <<<"$entry"
    running_count=$(check_pod_running "$svc" "$ns")

    if [[ -z "$running_count" || "$running_count" -le 0 ]]; then
      output+=("❌ $svc (namespace: $ns) is NOT ready")
      all_up=false
    else
      output+=("✅ $svc (namespace: $ns) is running")
    fi
  done

  # Print collected output
  for line in "${output[@]}"; do
    echo "$line"
  done

  if $all_up; then
    echo "-----------------------------------------"
    echo "🎉 All services are up and running!"
    break
  fi

  echo "Waiting 5 seconds before next check..."
  sleep 5

  # Remove previously printed lines (output lines + waiting message)
  remove_lines $(( ${#output[@]} + 2 ))
done

echo "Applying Port Forwarding..."
#sh -c "kubectl port-forward service/gateway 8084:8084 1>>/dev/null &"
sh -c "kubectl port-forward service/nginx 9000:9000 1>>/dev/null &"
#sh -c "kubectl port-forward service/kibana 5601:5601 -n kube-system 1>>/dev/null &"
#sh -c "kubectl port-forward service/jenkins 7080:7080 -n jenkins 1>>/dev/null &"

# echo "Starting HTTP-Server for Front-End..."
# rt=$(npm)
# if [ $? -eq 127 ]; then
#     echo "Installing NPM(Node Package Manager)..."
#     sh -c "cd direct-deal-ui && npm install --save-dev"
# fi

# NODE_OPTIONS=--openssl-legacy-provider sh -c "cd direct-deal-ui && npm run serve"


# echo "Closing Port Forwarding..."
# kill -9 `ps -aux | grep 'kubectl port-forward' | grep -v grep | awk '{print $2;}'`



