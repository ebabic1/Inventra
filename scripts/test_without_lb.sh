#!/bin/bash

URL_WITH_LB="http://localhost:8081/api/orders/instance-port"

REQUEST_COUNT=100

declare -A counts
counts["instance"]=0

TOTAL_TIME=0

echo "Testing without Load Balancer (Eureka)..."
for ((i=1; i<=REQUEST_COUNT; i++))
do
    START_TIME=$(date +%s%3N)
    RESPONSE=$(curl -s -w "%{http_code}" -o /dev/null $URL_WITH_LB)
    END_TIME=$(date +%s%3N)
    ELAPSED_TIME=$((END_TIME - START_TIME))
    TOTAL_TIME=$((TOTAL_TIME + ELAPSED_TIME))

    counts["instance"]=$((counts["instance"] + 1))

    sleep 0.6
done

echo "----------------------------------------"
echo "Average Response Time without Load Balancing: $((TOTAL_TIME / REQUEST_COUNT)) ms"