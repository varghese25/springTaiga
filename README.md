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

