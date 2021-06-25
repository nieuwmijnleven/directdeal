echo "Starting Direct-Deal Service..."

echo "Building Images..."
sh -c "./build-images.sh"

echo "Staring Services..."
sh -c "./kubectl-apply.sh"

while true
do
    rt1=$(kubectl get pods | grep 'gateway' | awk '{print $3}')
    rt2=$(kubectl get pods -n kube-system | grep 'kibana' | awk '{print $3}')
    rt3=$(kubectl get pods -n jenkins | grep 'jenkins' | awk '{print $3}')

    if [ "$rt1" = "Running" ] && [ "$rt2" = "Running" ] && [ "$rt3" = "Running" ]; then
    echo "Applying Port Forwarding..."
    sh -c "kubectl port-forward service/gateway 8084:8084 1>>/dev/null &"
    sh -c "kubectl port-forward service/kibana 5601:5601 -n kube-system 1>>/dev/null &"
    sh -c "kubectl port-forward service/jenkins 7080:7080 -n jenkins 1>>/dev/null &"
    break
    fi

    echo "Applying port forwarding is impossible; sleep for 10s before retry"
    sleep 10
done

sh -c "cd direct-deal-ui; npm run serve"

# echo "Closing Port Forwarding..."
# kill -9 `ps -aux | grep 'kubectl port-forward' | grep -v grep | awk '{print $2;}'`



