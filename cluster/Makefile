CLUSTER_NAME := observability-gitops-cluster

.PHONY: help cluster-up cluster-down

help: ## 사용 가능한 명령어 목록을 보여줍니다.
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'

cluster-up: ## Kind 클러스터를 생성합니다.
	@echo "K8s 클러스터($(CLUSTER_NAME))를 생성합니다..."
	kind create cluster --name $(CLUSTER_NAME) --config kind-config.yaml
	@echo "클러스터 생성 완료! (kubectl get nodes 로 확인하세요)"

cluster-down: ## Kind 클러스터를 삭제합니다.
	@echo "K8s 클러스터($(CLUSTER_NAME))를 삭제합니다..."
	kind delete cluster --name $(CLUSTER_NAME)
	@echo "클러스터 삭제 완료!"
