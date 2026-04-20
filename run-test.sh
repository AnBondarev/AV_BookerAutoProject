#!/bin/bash

echo "Starting Java tests build..."

cd "$(dirname "$0")"

if [ ! -x gradlew ]; then
    echo "Making gradlew executable..."
    chmod +x gradlew
fi

echo "Cleaning previous builds..."
./gradlew clean

echo "Running tests..."
# Запускаем тесты и запоминаем код возврата
./gradlew test
TEST_EXIT_CODE=$?

# Сохраняем allure-results (они уже должны быть сгенерированы Gradle)
echo "Allure results saved to: allure-results/"

# Возвращаем тот же код, который вернул gradlew, чтобы Jenkins его увидел
exit $TEST_EXIT_CODE