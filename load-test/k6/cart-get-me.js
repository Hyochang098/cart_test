import http from 'k6/http';
import { check, sleep } from 'k6';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:28080';
const AUTH_MODE = __ENV.AUTH_MODE || 'none';
const MEMBER_COUNT = Number(__ENV.MEMBER_COUNT || 100);
const SCENARIO = __ENV.SCENARIO || 'load';

const scenarioConfig = {
  smoke: {
    executor: 'shared-iterations',
    vus: 1,
    iterations: 10,
    maxDuration: '1m',
  },
  load: {
    executor: 'constant-vus',
    vus: 50,
    duration: '60s',
  },
  stress: {
    executor: 'constant-vus',
    vus: 100,
    duration: '120s',
  },
};

if (!scenarioConfig[SCENARIO]) {
  throw new Error(`지원하지 않는 SCENARIO 입니다: ${SCENARIO}`);
}

if (AUTH_MODE !== 'none') {
  throw new Error(`현재 스크립트는 AUTH_MODE=none 만 지원합니다. 입력값: ${AUTH_MODE}`);
}

export const options = {
  scenarios: {
    cartGetMeScenario: scenarioConfig[SCENARIO],
  },
  thresholds: {
    http_req_duration: ['p(95)<500'],
    checks: ['rate>0.99'],
  },
};

function pickRandomMemberId() {
  return Math.floor(Math.random() * MEMBER_COUNT) + 1;
}

export default function () {
  const memberId = pickRandomMemberId();
  const response = http.get(`${BASE_URL}/api/v1/carts/me`, {
    headers: {
      'X-Local-Member-Id': String(memberId),
    },
  });

  check(response, {
    'status is 200': (res) => res.status === 200,
    'response has items': (res) => {
      if (res.status !== 200) {
        return false;
      }
      const json = res.json();
      return json && Array.isArray(json.items);
    },
  });

  sleep(0.1);
}
