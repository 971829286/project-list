<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>PDF</title>
    <!-- <link rel="stylesheet" type="text/css" href="style/reset.css" />
    <link rel="stylesheet" type="text/css" href="style/PDFTicket.css" /> -->
    <style  rel="stylesheet"  type="text/css">
        *{margin:  0;padding:0}html{font-family:"PingFang  SC","Microsoft  Yahei  UI","Microsoft  Yahei",sans-serif
                               ;color:  #333;-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%;-webkit-font-smoothing:antialiased}body{margin:0}article,aside,details,figcaption,figure,footer,header,hgroup,main,menu,nav,section,summary{display:block}audio,canvas,progress,video{display:inline-block;vertical-align:baseline}audio:not([controls]){display:none;height:0}[hidden],template{display:none}a{background-color:transparent}a:active,a:hover{outline:0}abbr[title]{border-bottom:1px  dotted}b,strong{font-weight:bold}dfn{font-style:italic}h1{font-size:2em;}mark{background:#ff0;color:#333}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sup{top:-0.5em}sub{bottom:-0.25em}img{border:0}figure{margin:1em  40px}hr{box-sizing:content-box;height:0}pre{overflow:auto}code,kbd,pre,samp{font-family:monospace,monospace;font-size:1em}button,input,optgroup,select,textarea{color:inherit;font:inherit;margin:0}button{overflow:visible}button,select{text-transform:none}button,html  input[type="button"],input[type="reset"],input[type="submit"]{-webkit-appearance:button;cursor:pointer}button[disabled],html  input[disabled]{cursor:default}button::-moz-focus-inner,input::-moz-focus-inner{border:0;padding:0}input{line-height:normal}input[type="checkbox"],input[type="radio"]{box-sizing:border-box;padding:0}input[type="number"]::-webkit-inner-spin-button,input[type="number"]::-webkit-outer-spin-button{height:auto}input[type="search"]{-webkit-appearance:textfield;box-sizing:content-box}input[type="search"]::-webkit-search-cancel-button,input[type="search"]::-webkit-search-decoration{-webkit-appearance:none}fieldset{border:1px  solid  silver;margin:0  2px;padding:.35em  .625em  .75em}legend{border:0;padding:0}textarea{overflow:auto}optgroup{font-weight:bold}table{border-collapse:collapse;border-spacing:0}td,th{padding:0}
        input,button,select,textarea{outline:none}textarea{resize:none}a{text-decoration:none;color:#333}ul{padding-left:0;list-style:none;}

        html,  body  {
            position:  relative;
            width:  100%;
            height:  100%;
            background-color:  rgba(237,  240,  243,  1);
        }
        .mainBg  {
            position:  absolute;
            top: 0;
            left:  0;
            z-index:  -100;
        }
        .mainBg img {
            position:  relative;
            display: block;
            z-index:  -100;
        }
        .mainContainer  {
            position:  relative;
            top: 0;
            left: 0;
            width:  600px;
            height:  257px;
            /*  z-index:  1;  */
        }
        .mainContainer  >  .mainContent  {
            position:  relative;
            width:  100%;
            height:  100%;
        }
        .mainContainer  >  .mainContent  >  .remindInLeft  {
            position:  absolute;
            top:  0;
            left:  0;
            width:  51px;
            height:  100%;
        }
        .mainContainer  >  .mainContent  >  .remindInLeft  >  span  {
            position:  absolute;
            top:  50%;
            left:  50%;
            transform:  translate(-50%,  -50%)  rotate(-90deg);
            white-space:  nowrap;
            font-size:  18px;
            color: rgba(202, 223, 255, 1);
        }
        .mainContainer > .mainContent > .mainInRight {
            position: absolute;
            left: 51px;
            top: 0;
            width: 549px;
            height: 100%;
            justify-content: space-between;
        }
        .mainContainer > .mainContent > .mainInRight > .layoutLeft {
            position: absolute;
            top: 0;
            left: 0;
            width: 355px;
            height: 257px;
            /* padding: 30px 30px; */
            box-sizing: border-box;
        }
        .mainContainer > .mainContent > .mainInRight > .layoutLeft > h1.title {
            position: absolute;
            top: 30px;
            left: 30px;
            width: 295px;
            font-size: 17px;
            line-height: 24px;
            color: rgba(51, 51, 51, 1);
            font-weight: 400;
        }
        .mainContainer > .mainContent > .mainInRight > .layoutLeft > .ticketMsg {
            position: absolute;
            top: 80px;
            left: 30px;
            padding-top: 20px;
            width: 298px;
            height: 140px;
        }
        .mainContainer > .mainContent > .mainInRight > .layoutLeft > .ticketMsg > .msgItem {
            height: 111px;
            float: left;
            width: 149px;
            height: 85px;
        }
        .mainContainer > .mainContent > .mainInRight > .layoutLeft > .ticketMsg > .msgItem.msgItem_1 {
            top: 0;
            left: 0;
        }
        .mainContainer > .mainContent > .mainInRight > .layoutLeft > .ticketMsg > .msgItem.msgItem_2 {
            top: 0;
            left: 194px;
        }
        .mainContainer > .mainContent > .mainInRight > .layoutLeft > .ticketMsg > .msgItem.msgItem_3 {
            top: 65px;
            left: 0;
        }
        .mainContainer > .mainContent > .mainInRight > .layoutLeft > .ticketMsg > .msgItem.msgItem_4 {
            top: 65px;
            left: 194px;
        }
        .mainContainer > .mainContent > .mainInRight > .layoutLeft > .ticketMsg > .msgItem > .title {
            font-size: 12px;
            line-height: 18px;
            color: rgba(153, 153, 153, 1);
        }
        .mainContainer > .mainContent > .mainInRight > .layoutLeft > .ticketMsg > .msgItem > .text {
            font-size: 13px;
            line-height: 18px;
            padding-top: 2px;
            color: rgba(51, 51, 51, 1);
        }
        .mainContainer > .mainContent > .mainInRight > .layoutRight {
            position: absolute;
            top: 0;
            left: 355px;
            width: 194px;
            height: 100%;
        }
        .mainContainer > .mainContent > .mainInRight > .layoutRight > .QRCode {
            position: relative;
            width: 120px;
            height: 120px;
            margin: 50px auto 20px;
        }
        .mainContainer > .mainContent > .mainInRight > .layoutRight > .code {
            position: relative;
            font-size: 20px;
            line-height: 20px;
            color: rgba(77, 77, 77, 1);
            text-align: center;
            font-weight: 600;
        }

    </style>
</head>
<body>
<div class="mainBg">
  <#list ticketsRecords as ticketsRecord>
    <img width="600" height="257" src="../ticket_bg.png">
  </#list>
</div>
<#list ticketsRecords as ticketsRecord>
<div class="mainContainer">
    <div class="mainContent">
        <div class="remindInLeft">
            <!-- <span>请将此票保管好携带至会场</span> -->
        </div>
        <div class="mainInRight">
            <div class="layoutLeft">
                <h1 class="title">${activity.activityTitle}</h1>
                <div class="ticketMsg">
                    <div class="msgItem msgItem_1">
                        <p class="title">开始时间</p>
                        <p class="text">${activity.startTime?string('yyyy-MM-dd HH:mm:ss')}</p>
                    </div>
                    <div class="msgItem msgItem_2">
                        <p class="title">活动地址</p>
                        <p class="text">${address}</p>
                    </div>
                    <div class="msgItem msgItem_3">
                        <p class="title">参会人</p>
                        <p class="text">${ticketsRecord.confereeName}</p>
                    </div>
                    <div class="msgItem msgItem_4">
                        <p class="title">票名</p>
                        <p class="text">${ticketsRecord.ticketsName}</p>
                    </div>
                </div>
            </div>
            <div class="layoutRight">
                <div class="QRCode">
                    <img width="120" height="120" src="data:image/jpeg;base64,${ticketsRecord.authCode}"/>
                </div>
                <div class="code">${ticketsRecord.signCode?c}</div><!--处理输出整形的逗号问题-->
            </div>
        </div>
    </div>
</div>
</#list>
<#--<div class="mainContainer">-->
    <#--<div class="mainContent">-->
        <#--<div class="remindInLeft">-->
            <#--<!-- <span>请将此票保管好携带至会场</span> &ndash;&gt;-->
        <#--</div>-->
        <#--<div class="mainInRight">-->
            <#--<div class="layoutLeft">-->
                <#--<h1 class="title">东软安全2018美国RSA现场直播东软安全2018美国RSA现场直播</h1>-->
                <#--<div class="ticketMsg">-->
                    <#--<div class="msgItem msgItem_1">-->
                        <#--<p class="title">开始时间</p>-->
                        <#--<p class="text">2018-04-17 14:00</p>-->
                    <#--</div>-->
                    <#--<div class="msgItem msgItem_2">-->
                        <#--<p class="title">活动地址</p>-->
                        <#--<p class="text">美国旧金山美国旧金山美国旧金山美国旧金山美国旧金山美国旧金山</p>-->
                    <#--</div>-->
                    <#--<div class="msgItem msgItem_3">-->
                        <#--<p class="title">参会人</p>-->
                        <#--<p class="text">李磊</p>-->
                    <#--</div>-->
                    <#--<div class="msgItem msgItem_4">-->
                        <#--<p class="title">票名</p>-->
                        <#--<p class="text">VIP贵宾票</p>-->
                    <#--</div>-->
                <#--</div>-->
            <#--</div>-->
            <#--<div class="layoutRight">-->
                <#--<div class="QRCode">-->
                    <#--<img width="120" height="120" src="${imgUrl}">-->
                <#--</div>-->
                <#--<div class="code">421680</div>-->
            <#--</div>-->
        <#--</div>-->
    <#--</div>-->
<#--</div>-->
</body>
</html>
