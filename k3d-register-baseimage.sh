#!/bin/bash

registry_name="directdeal-registry.localhost"

# 1️⃣ k3d 클러스터 확인
if ! sudo k3d registry list | grep -q "${registry_name}"; then
  echo "🚀 k3d registry '${registry_name}'이 존재하지 않습니다. 생성 중..."
  sudo k3d cluster create directdeal --no-lb --k3s-arg "--kubelet-arg=feature-gates=KubeletInUserNamespace=true@server:0" --registry-create "${registry_name}:5000"
  k3d kubeconfig merge directdeal --kubeconfig-merge-default --kubeconfig-switch-context
else
  echo "✅ k3d registry '${registry_name}'이 실행 중입니다."
fi


images=(
  "minidocks/java:17-jre-nogui"
  "wurstmeister/kafka:2.12-2.4.1"
  "node:20"
  "mongo:4.0.25"
  "mysql:8.0.41"
  "busybox:latest"
  "zookeeper:3.9.1"
)

for image in "${images[@]}"; do
  # 1. Docker Hub(외부)에서 먼저 이미지 받아오기
  sudo docker pull "${image}"
  # 2. 로컬 레지스트리 주소로 태그 붙이기 (예: localhost:5000)
  sudo docker tag "${image}" "localhost:5000/${image}"
  # 3. 로컬 레지스트리에 푸시하기
  sudo docker push "localhost:5000/${image}"
done