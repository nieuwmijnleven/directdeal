echo "Starting Direct-Deal Service..."

echo "Building Images..."
sh -c "./build-images.sh"

echo "Staring Services..."
sh -c "./kubectl-apply.sh"

echo "Waiting for all services to be up..."
while true
do
    rt1=$(kubectl get pods | grep 'account-service' | awk '{print $3}')
    rt2=$(kubectl get pods | grep 'sale-service' | awk '{print $3}')
    rt3=$(kubectl get pods | grep 'sale-catalog-service' | awk '{print $3}')
    rt4=$(kubectl get pods | grep 'transaction-history-service' | awk '{print $3}')
    rt5=$(kubectl get pods | grep 'chatting-service' | awk '{print $3}')
    rt6=$(kubectl get pods -n kube-system | grep 'kibana' | awk '{print $3}')
    rt7=$(kubectl get pods -n jenkins | grep 'jenkins' | awk '{print $3}')

    if [ "$rt1" = "Running" ] && [ "$rt2" = "Running" ] && [ "$rt3" = "Running" ] && [ "$rt4" = "Running" ] && [ "$rt5" = "Running" ] && [ "$rt6" = "Running" ] && [ "$rt7" = "Running" ]; then
    echo "All service are up"
    break
    fi

    sleep 5
done

echo "Applying Port Forwarding..."
sh -c "kubectl port-forward service/gateway 8084:8084 1>>/dev/null &"
sh -c "kubectl port-forward service/kibana 5601:5601 -n kube-system 1>>/dev/null &"
sh -c "kubectl port-forward service/jenkins 7080:7080 -n jenkins 1>>/dev/null &"

sh -c "cd direct-deal-ui; npm run serve"

# echo "Closing Port Forwarding..."
# kill -9 `ps -aux | grep 'kubectl port-forward' | grep -v grep | awk '{print $2;}'`



