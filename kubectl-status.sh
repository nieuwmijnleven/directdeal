#!/bin/sh

echo "###### services ######"
kubectl get services

echo "###### deployments ######"
kubectl get deployments

echo "###### pods ######"
kubectl get pods

echo "###### endpoints ######"
kubectl get endpoints

echo "###### nodes ######"
kubectl describe nodes
