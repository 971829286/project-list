<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>email</title>
    <style>
        * {
            margin: 0;
            padding: 0
        }

        html {
            font-family: "PingFang SC", "Microsoft Yahei UI", "Microsoft Yahei", sans-serif;
            color: #333;
            -ms-text-size-adjust: 100%;
            -webkit-text-size-adjust: 100%;
            -webkit-font-smoothing: antialiased
        }

        body {
            margin: 0
            height:100%;
        }

        article, aside, details, figcaption, figure, footer, header, hgroup, main, menu, nav, section, summary {
            display: block
        }

        audio, canvas, progress, video {
            display: inline-block;
            vertical-align: baseline
        }

        audio:not([controls]) {
            display: none;
            height: 0
        }

        [hidden], template {
            display: none
        }

        a {
            background-color: transparent
        }

        a:active, a:hover {
            outline: 0
        }

        abbr[title] {
            border-bottom: 1px dotted
        }

        b, strong {
            font-weight: bold
        }

        dfn {
            font-style: italic
        }

        h1 {
            font-size: 2em;
        }

        mark {
            background: #ff0;
            color: #333
        }

        small {
            font-size: 80%
        }

        sub, sup {
            font-size: 75%;
            line-height: 0;
            position: relative;
            vertical-align: baseline
        }

        sup {
            top: -0.5em
        }

        sub {
            bottom: -0.25em
        }

        img {
            border: 0
        }

        figure {
            margin: 1em 40px
        }

        hr {
            box-sizing: content-box;
            height: 0
        }

        pre {
            overflow: auto
        }

        code, kbd, pre, samp {
            font-family: monospace, monospace;
            font-size: 1em
        }

        button, input, optgroup, select, textarea {
            color: inherit;
            font: inherit;
            margin: 0
        }

        button {
            overflow: visible
        }

        button, select {
            text-transform: none
        }

        button, html input[type="button"], input[type="reset"], input[type="submit"] {
            -webkit-appearance: button;
            cursor: pointer
        }

        button[disabled], html input[disabled] {
            cursor: default
        }

        button::-moz-focus-inner, input::-moz-focus-inner {
            border: 0;
            padding: 0
        }

        input {
            line-height: normal
        }

        input[type="checkbox"], input[type="radio"] {
            box-sizing: border-box;
            padding: 0
        }

        input[type="number"]::-webkit-inner-spin-button, input[type="number"]::-webkit-outer-spin-button {
            height: auto
        }

        input[type="search"] {
            -webkit-appearance: textfield;
            box-sizing: content-box
        }

        input[type="search"]::-webkit-search-cancel-button, input[type="search"]::-webkit-search-decoration {
            -webkit-appearance: none
        }

        fieldset {
            border: 1px solid silver;
            margin: 0 2px;
            padding: .35em .625em .75em
        }

        legend {
            border: 0;
            padding: 0
        }

        textarea {
            overflow: auto
        }

        optgroup {
            font-weight: bold
        }

        table {
            border-collapse: collapse;
            border-spacing: 0
        }

        td, th {
            padding: 0
        }

        input, button, select, textarea {
            outline: none
        }

        textarea {
            resize: none
        }

        a {
            text-decoration: none;
            color: #333
        }

        ul {
            padding-left: 0;
            list-style: none;
        }

    </style>
    <style>
        html, body {
            min-height: 100%;
            background: rgb(192, 186, 186);
        }

        .mainContainer {
            width: 750px;
            margin: 0 auto;
            background-color: RGBA(248, 250, 251, 1);
        }
        .body{
            width:750px;
            background: rgb(255, 252, 252);
            overflow: hidden;
            position: relative;
            box-sizing: border-box;
            /*margin-top:10%;*/
            left:50%;
            margin-left:-375px;
            padding:50px;
        }
        header img{
            height:40px;
            margin-bottom:40px;
        }
        header h2{
            font-weight: normal;
            margin-bottom:30px;
        }
        section{
            padding-bottom:50px;
            border-bottom:1px solid rgba(222, 222, 230, 0.856);
        }
        section p{
            padding:6px 0;
        }
        section p a{
            text-decoration:underline;
            color:rgb(17, 104, 235)
        }
        section p:nth-child(2){
            display: inline-block;
            width:100%;
            word-wrap: break-word;
        }
        footer{
            padding-top:30px;
        }
        footer p{
            font-size:14px;
            text-align:center;
            color: gray;
        }
    </style>
</head>
<body>
<div class="mainContainer">
        <div class="body">
                <header>
                    <img src="${systemDomain}pc_static/images/ymhd_logo.png" alt="映目活动">
                    <h2>您好&nbsp;${user.nickname !'${user.username}'}!</h2>
                </header>
                <section>
                    <#--//绑定type=1, 解绑type=0-->
                <#if type == 1>
                    <p>点击以下链接完成邮箱绑定验证：</p>
                </#if>
                <#if type == 0>
                     <p>点击以下链接完成邮箱解绑验证：</p>
                </#if>
                    <p><a href="${confirmUrl}">${confirmUrl}</a></p>
                    <p>如果以上链接无法打开，请复制到浏览器地址栏打开(该链接在48小时内有效)</p>
                </section>
                <footer>
                    <p>Copyright @ 2013-2018 版权所有 北京韦尔科技有限公司 京ICP备 14040981 号-2</p>
                    <p>联系我们：400-616-1905</p>
                </footer>
        </div>
</body>
</html>
