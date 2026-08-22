#!/bin/sh

##############################################################################
# Gradle startup script for POSIX
##############################################################################

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

# Use the maximum available heap size for optimal performance
GRADLE_OPTS="${GRADLE_OPTS:-}"

# Find the gradle wrapper jar
GRADLE_WRAPPER_JAR="gradle/wrapper/gradle-wrapper.jar"

# Resolve links: $0 may be a link
app_path="${0}"
while [ -h "${app_path}" ]; do
    ls="$(ls -ld "${app_path}")"
    link="$(expr "${ls}" : '.*-> \(.*\)$')"
    if expr "${link}" : '/.*' > /dev/null; then
        app_path="${link}"
    else
        app_path="$(dirname "${app_path}")/${link}"
    fi
done

# Determine the root directory
APP_BASE_NAME="$(basename "${app_path}")"
APP_HOME="$(dirname "${app_path}")"

# Determine the Java command
if [ -n "${JAVA_HOME:-}" ] ; then
    if [ -x "${JAVA_HOME}/bin/java" ] ; then
        JAVA_CMD="${JAVA_HOME}/bin/java"
    else
        echo "ERROR: JAVA_HOME is set to an invalid directory: ${JAVA_HOME}" >&2
        exit 1
    fi
else
    JAVA_CMD="java"
    which java > /dev/null 2>&1 || { echo "ERROR: JAVA_HOME is not set and no 'java' command could be found." >&2; exit 1; }
fi

# Resolve the gradle wrapper jar path
if [ -f "${APP_HOME}/${GRADLE_WRAPPER_JAR}" ]; then
    WRAPPER_JAR="${APP_HOME}/${GRADLE_WRAPPER_JAR}"
else
    echo "ERROR: Gradle wrapper jar not found at ${APP_HOME}/${GRADLE_WRAPPER_JAR}" >&2
    exit 1
fi

# Execute Gradle
exec "${JAVA_CMD}" "${DEFAULT_JVM_OPTS}" "${GRADLE_OPTS}" \
    -classpath "${WRAPPER_JAR}" \
    org.gradle.wrapper.GradleWrapperMain \
    "$@"
