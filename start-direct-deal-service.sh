echo "Starting Direct-Deal Service..."

sh -c "kubectl port-forward service/gateway 8084:8084 1>>/dev/null &"
sh -c "kubectl port-forward service/kibana 5601:5601 --namespace=kube-system 1>>/dev/null &"
sh -c "cd direct-deal-ui; npm run serve"

