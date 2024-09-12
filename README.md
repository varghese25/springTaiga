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