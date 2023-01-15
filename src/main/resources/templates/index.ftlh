<!DOCTYPE html>
<html>
    <head>
        <title>SessionRegistry Demo</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <p>
            <span style="margin-right: 60px;">Hello, ${name}!</span><a href="/logout">Logout</a>
        </p>
        <h4>Currently logged in users</h4>
        <ul>
            <#list sessionInformationList as sessionInformation>
                <li>${sessionInformation.principal.username},
                    lastRequest: ${sessionInformation.lastRequest?datetime?string.medium},
                    sessionId: ${sessionInformation.sessionId}
                </li>
            </#list>
        </ul>
    </body>
</html>
