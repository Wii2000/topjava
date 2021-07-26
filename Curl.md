# Curl requests
## Get all meals for user
    curl 'http://localhost:8080/topjava/rest/meals' | json_pp -json_opt pretty,utf8
## Creat meal
    curl -X POST -H 'Content-Type: application/json' -d '{"dateTime": "2020-02-01T18:00:00", "description": "Созданный ужин", "calories": 500}' 'http://localhost:8080/topjava/rest/meals' | json_pp -json_opt pretty,utf8
## Get filtered meals by date and time
    curl 'http://localhost:8080/topjava/rest/meals/filter?startDate=&startTime=&endDate=&endTime=18%3A00' | json_pp -json_opt pretty,utf8
## Delete meal
    curl -X DELETE 'http://localhost:8080/topjava/rest/meals/100008'
## Update meal
    curl -X PUT -H 'Content-Type: application/json' -d '{"id": 100002, "dateTime": "2020-01-30T08:00:00", "description": "Обновленный завтрак", "calories": 650}' 'http://localhost:8080/topjava/rest/meals/100002'
