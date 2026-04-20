# Observability & GitOps
본 프로젝트는 로컬 환경(macOS)에서 Kubernetes 클러스터를 구축하고, ArgoCD(App of Apps 패턴)를 통해 PLGT 스택(Prometheus, Loki, Grafana, Tempo) 기반의 MSA 옵저버빌리티를 구현하도록 합니다.

## Prerequisites
시작하기 전 아래 도구들이 설치되어 있어야 합니다.

- Docker Desktop
- kind (brew install kind)
- kubectl (brew install kubernetes-cli)

## Quick Start (실행 순서)
모든 인프라 구축은 cluster 디렉토리 내의 Makefile을 통해 자동화되어 있습니다.

### 1. k8s 클러스터 프로비저닝
1개의 Master 노드와 2개의 Worker 노드로 구성된 kind 클러스터를 생성합니다.
```
cd cluster
make cluster-up
```

### 2. ArgoCD 설치 및 초기 설정
GitOps 운영을 위한 ArgoCD를 설치하고 Pod가 준비될 때까지 대기합니다.
```
make argocd-install
```

### 3. ArgoCD UI 접속 및 비밀번호 확인
초기 admin 계정의 비밀번호를 확인하고 UI에 접속합니다.
```
# 초기 비밀번호 확인
make argocd-pw

# 포트 포워딩 실행 (다른 터미널에서 유지)
make argocd-pf
```
- 접속: localhost:38080
- ID: admin / PW: 위에서 확인한 비밀번호

### 4. GitOps Root App 배포 (App of Apps)
gitops/argo/apps 하위의 모든 설정을 자동으로 관리하는 Root Application을 배포합니다.
```
# 프로젝트 루트 디렉토리에서 실행
# cd ..
kubectl apply -f gitops/argo/root-app.yaml
```


## System Architecture
### GitOps Structure (App of Apps Pattern)
실무의 다중 환경 관리 기법을 모사하여 directory.recurse: true 설정을 적용했습니다.

- gitops/argo/: ArgoCD Application CRD 관리 (Root App 진입점)

- gitops/manifests/: 공통 인프라 및 옵저버빌리티(PLGT) 스택

- gitops/charts/: 마이크로서비스별 Helm Charts

### Observability Stack
- Prometheus: Metrics 수집 및 저장

- Loki: 로그 집계 및 분석

- Tempo: 분산 트레이싱(Tracing) 구현

- Grafana: 통합 대시보드 시각화

