#!/bin/sh
# Maven Wrapper Script for POSIX systems
MAVEN_WRAPPER_VERSION="3.9.6"
MAVEN_DOWNLOAD_URL="https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/${MAVEN_WRAPPER_VERSION}/apache-maven-${MAVEN_WRAPPER_VERSION}-bin.zip"

MAVEN_HOME="$HOME/.m2/wrapper/dists/apache-maven-${MAVEN_WRAPPER_VERSION}"
MAVEN_BIN="$MAVEN_HOME/bin/mvn"

if [ ! -f "$MAVEN_BIN" ]; then
    mkdir -p "$MAVEN_HOME"
    curl -sL "$MAVEN_DOWNLOAD_URL" -o "/tmp/maven.zip"
    unzip -q "/tmp/maven.zip" -d "$HOME/.m2/wrapper/dists/"
    mv "$HOME/.m2/wrapper/dists/apache-maven-${MAVEN_WRAPPER_VERSION}"* "$MAVEN_HOME" 2>/dev/null || true
fi

exec "$MAVEN_BIN" "$@"
