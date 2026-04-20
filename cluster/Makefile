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

argocd-install: ## 클러스터에 ArgoCD를 설치합니다.
	@echo "ArgoCD를 설치합니다..."
	kubectl create namespace argocd || true
	kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml --server-side
	@echo "ArgoCD Pod들이 Ready 상태가 될 때까지 기다립니다..."
	kubectl wait --for=condition=ready pod -l app.kubernetes.io/name=argocd-server -n argocd --timeout=300s
	@echo "ArgoCD 설치 완료!"

argocd-pw: ## ArgoCD 초기 admin 비밀번호를 확인합니다.
	@echo "ArgoCD 초기 비밀번호:"
	@kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d
	@echo ""

argocd-pf: ## 로컬에서 ArgoCD UI에 접근하기 위해 포트포워딩을 실행합니다 (localhost:38080).
	@echo "ArgoCD 포트포워딩 실행 중... (http://localhost:38080 으로 접속하세요. 종료하려면 Ctrl+C)"
	kubectl port-forward svc/argocd-server -n argocd 38080:443

grafana-pf: ## 로컬에서 Grafana UI에 접근하기 위해 포트포워딩을 실행합니다 (localhost:3000).
	@echo "Grafana 포트포워딩 실행 중... (http://localhost:3000 으로 접속하세요. 종료하려면 Ctrl+C)"
	@echo "ID: admin / PW: admin"
	kubectl port-forward svc/grafana -n monitoring 3000:80