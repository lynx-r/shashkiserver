sudo gitlab-runner register \
  --non-interactive \
  --url "https://gitlab.shashki.online/" \
  --registration-token "$TOKEN" \
  --executor "docker" \
  --docker-image $IMAGE \
  --description "docker-runner $NAME" \
  --tag-list "docker,$NAME" \
  --run-untagged \
  --locked="false"
