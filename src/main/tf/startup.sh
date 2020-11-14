#! /bin/bash

cd /tmp
git clone https://github.com/tillias/microservice-catalog.git
cd microservice-catalog
docker run --rm \
    -v /var/run/docker.sock:/var/run/docker.sock \
    -v "$PWD:$PWD" \
    -w="$PWD" \
    docker/compose up
