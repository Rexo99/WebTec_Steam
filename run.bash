#!/bin/bash

mvn package
mvn cargo:redeploy
