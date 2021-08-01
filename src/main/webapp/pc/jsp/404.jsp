<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>找不到页面</title>
    <style type="text/css">
        body, div, ul, li, h1, h2, h3, h4, h5, h6, p {
            margin: 0;
            padding: 0;
        }

        body {
            font-size: 14px;
            font-family: '微软雅黑', serif;
            background-color: #F9FAFC;
            min-width: 900px;
        }

        img {
            border: 0;
            display: block;
        }

        ul {
            list-style: none;
        }

        h1, h2, h3, h4, h5, h6 {
            font-size: 100%;
            color: #333;
            font-weight: normal;
        }

        a {
            text-decoration: none;
            color: #666;
        }

        /*页面样式*/
        html, body, .content {
            width: 100%;
            height: 100%;
            position: relative;
        }

        body {
            background: #F9FAFC url(/pc/image/404-bg.jpg) no-repeat center bottom;
        }

        .content .main {
            width: 500px;
            height: 500px;
            position: absolute;
            left: 50%;
            margin-left: -250px;
            bottom: 100px;
        }

        .content .main h1 {
            width: 440px;
            height: 185px;
            margin: 0 auto 20px;
            background: url(/pc/image/404.png) no-repeat center;
        }

        .content .main h2 {
            text-align: center;
            font-family: '微软雅黑', serif;
            font-size: 24px;
            line-height: 26px;
            font-weight: 900;
            color: #f85766;
        }

        .content .main h3 {
            text-align: center;
            font-size: 18px;
            line-height: 26px;
            color: #999;
        }

        .content .main .img h4 {
            font-size: 14px;
            color: #999;
            line-height: 18px;
            text-align: center;
        }

        .content .main .backIndex {
            display: block;
            margin: 100px auto;
            width: 167px;
            height: 43px;
            line-height: 43px;
            text-align: center;
            color: #FFF;
            background-color: #39b1bc;
            border-radius: 40px;
        }

        .content .main .backIndex:hover {
            opacity: 0.8;
            filter: alpha(opacity=80);
        }

        .content .main .nav a {
            font-size: 14px;
            line-height: 14px;
            display: inline-block;
            *display: inline;
            *zoom: 1;
            padding: 0 10px;
            border-right: 1px solid #e0e2e3;
        }

        .content .main .nav a:hover {
            color: #f85766;
        }
    </style>
</head>
<body>
<div class="content">
    <div class="main">
        <h1></h1>
        <h2>您访问的网页没有找到！</h2>
        <a href="${pageContext.request.contextPath}/" class="backIndex">回首页</a>
    </div>
</div>
</body>
</html>
