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

## Installing Curl Inside the pod

Kuber8 Commands



Microsoft Windows [Version 10.0.19045.4894]
(c) Microsoft Corporation. All rights reserved.

C:\Users\u>kubectl get pods -o wide --show-labels
NAME                                            READY   STATUS              RESTARTS   AGE    IP       NODE       NOMINATED NODE   READINESS GATES   LABELS
rmanoj-spring-deployment-pod-76cdff6666-447rl   0/1     ContainerCreating   0          2m1s   <none>   minikube   <none>           <none>            app=rmanoj-spring-deployment-pod,pod-template-hash=76cdff6666
rmanoj-spring-deployment-pod-76cdff6666-m7wng   0/1     ContainerCreating   0          2m1s   <none>   minikube   <none>           <none>            app=rmanoj-spring-deployment-pod,pod-template-hash=76cdff6666

C:\Users\u>kubectl get pods -o wide --show-labels
NAME                                            READY   STATUS              RESTARTS   AGE     IP       NODE       NOMINATED NODE   READINESS GATES   LABELS
rmanoj-spring-deployment-pod-76cdff6666-447rl   0/1     ContainerCreating   0          2m39s   <none>   minikube   <none>           <none>            app=rmanoj-spring-deployment-pod,pod-template-hash=76cdff6666
rmanoj-spring-deployment-pod-76cdff6666-m7wng   0/1     ContainerCreating   0          2m39s   <none>   minikube   <none>           <none>            app=rmanoj-spring-deployment-pod,pod-template-hash=76cdff6666

C:\Users\u>kubectl log
error: unknown command "log" for "kubectl"

Did you mean this?
        top
        logs

C:\Users\u>kubectl logs rmanoj-spring-deployment-pod-76cdff6666-m7wng
Error from server (BadRequest): container "spring-boot-hello" in pod "rmanoj-spring-deployment-pod-76cdff6666-m7wng" is waiting to start: ContainerCreating

C:\Users\u>kubectl describe pod rmanoj-spring-deployment-pod-76cdff6666-m7wng
Name:             rmanoj-spring-deployment-pod-76cdff6666-m7wng
Namespace:        default
Priority:         0
Service Account:  default
Node:             minikube/192.168.49.2
Start Time:       Fri, 20 Sep 2024 12:14:10 -0400
Labels:           app=rmanoj-spring-deployment-pod
                  pod-template-hash=76cdff6666
Annotations:      <none>
Status:           Pending
IP:
IPs:              <none>
Controlled By:    ReplicaSet/rmanoj-spring-deployment-pod-76cdff6666
Containers:
  spring-boot-hello:
    Container ID:
    Image:          rmanojcse06/spring-boot-hello:0.0.1
    Image ID:
    Port:           18080/TCP
3f2c9b4652b6: Downloading [=========>                                         ]  36.01MB/187.5MB
    State:          Waiting
3f2c9b4652b6: Downloading [===============================>                   ]  119.5MB/187.5MB
3f2c9b4652b6: Downloading [========>                                          ]  33.32MB/187.5MB
    Restart Count:  0
    Environment:    <none>
3f2c9b4652b6: Downloading [========>                                          ]  32.78MB/187.5MB
      /var/run/secrets/kubernetes.io/serviceaccount from kube-api-access-pkkrs (ro)
Conditions:
3f2c9b4652b6: Downloading [========>                                          ]  32.25MB/187.5MB
  PodReadyToStartContainers   False
3f2c9b4652b6: Downloading [=================================>                 ]  123.8MB/187.5MB
3f2c9b4652b6: Downloading [========>                                          ]  31.71MB/187.5MB
  ContainersReady             False
  PodScheduled                True
Volumes:
  kube-api-access-pkkrs:
3f2c9b4652b6: Pull complete
11849ef6ba8a: Pull complete
975de163316e: Pull complete
Digest: sha256:d670fa6d69cc937db8fde557fbd05a02e607723ea9bbc808b39a98fbb51e7995
Status: Downloaded newer image for rmanojcse06/spring-boot-hello:0.0.1
docker.io/rmanojcse06/spring-boot-hello:0.0.1
Node-Selectors:              <none>
What's next:                 node.kubernetes.io/not-ready:NoExecute op=Exists for 300s
    View a summary of image vulnerabilities and recommendations → docker scout quickview rmanojcse06/spring-boot-hello:0.0.1
Events:
C:\Users\u>6: Downloading [========>                                          ]  31.17MB/187.5MB
C:\Users\u>-----     ----   ----               -------
C:\Users\u>cheduled  4m42s  default-scheduler  Successfully assigned default/rmanoj-spring-deployment-pod-76cdff6666-m7wng to minikube
C:\Users\u>ulling    4m27s  kubelet            Pulling image "rmanojcse06/spring-boot-hello:0.0.1"
C:\Users\u>
C:\Users\u>docker pull rmanojcse06/spring-boot-hello:0.0.1
C:\Users\u>ing from rmanojcse06/spring-boot-hello
C:\Users\u>3: Pull complete
C:\Users\u>1: Pull complete
C:\Users\u>6: Downloading [========>                                          ]  30.63MB/187.5MB
C:\Users\u>a: Download complete
C:\Users\u>e: Download complete
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>
C:\Users\u>docker pull rmanojcse06/spring-boot-hello:0.0.1
0.0.1: Pulling from rmanojcse06/spring-boot-hello
Digest: sha256:d670fa6d69cc937db8fde557fbd05a02e607723ea9bbc808b39a98fbb51e7995
Status: Image is up to date for rmanojcse06/spring-boot-hello:0.0.1
docker.io/rmanojcse06/spring-boot-hello:0.0.1

What's next:
    View a summary of image vulnerabilities and recommendations → docker scout quickview rmanojcse06/spring-boot-hello:0.0.1

C:\Users\u>kubectl get pods
NAME                                            READY   STATUS    RESTARTS   AGE
rmanoj-spring-deployment-pod-76cdff6666-447rl   1/1     Running   0          18m
rmanoj-spring-deployment-pod-76cdff6666-m7wng   1/1     Running   0          18m

C:\Users\u>kubectl exec -it rmanoj-spring-deployment-pod-76cdff6666-447rl --sh
error: unknown flag: --sh
See 'kubectl exec --help' for usage.

C:\Users\u>kubectl exec -it rmanoj-spring-deployment-pod-76cdff6666-447rl -- /bin/sh
# curl localhost:18080/hello
/bin/sh: 1: curl: not found
# apk update
apk add curl
/bin/sh: 2: apk: not found
# /bin/sh: 3: apk: not found
# apk update
apk add curl
/bin/sh: 4: apk: not found
# /bin/sh: 5: apk: not found
# cat /etc/alpine-release
cat: /etc/alpine-release: No such file or directory
# uname - a
uname: extra operand ‘-’
Try 'uname --help' for more information.
# uname -a
Linux rmanoj-spring-deployment-pod-76cdff6666-447rl 5.15.153.1-microsoft-standard-WSL2 #1 SMP Fri Mar 29 23:14:13 UTC 2024 x86_64 GNU/Linux
# sudo apt update
sudo apt install curl
/bin/sh: 9: sudo: not found
# /bin/sh: 10: sudo: not found
# apt update
Get:1 http://security.debian.org/debian-security buster/updates InRelease [34.8 kB]
Get:2 http://deb.debian.org/debian buster InRelease [122 kB]
Get:3 http://deb.debian.org/debian buster-updates InRelease [56.6 kB]
Get:4 http://security.debian.org/debian-security buster/updates/main amd64 Packages [610 kB]
Get:5 http://deb.debian.org/debian buster/main amd64 Packages [7909 kB]
Get:6 http://deb.debian.org/debian buster-updates/main amd64 Packages [8788 B]
Fetched 8741 kB in 15s (569 kB/s)
Reading package lists... Done
Building dependency tree
Reading state information... Done
33 packages can be upgraded. Run 'apt list --upgradable' to see them.
# apt install curl
Reading package lists... Done
Building dependency tree
Reading state information... Done
The following additional packages will be installed:
  krb5-locales libcurl4 libgssapi-krb5-2 libk5crypto3 libkeyutils1 libkrb5-3 libkrb5support0 libldap-2.4-2 libldap-common libnghttp2-14 libpsl5 librtmp1 libsasl2-2
  libsasl2-modules libsasl2-modules-db libssh2-1 publicsuffix
Suggested packages:
  krb5-doc krb5-user libsasl2-modules-gssapi-mit | libsasl2-modules-gssapi-heimdal libsasl2-modules-ldap libsasl2-modules-otp libsasl2-modules-sql
The following NEW packages will be installed:
  curl krb5-locales libcurl4 libgssapi-krb5-2 libk5crypto3 libkeyutils1 libkrb5-3 libkrb5support0 libldap-2.4-2 libldap-common libnghttp2-14 libpsl5 librtmp1
  libsasl2-2 libsasl2-modules libsasl2-modules-db libssh2-1 publicsuffix
0 upgraded, 18 newly installed, 0 to remove and 33 not upgraded.
Need to get 2492 kB of archives.
After this operation, 5887 kB of additional disk space will be used.
Do you want to continue? [Y/n] Y
Get:1 http://security.debian.org/debian-security buster/updates/main amd64 krb5-locales all 1.17-3+deb10u6 [95.8 kB]
Get:2 http://security.debian.org/debian-security buster/updates/main amd64 libkrb5support0 amd64 1.17-3+deb10u6 [66.1 kB]
Get:3 http://security.debian.org/debian-security buster/updates/main amd64 libk5crypto3 amd64 1.17-3+deb10u6 [122 kB]
Get:4 http://deb.debian.org/debian buster/main amd64 libkeyutils1 amd64 1.6-6 [15.0 kB]
Get:5 http://security.debian.org/debian-security buster/updates/main amd64 libkrb5-3 amd64 1.17-3+deb10u6 [369 kB]
Get:6 http://deb.debian.org/debian buster/main amd64 libsasl2-modules-db amd64 2.1.27+dfsg-1+deb10u2 [69.2 kB]
Get:7 http://deb.debian.org/debian buster/main amd64 libsasl2-2 amd64 2.1.27+dfsg-1+deb10u2 [106 kB]
Get:8 http://security.debian.org/debian-security buster/updates/main amd64 libgssapi-krb5-2 amd64 1.17-3+deb10u6 [159 kB]
Get:9 http://deb.debian.org/debian buster/main amd64 libldap-common all 2.4.47+dfsg-3+deb10u7 [90.1 kB]
Get:10 http://deb.debian.org/debian buster/main amd64 libldap-2.4-2 amd64 2.4.47+dfsg-3+deb10u7 [224 kB]
Get:11 http://security.debian.org/debian-security buster/updates/main amd64 libnghttp2-14 amd64 1.36.0-2+deb10u3 [86.1 kB]
Get:12 http://security.debian.org/debian-security buster/updates/main amd64 libssh2-1 amd64 1.8.0-2.1+deb10u1 [141 kB]
Get:13 http://security.debian.org/debian-security buster/updates/main amd64 libcurl4 amd64 7.64.0-4+deb10u9 [336 kB]
Get:14 http://deb.debian.org/debian buster/main amd64 libpsl5 amd64 0.20.2-2 [53.7 kB]
Get:15 http://deb.debian.org/debian buster/main amd64 librtmp1 amd64 2.4+20151223.gitfa8646d.1-2 [60.5 kB]
Get:16 http://deb.debian.org/debian buster/main amd64 libsasl2-modules amd64 2.1.27+dfsg-1+deb10u2 [104 kB]
Get:17 http://deb.debian.org/debian buster/main amd64 publicsuffix all 20220811.1734-0+deb10u1 [127 kB]
Get:18 http://security.debian.org/debian-security buster/updates/main amd64 curl amd64 7.64.0-4+deb10u9 [267 kB]
Fetched 2492 kB in 3s (750 kB/s)
debconf: delaying package configuration, since apt-utils is not installed
Selecting previously unselected package krb5-locales.
(Reading database ... 6891 files and directories currently installed.)
Preparing to unpack .../00-krb5-locales_1.17-3+deb10u6_all.deb ...
Unpacking krb5-locales (1.17-3+deb10u6) ...
Selecting previously unselected package libkeyutils1:amd64.
Preparing to unpack .../01-libkeyutils1_1.6-6_amd64.deb ...
Unpacking libkeyutils1:amd64 (1.6-6) ...
Selecting previously unselected package libkrb5support0:amd64.
Preparing to unpack .../02-libkrb5support0_1.17-3+deb10u6_amd64.deb ...
Unpacking libkrb5support0:amd64 (1.17-3+deb10u6) ...
Selecting previously unselected package libk5crypto3:amd64.
Preparing to unpack .../03-libk5crypto3_1.17-3+deb10u6_amd64.deb ...
Unpacking libk5crypto3:amd64 (1.17-3+deb10u6) ...
Selecting previously unselected package libkrb5-3:amd64.
Preparing to unpack .../04-libkrb5-3_1.17-3+deb10u6_amd64.deb ...
Unpacking libkrb5-3:amd64 (1.17-3+deb10u6) ...
Selecting previously unselected package libgssapi-krb5-2:amd64.
Preparing to unpack .../05-libgssapi-krb5-2_1.17-3+deb10u6_amd64.deb ...
Unpacking libgssapi-krb5-2:amd64 (1.17-3+deb10u6) ...
Selecting previously unselected package libsasl2-modules-db:amd64.
Preparing to unpack .../06-libsasl2-modules-db_2.1.27+dfsg-1+deb10u2_amd64.deb ...
Unpacking libsasl2-modules-db:amd64 (2.1.27+dfsg-1+deb10u2) ...
Selecting previously unselected package libsasl2-2:amd64.
Preparing to unpack .../07-libsasl2-2_2.1.27+dfsg-1+deb10u2_amd64.deb ...
Unpacking libsasl2-2:amd64 (2.1.27+dfsg-1+deb10u2) ...
Selecting previously unselected package libldap-common.
Preparing to unpack .../08-libldap-common_2.4.47+dfsg-3+deb10u7_all.deb ...
Unpacking libldap-common (2.4.47+dfsg-3+deb10u7) ...
Selecting previously unselected package libldap-2.4-2:amd64.
Preparing to unpack .../09-libldap-2.4-2_2.4.47+dfsg-3+deb10u7_amd64.deb ...
Unpacking libldap-2.4-2:amd64 (2.4.47+dfsg-3+deb10u7) ...
Selecting previously unselected package libnghttp2-14:amd64.
Preparing to unpack .../10-libnghttp2-14_1.36.0-2+deb10u3_amd64.deb ...
Unpacking libnghttp2-14:amd64 (1.36.0-2+deb10u3) ...
Selecting previously unselected package libpsl5:amd64.
Preparing to unpack .../11-libpsl5_0.20.2-2_amd64.deb ...
Unpacking libpsl5:amd64 (0.20.2-2) ...
Selecting previously unselected package librtmp1:amd64.
Preparing to unpack .../12-librtmp1_2.4+20151223.gitfa8646d.1-2_amd64.deb ...
Unpacking librtmp1:amd64 (2.4+20151223.gitfa8646d.1-2) ...
Selecting previously unselected package libssh2-1:amd64.
Preparing to unpack .../13-libssh2-1_1.8.0-2.1+deb10u1_amd64.deb ...
Unpacking libssh2-1:amd64 (1.8.0-2.1+deb10u1) ...
Selecting previously unselected package libcurl4:amd64.
Preparing to unpack .../14-libcurl4_7.64.0-4+deb10u9_amd64.deb ...
Unpacking libcurl4:amd64 (7.64.0-4+deb10u9) ...
Selecting previously unselected package curl.
Preparing to unpack .../15-curl_7.64.0-4+deb10u9_amd64.deb ...
Unpacking curl (7.64.0-4+deb10u9) ...
Selecting previously unselected package libsasl2-modules:amd64.
Preparing to unpack .../16-libsasl2-modules_2.1.27+dfsg-1+deb10u2_amd64.deb ...
Unpacking libsasl2-modules:amd64 (2.1.27+dfsg-1+deb10u2) ...
Selecting previously unselected package publicsuffix.
Preparing to unpack .../17-publicsuffix_20220811.1734-0+deb10u1_all.deb ...
Unpacking publicsuffix (20220811.1734-0+deb10u1) ...
Setting up libkeyutils1:amd64 (1.6-6) ...
Setting up libpsl5:amd64 (0.20.2-2) ...
Setting up libsasl2-modules:amd64 (2.1.27+dfsg-1+deb10u2) ...
Setting up libnghttp2-14:amd64 (1.36.0-2+deb10u3) ...
Setting up krb5-locales (1.17-3+deb10u6) ...
Setting up libldap-common (2.4.47+dfsg-3+deb10u7) ...
Setting up libkrb5support0:amd64 (1.17-3+deb10u6) ...
Setting up libsasl2-modules-db:amd64 (2.1.27+dfsg-1+deb10u2) ...
Setting up librtmp1:amd64 (2.4+20151223.gitfa8646d.1-2) ...
Setting up libk5crypto3:amd64 (1.17-3+deb10u6) ...
Setting up libsasl2-2:amd64 (2.1.27+dfsg-1+deb10u2) ...
Setting up libssh2-1:amd64 (1.8.0-2.1+deb10u1) ...
Setting up libkrb5-3:amd64 (1.17-3+deb10u6) ...
Setting up publicsuffix (20220811.1734-0+deb10u1) ...
Setting up libldap-2.4-2:amd64 (2.4.47+dfsg-3+deb10u7) ...
Setting up libgssapi-krb5-2:amd64 (1.17-3+deb10u6) ...
Setting up libcurl4:amd64 (7.64.0-4+deb10u9) ...
Setting up curl (7.64.0-4+deb10u9) ...
Processing triggers for libc-bin (2.28-10) ...
# curl localhost:18080/hello
Hello created by Manoj and Tiju# exit

C:\Users\u>kubectl get pods
NAME                                            READY   STATUS    RESTARTS   AGE
rmanoj-spring-deployment-pod-76cdff6666-447rl   1/1     Running   0          31m
rmanoj-spring-deployment-pod-76cdff6666-m7wng   1/1     Running   0          31m

C:\Users\u>kubectl exec -it rmanoj-spring-deployment-pod-76cdff6666-m7wng -- /bin/sh
# apt update && apt install curl
Get:1 http://deb.debian.org/debian buster InRelease [122 kB]
Get:2 http://deb.debian.org/debian buster-updates InRelease [56.6 kB]
Get:3 http://security.debian.org/debian-security buster/updates InRelease [34.8 kB]
Get:4 http://deb.debian.org/debian buster/main amd64 Packages [7909 kB]
Get:5 http://security.debian.org/debian-security buster/updates/main amd64 Packages [610 kB]
Get:6 http://deb.debian.org/debian buster-updates/main amd64 Packages [8788 B]
Fetched 8741 kB in 15s (574 kB/s)
Reading package lists... Done
Building dependency tree
Reading state information... Done
33 packages can be upgraded. Run 'apt list --upgradable' to see them.
Reading package lists... Done
Building dependency tree
Reading state information... Done
The following additional packages will be installed:
  krb5-locales libcurl4 libgssapi-krb5-2 libk5crypto3 libkeyutils1 libkrb5-3 libkrb5support0 libldap-2.4-2 libldap-common libnghttp2-14 libpsl5 librtmp1 libsasl2-2
  libsasl2-modules libsasl2-modules-db libssh2-1 publicsuffix
Suggested packages:
  krb5-doc krb5-user libsasl2-modules-gssapi-mit | libsasl2-modules-gssapi-heimdal libsasl2-modules-ldap libsasl2-modules-otp libsasl2-modules-sql
The following NEW packages will be installed:
  curl krb5-locales libcurl4 libgssapi-krb5-2 libk5crypto3 libkeyutils1 libkrb5-3 libkrb5support0 libldap-2.4-2 libldap-common libnghttp2-14 libpsl5 librtmp1
  libsasl2-2 libsasl2-modules libsasl2-modules-db libssh2-1 publicsuffix
0 upgraded, 18 newly installed, 0 to remove and 33 not upgraded.
Need to get 2492 kB of archives.
After this operation, 5887 kB of additional disk space will be used.
Do you want to continue? [Y/n] Y
Get:1 http://security.debian.org/debian-security buster/updates/main amd64 krb5-locales all 1.17-3+deb10u6 [95.8 kB]
Get:2 http://deb.debian.org/debian buster/main amd64 libkeyutils1 amd64 1.6-6 [15.0 kB]
Get:3 http://security.debian.org/debian-security buster/updates/main amd64 libkrb5support0 amd64 1.17-3+deb10u6 [66.1 kB]
Get:4 http://deb.debian.org/debian buster/main amd64 libsasl2-modules-db amd64 2.1.27+dfsg-1+deb10u2 [69.2 kB]
Get:5 http://security.debian.org/debian-security buster/updates/main amd64 libk5crypto3 amd64 1.17-3+deb10u6 [122 kB]
Get:6 http://deb.debian.org/debian buster/main amd64 libsasl2-2 amd64 2.1.27+dfsg-1+deb10u2 [106 kB]
Get:7 http://security.debian.org/debian-security buster/updates/main amd64 libkrb5-3 amd64 1.17-3+deb10u6 [369 kB]
Get:8 http://deb.debian.org/debian buster/main amd64 libldap-common all 2.4.47+dfsg-3+deb10u7 [90.1 kB]
Get:9 http://deb.debian.org/debian buster/main amd64 libldap-2.4-2 amd64 2.4.47+dfsg-3+deb10u7 [224 kB]
Get:10 http://security.debian.org/debian-security buster/updates/main amd64 libgssapi-krb5-2 amd64 1.17-3+deb10u6 [159 kB]
Get:11 http://security.debian.org/debian-security buster/updates/main amd64 libnghttp2-14 amd64 1.36.0-2+deb10u3 [86.1 kB]
Get:12 http://deb.debian.org/debian buster/main amd64 libpsl5 amd64 0.20.2-2 [53.7 kB]
Get:13 http://deb.debian.org/debian buster/main amd64 librtmp1 amd64 2.4+20151223.gitfa8646d.1-2 [60.5 kB]
Get:14 http://deb.debian.org/debian buster/main amd64 libsasl2-modules amd64 2.1.27+dfsg-1+deb10u2 [104 kB]
Get:15 http://security.debian.org/debian-security buster/updates/main amd64 libssh2-1 amd64 1.8.0-2.1+deb10u1 [141 kB]
Get:16 http://security.debian.org/debian-security buster/updates/main amd64 libcurl4 amd64 7.64.0-4+deb10u9 [336 kB]
Get:17 http://deb.debian.org/debian buster/main amd64 publicsuffix all 20220811.1734-0+deb10u1 [127 kB]
Get:18 http://security.debian.org/debian-security buster/updates/main amd64 curl amd64 7.64.0-4+deb10u9 [267 kB]
Fetched 2492 kB in 3s (857 kB/s)
debconf: delaying package configuration, since apt-utils is not installed
Selecting previously unselected package krb5-locales.
(Reading database ... 6891 files and directories currently installed.)
Preparing to unpack .../00-krb5-locales_1.17-3+deb10u6_all.deb ...
Unpacking krb5-locales (1.17-3+deb10u6) ...
Selecting previously unselected package libkeyutils1:amd64.
Preparing to unpack .../01-libkeyutils1_1.6-6_amd64.deb ...
Unpacking libkeyutils1:amd64 (1.6-6) ...
Selecting previously unselected package libkrb5support0:amd64.
Preparing to unpack .../02-libkrb5support0_1.17-3+deb10u6_amd64.deb ...
Unpacking libkrb5support0:amd64 (1.17-3+deb10u6) ...
Selecting previously unselected package libk5crypto3:amd64.
Preparing to unpack .../03-libk5crypto3_1.17-3+deb10u6_amd64.deb ...
Unpacking libk5crypto3:amd64 (1.17-3+deb10u6) ...
Selecting previously unselected package libkrb5-3:amd64.
Preparing to unpack .../04-libkrb5-3_1.17-3+deb10u6_amd64.deb ...
Unpacking libkrb5-3:amd64 (1.17-3+deb10u6) ...
Selecting previously unselected package libgssapi-krb5-2:amd64.
Preparing to unpack .../05-libgssapi-krb5-2_1.17-3+deb10u6_amd64.deb ...
Unpacking libgssapi-krb5-2:amd64 (1.17-3+deb10u6) ...
Selecting previously unselected package libsasl2-modules-db:amd64.
Preparing to unpack .../06-libsasl2-modules-db_2.1.27+dfsg-1+deb10u2_amd64.deb ...
Unpacking libsasl2-modules-db:amd64 (2.1.27+dfsg-1+deb10u2) ...
Selecting previously unselected package libsasl2-2:amd64.
Preparing to unpack .../07-libsasl2-2_2.1.27+dfsg-1+deb10u2_amd64.deb ...
Unpacking libsasl2-2:amd64 (2.1.27+dfsg-1+deb10u2) ...
Selecting previously unselected package libldap-common.
Preparing to unpack .../08-libldap-common_2.4.47+dfsg-3+deb10u7_all.deb ...
Unpacking libldap-common (2.4.47+dfsg-3+deb10u7) ...
Selecting previously unselected package libldap-2.4-2:amd64.
Preparing to unpack .../09-libldap-2.4-2_2.4.47+dfsg-3+deb10u7_amd64.deb ...
Unpacking libldap-2.4-2:amd64 (2.4.47+dfsg-3+deb10u7) ...
Selecting previously unselected package libnghttp2-14:amd64.
Preparing to unpack .../10-libnghttp2-14_1.36.0-2+deb10u3_amd64.deb ...
Unpacking libnghttp2-14:amd64 (1.36.0-2+deb10u3) ...
Selecting previously unselected package libpsl5:amd64.
Preparing to unpack .../11-libpsl5_0.20.2-2_amd64.deb ...
Unpacking libpsl5:amd64 (0.20.2-2) ...
Selecting previously unselected package librtmp1:amd64.
Preparing to unpack .../12-librtmp1_2.4+20151223.gitfa8646d.1-2_amd64.deb ...
Unpacking librtmp1:amd64 (2.4+20151223.gitfa8646d.1-2) ...
Selecting previously unselected package libssh2-1:amd64.
Preparing to unpack .../13-libssh2-1_1.8.0-2.1+deb10u1_amd64.deb ...
Unpacking libssh2-1:amd64 (1.8.0-2.1+deb10u1) ...
Selecting previously unselected package libcurl4:amd64.
Preparing to unpack .../14-libcurl4_7.64.0-4+deb10u9_amd64.deb ...
Unpacking libcurl4:amd64 (7.64.0-4+deb10u9) ...
Selecting previously unselected package curl.
Preparing to unpack .../15-curl_7.64.0-4+deb10u9_amd64.deb ...
Unpacking curl (7.64.0-4+deb10u9) ...
Selecting previously unselected package libsasl2-modules:amd64.
Preparing to unpack .../16-libsasl2-modules_2.1.27+dfsg-1+deb10u2_amd64.deb ...
Unpacking libsasl2-modules:amd64 (2.1.27+dfsg-1+deb10u2) ...
Selecting previously unselected package publicsuffix.
Preparing to unpack .../17-publicsuffix_20220811.1734-0+deb10u1_all.deb ...
Unpacking publicsuffix (20220811.1734-0+deb10u1) ...
Setting up libkeyutils1:amd64 (1.6-6) ...
Setting up libpsl5:amd64 (0.20.2-2) ...
Setting up libsasl2-modules:amd64 (2.1.27+dfsg-1+deb10u2) ...
Setting up libnghttp2-14:amd64 (1.36.0-2+deb10u3) ...
Setting up krb5-locales (1.17-3+deb10u6) ...
Setting up libldap-common (2.4.47+dfsg-3+deb10u7) ...
Setting up libkrb5support0:amd64 (1.17-3+deb10u6) ...
Setting up libsasl2-modules-db:amd64 (2.1.27+dfsg-1+deb10u2) ...
Setting up librtmp1:amd64 (2.4+20151223.gitfa8646d.1-2) ...
Setting up libk5crypto3:amd64 (1.17-3+deb10u6) ...
Setting up libsasl2-2:amd64 (2.1.27+dfsg-1+deb10u2) ...
Setting up libssh2-1:amd64 (1.8.0-2.1+deb10u1) ...
Setting up libkrb5-3:amd64 (1.17-3+deb10u6) ...
Setting up publicsuffix (20220811.1734-0+deb10u1) ...
Setting up libldap-2.4-2:amd64 (2.4.47+dfsg-3+deb10u7) ...
Setting up libgssapi-krb5-2:amd64 (1.17-3+deb10u6) ...
Setting up libcurl4:amd64 (7.64.0-4+deb10u9) ...
Setting up curl (7.64.0-4+deb10u9) ...
Processing triggers for libc-bin (2.28-10) ...
# curl localhost:18080/hello
Hello created by Manoj and Tiju# ps
/bin/sh: 3: ps: not found
# top
/bin/sh: 4: top: not found
#
<br>---------------------------------------------<br>


## Docker basic Command
PS C:\Users\u> docker search hello
NAME                    DESCRIPTION                                     STARS     OFFICIAL
nginxdemos/hello        NGINX webserver that serves a simple page co…   157
silasbw/hello                                                           0
bihero/hello            'Hello' word microservice                       0
cy4n/hello              springboot hello playground                     1
circleci/hello          This image is for internal use                  0
widdix/hello            Hello World!                                    0
kelseyhightower/hello                                                   10
johanburati/hello       Http server for testing traffic between mach…   0
raeffs/hello                                                            0
lizrice/hello           Simple examples of containerizing Golang cod…   0
cfgarden/hello                                                          0
anafomin/hello                                                          0
corticoai/hello                                                         0
monopole/hello                                                          1
mulspace/hello          Hello docker for web. It will run python web…   0
tomvo12/hello                                                           0
sigmadev/hello                                                          0
ovhplatform/hello                                                       1
jeromebaude/hello                                                       0
singatwaria/hello                                                       0
treeder/hello                                                           0
cloudecho/hello         Hello                                           0
acarmack/hello                                                          0
chiphwang/hello                                                         0
tanzufun/hello                                                          1
PS C:\Users\u> docker pull hello-world
Using default tag: latest
latest: Pulling from library/hello-world
c1ec31eb5944: Pull complete
Digest: sha256:91fb4b041da273d5a3273b6d587d62d518300a6ad268b28628f74997b93171b2
Status: Downloaded newer image for hello-world:latest
docker.io/library/hello-world:latest

What's next:
    View a summary of image vulnerabilities and recommendations → docker scout quickview hello-world
PS C:\Users\u> docker image ls
REPOSITORY                        TAG                IMAGE ID       CREATED         SIZE
rmanojcse06/spring-boot-hello     0.0.1              9d4fd7dce456   4 days ago      420MB
my-spring-boot-app                latest             1dfccde513ea   4 days ago      351MB
tiju/my-spring-boot-app           latest             1dfccde513ea   4 days ago      351MB
varghese25/my-spring-boot-app     latest             1dfccde513ea   4 days ago      351MB
yourusername/my-spring-boot-app   latest             1dfccde513ea   4 days ago      351MB
gcr.io/k8s-minikube/kicbase       v0.0.45            aeed0e1d4642   2 weeks ago     1.28GB
postgres                          latest             b781f3a53e61   6 weeks ago     432MB
postgres                          13.16-alpine3.20   ed06eaccad7e   6 weeks ago     238MB
hello-world                       latest             d2c94e258dcb   16 months ago   13.3kB
openjdk                           17-jdk-alpine      264c9bdce361   3 years ago     326MB
PS C:\Users\u> docker rmi hello-world
Untagged: hello-world:latest
Untagged: hello-world@sha256:91fb4b041da273d5a3273b6d587d62d518300a6ad268b28628f74997b93171b2
Deleted: sha256:d2c94e258dcb3c5ac2798d32e1249e42ef01cba4841c2234249495f87264ac5a
Deleted: sha256:ac28800ec8bb38d5c35b49d45a6ac4777544941199075dff8c4eb63e093aa81e
PS C:\Users\u> docker image ls
REPOSITORY                        TAG                IMAGE ID       CREATED       SIZE
rmanojcse06/spring-boot-hello     0.0.1              9d4fd7dce456   4 days ago    420MB
tiju/my-spring-boot-app           latest             1dfccde513ea   4 days ago    351MB
varghese25/my-spring-boot-app     latest             1dfccde513ea   4 days ago    351MB
yourusername/my-spring-boot-app   latest             1dfccde513ea   4 days ago    351MB
my-spring-boot-app                latest             1dfccde513ea   4 days ago    351MB
gcr.io/k8s-minikube/kicbase       v0.0.45            aeed0e1d4642   2 weeks ago   1.28GB
postgres                          latest             b781f3a53e61   6 weeks ago   432MB
postgres                          13.16-alpine3.20   ed06eaccad7e   6 weeks ago   238MB
openjdk                           17-jdk-alpine      264c9bdce361   3 years ago   326MB
PS C:\Users\u> docker pull hello-world
Using default tag: latest
latest: Pulling from library/hello-world
c1ec31eb5944: Pull complete
Digest: sha256:91fb4b041da273d5a3273b6d587d62d518300a6ad268b28628f74997b93171b2
Status: Downloaded newer image for hello-world:latest
docker.io/library/hello-world:latest

What's next:
    View a summary of image vulnerabilities and recommendations → docker scout quickview hello-world
PS C:\Users\u> docker image ls
REPOSITORY                        TAG                IMAGE ID       CREATED         SIZE
rmanojcse06/spring-boot-hello     0.0.1              9d4fd7dce456   4 days ago      420MB
my-spring-boot-app                latest             1dfccde513ea   4 days ago      351MB
tiju/my-spring-boot-app           latest             1dfccde513ea   4 days ago      351MB
varghese25/my-spring-boot-app     latest             1dfccde513ea   4 days ago      351MB
yourusername/my-spring-boot-app   latest             1dfccde513ea   4 days ago      351MB
gcr.io/k8s-minikube/kicbase       v0.0.45            aeed0e1d4642   2 weeks ago     1.28GB
postgres                          latest             b781f3a53e61   6 weeks ago     432MB
postgres                          13.16-alpine3.20   ed06eaccad7e   6 weeks ago     238MB
hello-world                       latest             d2c94e258dcb   16 months ago   13.3kB
openjdk                           17-jdk-alpine      264c9bdce361   3 years ago     326MB
PS C:\Users\u> docker run hello-world

Hello from Docker!
This message shows that your installation appears to be working correctly.

To generate this message, Docker took the following steps:
 1. The Docker client contacted the Docker daemon.
 2. The Docker daemon pulled the "hello-world" image from the Docker Hub.
    (amd64)
 3. The Docker daemon created a new container from that image which runs the
    executable that produces the output you are currently reading.
 4. The Docker daemon streamed that output to the Docker client, which sent it
    to your terminal.

To try something more ambitious, you can run an Ubuntu container with:
 $ docker run -it ubuntu bash

Share images, automate workflows, and more with a free Docker ID:
 https://hub.docker.com/

For more examples and ideas, visit:
 https://docs.docker.com/get-started/

PS C:\Users\u> docker ps -a
CONTAINER ID   IMAGE         COMMAND    CREATED          STATUS                      PORTS     NAMES
a2a3a435f387   hello-world   "/hello"   44 seconds ago   Exited (0) 39 seconds ago             relaxed_borg
PS C:\Users\u> docker run hello-world

Hello from Docker!
This message shows that your installation appears to be working correctly.

To generate this message, Docker took the following steps:
 1. The Docker client contacted the Docker daemon.
 2. The Docker daemon pulled the "hello-world" image from the Docker Hub.
    (amd64)
 3. The Docker daemon created a new container from that image which runs the
    executable that produces the output you are currently reading.
 4. The Docker daemon streamed that output to the Docker client, which sent it
    to your terminal.

To try something more ambitious, you can run an Ubuntu container with:
 $ docker run -it ubuntu bash

Share images, automate workflows, and more with a free Docker ID:
 https://hub.docker.com/

For more examples and ideas, visit:
 https://docs.docker.com/get-started/

PS C:\Users\u> docker ps -a
CONTAINER ID   IMAGE         COMMAND    CREATED              STATUS                          PORTS     NAMES
044c1cf201d4   hello-world   "/hello"   12 seconds ago       Exited (0) 8 seconds ago                  youthful_bhabha
a2a3a435f387   hello-world   "/hello"   About a minute ago   Exited (0) About a minute ago             relaxed_borg
PS C:\Users\u> docker run --name myHello hello-world

Hello from Docker!
This message shows that your installation appears to be working correctly.

To generate this message, Docker took the following steps:
 1. The Docker client contacted the Docker daemon.
 2. The Docker daemon pulled the "hello-world" image from the Docker Hub.
    (amd64)
 3. The Docker daemon created a new container from that image which runs the
    executable that produces the output you are currently reading.
 4. The Docker daemon streamed that output to the Docker client, which sent it
    to your terminal.

To try something more ambitious, you can run an Ubuntu container with:
 $ docker run -it ubuntu bash

Share images, automate workflows, and more with a free Docker ID:
 https://hub.docker.com/

For more examples and ideas, visit:
 https://docs.docker.com/get-started/

PS C:\Users\u> docker ps -a
CONTAINER ID   IMAGE         COMMAND    CREATED          STATUS                          PORTS     NAMES
cd4adf9301a4   hello-world   "/hello"   13 seconds ago   Exited (0) 9 seconds ago                  myHello
044c1cf201d4   hello-world   "/hello"   2 minutes ago    Exited (0) About a minute ago             youthful_bhabha
a2a3a435f387   hello-world   "/hello"   3 minutes ago    Exited (0) 3 minutes ago                  relaxed_borg
PS C:\Users\u> docker image ls
REPOSITORY                        TAG                IMAGE ID       CREATED         SIZE
rmanojcse06/spring-boot-hello     0.0.1              9d4fd7dce456   4 days ago      420MB
my-spring-boot-app                latest             1dfccde513ea   4 days ago      351MB
tiju/my-spring-boot-app           latest             1dfccde513ea   4 days ago      351MB
varghese25/my-spring-boot-app     latest             1dfccde513ea   4 days ago      351MB
yourusername/my-spring-boot-app   latest             1dfccde513ea   4 days ago      351MB
gcr.io/k8s-minikube/kicbase       v0.0.45            aeed0e1d4642   2 weeks ago     1.28GB
postgres                          latest             b781f3a53e61   6 weeks ago     432MB
postgres                          13.16-alpine3.20   ed06eaccad7e   6 weeks ago     238MB
hello-world                       latest             d2c94e258dcb   16 months ago   13.3kB
openjdk                           17-jdk-alpine      264c9bdce361   3 years ago     326MB
PS C:\Users\u> docker rmi hello-world
Error response from daemon: conflict: unable to remove repository reference "hello-world" (must force) - container a2a3a435f387 is using its referenced image d2c94e258dcb
PS C:\Users\u> docker rm cd4adf9301a4
cd4adf9301a4
PS C:\Users\u> docker ps
CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES
PS C:\Users\u> docker ls
docker: 'ls' is not a docker command.
See 'docker --help'
PS C:\Users\u> docker ps -a
CONTAINER ID   IMAGE         COMMAND    CREATED         STATUS                     PORTS     NAMES
044c1cf201d4   hello-world   "/hello"   4 minutes ago   Exited (0) 4 minutes ago             youthful_bhabha
a2a3a435f387   hello-world   "/hello"   6 minutes ago   Exited (0) 6 minutes ago             relaxed_borg
PS C:\Users\u> docker run --name TijuHello hello-world

Hello from Docker!
This message shows that your installation appears to be working correctly.

To generate this message, Docker took the following steps:
 1. The Docker client contacted the Docker daemon.
 2. The Docker daemon pulled the "hello-world" image from the Docker Hub.
    (amd64)
 3. The Docker daemon created a new container from that image which runs the
    executable that produces the output you are currently reading.
 4. The Docker daemon streamed that output to the Docker client, which sent it
    to your terminal.

To try something more ambitious, you can run an Ubuntu container with:
 $ docker run -it ubuntu bash

Share images, automate workflows, and more with a free Docker ID:
 https://hub.docker.com/

For more examples and ideas, visit:
 https://docs.docker.com/get-started/

PS C:\Users\u> docker ps -a
CONTAINER ID   IMAGE         COMMAND    CREATED         STATUS                     PORTS     NAMES
af2ad1b8f827   hello-world   "/hello"   9 seconds ago   Exited (0) 5 seconds ago             TijuHello
044c1cf201d4   hello-world   "/hello"   5 minutes ago   Exited (0) 5 minutes ago             youthful_bhabha
a2a3a435f387   hello-world   "/hello"   6 minutes ago   Exited (0) 6 minutes ago             relaxed_borg
PS C:\Users\u> docker rm af2ad1b8f827
af2ad1b8f827
PS C:\Users\u> docker ls
docker: 'ls' is not a docker command.
See 'docker --help'
PS C:\Users\u> docker image ls
REPOSITORY                        TAG                IMAGE ID       CREATED         SIZE
rmanojcse06/spring-boot-hello     0.0.1              9d4fd7dce456   4 days ago      420MB
my-spring-boot-app                latest             1dfccde513ea   4 days ago      351MB
tiju/my-spring-boot-app           latest             1dfccde513ea   4 days ago      351MB
varghese25/my-spring-boot-app     latest             1dfccde513ea   4 days ago      351MB
yourusername/my-spring-boot-app   latest             1dfccde513ea   4 days ago      351MB
gcr.io/k8s-minikube/kicbase       v0.0.45            aeed0e1d4642   2 weeks ago     1.28GB
postgres                          latest             b781f3a53e61   6 weeks ago     432MB
postgres                          13.16-alpine3.20   ed06eaccad7e   6 weeks ago     238MB
hello-world                       latest             d2c94e258dcb   16 months ago   13.3kB
openjdk                           17-jdk-alpine      264c9bdce361   3 years ago     326MB
PS C:\Users\u> docker container ls -a
CONTAINER ID   IMAGE         COMMAND    CREATED         STATUS                     PORTS     NAMES
044c1cf201d4   hello-world   "/hello"   6 minutes ago   Exited (0) 6 minutes ago             youthful_bhabha
a2a3a435f387   hello-world   "/hello"   7 minutes ago   Exited (0) 7 minutes ago             relaxed_borg
PS C:\Users\u> docker rmi hello-world
Error response from daemon: conflict: unable to remove repository reference "hello-world" (must force) - container 044c1cf201d4 is using its referenced image d2c94e258dcb
PS C:\Users\u> docker rm 044c1cf201d4
044c1cf201d4
PS C:\Users\u> docker container ls -a
CONTAINER ID   IMAGE         COMMAND    CREATED          STATUS                      PORTS     NAMES
a2a3a435f387   hello-world   "/hello"   10 minutes ago   Exited (0) 10 minutes ago             relaxed_borg
PS C:\Users\u> docker rm a2a3a435f387
a2a3a435f387
PS C:\Users\u> docker container ls -a
CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES
PS C:\Users\u> docker run hello-world

Hello from Docker!
This message shows that your installation appears to be working correctly.

To generate this message, Docker took the following steps:
 1. The Docker client contacted the Docker daemon.
 2. The Docker daemon pulled the "hello-world" image from the Docker Hub.
    (amd64)
 3. The Docker daemon created a new container from that image which runs the
    executable that produces the output you are currently reading.
 4. The Docker daemon streamed that output to the Docker client, which sent it
    to your terminal.

To try something more ambitious, you can run an Ubuntu container with:
 $ docker run -it ubuntu bash

Share images, automate workflows, and more with a free Docker ID:
 https://hub.docker.com/

For more examples and ideas, visit:
 https://docs.docker.com/get-started/

PS C:\Users\u> docker container ls -a
CONTAINER ID   IMAGE         COMMAND    CREATED          STATUS                     PORTS     NAMES
ae71337af281   hello-world   "/hello"   10 seconds ago   Exited (0) 6 seconds ago             gifted_kirch
PS C:\Users\u> docker ps -a
CONTAINER ID   IMAGE         COMMAND    CREATED              STATUS                          PORTS     NAMES
ae71337af281   hello-world   "/hello"   About a minute ago   Exited (0) About a minute ago             gifted_kirch
PS C:\Users\u> docker run alpine ls
Unable to find image 'alpine:latest' locally
latest: Pulling from library/alpine
43c4264eed91: Already exists
Digest: sha256:beefdbd8a1da6d2915566fde36db9db0b524eb737fc57cd1367effd16dc0d06d
Status: Downloaded newer image for alpine:latest
bin
dev
etc
home
lib
media
mnt
opt
proc
root
run
sbin
srv
sys
tmp
usr
var
PS C:\Users\u> docker run -it alphine sh
Unable to find image 'alphine:latest' locally
docker: Error response from daemon: pull access denied for alphine, repository does not exist or may require 'docker login': denied: requested access to the resource is denied.
See 'docker run --help'.
PS C:\Users\u> docker ps
CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES
PS C:\Users\u> docker run -it alphine sh
Unable to find image 'alphine:latest' locally
docker: Error response from daemon: pull access denied for alphine, repository does not exist or may require 'docker login': denied: requested access to the resource is denied.
See 'docker run --help'.
PS C:\Users\u> docker exec it ae71337af281
Error response from daemon: No such container: it
PS C:\Users\u>

<br>--------------------------------------------------------------------------<br>


                     ##Docker Command & Practise



Docker : https://www.youtube.com/watch?v=0O_FxUMCmYc


Practical introduction to docker architecture, commands, images and containers.
- docker pull
- docker run
- docker start
- docker exec
- docker stop
- docker restart



docker pull hello-world #Pull the images#

docker image ls #List the image#

docker rmi hello-world #rmi remove image hello-world#

docker run hello-world #Execute OutPut display result as: Hello from Docker! and exit from the container #

docker ps #Show running #

docker ps -a  #showa all running container Even Stopped Container#

docker run --name Varghese25 hello-world   #this used created my Own name container  as varghese25 to pulled hello-world image #

docker container ls -a (docker ps -a)

docker run hello-world #Unaavailable locally it pull from docker registry it download image and container give us the outPut#


Alpine

docker run alpine

docker image ls

docker ps -a

docker run alpine ls

docker ps -a



Open in Terminal 1
docker run -it alpine sh (it interactive sh shell commands can be writter) 
#
Open in Terminal 2
We can see the Termainal 1  --> its say that shell is running Status is  Up 2 minutes
C:\Users\u>docker ps
CONTAINER ID   IMAGE     COMMAND   CREATED         STATUS         PORTS     NAMES
b33fa8702f8c   alpine    "sh"      2 minutes ago   Up 2 minutes             confident_bell

# bin    dev    etc    home   lib    media  mnt    opt    proc   root   run    sbin   srv    sys    tmp    usr    var

# ls
/ # apk add wget
fetch https://dl-cdn.alpinelinux.org/alpine/v3.20/main/x86_64/APKINDEX.tar.gz
fetch https://dl-cdn.alpinelinux.org/alpine/v3.20/community/x86_64/APKINDEX.tar.gz
(1/4) Installing libunistring (1.2-r0)
(2/4) Installing libidn2 (2.3.7-r0)
(3/4) Installing pcre2 (10.43-r0)
(4/4) Installing wget (1.24.5-r0)
Executing busybox-1.36.1-r29.trigger
OK: 11 MiB in 18 packages
/ #exit

docker ps #once exit can't se the working container will be closed#


docker start c9f938e4888d #To start container with container Id or Name Example alphine##

Open New Termainal or Existing Terminal to check the container Working or not

docker exec -it b33fa8702f8c sh #Exec -it to run again the container#

if you stop docker running container will terminate from all terminal

docker restart b33fa8702f8c #Restart the conatiner with conatiner name#
----------------------------------------------------------------------------------------------------------


 creating images and sharing
 
 docker pull httpd
 
 
  docker run -d --name WebServerTiju -p 9090:80 httpd #port exposing 9090:80 localhost. webservers by default listen 80 port run's inside container which will forward local host 9090	 #
  
  
  PS C:\Users\u> docker run -d --name WebServerTiju -p 9090:80 httpd
  
229f96783dce643bc64650e3ae138a5324390b468a93ca00be5aedcea498f03d

PS C:\Users\u> docker ps -a
CONTAINER ID   IMAGE         COMMAND              CREATED          STATUS                        PORTS                  NAMES
229f96783dce   httpd         "httpd-foreground"   6 minutes ago    Up 6 minutes                  0.0.0.0:9090->80/tcp   WebServerTiju

Step 1: Go any browser
Step 2:  http://localhost:9090/
Step 3: Output will display As "It Works"




docker exec -it WebServerTiju bash
root@229f96783dce:/usr/local/apache2# cd htdocs/
root@229f96783dce:/usr/local/apache2/htdocs# ls
index.html
root@229f96783dce:/usr/local/apache2/htdocs#

root@229f96783dce:/usr/local/apache2/htdocs# cat index.html
<html><body><h1>It works!</h1></body></html>
root@229f96783dce:/usr/local/apache2/htdocs# cat > mypage.html
This is my Docker Page and Commands                                                       #Control D for exit fromTyping#
root@229f96783dce:/usr/local/apache2/htdocs#


OutPut 
http://localhost:9090/mypage.html


This is my Docker Page and Commands




root@229f96783dce:/usr/local/apache2/htdocs# exit
exit

What's next:
    Try Docker Debug for seamless, persistent debugging tools in any container or image → docker debug WebServerTiju
    Learn more at https://docs.docker.com/go/debug-cli/
	
	
	
PS C:\Users\u> docker ps
CONTAINER ID   IMAGE     COMMAND              CREATED          STATUS          PORTS                  NAMES
229f96783dce   httpd     "httpd-foreground"   18 minutes ago   Up 18 minutes   0.0.0.0:9090->80/tcp   WebServerTiju


---------------Add - A Chnaged -C

PS C:\Users\u> docker diff WebServerTiju
C /usr
C /usr/local
C /usr/local/apache2
C /usr/local/apache2/htdocs
A /usr/local/apache2/htdocs/mypage.html
C /usr/local/apache2/logs
A /usr/local/apache2/logs/httpd.pid
C /root
A /root/.bash_history
PS C:\Users\u>


PS C:\Users\u> docker commit WebServerTiju  (New Image Id Created)
sha256:b988aa501844a108ab58efcca1eb573fcf728ff3876f819a5ad4a64156bb7664
s

PS C:\Users\u> docker image ls
REPOSITORY                        TAG                IMAGE ID       CREATED          SIZE
<none>                            <none>             b988aa501844   18 seconds ago   148MB  #new imageId match#

PS C:\Users\u> docker ps
CONTAINER ID   IMAGE     COMMAND              CREATED          STATUS          PORTS                  NAMES
229f96783dce   httpd     "httpd-foreground"   28 minutes ago   Up 28 minutes   0.0.0.0:9090->80/tcp   WebServerTiju



PS C:\Users\u> docker ps
CONTAINER ID   IMAGE     COMMAND              CREATED          STATUS          PORTS                  NAMES
229f96783dce   httpd     "httpd-foreground"   28 minutes ago   Up 28 minutes   0.0.0.0:9090->80/tcp   WebServerTiju
PS C:\Users\u> docker stop WebServerTiju
WebServerTiju
PS C:\Users\u> docker image ls
REPOSITORY                        TAG                IMAGE ID       CREATED          SIZE
<none>                            <none>             b988aa501844   10 minutes ago   148MB
rmanojcse06/spring-boot-hello     0.0.1              9d4fd7dce456   6 days ago       420MB
tiju/my-spring-boot-app           latest             1dfccde513ea   6 days ago       351MB
varghese25/my-spring-boot-app     latest             1dfccde513ea   6 days ago       351MB
yourusername/my-spring-boot-app   latest             1dfccde513ea   6 days ago       351MB
my-spring-boot-app                latest             1dfccde513ea   6 days ago       351MB
alpine                            latest             91ef0af61f39   2 weeks ago      7.8MB
gcr.io/k8s-minikube/kicbase       v0.0.45            aeed0e1d4642   2 weeks ago      1.28GB
postgres                          latest             b781f3a53e61   6 weeks ago      432MB
postgres                          13.16-alpine3.20   ed06eaccad7e   6 weeks ago      238MB
httpd                             latest             9cb0a2315602   2 months ago     148MB
hello-world                       latest             d2c94e258dcb   17 months ago    13.3kB
openjdk                           17-jdk-alpine      264c9bdce361   3 years ago      326MB
PS C:\Users\u> docker run -d --name myOwnServer  -p 5070:80 b988aa501844
1d3c8532d2082e0e595c2ef617ca1ce890c532ead3c8cc2dbab6742dd587c20b



PS C:\Users\u> docker ps  #Our Own Image ID#
CONTAINER ID   IMAGE          COMMAND              CREATED              STATUS              PORTS                  NAMES
1d3c8532d208   b988aa501844   "httpd-foreground"   About a minute ago   Up About a minute   0.0.0.0:5070->80/tcp   myOwnServer


http://localhost:5070/mypage.html

This is my Docker Page and Commands




PS C:\Users\u> docker image ls
REPOSITORY                        TAG                IMAGE ID       CREATED          SIZE
<none>                            <none>             b988aa501844   14 minutes ago   148MB

PS C:\Users\u> docker tag b988aa501844 myWebserver:Version1

Error parsing reference: "myWebserver:Version1" is not a valid repository/tag: invalid reference format: repository name (library/myWebserver) must be lowercase
PS C:\Users\u> docker tag b988aa501844 mywebserver:version1

PS C:\Users\u> docker image ls
REPOSITORY                        TAG                IMAGE ID       CREATED          SIZE
mywebserver                       version1           b988aa501844   16 minutes ago   148MB

PS C:\Users\u> docker save mywebserver:version1 > mywebserver-version1.tar   #Alternative COMMAND both does samework docker save -o mywebserver-version1.tar #


PS C:\Users\u> ls


    Directory: C:\Users\u


Mode                 LastWriteTime         Length Name
----                 -------------         ------ ----

-a----           9/23/24   3:22 PM      139280510 mywebserver-version1.tar
-a----           9/20/24   2:18 PM            325 service.yaml


 docker load -i mywebserver-version1.tar // To share file to other's it similar to pull commmad to check our outPut to view locally with send to docker Hub
 
 
 
 PS C:\Users\u> docker login
Authenticating with existing credentials...
Login Succeeded
PS C:\Users\u> docker push varghese25/mywebserver:version1
The push refers to repository [docker.io/varghese25/mywebserver]
An image does not exist locally with the tag: varghese25/mywebserver
PS C:\Users\u> docker image ls
REPOSITORY                        TAG                IMAGE ID       CREATED          SIZE
mywebserver                       version1           b988aa501844   32 minutes ago   148MB
rmanojcse06/spring-boot-hello     0.0.1              9d4fd7dce456   6 days ago       420MB
my-spring-boot-app                latest             1dfccde513ea   6 days ago       351MB
tiju/my-spring-boot-app           latest             1dfccde513ea   6 days ago       351MB
varghese25/my-spring-boot-app     latest             1dfccde513ea   6 days ago       351MB
yourusername/my-spring-boot-app   latest             1dfccde513ea   6 days ago       351MB
alpine                            latest             91ef0af61f39   2 weeks ago      7.8MB
gcr.io/k8s-minikube/kicbase       v0.0.45            aeed0e1d4642   2 weeks ago      1.28GB
postgres                          latest             b781f3a53e61   6 weeks ago      432MB
postgres                          13.16-alpine3.20   ed06eaccad7e   6 weeks ago      238MB
httpd                             latest             9cb0a2315602   2 months ago     148MB
hello-world                       latest             d2c94e258dcb   17 months ago    13.3kB
openjdk                           17-jdk-alpine      264c9bdce361   3 years ago      326MB


PS C:\Users\u> docker tag mywebserver:version1 varghese25/mywebserver


PS C:\Users\u> docker image ls
REPOSITORY                        TAG                IMAGE ID       CREATED          SIZE
mywebserver                       version1           b988aa501844   38 minutes ago   148MB
varghese25/mywebserver            latest             b988aa501844   38 minutes ago   148MB

<br>------------------------------------------------------------------------------------------------------------------------<br>
##Date 25-09-2024<br>

 Kubernetes (k8S) from k to s 8 letters in short k8s <br>


 kubectl (client) #Master server which talk to k8s Api response shows as output similiar to docker tools running docker machine showed us docker out same kubectl#<br>
 
 
 minikube (single-node cluster)<br>
 
 kubeadm    <br>
 
 node -> computer machine or virtural machine<br>
 
 pod -> container<br>
 
 

##Linux(Ubutu)<br>

varghese@DESKTOP-OODIU93:~$ alias k=kubectl<br>

varghese@DESKTOP-OODIU93:~$ k version --client<br>

Client Version: v1.30.2<br>
Kustomize Version: v5.0.4-0.20230601165947-6ce0bf390ce3<br>
varghese@DESKTOP-OODIU93:~$ pwd<br>
/home/varghese<br>
varghese@DESKTOP-OODIU93:~$<br>



<br>-------------------------------------------------------------------------------------<br>


Date 26-09-2024 K8s

##Topic: Minikube with single Node(Ip System) cluster Created & Kubectl Config and with node wheather Its communicting or not checked.<br>

(https://www.youtube.com/watch?v=7g5HLFg9QMI&list=PL5eh956CtlETZMJEMR866PCS2YNg_3O6d).<br>

PS C:\Users\u> Set-Alias m minikube

PS C:\Users\u> minikube delete
* Deleting "minikube" in docker ...
* Deleting container "minikube" ...
* Removing C:\Users\u\.minikube\machines\minikube ...
* Removed all traces of the "minikube" cluster.

PS C:\Users\u> minikube update-check
CurrentVersion: v1.34.0
LatestVersion: v1.34.0

PS C:\Users\u> m start --driver=docker
* minikube v1.34.0 on Microsoft Windows 10 Pro 10.0.19045.4957 Build 19045.4957
* Using the docker driver based on user configuration
* Using Docker Desktop driver with root privileges
* Starting "minikube" primary control-plane node in "minikube" cluster
* Pulling base image v0.0.45 ...
* Creating docker container (CPUs=4, Memory=3000MB) ...
! Failing to connect to https://registry.k8s.io/ from inside the minikube container
* To pull new external images, you may need to configure a proxy: https://minikube.sigs.k8s.io/docs/reference/networking/proxy/
* Preparing Kubernetes v1.31.0 on Docker 27.2.0 ...
  - Generating certificates and keys ...
  - Booting up control plane ...
  - Configuring RBAC rules ...
* Configuring bridge CNI (Container Networking Interface) ...
* Verifying Kubernetes components...
  - Using image gcr.io/k8s-minikube/storage-provisioner:v5
* Enabled addons: storage-provisioner, default-storageclass
* Done! kubectl is now configured to use "minikube" cluster and "default" namespace by default

PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u> m status

minikube
type: Control Plane
host: Running
kubelet: Running
apiserver: Running
kubeconfig: Configured

PS C:\Users\u> m version
minikube version: v1.34.0
commit: 210b148df93a80eb872ecbeb7e35281b3c582c61

PS C:\Users\u> docker ps

CONTAINER ID   IMAGE                                 COMMAND                  CREATED          STATUS          PORTS                                                                                                                                  NAMES
fbc9c3817d01   gcr.io/k8s-minikube/kicbase:v0.0.45   "/usr/local/bin/entr…"   49 minutes ago   Up 49 minutes   127.0.0.1:51682->22/tcp, 127.0.0.1:51678->2376/tcp, 127.0.0.1:51680->5000/tcp, 127.0.0.1:51681->8443/tcp, 127.0.0.1:51679->32443/tcp   minikube
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u> docker exec -it minikube bash
root@minikube:/#exit
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u> m ssh
docker@minikube:~$
PS C:\Users\u> m ssh
docker@minikube:~$   #docker@minikube is a node other words virtual machine#
docker@minikube:~$
docker@minikube:~$
docker@minikube:~$
docker@minikube:~$ docker ps
docker@minikube:~$ docker ps #Master node how container running we can check below#
CONTAINER ID   IMAGE                        COMMAND                  CREATED          STATUS          PORTS     NAMES
8280c3feab8d   6e38f40d628d                 "/storage-provisioner"   50 minutes ago   Up 50 minutes             k8s_storage-provisioner_storage-provisioner_kube-system_c0616760-dc06-4863-b269-b6d222d729ca_0
0d4c606ed0cc   cbb01a7bd410                 "/coredns -conf /etc…"   50 minutes ago   Up 50 minutes             k8s_coredns_coredns-6f6b679f8f-h49rl_kube-system_ab450272-2c88-47b8-a0ea-4d801893e276_0
6c7bdd786781   ad83b2ca7b09                 "/usr/local/bin/kube…"   50 minutes ago   Up 50 minutes             k8s_kube-proxy_kube-proxy-t5sx7_kube-system_36148bed-87f6-4308-9c3d-4a29790cea65_0
dc91a9809039   registry.k8s.io/pause:3.10   "/pause"                 50 minutes ago   Up 50 minutes             k8s_POD_storage-provisioner_kube-system_c0616760-dc06-4863-b269-b6d222d729ca_0
37143475f67e   registry.k8s.io/pause:3.10   "/pause"                 50 minutes ago   Up 50 minutes             k8s_POD_coredns-6f6b679f8f-h49rl_kube-system_ab450272-2c88-47b8-a0ea-4d801893e276_0
118c73c9a2c9   registry.k8s.io/pause:3.10   "/pause"                 50 minutes ago   Up 50 minutes             k8s_POD_kube-proxy-t5sx7_kube-system_36148bed-87f6-4308-9c3d-4a29790cea65_0
09cf1515a99e   045733566833                 "kube-controller-man…"   51 minutes ago   Up 51 minutes             k8s_kube-controller-manager_kube-controller-manager-minikube_kube-system_40f5f661ab65f2e4bfe41ac2993c01de_2
adbbc2825349   604f5db92eaa                 "kube-apiserver --ad…"   52 minutes ago   Up 52 minutes             k8s_kube-apiserver_kube-apiserver-minikube_kube-system_9e315b3a91fa9f6f7463439d9dac1a56_0
fc17a65ca445   2e96e5913fc0                 "etcd --advertise-cl…"   52 minutes ago   Up 52 minutes             k8s_etcd_etcd-minikube_kube-system_a5363f4f31e043bdae3c93aca4991903_0
8b118bddf2ce   1766f54c897f                 "kube-scheduler --au…"   52 minutes ago   Up 52 minutes             k8s_kube-scheduler_kube-scheduler-minikube_kube-system_e039200acb850c82bb901653cc38ff6e_0
6838c9099afe   registry.k8s.io/pause:3.10   "/pause"                 53 minutes ago   Up 52 minutes             k8s_POD_kube-scheduler-minikube_kube-system_e039200acb850c82bb901653cc38ff6e_0
0d0d149f8c0a   registry.k8s.io/pause:3.10   "/pause"                 53 minutes ago   Up 52 minutes             k8s_POD_kube-apiserver-minikube_kube-system_9e315b3a91fa9f6f7463439d9dac1a56_0
df868b83e320   registry.k8s.io/pause:3.10   "/pause"                 53 minutes ago   Up 52 minutes             k8s_POD_etcd-minikube_kube-system_a5363f4f31e043bdae3c93aca4991903_0
f64c0a3abc02   registry.k8s.io/pause:3.10   "/pause"                 53 minutes ago   Up 52 minutes             k8s_POD_kube-controller-manager-minikube_kube-system_40f5f661ab65f2e4bfe41ac2993c01de_0
docker@minikube:~$
docker@minikube:~$ #CTRL+D#
logout
PS C:\Users\u>
PS C:\Users\u> m kubectl -- get pods   #this command execute minikube container inside the kubctl tool not available minikube for first time it will install #
    > kubectl.exe.sha256:  64 B / 64 B [---------------------] 100.00% ? p/s 0s
    > kubectl.exe:  55.20 MiB / 55.20 MiB [------] 100.00% 488.35 KiB p/s 1m56s
No resources found in default namespace.
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u> kubectl get pods  #Runn's in host kubctl #
No resources found in default namespace.
PS C:\Users\u>
PS C:\Users\u> Set-Alias k kubectl
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u> k config view
apiVersion: v1
clusters:
- cluster:
    certificate-authority: C:\Users\u\.minikube\ca.crt
    extensions:
    - extension:
        last-update: Thu, 26 Sep 2024 13:15:17 EDT
        provider: minikube.sigs.k8s.io
        version: v1.34.0
      name: cluster_info
    server: https://127.0.0.1:51681                                      #Only one node which is my System Ip going to work we can any number of node #
  name: minikube
contexts:
- context:
    cluster: minikube
    extensions:
    - extension:
        last-update: Thu, 26 Sep 2024 13:15:17 EDT
        provider: minikube.sigs.k8s.io
        version: v1.34.0
      name: context_info
    namespace: default
    user: minikube
  name: minikube
current-context: minikube
kind: Config
preferences: {}
users:
- name: minikube
  user:
    client-certificate: C:\Users\u\.minikube\profiles\minikube\client.crt
    client-key: C:\Users\u\.minikube\profiles\minikube\client.key
PS C:\Users\u>

<br>-------------------------------------------------------------------------------------------------<br>



##Date 28-09-2024
##Follow this first step 1 & Then go 2 #How To create Pod & Deploy in webBrowser# <br>

#first step 1 Start driver Docker#

PS C:\Users\u> m start --driver=docker
* minikube v1.34.0 on Microsoft Windows 10 Pro 10.0.19045.4957 Build 19045.4957
* Using the docker driver based on existing profile
* Starting "minikube" primary control-plane node in "minikube" cluster
* Pulling base image v0.0.45 ...
* Updating the running docker "minikube" container ...
! Failing to connect to https://registry.k8s.io/ from inside the minikube container
* To pull new external images, you may need to configure a proxy: https://minikube.sigs.k8s.io/docs/reference/networking/proxy/
* Preparing Kubernetes v1.31.0 on Docker 27.2.0 ...
* Verifying Kubernetes components...
  - Using image gcr.io/k8s-minikube/storage-provisioner:v5
* Enabled addons: storage-provisioner, default-storageclass
* Done! kubectl is now configured to use "minikube" cluster and "default" namespace by default
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>                                            
PS C:\Users\u> k get nodes                         
NAME       STATUS   ROLES           AGE   VERSION                        #Roles "Control-plane" Terminionlog changed by k8s before it was Roles "master"#
minikube   Ready    control-plane   2d    v1.31.0


#nodes in wide Format#

PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u> k get nodes -o wide
NAME       STATUS   ROLES           AGE   VERSION   INTERNAL-IP    EXTERNAL-IP   OS-IMAGE             KERNEL-VERSION                       CONTAINER-RUNTIME
minikube   Ready    control-plane   2d    v1.31.0   192.168.49.2   <none>        Ubuntu 22.04.4 LTS   5.15.153.1-microsoft-standard-WSL2   docker://27.2.0



#nodes in Yaml Format#

PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u> k get nodes -o yaml
apiVersion: v1
items:
- apiVersion: v1
  kind: Node
  metadata:
    annotations:
      kubeadm.alpha.kubernetes.io/cri-socket: unix:///var/run/cri-dockerd.sock
      node.alpha.kubernetes.io/ttl: "0"
      volumes.kubernetes.io/controller-managed-attach-detach: "true"
    creationTimestamp: "2024-09-26T17:14:17Z"
    labels:
      beta.kubernetes.io/arch: amd64
      beta.kubernetes.io/os: linux
      kubernetes.io/arch: amd64
      kubernetes.io/hostname: minikube
      kubernetes.io/os: linux
      minikube.k8s.io/commit: 210b148df93a80eb872ecbeb7e35281b3c582c61
      minikube.k8s.io/name: minikube
      minikube.k8s.io/primary: "true"
      minikube.k8s.io/updated_at: 2024_09_26T13_15_14_0700
      minikube.k8s.io/version: v1.34.0
      node-role.kubernetes.io/control-plane: ""
      node.kubernetes.io/exclude-from-external-load-balancers: ""
    name: minikube
    resourceVersion: "6351"
    uid: dd11d68b-43be-4b9a-88d1-dc506727abea
  spec:
    podCIDR: 10.244.0.0/24
    podCIDRs:
    - 10.244.0.0/24
  status:
    addresses:
    - address: 192.168.49.2
      type: InternalIP
    - address: minikube
      type: Hostname
    allocatable:
      cpu: "4"
      ephemeral-storage: 1055762868Ki
      hugepages-1Gi: "0"
      hugepages-2Mi: "0"
      memory: 3978116Ki
      pods: "110"
    capacity:
      cpu: "4"
      ephemeral-storage: 1055762868Ki
      hugepages-1Gi: "0"
      hugepages-2Mi: "0"
      memory: 3978116Ki
      pods: "110"
    conditions:
    - lastHeartbeatTime: "2024-09-28T17:33:20Z"
      lastTransitionTime: "2024-09-26T17:14:17Z"
      message: kubelet has sufficient memory available
      reason: KubeletHasSufficientMemory
      status: "False"
      type: MemoryPressure
    - lastHeartbeatTime: "2024-09-28T17:33:20Z"
      lastTransitionTime: "2024-09-26T17:14:17Z"
      message: kubelet has no disk pressure
      reason: KubeletHasNoDiskPressure
      status: "False"
      type: DiskPressure
    - lastHeartbeatTime: "2024-09-28T17:33:20Z"
      lastTransitionTime: "2024-09-26T17:14:17Z"
      message: kubelet has sufficient PID available
      reason: KubeletHasSufficientPID
      status: "False"
      type: PIDPressure
    - lastHeartbeatTime: "2024-09-28T17:33:20Z"
      lastTransitionTime: "2024-09-26T17:14:18Z"
      message: kubelet is posting ready status
      reason: KubeletReady
      status: "True"
      type: Ready
    daemonEndpoints:
      kubeletEndpoint:
        Port: 10250
    images:
    - names:
      - registry.k8s.io/etcd@sha256:a6dc63e6e8cfa0307d7851762fa6b629afb18f28d8aa3fab5a6e91b4af60026a
      - registry.k8s.io/etcd:3.5.15-0
      sizeBytes: 147945345
    - names:
      - registry.k8s.io/kube-apiserver@sha256:470179274deb9dc3a81df55cfc24823ce153147d4ebf2ed649a4f271f51eaddf
      - registry.k8s.io/kube-apiserver:v1.31.0
      sizeBytes: 94175876
    - names:
      - registry.k8s.io/kube-proxy@sha256:c727efb1c6f15a68060bf7f207f5c7a765355b7e3340c513e582ec819c5cd2fe
      - registry.k8s.io/kube-proxy:v1.31.0
      sizeBytes: 91471299
    - names:
      - registry.k8s.io/kube-controller-manager@sha256:f6f3c33dda209e8434b83dacf5244c03b59b0018d93325ff21296a142b68497d
      - registry.k8s.io/kube-controller-manager:v1.31.0
      sizeBytes: 88380387
    - names:
      - registry.k8s.io/kube-scheduler@sha256:96ddae9c9b2e79342e0551e2d2ec422c0c02629a74d928924aaa069706619808
      - registry.k8s.io/kube-scheduler:v1.31.0
      sizeBytes: 67363811
    - names:
      - registry.k8s.io/coredns/coredns@sha256:1eeb4c7316bacb1d4c8ead65571cd92dd21e27359f0d4917f1a5822a73b75db1
      - registry.k8s.io/coredns/coredns:v1.11.1
      sizeBytes: 59820619
    - names:
      - gcr.io/k8s-minikube/storage-provisioner@sha256:18eb69d1418e854ad5a19e399310e52808a8321e4c441c1dddad8977a0d7a944
      - gcr.io/k8s-minikube/storage-provisioner:v5
      sizeBytes: 31465472
    - names:
      - registry.k8s.io/pause@sha256:ee6521f290b2168b6e0935a181d4cff9be1ac3f505666ef0e3c98fae8199917a
      - registry.k8s.io/pause:3.10
      sizeBytes: 735760
    nodeInfo:
      architecture: amd64
      bootID: 9dffcac1-96ea-4361-bdf7-cfc5a1fc1e08
      containerRuntimeVersion: docker://27.2.0
      kernelVersion: 5.15.153.1-microsoft-standard-WSL2
      kubeProxyVersion: ""
      kubeletVersion: v1.31.0
      machineID: e5854f5e7e614bd7bda57f7ae2708452
      operatingSystem: linux
      osImage: Ubuntu 22.04.4 LTS
      systemUUID: e5854f5e7e614bd7bda57f7ae2708452
kind: List
metadata:
  resourceVersion: ""
  
  
  
#nodes in Json Format#


PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
  
  PS C:\Users\u> k get nodes -o json
{
    "apiVersion": "v1",
    "items": [
        {
            "apiVersion": "v1",
            "kind": "Node",
            "metadata": {
                "annotations": {
                    "kubeadm.alpha.kubernetes.io/cri-socket": "unix:///var/run/cri-dockerd.sock",
                    "node.alpha.kubernetes.io/ttl": "0",
                    "volumes.kubernetes.io/controller-managed-attach-detach": "true"
                },
                "creationTimestamp": "2024-09-26T17:14:17Z",
                "labels": {
                    "beta.kubernetes.io/arch": "amd64",
                    "beta.kubernetes.io/os": "linux",
                    "kubernetes.io/arch": "amd64",
                    "kubernetes.io/hostname": "minikube",
                    "kubernetes.io/os": "linux",
                    "minikube.k8s.io/commit": "210b148df93a80eb872ecbeb7e35281b3c582c61",
                    "minikube.k8s.io/name": "minikube",
                    "minikube.k8s.io/primary": "true",
                    "minikube.k8s.io/updated_at": "2024_09_26T13_15_14_0700",
                    "minikube.k8s.io/version": "v1.34.0",
                    "node-role.kubernetes.io/control-plane": "",
                    "node.kubernetes.io/exclude-from-external-load-balancers": ""
                },
                "name": "minikube",
                "resourceVersion": "6351",
                "uid": "dd11d68b-43be-4b9a-88d1-dc506727abea"
            },
            "spec": {
                "podCIDR": "10.244.0.0/24",
                "podCIDRs": [
                    "10.244.0.0/24"
                ]
            },
            "status": {
                "addresses": [
                    {
                        "address": "192.168.49.2",
                        "type": "InternalIP"
                    },
                    {
                        "address": "minikube",
                        "type": "Hostname"
                    }
                ],
                "allocatable": {
                    "cpu": "4",
                    "ephemeral-storage": "1055762868Ki",
                    "hugepages-1Gi": "0",
                    "hugepages-2Mi": "0",
                    "memory": "3978116Ki",
                    "pods": "110"
                },
                "capacity": {
                    "cpu": "4",
                    "ephemeral-storage": "1055762868Ki",
                    "hugepages-1Gi": "0",
                    "hugepages-2Mi": "0",
                    "memory": "3978116Ki",
                    "pods": "110"
                },
                "conditions": [
                    {
                        "lastHeartbeatTime": "2024-09-28T17:33:20Z",
                        "lastTransitionTime": "2024-09-26T17:14:17Z",
                        "message": "kubelet has sufficient memory available",
                        "reason": "KubeletHasSufficientMemory",
                        "status": "False",
                        "type": "MemoryPressure"
                    },
                    {
                        "lastHeartbeatTime": "2024-09-28T17:33:20Z",
                        "lastTransitionTime": "2024-09-26T17:14:17Z",
                        "message": "kubelet has no disk pressure",
                        "reason": "KubeletHasNoDiskPressure",
                        "status": "False",
                        "type": "DiskPressure"
                    },
                    {
                        "lastHeartbeatTime": "2024-09-28T17:33:20Z",
                        "lastTransitionTime": "2024-09-26T17:14:17Z",
                        "message": "kubelet has sufficient PID available",
                        "reason": "KubeletHasSufficientPID",
                        "status": "False",
                        "type": "PIDPressure"
                    },
                    {
                        "lastHeartbeatTime": "2024-09-28T17:33:20Z",
                        "lastTransitionTime": "2024-09-26T17:14:18Z",
                        "message": "kubelet is posting ready status",
                        "reason": "KubeletReady",
                        "status": "True",
                        "type": "Ready"
                    }
                ],
                "daemonEndpoints": {
                    "kubeletEndpoint": {
                        "Port": 10250
                    }
                },
                "images": [
                    {
                        "names": [
                            "registry.k8s.io/etcd@sha256:a6dc63e6e8cfa0307d7851762fa6b629afb18f28d8aa3fab5a6e91b4af60026a",
                            "registry.k8s.io/etcd:3.5.15-0"
                        ],
                        "sizeBytes": 147945345
                    },
                    {
                        "names": [
                            "registry.k8s.io/kube-apiserver@sha256:470179274deb9dc3a81df55cfc24823ce153147d4ebf2ed649a4f271f51eaddf",
                            "registry.k8s.io/kube-apiserver:v1.31.0"
                        ],
                        "sizeBytes": 94175876
                    },
                    {
                        "names": [
                            "registry.k8s.io/kube-proxy@sha256:c727efb1c6f15a68060bf7f207f5c7a765355b7e3340c513e582ec819c5cd2fe",
                            "registry.k8s.io/kube-proxy:v1.31.0"
                        ],
                        "sizeBytes": 91471299
                    },
                    {
                        "names": [
                            "registry.k8s.io/kube-controller-manager@sha256:f6f3c33dda209e8434b83dacf5244c03b59b0018d93325ff21296a142b68497d",
                            "registry.k8s.io/kube-controller-manager:v1.31.0"
                        ],
                        "sizeBytes": 88380387
                    },
                    {
                        "names": [
                            "registry.k8s.io/kube-scheduler@sha256:96ddae9c9b2e79342e0551e2d2ec422c0c02629a74d928924aaa069706619808",
                            "registry.k8s.io/kube-scheduler:v1.31.0"
                        ],
                        "sizeBytes": 67363811
                    },
                    {
                        "names": [
                            "registry.k8s.io/coredns/coredns@sha256:1eeb4c7316bacb1d4c8ead65571cd92dd21e27359f0d4917f1a5822a73b75db1",
                            "registry.k8s.io/coredns/coredns:v1.11.1"
                        ],
                        "sizeBytes": 59820619
                    },
                    {
                        "names": [
                            "gcr.io/k8s-minikube/storage-provisioner@sha256:18eb69d1418e854ad5a19e399310e52808a8321e4c441c1dddad8977a0d7a944",
                            "gcr.io/k8s-minikube/storage-provisioner:v5"
                        ],
                        "sizeBytes": 31465472
                    },
                    {
                        "names": [
                            "registry.k8s.io/pause@sha256:ee6521f290b2168b6e0935a181d4cff9be1ac3f505666ef0e3c98fae8199917a",
                            "registry.k8s.io/pause:3.10"
                        ],
                        "sizeBytes": 735760
                    }
                ],
                "nodeInfo": {
                    "architecture": "amd64",
                    "bootID": "9dffcac1-96ea-4361-bdf7-cfc5a1fc1e08",
                    "containerRuntimeVersion": "docker://27.2.0",
                    "kernelVersion": "5.15.153.1-microsoft-standard-WSL2",
                    "kubeProxyVersion": "",
                    "kubeletVersion": "v1.31.0",
                    "machineID": "e5854f5e7e614bd7bda57f7ae2708452",
                    "operatingSystem": "linux",
                    "osImage": "Ubuntu 22.04.4 LTS",
                    "systemUUID": "e5854f5e7e614bd7bda57f7ae2708452"
                }
            }
        }
    ],
    "kind": "List",
    "metadata": {
        "resourceVersion": ""
    }
}
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u> k get pods
No resources found in default namespace.
PS C:\Users\u> k get deploy
No resources found in default namespace.
PS C:\Users\u> k get svc
NAME         TYPE        CLUSTER-IP   EXTERNAL-IP   PORT(S)   AGE
kubernetes   ClusterIP   10.96.0.1    <none>        443/TCP   2d
PS C:\Users\u>


PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u> m ssh
docker@minikube:~$ docker ps
CONTAINER ID   IMAGE                        COMMAND                  CREATED          STATUS          PORTS     NAMES
1d92f31aeba0   6e38f40d628d                 "/storage-provisioner"   14 minutes ago   Up 14 minutes             k8s_storage-provisioner_storage-provisioner_kube-system_c0616760-dc06-4863-b269-b6d222d729ca_2
f6b8bb57e8f5   cbb01a7bd410                 "/coredns -conf /etc…"   15 minutes ago   Up 15 minutes             k8s_coredns_coredns-6f6b679f8f-h49rl_kube-system_ab450272-2c88-47b8-a0ea-4d801893e276_1
3268ea340764   ad83b2ca7b09                 "/usr/local/bin/kube…"   15 minutes ago   Up 15 minutes             k8s_kube-proxy_kube-proxy-t5sx7_kube-system_36148bed-87f6-4308-9c3d-4a29790cea65_1
docker@minikube:~$
docker@minikube:~$
docker@minikube:~$
docker@minikube:~$
docker@minikube:~$exit



#Default Containers#
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u> k get pod --namespace kube-system
NAME                               READY   STATUS    RESTARTS      AGE                     Tip
coredns-6f6b679f8f-h49rl           1/1     Running   1 (49m ago)   2d           #K8s internall created object has a prefix Deployment name"coredns"  and suffix"6f6b679f8f" id #
etcd-minikube                      1/1     Running   1 (49m ago)   2d                      #Dircet Pod name Given #
kube-apiserver-minikube            1/1     Running   1 (49m ago)   2d
kube-controller-manager-minikube   1/1     Running   3 (49m ago)   2d
kube-proxy-t5sx7                   1/1     Running   1 (46h ago)   2d
kube-scheduler-minikube            1/1     Running   1 (49m ago)   2d
storage-provisioner                1/1     Running   2 (19m ago)   2d
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u> 

#k8s Created Deployment "Coredns" running in one replica coredns-6f6b679f8f-h49rl #
PS C:\Users\u> k get deploy --namespace kube-system
NAME      READY   UP-TO-DATE   AVAILABLE   AGE
coredns   1/1     1            1           2d

PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>

#k8s above deployment created one replica Set #
PS C:\Users\u> k get replicaset --namespace kube-system
NAME                 DESIRED   CURRENT   READY   AGE
coredns-6f6b679f8f   1         1         1       2d
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>



#Object information use describe nodes#

PS C:\Users\u> k describe nodes
Name:               minikube
Roles:              control-plane
Labels:             beta.kubernetes.io/arch=amd64
                    beta.kubernetes.io/os=linux
                    kubernetes.io/arch=amd64
                    kubernetes.io/hostname=minikube
                    kubernetes.io/os=linux
                    minikube.k8s.io/commit=210b148df93a80eb872ecbeb7e35281b3c582c61
                    minikube.k8s.io/name=minikube
                    minikube.k8s.io/primary=true
                    minikube.k8s.io/updated_at=2024_09_26T13_15_14_0700
                    minikube.k8s.io/version=v1.34.0
                    node-role.kubernetes.io/control-plane=
                    node.kubernetes.io/exclude-from-external-load-balancers=
Annotations:        kubeadm.alpha.kubernetes.io/cri-socket: unix:///var/run/cri-dockerd.sock
                    node.alpha.kubernetes.io/ttl: 0
                    volumes.kubernetes.io/controller-managed-attach-detach: true
CreationTimestamp:  Thu, 26 Sep 2024 13:14:17 -0400
Taints:             <none>
Unschedulable:      false
Lease:
  HolderIdentity:  minikube
  AcquireTime:     <unset>
  RenewTime:       Sat, 28 Sep 2024 13:57:46 -0400
Conditions:
  Type             Status  LastHeartbeatTime                 LastTransitionTime                Reason                       Message
  ----             ------  -----------------                 ------------------                ------                       -------
  MemoryPressure   False   Sat, 28 Sep 2024 13:53:44 -0400   Thu, 26 Sep 2024 13:14:17 -0400   KubeletHasSufficientMemory   kubelet has sufficient memory available
  DiskPressure     False   Sat, 28 Sep 2024 13:53:44 -0400   Thu, 26 Sep 2024 13:14:17 -0400   KubeletHasNoDiskPressure     kubelet has no disk pressure
  PIDPressure      False   Sat, 28 Sep 2024 13:53:44 -0400   Thu, 26 Sep 2024 13:14:17 -0400   KubeletHasSufficientPID      kubelet has sufficient PID available
  Ready            True    Sat, 28 Sep 2024 13:53:44 -0400   Thu, 26 Sep 2024 13:14:18 -0400   KubeletReady                 kubelet is posting ready status
Addresses:
  InternalIP:  192.168.49.2
  Hostname:    minikube
Capacity:
  cpu:                4
  ephemeral-storage:  1055762868Ki
  hugepages-1Gi:      0
  hugepages-2Mi:      0
  memory:             3978116Ki
  pods:               110
Allocatable:
  cpu:                4
  ephemeral-storage:  1055762868Ki
  hugepages-1Gi:      0
  hugepages-2Mi:      0
  memory:             3978116Ki
  pods:               110
System Info:
  Machine ID:                 e5854f5e7e614bd7bda57f7ae2708452
  System UUID:                e5854f5e7e614bd7bda57f7ae2708452
  Boot ID:                    9dffcac1-96ea-4361-bdf7-cfc5a1fc1e08
  Kernel Version:             5.15.153.1-microsoft-standard-WSL2
  OS Image:                   Ubuntu 22.04.4 LTS
  Operating System:           linux
  Architecture:               amd64
  Container Runtime Version:  docker://27.2.0
  Kubelet Version:            v1.31.0
  Kube-Proxy Version:
PodCIDR:                      10.244.0.0/24
PodCIDRs:                     10.244.0.0/24
Non-terminated Pods:          (7 in total)
  Namespace                   Name                                CPU Requests  CPU Limits  Memory Requests  Memory Limits  Age
  ---------                   ----                                ------------  ----------  ---------------  -------------  ---
  kube-system                 coredns-6f6b679f8f-h49rl            100m (2%)     0 (0%)      70Mi (1%)        170Mi (4%)     2d
  kube-system                 etcd-minikube                       100m (2%)     0 (0%)      100Mi (2%)       0 (0%)         2d
  kube-system                 kube-apiserver-minikube             250m (6%)     0 (0%)      0 (0%)           0 (0%)         2d
  kube-system                 kube-controller-manager-minikube    200m (5%)     0 (0%)      0 (0%)           0 (0%)         2d
  kube-system                 kube-proxy-t5sx7                    0 (0%)        0 (0%)      0 (0%)           0 (0%)         2d
  kube-system                 kube-scheduler-minikube             100m (2%)     0 (0%)      0 (0%)           0 (0%)         2d
  kube-system                 storage-provisioner                 0 (0%)        0 (0%)      0 (0%)           0 (0%)         2d
Allocated resources:
  (Total limits may be over 100 percent, i.e., overcommitted.)
  Resource           Requests    Limits
  --------           --------    ------
  cpu                750m (18%)  0 (0%)
  memory             170Mi (4%)  170Mi (4%)
  ephemeral-storage  0 (0%)      0 (0%)
  hugepages-1Gi      0 (0%)      0 (0%)
  hugepages-2Mi      0 (0%)      0 (0%)
Events:
  Type     Reason                             Age                From             Message
  ----     ------                             ----               ----             -------
  Normal   Starting                           33m                kube-proxy
  Warning  PossibleMemoryBackedVolumesOnDisk  35m                kubelet          The tmpfs noswap option is not supported. Memory-backed volumes (e.g. secrets, emptyDirs, etc.) might be swapped to disk and should no longer be considered secure.
  Normal   Starting                           35m                kubelet          Starting kubelet.
  Warning  CgroupV1                           35m                kubelet          Cgroup v1 support is in maintenance mode, please migrate to Cgroup v2.
  Normal   NodeHasSufficientMemory            35m (x7 over 35m)  kubelet          Node minikube status is now: NodeHasSufficientMemory
  Normal   NodeHasNoDiskPressure              35m (x7 over 35m)  kubelet          Node minikube status is now: NodeHasNoDiskPressure
  Normal   NodeHasSufficientPID               35m (x7 over 35m)  kubelet          Node minikube status is now: NodeHasSufficientPID
  Normal   NodeAllocatableEnforced            35m                kubelet          Updated Node Allocatable limit across pods
  Normal   RegisteredNode                     34m                node-controller  Node minikube event: Registered Node minikube in Controller
PS C:\Users\u>



PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>


--------------------------------#How To create Pod#---------------------------------------------------------------------




PS C:\Users\u> k run npod --image=nginx:latest
pod/npod created
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>

#Deleted nPod deploy deleted#
PS C:\Users\u> kubectl delete pod npod
pod "npod" deleted
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>

#k8s how to create pod name is www image name is nginx port used is 80#

PS C:\Users\u> k run www --image=nginx --port=80
pod/www created
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>


#Pod Created in the name of www#

PS C:\Users\u> k get pods
NAME   READY   STATUS    RESTARTS   AGE
www    1/1     Running   0          77s
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>

#no deployments seen pervious we deleted npod deployement refer above#
PS C:\Users\u> k get deploy
No resources found in default namespace.
PS C:\Users\u>


#Using LoadBalance How to expose www pod from cluster to out side meaning external Broswe#
*ext - external

PS C:\Users\u> k expose pod www --port=80 --name=www-ext --type=LoadBalancer
service/www-ext exposed
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>

#service Command, Minikube working as container, as a cluster external-Ip doesnt have ip address to access#

PS C:\Users\u> k get svc
NAME         TYPE           CLUSTER-IP       EXTERNAL-IP   PORT(S)        AGE
kubernetes   ClusterIP      10.96.0.1        <none>        443/TCP        2d1h
www-ext      LoadBalancer   10.107.116.195   <pending>     80:30922/TCP   116s
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>


#External ip willexpose with this command minikube conatiner which has www pod can extrally viewed in browser #


PS C:\Users\u> m service www-ext
|-----------|---------|-------------|---------------------------|
| NAMESPACE |  NAME   | TARGET PORT |            URL            |
|-----------|---------|-------------|---------------------------|
| default   | www-ext |          80 | http://192.168.49.2:30922 |
|-----------|---------|-------------|---------------------------|
* Starting tunnel for service www-ext.
|-----------|---------|-------------|------------------------|
| NAMESPACE |  NAME   | TARGET PORT |          URL           |
|-----------|---------|-------------|------------------------|
| default   | www-ext |             | http://127.0.0.1:62306 |
|-----------|---------|-------------|------------------------|
* Opening service default/www-ext in default browser...
! Because you are using a Docker driver on windows, the terminal needs to be open to run it.

#CTRL + C#
* Stopped tunnel for service www-ext. 


PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u> m service www-ext --url

http://127.0.0.1:62420 #Open this ip in browser#

! Because you are using a Docker driver on windows, the terminal needs to be open to run it.
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>


--------------How Create  Deployment same image nginx----------------------------
* wd = web deployment


PS C:\Users\u> k create deploy wd --image=nginx
deployment.apps/wd created
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u> k get deploy
NAME   READY   UP-TO-DATE   AVAILABLE   AGE
wd     0/1     1            0           12s

#Sucessfully Created#

PS C:\Users\u> k get deploy
NAME   READY   UP-TO-DATE   AVAILABLE   AGE
wd     1/1     1            1           68s
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>

#Two Pod running www & wd in minikube#

PS C:\Users\u> k get pods
NAME                  READY   STATUS    RESTARTS   AGE
wd-6595c9877d-4qgzr   1/1     Running   0          2m37s
www                   1/1     Running   0          30m

PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>

#decelarativ method such as yaml file file path: C:\Users\u\deploy.yaml#

PS C:\Users\u> k apply -f deloy.yaml
Warning: resource deployments/wd is missing the kubectl.kubernetes.io/last-applied-configuration annotation which is required by kubectl apply. kubectl apply should only be used on resources created declaratively by either kubectl create --save-config or kubectl apply. The missing annotation will be patched automatically.
deployment.apps/wd configured


# We can see three Replicas #
PS C:\Users\u> k get deploy
NAME   READY   UP-TO-DATE   AVAILABLE   AGE
wd     3/3     3            3           34m
PS C:\Users\u>

# We can see three Replicas#

PS C:\Users\u> k get replicaset
NAME            DESIRED   CURRENT   READY   AGE
wd-864758c6     3         3         3       7m26s


PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>

#and three Pods #
PS C:\Users\u> k get pods
NAME                READY   STATUS    RESTARTS   AGE
wd-864758c6-fs446   1/1     Running   0          7m59s
wd-864758c6-qsmdg   1/1     Running   0          7m45s
wd-864758c6-whbts   1/1     Running   0          8m35s
www                 1/1     Running   0          65m
PS C:\Users\u>

PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>



#Delete any pods #


PS C:\Users\u> k delete pod wd-864758c6-fs446
pod "wd-864758c6-fs446" deleted
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>



#k8s detected one down immedately it created another pod#

PS C:\Users\u> k get pods
NAME                READY   STATUS    RESTARTS   AGE
wd-864758c6-nhtbz   1/1     Running   0          2m8s   # New Pod k8s created after delete or down#
wd-864758c6-qsmdg   1/1     Running   0          15m
wd-864758c6-whbts   1/1     Running   0          16m
www                 1/1     Running   0          73m
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>



#Similar to logs what all pods deleted created etc info can seen in the events #
PS C:\Users\u> k get events

LAST SEEN   TYPE     REASON              OBJECT                     MESSAGE
47m         Normal   Scheduled           pod/wd-6595c9877d-4qgzr    Successfully assigned default/wd-6595c9877d-4qgzr to minikube
47m         Normal   Pulling             pod/wd-6595c9877d-4qgzr    Pulling image "nginx"
47m         Normal   Pulled              pod/wd-6595c9877d-4qgzr    Successfully pulled image "nginx" in 1.503s (1.503s including waiting). Image size: 187706909 bytes.



PS C:\Users\u> m addons
addons modifies minikube addons files using subcommands like "minikube addons enable dashboard"



# minikube dashboard in powershell if it loading ctr+D it automatically it will take us to dashboard browser with ip#

PS C:\Users\u> m dashboard
* Enabling dashboard ...
  - Using image docker.io/kubernetesui/dashboard:v2.7.0
  - Using image docker.io/kubernetesui/metrics-scraper:v1.0.8
* Some dashboard features require the metrics-server addon. To enable all features please run:

        minikube addons enable metrics-server

* Verifying dashboard health ...
* Launching proxy ...
* Verifying proxy health ...
* Opening http://127.0.0.1:62912/api/v1/namespaces/kubernetes-dashboard/services/http:kubernetes-dashboard:/proxy/ in your default browser...




##NOTES:
Kubernetes (K8s)
 kubectl (client) #Master server which talk to k8s Api response shows as output similiar to docker tools running docker machine showed us docker out same kubectl#
 minikube (single-node cluster)
 kubeadm    
 node -> computer machine or virtural machine


  
  ##pod <- container (Example for pod Vegetable peas skin called pod & peas are container) <-ReplicaSet <-  Deployment (Example, will create replicate set, every time we can't create 5 or more replicae set if it in deployment config )
DaemonSet  -> create copy of container in every nod means working virtual machine
NameSpace - Particular set object we can seperate 
Services - Like how we expose ip and view in broswer (Don't required pod to pod communication this service not required)
 
 
 Command for  K8s
 Imperative -> (Run, Expose, Create, Delete, Scale,....) dircetly we can use this manual process
 Declarative Type (Yamel file here we config all steps pods delete etc) -> (apply, diff..)









<br>---------------------------------##Date 28-09-2024 End---------------------------------<br>




<br>--------------------------------##Date 02-10-2024 Start---------------------------------<br>


#How Delete Pod/Deployment/ReplicaSet# & Follow Above #How To create Pod#<br>
#####First get the  pods#####<br>


PS C:\Users\u> kubectl get pods
NAME                READY   STATUS    RESTARTS      AGE<br>
wd-864758c6-27lh5   1/1     Running   0             39s <br>
wd-864758c6-d4ghd   1/1     Running   0             40s<br>
wd-864758c6-wtlb2   1/1     Running   0             37s<br>
www                 1/1     Running   2 (23m ago)   3d21h<br>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>


####Second go for Normal delete or if  doesn't work go for Forcefully Delete Pods####<br>

PS C:\Users\u> kubectl delete pod wd-864758c6-27lh5 wd-864758c6-d4ghd wd-864758c6-wtlb2 --grace-period=0 --force  
Warning: Immediate deletion does not wait for confirmation that the running resource has been terminated. The resource may continue to run on the cluster indefinitely.
pod "wd-864758c6-27lh5" force deleted
pod "wd-864758c6-d4ghd" force deleted
pod "wd-864758c6-wtlb2" force deleted
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u>

####Third check wd pod deleted or not with below command####
PS C:\Users\u> kubectl get pods 
NAME   READY   STATUS    RESTARTS      AGE
www    1/1     Running   2 (90m ago)   3d22h


####Fourth delete the (wd web devlopment container) deployment (it should say no resourse found namespace)#### Note Before deleting ensure which deployment(like wd in my case) should be deleted

PS C:\Users\u> kubectl delete deployment wd --grace-period=0 --force
Warning: Immediate deletion does not wait for confirmation that the running resource has been terminated. The resource may continue to run on the cluster indefinitely.
deployment.apps "wd" force deleted

PS C:\Users\u> kubectl get deploy
No resources found in default namespace.
PS C:\Users\u>




####Fivth check the Replicaset & delete####Note Before deleting ensure which Replicaset(like wd in my case) should be deleted

PS C:\Users\u>
PS C:\Users\u> kubectl delete replicaset --all
No resources found
PS C:\Users\u>
PS C:\Users\u>

PS C:\Users\u> kubectl get replicaset
No resources found in default namespace.
PS C:\Users\u>



####Finally its sucessfully deleted pod/deployment/replicaset####

PS C:\Users\u> kubectl get pods
NAME   READY   STATUS    RESTARTS       AGE
www    1/1     Running   2 (106m ago)   3d23h
PS C:\Users\u> kubectl get deploy
No resources found in default namespace.
PS C:\Users\u> kubectl get replicaset
No resources found in default namespace.
PS C:\Users\u>


##Component Health Status
PS C:\Users\u>
PS C:\Users\u>
PS C:\Users\u> kubectl get componentstatuses
Warning: v1 ComponentStatus is deprecated in v1.19+
NAME                 STATUS    MESSAGE   ERROR
controller-manager   Healthy   ok
scheduler            Healthy   ok
etcd-0               Healthy   ok
PS C:\Users\u>



<br>--------------------##END---------------------------<br>






















 
