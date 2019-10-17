#!/bin/bash
set -e

export IMAGE_NAME=pytorch_on_angel

docker build -t ${IMAGE_NAME} .
docker run -it --rm $(pwd)/dist:/output ${IMAGE_NAME}
