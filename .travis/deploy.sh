#!/bin/bash
set -ev
if [ "${TRAVIS_PULL_REQUEST}" = "false" ]; then
  ./gradlew assembleRelease \
     -Pandroid.injected.signing.store.file=.credentials/key.jks \
     -Pandroid.injected.signing.store.password=$STORE_PASSWORD \
     -Pandroid.injected.signing.key.alias=migrate \
     -Pandroid.injected.signing.key.password=$STORE_PASSWORD
  github-release release \
    --user dankideacentral \
    --repo migrate-app \
    --tag v$(cat .version) \
    --name $(cat .version) \
    --description "Built by your friendly neighbourhood Travis CI"
  github-release upload  \
    --user dankideacentral \
    --repo migrate-app  \
    --tag v$(cat .version) \
    --name "migrate-release-$(date +%Y-%m-%d)-v$(cat .version).apk" \
    --file "app/build/outputs/apk/app-release.apk"
fi
