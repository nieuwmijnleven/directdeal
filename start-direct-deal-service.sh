#!/bin/sh

echo "Starting Direct-Deal Service..."

echo "Building Images..."
sh -c "./build-images.sh"

echo "Staring Services..."
sh -c "./kubectl-apply.sh"

echo "Waiting for all services to be up...(Maybe it takes up to about 5 minutes)"
while true
do
    rt1=$(kubectl get pods | grep 'account-service' | grep 'Running' | awk '{print $2}' | awk -F '/' '{print $1}')
    rt2=$(kubectl get pods | grep 'sale-service' | grep 'Running' | awk '{print $2}' | awk -F '/' '{print $1}')
    rt3=$(kubectl get pods | grep 'sale-catalog-service' | grep 'Running' | awk '{print $2}' | awk -F '/' '{print $1}')
    rt4=$(kubectl get pods | grep 'transaction-history-service' | grep 'Running' | awk '{print $2}' | awk -F '/' '{print $1}')
    rt5=$(kubectl get pods | grep 'chatting-service' | grep 'Running' | awk '{print $2}' | awk -F '/' '{print $1}')
    rt6=$(kubectl get pods | grep 'gateway' | grep 'Running' | awk '{print $2}' | awk -F '/' '{print $1}')
    rt7=$(kubectl get pods -n kube-system | grep 'kibana' | grep 'Running' | awk '{print $2}' | awk -F '/' '{print $1}')
    rt8=$(kubectl get pods -n jenkins | grep 'jenkins' | grep 'Running' | awk '{print $2}' | awk -F '/' '{print $1}')

    if [ "$rt1" ] && [ "$rt2" ] && [ "$rt3" ] && [ "$rt4" ] && [ "$rt5" ] && [ "$rt6" ] && [ "$rt7" ] && [ "$rt8" ]; then
        if [ "$rt1" -gt 0 ] && [ "$rt2" -gt 0 ] && [ "$rt3" -gt 0 ] && [ "$rt4" -gt 0 ] && [ "$rt5" -gt 0 ] && [ "$rt6" -gt 0 ] && [ "$rt7" -gt 0 ] && [ "$rt8" -gt 0 ]; then
        echo "All services are up"
        break
        fi
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



