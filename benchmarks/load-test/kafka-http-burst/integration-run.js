import faker from 'https://cdnjs.cloudflare.com/ajax/libs/Faker/3.1.0/faker.min.js'


import {getConfig, getRequestBody} from './config-factory.js';
import http from 'k6/http';
import { check } from 'k6';


export const options = {
  stages: [
    { target: 50, duration: '10s' },
    { target: 100, duration: '300s' },
    { target: 0, duration: '60s' },
  ],
};


async function makeRequest(baseUrl, requestBody) {
  const result = http.post(`${baseUrl}/kafka-benchmark/simple-producer`, JSON.stringify(requestBody), { headers: { 'Content-Type': "application/json" } });
  check(result, {
      'http response status code is 200': result.status === 200,
  });
  return JSON.parse(result.body);
}

export default function () {
  //get config
  const config = getConfig();
  let baseUrl = config.appUrl;
  let requestBody = getRequestBody();

  let requestPromises = []
  for(let i = 0; i < config.numberOfRequests; i++){
    requestPromises.push(makeRequest(baseUrl, requestBody));
  }
  return Promise.all(requestPromises);
}