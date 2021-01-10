<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="img/favicon.ico">

    <title>People list</title>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.13/css/all.css" integrity="sha384-DNOHZ68U8hZfKXOrtjWvjxusGo9WQnrNx2sqG0tfsghAvtVlRW3tvkXWZh58N9jp" crossorigin="anonymous">
    <!-- Bootstrap core CSS -->
    <link href="../assets/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link rel="stylesheet" href="../assets/css/style.css">
</head>
<body>

<div class="container">
    <div class="row">
        <div class="col-8 offset-2">
            <div class="panel panel-default user_panel">
                <div class="panel-heading">
                    <h3 class="panel-title">Liked List</h3>
                </div>
                <div class="panel-body">
                    <form class="table-container">
                        <table class="table-users table" border="0">
                            <tbody>
                                <#list liked as user>
                                    <tr onclick="window.open('/messages/${user.id}', '_self')">
                                        <td width="10">
                                            <div class="avatar-img">
                                                <img class="img-circle" src="${user.imgUrl}" />
                                            </div>

                                        </td>
                                        <td class="align-middle">
                                            ${user.name}
                                        </td>
                                    </tr>
                                </#list>
                            </tbody>
                        </table>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>