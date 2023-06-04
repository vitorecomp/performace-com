
const fromEnv = (name, defaultValue) => {
  const value = __ENV[name];
  if (value === undefined) {
    return defaultValue;
  }
  return value;
}

export const getConfig = () => {
  const config = {
    messageSize: 2000,
    numMessages: 20,
    numberOfRequests: 10,

    appUrl: fromEnv('SERVICE_URL', 'http://app.bench-application.svc.cluster.local'),
  };
  return config;
}

