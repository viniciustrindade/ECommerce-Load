#!/bin/bash

source env.sh

nohup Xvfb :10 -ac &
export DISPLAY=:10;

$LOAD_GEN_HOME/bin/ECommerce-Load $NUM_OF_USERS $RAMP_TIME $TIME_BETWEEN_RUNS $TARGET_HOST $TARGET_PORT $WAIT_TIME

