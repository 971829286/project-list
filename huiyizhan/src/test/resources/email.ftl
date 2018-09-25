<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>email</title>
  <link rel="stylesheet" type="text/css" href="style/reset.css" />
  <link rel="stylesheet" type="text/css" href="style/email.css" />
</head>
<body>
  <div class="mainContainer">
    <header class="header">
      <img width=100% height=100% src="http://p5zhgy42k.bkt.clouddn.com/test/15/20180322/1523002854047.jpg" alt="">
      <h1>2018第五届中国国际咖啡展览会</h1>
    </header>
    <div class="mainWrapper">
      <div class="content">
        <div class="emailContent">
          <p class="text">亲爱的映目用户：</p>
          <p class="text">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您报名的<span>${user.nickname}</span>举办的<span>【${activity.activityTitle}】</span>正在审核</p>
          <p class="time_address">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;时间：<span>${activity.startTime?string("yyyy-MM-dd HH:mm:ss")}</span></p>
          <p class="time_address">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;地址：<span>${activity.activityAddress!'请咨询活动主办方'}</span></p>
        </div>
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
            <tr>
              <td>${ticketsRecord.confereeName}</td>
              <td>${ticketsRecord.ticketsName}</td>
              <#assign ticketStatus = ticketsRecord.ticketStatus/>
              <#if ticketStatus == 0>
                <td>未生成</td>
              </#if>
              <#if ticketStatus == 1>
                <td>未签到</td>
              </#if>
              <#if ticketStatus == 2>
                <td>已签到</td>
              </#if>
              <#if ticketStatus == 3>
                <td>待审核</td>
              </#if>
              <#if ticketStatus == 4>
                <td>审核未通过</td>
              </#if>
              <#if ticketStatus == 9>
                <td>已退票</td>
              </#if>
            </tr>
            </#list>
          </tbody>
        </table>
        <div class="QRCode">
          <p class="text">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;微信扫描获取审核通知：</p>
          <img width="166" height="166" src="https://gss0.bdstatic.com/94o3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=fa9140accd95d143ce7bec711299e967/2934349b033b5bb571dc8c5133d3d539b600bc12.jpg" alt="">
        </div>
        <div class="QRCode QRCodeWithProcess">
          <p class="text">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;准时参加活动，你可以通过以下两种方式获取电子门票：</p>
          <p class="text">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1.微信扫描二维码获取门票。</p>
          <img width="166" height="166" src="https://gss0.bdstatic.com/94o3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=fa9140accd95d143ce7bec711299e967/2934349b033b5bb571dc8c5133d3d539b600bc12.jpg" alt="">
          <p class="text">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2.下载邮件里的附件（<span>AGE_324534.pdf</span>），此文件为活动验票凭证，请携带至活动现场。</p>
        </div>
      </div>
    </div>
    <footer class="footer border-1px-t">
      <p class="text">该票务由<a href="">映目活动（inmu.cc）</a>提供</p>
      <a href="">登录</a>
    </footer>
  </div>
</body>
</html>
