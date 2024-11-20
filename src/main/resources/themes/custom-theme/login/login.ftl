<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${msg("loginTitle")}</title>
    <link rel="stylesheet" href="${url.resourcesPath}/css/style.css">
</head>
<body>
<div class="login-pf">
    <div class="container">
        <div class="row">
            <div class="col-xs-12 col-sm-8 col-md-6 col-lg-4 login">
                <h1>${msg("loginTitle")}</h1>
                <form action="${url.loginAction}" method="post">
                    <div class="form-group">
                        <label for="username">${msg("username")}</label>
                        <input type="text" id="username" name="username" class="form-control" value="">
                    </div>
                    <div class="form-group">
                        <label for="password">${msg("password")}</label>
                        <input type="password" id="password" name="password" class="form-control">
                    </div>
                    <div class="form-group">
                        <button type="submit" class="btn btn-primary">${msg("doLogIn")}</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>