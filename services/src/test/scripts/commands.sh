#!/bin/bash
curl -X POST --data '{"name":"fireeye","path":"/Volumes/data/projects/github/code_quality/fireeye"}' -H "Content-Type: application/json" localhost:9000/projects/create/fireeye
#!/bin/bash
curl -X POST --data '{"username":"wei.chen.mr@gmail.com", "password":"Iam1gitter", "gitRepo":"https://github.com/jumpingstone/jpress.git"}' -H "Content-Type: application/json" localhost:9000/projects/clone