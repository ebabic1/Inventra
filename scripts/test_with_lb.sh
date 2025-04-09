#!/bin/bash

URL_WITH_LB="http://localhost:8082/api/reports/test-loadbalancing"

REQUEST_COUNT=100

declare -A counts
counts["instance_8081"]=0
counts["instance_8084"]=0

TOTAL_TIME=0

echo "Testing with Load Balancer (Eureka)..."
for ((i=1; i<=REQUEST_COUNT; i++))
do
    START_TIME=$(date +%s%3N)
    RESPONSE=$(curl -s -w "%{http_code}" -o /dev/null $URL_WITH_LB)
    END_TIME=$(date +%s%3N)
    ELAPSED_TIME=$((END_TIME - START_TIME))
    TOTAL_TIME=$((TOTAL_TIME + ELAPSED_TIME))

    if [[ $RESPONSE == 200 ]]; then
        INSTANCE=$(curl -s $URL_WITH_LB)
        if [[ $INSTANCE == *"8081"* ]]; then
            counts["instance_8081"]=$((counts["instance_8081"] + 1))
        elif [[ $INSTANCE == *"8084"* ]]; then
            counts["instance_8084"]=$((counts["instance_8084"] + 1))
        fi
    fi

    sleep 0.6
done

echo "----------------------------------------"
echo "Results:"
echo "Requests with Load Balancer:"
echo "Instance 8081: ${counts["instance_8081"]} requests"
echo "Instance 8084: ${counts["instance_8084"]} requests"
echo "Average Response Time with Load Balancing: $((TOTAL_TIME / REQUEST_COUNT)) ms"