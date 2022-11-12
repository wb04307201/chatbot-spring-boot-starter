<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>已发送群消息列表</title>
    <link rel="stylesheet" type="text/css" href="/bootstrap/5.1.3/css/bootstrap.css"/>
    <script type="text/javascript" src="/bootstrap/5.1.3/js/bootstrap.bundle.js"></script>
</head>
<body>
<div class="container-fluid">
    <table class="table table-bordered table-striped">
        <thead>
        <tr>
            <th scope="col">#</th>
            <th scope="col">方式</th>
            <th scope="col">请求</th>
            <th scope="col">响应</th>
        </tr>
        </thead>
        <tbody>
        <#list list as row>
        <tr>
            <th scope="row">${row.id}</th>
            <td>${row.type!'-'}</td>
            <td>${row.request!'-'}</td>
            <td>${row.response!'-'}</td>
        </tr>
        </#list>
        </tbody>
    </table>
</div>
</body>
</html>