## Local docker-compose stack
### Prerequisites
- setup `auth.bank.lukgaw.com` domain to resolve to `127.0.0.1`
  - Linux: edit `/etc/hosts` file
  - Windows: TODO
- setup `accounts.bank.lukgaw.com` domain to resolve to `127.0.0.1`
  - Linux edit `/etc/hosts` file
  - Windows TODO
- docker engine installed (https://docs.docker.com/engine/install/)
- docker-compose tool installed (https://docs.docker.com/compose/install/)
  
### Setup local stack
- navigate to `${GIT_REPO}/local-stack`
- run `docker-compose up -d`
- verify `docker-compose ps` 
- keycloak-init's state should be `Exit 0` after a while
