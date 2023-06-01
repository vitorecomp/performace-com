#!/bin/bash

export USER_SERVICE_URL=http://user-service-social-application.apps.cluster-lwrgl.lwrgl.sandbox2523.opentlc.com
export POST_SERVICE_URL=http://post-service-social-application.apps.cluster-lwrgl.lwrgl.sandbox2523.opentlc.com
export FEED_SERVICE_URL=http://feed-service-social-application.apps.cluster-lwrgl.lwrgl.sandbox2523.opentlc.com

k6 run integration-run.js