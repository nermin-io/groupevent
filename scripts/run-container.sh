docker run --rm --name groupevent-api  --env-file Docker.env --network=psql_db -p 8080:8080 groupevent:v2
