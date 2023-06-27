
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
    numMessages: 100,
    numberOfRequests: 10,

    appUrl: fromEnv('APP_URL', 'http://localhost:8080'),
  };
  return config;
}

export const getRequestBody = () => {
  const config = getConfig();
  return {
    messageSize: config.messageSize,
    numMessages: config.numMessages
  }
}

