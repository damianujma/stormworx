curl --location --request POST 'localhost:8080/alarm-conditions/sendPredictions' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "damianujma.kontakt@gmail.com"
}'
curl --location --request POST 'localhost:8080/alarm-conditions/sendPredictions' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "$SECRET_EMAIL"
}'