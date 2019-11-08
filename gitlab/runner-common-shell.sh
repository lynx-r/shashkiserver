sudo gitlab-runner register \
  --non-interactive \
  --url "https://gitlab.shashki.online/" \
  --registration-token "$TOKEN" \
  --executor "shell" \
  --description "shell $NAME" \
  --tag-list "shell,$NAME" \
  --run-untagged \
  --locked="false"
