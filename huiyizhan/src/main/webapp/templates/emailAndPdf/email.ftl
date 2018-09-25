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
        .border-1px-t {
            position: relative;
        }

        .border-1px-t:after {
            content: '';
            display: block;
            position: absolute;
            left: 0;
            top: 0;
            width: 100%;
            height: 1px;
            border-bottom: 1px solid RGBA(216, 219, 224, 1);
            transform-origin: top left;
            transform: scaleY(0.5);
        }

        .border-1px-b-dotted {
            position: relative;
        }

        .border-1px-b-dotted:after {
            content: '';
            display: block;
            position: absolute;
            left: 0;
            bottom: 0;
            width: 100%;
            height: 1px;
            border-bottom: 1px dashed RGBA(216, 219, 224, 1);
            transform-origin: top left;
            transform: scaleY(0.5);
        }

        html, body {
            min-height: 100%;
        }

        .mainContainer {
            width: 750px;
            height: 100%;
            margin: 0 auto;
            background-color: RGBA(248, 250, 251, 1);
        }

        .mainContainer > .header {
            position: relative;
            width: 100%;
            height: 120px;
        }

        .mainContainer > .header > img {
            position: absolute;
            object-fit: cover;
            z-index: 0;
        }

        .mainContainer > .header > h1 {
            position: relative;
            font-size: 30px;
            line-height: 120px;
            padding: 0 44px;
            color: RGBA(254, 254, 254, 1);
        }

        .mainContainer .mainWrapper {
            width: 100%;
            height: auto;
            min-height: calc(100% - 120px);
        }

        .mainContainer .mainWrapper .content {
            padding: 49px 57px 162px 44px;
        }

        .mainContainer .mainWrapper .content > .emailContent {
            width: 100%;
            box-sizing: border-box;
            padding-bottom: 20px;
        }

        .mainContainer .mainWrapper .content > .emailContent > p {
            line-height: 48px;
            font-size: 20px;
            color: RGBA(102, 102, 102, 1);
        }

        .mainContainer .mainWrapper .content > .emailContent > p.text > span {
            color: RGBA(44, 125, 250, 1);
            text-decoration: underline;
        }

        .mainContainer .mainWrapper .content > .emailContent > p.time_address > span {
            color: RGBA(255, 144, 65, 1);
        }

        .mainContainer .mainWrapper .content > .table {
            width: calc(100% - 36px);
            margin-left: 36px;
            text-align: left;
            font-size: 18px;
            line-height: 48px;
            background-color: #ffffff;
        }

        .mainContainer .mainWrapper .content > .table > thead > tr > th, .mainContainer .mainWrapper .content > .table > tbody > tr > td {
            padding: 0 25px;
            vertical-align: top;
            border-top: 1px solid #ddd;
            color: RGBA(51, 51, 51, 1);
        }

        .mainContainer .mainWrapper .content > .table > thead > tr > th {
            color: RGBA(153, 153, 153, 1);
            border: 0;
        }

        .mainContainer .mainWrapper .content > .QRCode {
            padding-top: 30px;
        }

        .mainContainer .mainWrapper .content > .QRCode > .text {
            font-size: 20px;
            line-height: 48px;
            color: RGBA(102, 102, 102, 1);
        }

        .mainContainer .mainWrapper .content > .QRCode > img {
            margin-top: 15px;
            margin-left: 40px;
        }

        .mainContainer .footer {
            position: relative;
            margin-top: -113px;
            height: 113px;
            padding: 0 57px 0 43px;
            line-height: 113px;
            font-size: 20px;
            display: flex;
            justify-content: space-between;
        }

        .mainContainer .footer > .text {
            color: RGBA(184, 188, 196, 1);
        }

        .mainContainer .footer > .text > a, .mainContainer .footer > a {
            color: RGBA(44, 125, 250, 1);
        }

    </style>
</head>
<body>
<div class="mainContainer">
    <header class="header">
        <img width=100% height=100% src="http://p5zhgy42k.bkt.clouddn.com/test/15/20180322/1523002854047.jpg" alt="">
        <h1>${activity.activityTitle}</h1>
    </header>
    <div class="mainWrapper">
        <div class="content">
            <div class="emailContent">.
                <p class="text">亲爱的映目用户：</p>

                <p class="text">
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您报名的<span>${user.nickname !'${user.username}'}</span>举办的
                <span>【${activity.activityTitle}】</span>

                <#if checkNot != 0><!--有未审核的-->
                     本次订单有部分门票需要组织者审核,请耐心等待</p>
                </#if>
                &nbsp;&nbsp;
                <p class="time_address">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;活动时间：
                    <span>${activity.startTime}</span>
                </p>
                <p class="time_address">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;活动地址：
                    <span>
                    ${address}
                    </span>
                </p>
            </div>
            <#if checkNot != 0 && check != 0><!--存在未审核的票&&存在已审核过的票-->
                <table class="table">
                    <thead>
                    <tr>
                        <th>参会人员</th>
                        <th>门票名</th>
                        <th>状态</th>
                    </tr>
                    </thead>
                    <tbody>
                        <#list ticketsRecords as ticketsRecord>
                            <#assign ticketStatus = ticketsRecord.ticketStatus/>
                            <#if ticketStatus == 3>
                                <tr>
                                    <td>${ticketsRecord.confereeName}</td>
                                    <td>${ticketsRecord.ticketsName}</td>
                                    <td>待审核</td>
                                </tr>
                            </#if>
                            <#if ticketStatus != 3>
                                <tr>
                                    <td>${ticketsRecord.confereeName}</td>
                                    <td>${ticketsRecord.ticketsName}</td>
                                    <td>已审核</td>
                                </tr>
                            </#if>
                        </#list>
                </tbody>
                </table>
             </#if>
            <div class="QRCode QRCodeWithProcess">
                <p class="text">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;准时参加活动，你可以通过以下两种方式获取电子门票：</p>
                <p class="text">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1.微信扫描二维码，同时可以接收到审核通知。</p>
                <img width="166" height="166"
                     src=${QRCode}
                     alt="">
                <#if check != 0> <!--存在已经审核的票-->
                <p class="text">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2.下载邮件里的附件（<span>${filename.pdf !'ticket.pdf'}</span>），此文件为活动验票凭证，请携带至活动现场。
                </p>
                </#if>
            </div>

        </div>
    </div>
    <footer class="footer border-1px-t">
        <p class="text">该票务由<a href="${systemDomain}">映目活动（inmuu.com）</a>提供</p>
        <a href="${SSODomain}">登录</a>
    </footer>
</div>
</body>
</html>
