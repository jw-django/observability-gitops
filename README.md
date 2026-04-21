# Observability & GitOps
본 프로젝트는 로컬 환경(macOS)에서 Kubernetes 클러스터를 구축하고, ArgoCD(App of Apps 패턴)를 통해 PLGT 스택(Prometheus, Loki, Grafana, Tempo) 기반의 MSA 옵저버빌리티를 구현하도록 합니다.

## Prerequisites
시작하기 전 아래 도구들이 설치되어 있어야 합니다.

- Docker Desktop
- kind (`brew install kind`)
- kubectl (`brew install kubernetes-cli`)

## Quick Start (실행 순서)
모든 인프라 구축은 루트 디렉토리 내의 Makefile을 통해 자동화되어 있습니다.

### 1. k8s 클러스터 프로비저닝
1개의 Master 노드와 2개의 Worker 노드로 구성된 kind 클러스터를 생성합니다.
```
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
`gitops/argo/apps` 하위의 모든 설정을 자동으로 관리하는 Root Application을 배포합니다.
```
kubectl apply -f gitops/argo/root-app.yaml
```
- Sync가 되면서 Pod 들이 배포됩니다.

### 5. Grafana UI 접속 (대시보드)
ArgoCD를 통해 옵저버빌리티 스택 배포가 완료된 후, 아래 명령어를 통해 Grafana에 접속하여 메트릭, 로그, 트레이스를 확인합니다.
```
# 포트 포워딩 실행 (다른 터미널에서 유지)
make grafana-pf
```
- 접속: localhost:3000
- ID: admin / PW: admin

### 6. 통합 대시보드 시각화 (Grafana)

커뮤니티 표준 대시보드를 기반으로, Alloy에 맞게 쿼리를 직접 최적화했습니다.

1. `make grafana-pf` 실행 후 `http://localhost:3000` 접속 (ID/PW: admin/admin)
2. 좌측 메뉴 **[Dashboards]** -> 우측 상단 **[New]** -> **[Import]** 클릭
3. 리포지토리의 `docs/k8s-cluster-dashboard.json` 파일을 업로드하여 대시보드 구성 완료

---

### 7. 리소스 정리
모든 작업이 끝난 후 아래 명령어로 클러스터를 삭제합니다.
```
make cluster-down
```

## System Architecture
### GitOps Structure (App of Apps Pattern)
- `gitops/argo/`: CD 오케스트레이션 및 배포 정의
  - App-of-Apps 패턴을 적용하여 클러스터 전체 리소스를 계층적으로 관리합니다.
  - Root App 및 각 서비스(MSA, Monitoring 스택 등)의 ArgoCD Application 리소스를 정의합니다.

- `gitops/manifests/`: 공통 인프라 및 옵저버빌리티(PLGT) 스택의 실제 동작 방식을 결정하는 Helm Values 파일들을 관리합니다.

- `gitops/charts/`: 마이크로서비스 공통 템플릿
  - 마이크로서비스 공통 Helm Chart입니다.
  - 각 서비스들이 동일한 구조로 배포될 수 있도록 템플릿화하였습니다.

### Observability Stack
- Prometheus: Metrics 수집 및 저장

- Loki: 로그 집계 및 분석

- Tempo: 분산 트레이싱(Tracing) 구현

- Grafana: 통합 대시보드 시각화

