#!/bin/sh

set -m # Enable Job Control

  for i in `seq 50`; do
  curl "http://localhost:8080/aggregate" \
  -X POST \
  -d "[\"+1983236248\", \"+1 7490276403\", \"001382355A\", \"+351917382672\", \"+35191734022\"]" \
  -H "Content-Type: application/json" &
done

# Wait for all parallel jobs to finish
while [ 1 ]; do fg 2> /dev/null; [ $? == 1 ] && break; done