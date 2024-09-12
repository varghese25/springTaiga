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
 You can add this secret by going to your repository’s Settings > Secrets and variables > Actions, and create a secret <br>called TAIGA_AUTH_TOKEN with the value being your authentication token (afb50e1cbf8b4a228851710cc630ea57)..<br>