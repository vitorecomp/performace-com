import faker from 'https://cdnjs.cloudflare.com/ajax/libs/Faker/3.1.0/faker.min.js'


import {getConfig, shouldPost, shouldLike, shouldComment, shouldFollow} from './config-factory.js';


export const options = {
  stages: [
    { target: 50, duration: '20s' },
    { target: 1000, duration: '300s' },
    { target: 0, duration: '60s' },
  ],
};


async function makeRequest(baseUrl, requestBody) {
  const result = http.post(`${config.userUrl}/user`, JSON.stringify(requestBody), { headers: { 'Content-Type': "application/json" } });
  check(result, {
      'http response status code is 200 or 201': result.status === 200 || result.status === 201,
  });
  return JSON.parse(result.body);
}

export default function () {
  //get config
  const config = getConfig();
  let baseUrl = config.appUrl;
  let requestBody = config.getRequestBody();

  requestPromises = []
  for(let i = 0; i < 0; i++){
    requestPromises.push(makeRequest(baseUrl, requestBody));
  }
  Promise.all(requestPromises);
}