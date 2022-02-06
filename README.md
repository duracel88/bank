### Bank app
#### Local test stack
![diagram](https://github.com/duracel88/bank/blob/master/bank-diagram.0.1.jpg?raw=true)

go here https://github.com/duracel88/bank/tree/master/local-stack
#### Run in IDE
- navigate to local-stack `$ cd local-stack` 
- setup keycloak in order to authorize REST http calls
```
$ docker-compose up -d keycloak keycloak-init gateway
# test http://localhost/auth if Keycloak is running and is accesible throgh the gateway 
```
- run accounts-service and/or tx-service with `local` profile
- access services by their opened ports (8081/8082, defined in `application-local.yml` profile file)
```
$ curl http://localhost:8081/api \
    -H "Authorization: Bearer $(./get_token.sh chuck.norris)" \
    -H 'Content-type: application/json' \
    -X POST \
    -d '{
        "firstName":"XXX", 
        "lastName":"GGG", 
        "currency":"PLN", 
        "firstDepositAmount": 15.13
        }'
```
