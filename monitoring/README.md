# Monitoring 실행 가이드

## 1) 서버 실행

먼저 두 서버를 각각 실행합니다.

```bash
./gradlew :cart-state-server:bootRun
./gradlew :cart-display-server:bootRun
```

메트릭 엔드포인트 확인:

```bash
curl http://localhost:28080/actuator/prometheus
curl http://localhost:28081/actuator/prometheus
```

## 2) Prometheus + Grafana 실행

```bash
cd monitoring
docker compose up -d
```

- Prometheus: [http://localhost:19090](http://localhost:19090)
- Grafana: [http://localhost:13000](http://localhost:13000)
  - ID: `admin`
  - PW: `admin`

## 3) 대시보드

Grafana에 접속하면 `Cart Comparison` 폴더에 대시보드 3개가 자동 등록됩니다.

- `Cart Servers Comparison Overview` — 두 서버 비교용
- `Cart State Server (28080)` — state 서버 단독 (스냅샷용)
- `Cart Display Server (28081)` — display 서버 단독 (스냅샷용)

각 대시보드 포함 항목:
- `GET /api/v1/carts/me` RPS
- `GET /api/v1/carts/me` Latency (p95, p99)
- 5xx 에러 비율
- JVM Heap (used, max)
