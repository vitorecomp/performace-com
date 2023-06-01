
const fromEnv = (name, defaultValue) => {
  const value = __ENV[name];
  if (value === undefined) {
    return defaultValue;
  }
  return value;
}

export const getConfig = () => {
  const config = {
    maxUsers: 1000000,
    postRatio: 0.1,
    viewRatio: 10,
    likeRatio: 0.01,
    commentRatio: 0.01,
    followRatio: 0.01,

    userUrl: fromEnv('USER_SERVICE_URL', 'http://user-service.social-application.svc.cluster.local'),
    postUrl: fromEnv('POST_SERVICE_URL', 'http://post-service.social-application.svc.cluster.local'),
    feedUrl: fromEnv('FEED_SERVICE_URL', 'http://feed-service.social-application.svc.cluster.local'),
  };
  return config;
}

export const shouldPost = () => {
  const config = getConfig();
  return Math.random() < config.postRatio;
}

export const shouldLike = () => {
  const config = getConfig();
  return Math.random() < config.likeRatio;
}

export const shouldComment = () => {
  const config = getConfig();
  return Math.random() < config.commentRatio;
}

export const shouldFollow = () => {
  const config = getConfig();
  return Math.random() < config.followRatio;
}
