<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>已发送群消息列表</title>
    <link rel="stylesheet" type="text/css" href="/bootstrap/5.1.3/css/bootstrap.css"/>
    <script type="text/javascript" src="/bootstrap/5.1.3/js/bootstrap.bundle.js"></script>
</head>
<body>
<div class="container">
    <table class="table table-bordered table-striped">
        <thead>
        <tr>
            <th scope="col">#</th>
            <th scope="col">标题</th>
            <th scope="col">内容</th>
            <th scope="col">所有人</th>
            <th scope="col">电话</th>
            <th scope="col">用户id</th>
            <th scope="col">响应结果</th>
        </tr>
        </thead>
        <tbody>
        <#list list as row>
        <tr>
            <th scope="row">${row.id}</th>
            <td>${row.title!'-'}</td>
            <td>${row.text!'-'}</td>
            <td>${row.atAll?string("yes","no")}</td>
            <td>${row.mobiles!'-'}</td>
            <td>${row.userIds!'-'}</td>
            <td>${row.response!'-'}</td>
        </tr>
        </#list>
        </tbody>
    </table>
</div>
</body>
</html>