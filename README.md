<b>Taiga And GitHub Intergration</b><br> Date 10/09/2024<br>

Step 1 : Payload URL from taiga<br>
Step 2 : Create Repo(springTaiga)<br>
Step 3 : Repo, springTaiga -> Settings -> Webhooks -> add the Payload URL * & Secret key /<br>
Which events would you like to trigger this webhook?<br>
Send me everything.

https://api.taiga.io/api/v1/github-hook (all events) // Everything Fine it show green Tick<br>


<br>---------------------------------------------------------------------------------------<br>
Date 11-09-2024<br>

taiga-integration.yml<br>
Created a directory in https://github.com/varghese25/springTaiga<br>
.github/workflows/<br>
Create a file inside that folder (ie. springTaiga/.github/workflows/) naming<br>
Or else simple put the uploaded file (taiga-integration.yml) into folder<br>

<br>---------------------------------------------------------------------------------------<br>


Date 12-09-2024<br>
Work Flow Interagation<br>

 
 
 Step 1 <br>
 Create work flow in github .github/workflows/taiga-integration.yml<br>
 
 Step 2<br>
 name: Taiga Integration<br>

on:<br>
 push:<br>
   branches:<br>
     - main<br>
 pull_request:<br>
   branches:<br>
     - main<br>

jobs:<br>
 update-taiga:<br>
   runs-on: ubuntu-latest<br>

   steps:<br>
   - name: Checkout code<br>
     uses: actions/checkout@v2<br>

   - name: Send updates to Taiga<br>
     env:<br>
       TAIGA_API_URL: "https://api.taiga.io" #  Hardcoding the API URL <br>
       TAIGA_AUTH_TOKEN: ${{ secrets.TAIGA_AUTH_TOKEN}}  # Secret reference for the auth token<br>
     run: |<br>
       curl -X POST "$TAIGA_API_URL/github-hook?project=1585351" \<br>
       -H "Authorization: Bearer $TAIGA_AUTH_TOKEN" \<br>
       -H "Content-Type: application/json" \<br>
       --data '{<br>
         "repository": {<br>
           "name": "'"$GITHUB_REPOSITORY"'",<br>
           "url": "'"$GITHUB_SERVER_URL/$GITHUB_REPOSITORY"'"<br>
         },<br>
         "action": "update"<br>
       }'<br>
 
 
 
 Step 3<br>
 Referencing the Token as a Secret<br>
 
 The TAIGA_AUTH_TOKEN should be stored securely as a secret in GitHub.<br>
 You can add this secret by going to your repository’s Settings > Secrets and variables > Actions, and create a secret <br>called TAIGA_AUTH_TOKEN with the value being your authentication token (afb50e1cbf8b4a228851710cc630ea57)....<br>


<br>---------------------------------------------------------------------------------------<br>

17-09-2024<br>

Installled miniKube <br>
install latest kubernetes/ kubectl <br>
Minikube start<br>
Minikube stop<br>
 Kubectl get svc<br>
Kubectl get pods<br>

<br>----<br>
Importatnt docker command<br> 
docker pull, <br>
docker run,<br>
docker exec,<br>
docker logs,<br>
docker build,<br>
docker tag,<br>
docker push,<br>

docker images,<br>
docker ps,<br>
docker system prune<br>


<br>----------------------------------------------------<br>
## Date 20-09-2024
## Make Image as Pod & Mutliple Replica's Command
Open 2 terminals:
Terminal-1:
===========
> minikube start #Kub8 Command#
(it will take long time to create a node)
-----
# Make 2 replicas of rmanojcse06/spring-boot-hello:0.0.1, 
# without making image to container (docker run -d -n <NAME> -p <LOC_PORT>:18080 <IMAGE>
# We are using kubectl to make 2 containers

> kubectl create deployment rmanoj-spring-deployment-pod --image=rmanojcse06/spring-boot-hello:0.0.1 --port=18080 --replicas=2 -o yaml #Kub8 Command#

> kubectl get pods -o wide --show-labels #Kub8 Command#
C:\Users\lenovo>kubectl get pods --show-labels -o wide
NAME                                           READY   STATUS    RESTARTS   AGE   IP            NODE       NOMINATED NODE   READINESS GATES   LABELS
rmanoj-spring-deployment-pod-5477d68b5-f64cl   1/1     Running   0          70s   10.244.0.14   minikube   <none>           <none>            app=rmanoj-spring-deployment-pod,pod-template-hash=5477d68b5
rmanoj-spring-deployment-pod-5477d68b5-rw5w4   1/1     Running   0          70s   10.244.0.15   minikube   <none>           <none>            app=rmanoj-spring-deployment-pod,pod-template-hash=5477d68b5


# Make a curl pod (a Linux machine having curl inside minikube cluster) and try checking replica

C:\Users\lenovo>kubectl run curlpod --image=curlimages/curl -i --tty -- sh
If you don't see a command prompt, try pressing enter.
~ $ curl 10.244.0.14:18080
{"timestamp":"2024-09-18T03:28:20.619+00:00","status":404,"error":"Not Found","path":"/"}~ $
~ $ curl 10.244.0.14:18080/hello
Hello created by Manoj and Tiju~ $
~ $ curl 10.244.0.15:18080/hello
Hello created by Manoj and Tiju~ $
~ $ exit
Session ended, resume using 'kubectl attach curlpod -c curlpod -i -t' command when the pod is running

C:\Users\lenovo>kubectl get pods
NAME                                           READY   STATUS    RESTARTS      AGE
curlpod                                        1/1     Running   1 (10s ago)   57s
rmanoj-spring-deployment-pod-5477d68b5-f64cl   1/1     Running   0             4m14s
rmanoj-spring-deployment-pod-5477d68b5-rw5w4   1/1     Running   0             4m14s

Now create service for 2 replicas
> kubectl create service nodeport rmanoj-spring-service --tcp=30000:18080 --dry-run=client -o yaml > service.yaml  
#edit (C:\Users\u\Varghese Baby)service.yaml to point the pod having label
spec>selector>app: rmanoj-spring-deployment-pod #

> kubectl apply -f service.yaml

C:\Users\lenovo>kubectl get svc
NAME                    TYPE        CLUSTER-IP     EXTERNAL-IP   PORT(S)           AGE
kubernetes              ClusterIP   10.96.0.1      <none>        443/TCP           10h
rmanoj-spring-service   NodePort    10.110.92.29   <none>        30000:32321/TCP   12s

C:\Users\lenovo>kubectl attach curlpod -c curlpod -i -t
If you don't see a command prompt, try pressing enter.
~ $ curl 10.110.92.29:30000/hello
Hello created by Manoj and Tiju~ $
~ $ exit

Now 2 pods are running inside a cluster, 1 service exposes two pods
But you cannot open it in your local


Open 2 Terminal 2
To do Open another Terminal
kubectl port-forward svc/rmanoj-spring-service 9090:30000


Now goto Browser
http://localhost:9090/hello
will run the pod inside cluster


you can check the logs for running containers also
> kubectl get pods
> kubectl logs rmanoj-spring-deployment-pod-5477d68b5-f64cl






C:\Users\u>kubectl get pods
NAME                                            READY   STATUS      RESTARTS      AGE
curlpod                                         0/1     Completed   7 (12m ago)   78m
rmanoj-spring-deployment-pod-76cdff6666-447rl   1/1     Running     0             129m
rmanoj-spring-deployment-pod-76cdff6666-m7wng   1/1     Running     0             129m

C:\Users\u>kubectl delete pod rmanoj-spring-deployment-pod-76cdff6666-447rl
pod "rmanoj-spring-deployment-pod-76cdff6666-447rl" deleted

C:\Users\u>kubectl get pods
NAME                                            READY   STATUS              RESTARTS      AGE
curlpod                                         1/1     Running             8 (65s ago)   79m
rmanoj-spring-deployment-pod-76cdff6666-m7wng   1/1     Running             0             130m
rmanoj-spring-deployment-pod-76cdff6666-tf2wv   0/1     ContainerCreating   0             18s

C:\Users\u>kubectl get pods
NAME                                            READY   STATUS    RESTARTS      AGE
curlpod                                         1/1     Running   8 (13m ago)   92m
rmanoj-spring-deployment-pod-76cdff6666-m7wng   1/1     Running   0             142m
rmanoj-spring-deployment-pod-76cdff6666-tf2wv   1/1     Running   0             12m

C:\Users\u>kubectl get deployment
NAME                           READY   UP-TO-DATE   AVAILABLE   AGE
rmanoj-spring-deployment-pod   2/2     2            2           143m

C:\Users\u>kubectl scale deployment --replicas=4
error: resource(s) were provided, but no name was specified

C:\Users\u>kubectl scale deployment --replicas=4 rmanoj-spring-deployment-pod
deployment.apps/rmanoj-spring-deployment-pod scaled

C:\Users\u>kubectl get pods
NAME                                            READY   STATUS              RESTARTS      AGE
curlpod                                         1/1     Running             8 (15m ago)   94m
rmanoj-spring-deployment-pod-76cdff6666-88rxf   1/1     Running             0             18s
rmanoj-spring-deployment-pod-76cdff6666-m7wng   1/1     Running             0             144m
rmanoj-spring-deployment-pod-76cdff6666-tf2wv   1/1     Running             0             14m
rmanoj-spring-deployment-pod-76cdff6666-zr2lz   0/1     ContainerCreating   0             18s

C:\Users\u>kubectl get pods
<br>-------------------------------------------------------------<br>


