function setCookie(c_name, value, expiredays){
    var exdate = new Date()
    exdate.setDate(exdate.getDate() + expiredays)
    document.cookie = c_name + "=" + escape(value) +
      ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString())
}
function getCookie(c_name){
    if (document.cookie.length > 0) {
        var	c_start = document.cookie.indexOf(c_name + "=")
        if (c_start != -1) {
        c_start = c_start + c_name.length + 1
        var c_end = document.cookie.indexOf(";", c_start)
        if (c_end == -1) c_end = document.cookie.length
        return unescape(document.cookie.substring(c_start, c_end))
    }
    }
    return ""
}
function delCookie(c_name){
    if (document.cookie.length > 0) {
        setCookie(c_name , "", -1);
    }
    return ""
}
function initRemberPWD(){
    setTimeout(function() {
    // $('#registerForm .password').on("focus", function () {
    //     // debugger;
    //     $(this).attr("type", "password");
    // });
      var username = getCookie("This is username");
      //如果用户名为空,则给表单元素赋空值
      if (username == "") {
        $('#loginForm .username').val('');
        $('#loginForm .password').val('');
        document.getElementById("iconfont").checked = false;
      }else {
        //获取对应的密码,并把用户名,密码赋值给表单
        var password = getCookie(username);
        $('#loginForm .username').val(username);
        $('#loginForm .password').val(password);
        document.getElementById("iconfont").checked = true;
      }
      $('#registerForm .username').val('');
      $('#registerForm .password').val('');
      var newUser = getCookie("This is justRegister");
      if(newUser){
        $('#loginForm .username').val(newUser);
        $('#loginForm .password').val(getCookie(newUser));
      }
    }, 300);
}

function showHelp(id, type, text, write){
    // debugger
    var parentEle = $(id+' '+type).parent();
    parentEle.addClass("warnig-light");
    type === '.smsCode'?parentEle = parentEle.parent():'';
    // .children("input").focus()
    //再做处理判断
    id === '#loginForm'
    ?(function(){
        // debugger;
        $('#login-error').text(text);
        $(id+' '+type).focus();
    })()
    :write
    ?parentEle.next(".reg-remind-text").text(text).addClass("reg-remind-normal").slideDown(200)
    :parentEle.next(".reg-remind-text").text(text).removeClass("reg-remind-normal").slideDown(200);
}
function removeHelp(id, type){
    var parentEle = $(id+' '+type).parent();
    parentEle.removeClass("warnig-light");
    type === '.smsCode'?parentEle = parentEle.parent():'';
    id === '#loginForm'
    ?$('#login-error').text('')
    :parentEle.next(".reg-remind-text").text('').slideUp(200);
}
function pwdValidate(id, type) {
    // debugger;
    var pwdReal = $(id+" "+type).val().replace(/\s+/g, '');
    var regPw=/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$/;
    if (pwdReal === '') {
        showHelp(id, type, '请输入密码')
        return false;
    } else if (!regPw.test(pwdReal)) {
        showHelp(id, type, '密码格式不正确')
        return false;
    } else {
        removeHelp(id, type)
        return true;
    }
}
function phoneValidate(id, type, flag) {
    // debugger;
    var phoneReal = $(id+" "+type).val().replace(/\s+/g, '');
    var regPhone=/^(\+86)?\s*1[3456789]\d{9}$/;
    if (phoneReal === '') {
        showHelp(id, type, '请输入手机号')
        return false;
    }else if(!regPhone.test(phoneReal)){
        showHelp(id, type, '手机格式不正确')
        return false;
    }else{
        removeHelp(id, type)
        if(id !== '#registerForm'){
            return true
        }else{
            return validPhoneFn(); 
        }
    }
}
// 检测手机号是否可用
function validPhoneFn() {
    var phoneValue = $('#registerForm .phone').val();
    var jsonParamObj = {
        'mobPhone':phoneValue,
    };
    var jsonParam = JSON.stringify(jsonParamObj);
    var phoneNeverUsed;
    $.ajax({
        url: '/validPhone',
        type: 'post',
        data: jsonParam,
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        async: false,
        success: function (data) {
            //TODO
            if (data.code === -1) {
                phoneNeverUsed = false;
                showHelp('#registerForm', '.phone', data.msg);
            } else if(data.code === 0){
                phoneNeverUsed = true;
                $('#registerForm .phone-success').show();
            }
        },
        error: function (data) {
            showHelp('#registerForm', '.phone', data.msg);
            phoneNeverUsed = false;
        }
    })
    return phoneNeverUsed;
};
// 登录时用户名和手机号正则 
function userValidate(id, type) {
    var usernameReal = $(id +" "+type).val().replace(/\s+/g, '');
    if (usernameReal === '') {
        showHelp(id, type, '请输入手机号或用户名')
        return false;
    } else {
        removeHelp(id, type)
        return true;
    }
}
// 验证码
function vertifyValidate(id, type) {
    // var codeValue = ;
    // debugger;
    var codeReal = $(id+" "+type).val().replace(/\s+/g, '');
    if (codeReal === '') {
        showHelp(id, type, '请输入验证码');
        return false;
    } else {
        removeHelp(id, type);
        return true;
    }
}

function nickNameValidate(id, type) {
    // var regUser=/^[0-9A-Za-z]{6,20}$/;
    var regUser=/^[\s\S]{1,10}$/;
    var userReal = $(id+" "+type).val().replace(/\s+/g, '');
    if (userReal === '') {
        showHelp(id, type, '请填写昵称')
        return false;
    } else if (!regUser.test(userReal)) {
        showHelp(id, type, '昵称格式不正确')
        return false;
    } else {
        removeHelp(id, type)
        return true;
    }
}

$(document).ready(function(){
    // title
    var currentHref = window.location.href.split('/')[3].split('?')[0];
    if (currentHref === 'login') {
        $("#changeTitle").html('登录 | 映目活动');
    } else if (currentHref === 'register') {
        $("#changeTitle").html('注册 | 映目活动');
    } else if (currentHref === 'resetPW') {
        $("#changeTitle").html('重置密码 | 映目活动');
    }
    // 禁止ios缩放
    document.documentElement.addEventListener('touchstart', function (event) {
        if (event.touches.length > 1) {
          event.preventDefault();
        }
      }, false);
    var lastTouchEnd = 0;
    document.documentElement.addEventListener('touchend', function (event) {
        var now = Date.now();
        if (now - lastTouchEnd <= 300) {
            event.preventDefault();
        }
        lastTouchEnd = now;
    }, false);
    // 如果是移动端，暂时隐藏微信登录按钮
    if (navigator.userAgent.match(/(iPhone|iPod|Android|ios|iPad|MicroMessenger)/i) ) {
        $(".weixinloginBtn").hide();
        $(".lineOr").hide();
    } else {
        $(".weixinloginBtn").show();
        $(".lineOr").show();
    }
    // $("#loginForm #loginCodeInput").hide();
    // 点击logo跳转首页
    $(".headLoge").click(function() {
        if(!sessionStorage.getItem("redirectURL")){
            setTimeout(function () {
                sessionStorage.setItem("redirectURL",$('#service').val());
            },300);
        }
        window.location.href = sessionStorage.getItem("redirectURL");
    })
    //  登录===========================================================================

    $("#loginForm input,#registerForm input,#nickNameForm input,#resPwdForm input,#bindForm input").focus(function(){
        var flag=$(this).parent().hasClass('warnig-light');
        !flag ? $(this).parent().addClass('focusBorder'):''
    })

    $("#loginForm input").on('input propertychange',function(e){
        $(this).parent().removeClass('warnig-light').addClass('focusBorder');
    }).blur(function(){
        $(this).parent().removeClass('focusBorder warnig-light');
    })
    
    $("#nickNameForm input").on('input propertychange',function(e){
        $(this).parent().removeClass('warnig-light').addClass('focusBorder');
        removeHelp('#nickNameForm','.'+$(this).attr("class"));
    })

    const form = ['#registerForm','#resPwdForm','#bindForm'];
    for(var i=0,len=form.length;i<len;i++){
        // debugger;
        $(form[i]+" input").on('input propertychange',{paramName:i},function(e){
            $(this).parent().removeClass('warnig-light').addClass('focusBorder');
            //密码特殊判断
            $(this).parent().hasClass('reg-pwd') ? showHelp(form[e.data.paramName],'.password','密码包含：数字、字母，6-16位字符',true):removeHelp(form[e.data.paramName],'.'+$(this).attr("class"))
            //手机号码特殊判断
            $(this).hasClass('phone') && !$(".phone-success").is(":hidden")?$(form[e.data.paramName]+' .phone-success').hide():'';
        })
        $(form[i]+" .phone").blur({paramName:i},function(e){
            // debugger;
            $(this).parent().removeClass('focusBorder');
            var phoneTest = phoneValidate(form[e.data.paramName],'.phone');
        })
        $(form[i]+" .password").blur({paramName:i},function(e){
            $(this).parent().removeClass('focusBorder');
            var pwdTest = pwdValidate(form[e.data.paramName],'.password');
        })
    
        $(form[i]+" .smsCode").blur({paramName:i},function(e) {
            $(this).parent().removeClass('focusBorder');
            vertifyValidate(form[e.data.paramName],'.smsCode');
        })
    }
    
    $("#nickNameForm .nickName").blur(function() {
        $(this).parent().removeClass('focusBorder');
        nickNameValidate('#nickNameForm','.nickName');
    })

    $("#loginForm .userInput,#registerForm .userInput,#resPwdForm .userInput,#bindForm .userInput").click(function(){
        var flag=$(this).hasClass('warnig-light');
        !flag ? $(this).addClass('focusBorder').children('input').focus():''
    })
})
// 登录
var failNum = 0;
function goLogin() {
    var userTest = userValidate('#loginForm','.username');
    if(!userTest) return;
    var pwdTest = pwdValidate('#loginForm','.password');
    if(!pwdTest) return;
    var pwdTest = true;
    if(failNum >= 3){
        pwdTest = vertifyValidate('#loginForm','.smsCode');
    } 
    if (userTest && pwdTest && pwdTest) {
        ajaxGoLogin();
    }
}
// 登录接口
function ajaxGoLogin() {
    var usernameValue = $('#loginForm .username').val();
    var passwordValue = $('#loginForm .password').val();
    var codeValue = $('#loginForm .smsCode').val();
    var jsonParamObj = {
        'username': usernameValue,
        'password': passwordValue,
        'service': $('#service').val(),
        'code': codeValue,
        'loginNum': failNum,
    };
    var jsonParam = JSON.stringify(jsonParamObj)
    $.ajax({
        url:'/doLogin',
        type:'post',
        data:jsonParam,
        dataType:'json',
        contentType: "application/json; charset=utf-8",
        async:false,
        success:function (data) {
            // 三次用户名或密码输错以后，出现验证码
            if(data.code === -1) {
                getVerify(100,50);
                if (data.msg === '用户名或密码错误！') {
                    showHelp('#loginForm', '.password', data.msg);
                    failNum ++;
                    if (failNum >= 3) {
                        $("#loginForm #loginCodeInput").show();
                    }
                }else if(data.msg === '验证码错误！'){
                    showHelp('#loginForm', '.smsCode', data.msg);
                }
            }else if(data.code === 0){
                // debugger;
                var isRemember = document.getElementById("iconfont").checked;
                sessionStorage.setItem("redirectURL",$('#service').val());
                if(isRemember == true ){
                    setCookie("This is username", usernameValue, 7);
                    setCookie(usernameValue, passwordValue, 7);
                }else{
                    delCookie("This is username");
                    delCookie(usernameValue);
                }
                var newUser = getCookie("This is justRegister");
                if(newUser){
                    delCookie("This is justRegister");
                    delCookie(newUser);
                }
                window.location.href=decodeURIComponent(sessionStorage.getItem("redirectURL"))+'?ticket='+data.ticket;
            }
        },
        error:function(data) {
            getVerify(100,50);
            $('#errorMsg span').html(data.msg);
        }
    });
}

//注册提交
function goRegister() {
    // debugger;
    var phone = phoneValidate('#registerForm','.phone');
    if(!phone) return;
    var code = vertifyValidate('#registerForm','.smsCode');
    if(!code) return;
    var pw = pwdValidate('#registerForm', '.password');
    if(!pw) return;
    if(pw && phone && code){
        ajaxGoRegister();
    }
}
// 注册接口
function ajaxGoRegister() {
    var jsonParamObj = {
        'mobPhone': $('#registerForm .phone').val(),
        'password': $('#registerForm .password').val(),
        'smsCode': $('#registerForm .smsCode').val(),
    };
    var jsonParam = JSON.stringify(jsonParamObj)
    $.ajax({
        url:'/doRegister',
        type:'post',
        data:jsonParam,
        dataType:'json',
        contentType: "application/json; charset=utf-8",
        async:false,
        success:function (data) {
            if(data.code === -1) {
                $('#reg-error').text(data.msg);
            }else{
                setCookie("This is justRegister", $('#registerForm .username').val(), 1);
                setCookie($('#registerForm .username').val(), $('#registerForm .password').val(), 1);
                window.location.href = '/nickName';
            }
        },
        error:function(data) {
            $('#reg-error').text(data.msg);
        }
    });
}
//昵称
function goNickName(){
    var nickNameTest = nickNameValidate("#nickNameForm", ".nickName");
    if (nickNameTest === true) {
        ajaxGoNickName();
    }
}

function ajaxGoNickName(){
    var value=$("#nickNameForm .nickName").val();
    // debugger;
    $.ajax({
        url:'/createNickName?nickName='+value,
        type:'post',
        dataType:'json',
        contentType: "application/json; charset=utf-8",
        async:false,
        success:function (data) { 
            //TODO
            if(data.code === -1) {
                $("#nick-error").text(data.msg)
            }else{
                $(".nickName #createSuccess").show();
                $(".nickName #createTitle").hide();
                $(".nickName #createContent").hide();
                // 倒计时3s之后的跳转
                clearInterval(timer);
                var num = 4;
                var timer = setInterval(function(){
                    num --;
                    $("#createSuccess .down-num").html(num);
                    if (num <= 0) {
                        num = 0;
                        clearInterval(timer);
                        window.location.href=decodeURIComponent(sessionStorage.getItem("redirectURL"))+'?ticket='+data.ticket;
                    }
                },1000)
            }
        },
        error:function(data) {
            $('#errorMsg span').html(data.msg);
        }
    });
}
function backHome(){
    window.location.href = sessionStorage.getItem("redirectURL");
}

// 重置密码
function goFogetPassword() {
    var phone = phoneValidate('#resPwdForm', '.phone');
    if(!phone) return;
    var code = vertifyValidate('#resPwdForm','.smsCode');
    if(!code) return;
    var pw = pwdValidate('#resPwdForm', '.password');
    if(!pw) return;
    if(pw && phone && code){
        ajaxForgetPassword();
    }
}

//重置密码接口
function ajaxForgetPassword(){
    var jsonParamObj = {
        'mobPhone':$('#resPwdForm .phone').val(),
        'password':$('#resPwdForm .password').val(),
        'smsCode':$('#resPwdForm .smsCode').val()
    };
    var jsonParam = JSON.stringify(jsonParamObj) 
    $.ajax({
        url:'/forgetPassword',
        type:'post',
        data:jsonParam,
        dataType:'json',
        contentType: "application/json; charset=utf-8",
        async:false,
        success:function(data){
            if(data.code === -1) {
                $('#reset-error').text(data.msg);
            }else{
                alert("重置密码成功")
                window.location.href='/login'
            }
        },
        error:function(data) {
            $('#errorMsg span').html(data.msg);
        }
    })
}

//获取图形验证码,指定宽和高,默认width=100,heigh=50
function getVerify(width,heigh) {
    $("#imgVerify").attr('src',"/getVerify?rand="+Math.random()+"&width="+width+"&heigh="+heigh);
}
// *****************************************************************************************************************
function setTimeFn(id, target) {
    var countdown_bind = 60;
    clearInterval(timer);
    $(id+" "+target).attr("disabled", true);
    var timer = setInterval(function() {
        countdown_bind --;
        $(id+" "+target).html("重新发送(" + countdown_bind + "s)");
        if (countdown_bind === 0) {
            $(id+" "+target).removeAttr("disabled");
            $(id+" "+target).html("发送验证码");
            clearInterval(timer);
        }
    }, 1000);
}

//发送验证码接口
function sendMessage(id, type, target) {
    var phoneValue = $(id+" "+type).val();
    var phoneTest = phoneValidate(id,type);
    if (phoneTest) {
        var jsonParam = {
            mobPhone:phoneValue
        };
        $.ajax({
            url:'/sendMessage',
            type:'post',
            data:jsonParam,
            dataType:'json',
            async:false,
            success:function (data) {
                if(data.code === -1) {
                    showHelp(id,type,data.msg)
                } else {
                    setTimeFn(id,target);
                }
            }
        });
    } else {
        return false;
    }
}
function goBind() {
    var phone = phoneValidate('#bindForm', '.phone');
    if(!phone) return;
    var code = vertifyValidate('#bindForm','.smsCode');
    if(!code) return;
    var pw = pwdValidate('#bindForm', '.password');
    if(!pw) return;
    if(pw && phone && code){
        ajaxGoWxLogin();
    }
}

function ajaxGoWxLogin() {
    var jsonParamObj = {
        'phoneumOrMail': $('#bindForm .phone').val(),
        'password': $('#bindForm .password').val(),
        'code': $('#bindForm .smsCode').val(),
        'unionId':$('#bindForm .unionId').val(),
        'nickname':$('#bindForm .nickname').val(),
        'avatar':$('#bindForm .avatar').val(),
    };
    var jsonParam = JSON.stringify(jsonParamObj)
    $.ajax({
        url:'/login/afterBind',
        type:'post',
        data:jsonParam,
        dataType:'json',
        contentType: "application/json; charset=utf-8",
        async:false,
        success:function (data) {
            if(data.code === -1) {
                $('#bind-error').text(data.msg);
            }else{
                alert(data.msg);
                window.location.href=decodeURIComponent(sessionStorage.getItem("redirectURL"))+'?ticket='+data.ticket;
            }
        },
        error:function(data) {
            getVerify(100,50);
            $('#errorMsg span').html(data.msg);
        }

    });
}
// 展示服务条款
function showDialog() {
    $('#mask').addClass('show');
    setTimeout(function(){
        $('#TermsOfService').addClass('show');
    },0);
}

function closeDialog() {
    $('#mask').removeClass('show');
    $('#TermsOfService').removeClass('show');  
}

function weixinLogin(){
    if(!sessionStorage.getItem("redirectURL")){
        setTimeout(function () {
            sessionStorage.setItem("redirectURL",$('#service').val());
        },300);
    }
    window.location.href = '/login/getCode?state=1&service='+sessionStorage.getItem("redirectURL")
}

(function(){
    
    initRemberPWD();
    if(!sessionStorage.getItem("redirectURL")){
        setTimeout(function () {
            sessionStorage.setItem("redirectURL",$('#service').val());
        },300);
    }
    document.onkeydown = function(e) { 
        // 兼容FF和IE和Opera  
        var theEvent = e || window.event;  
        var code = theEvent.keyCode || theEvent.which || theEvent.charCode;  
        // var activeElementId = document.activeElement.id;//当前处于焦点的元素的id 
        var currentHref = window.location.href.split('/')[3].split('?')[0];
        if (code === 13 && currentHref == "login") {
            goLogin();//要触发的方法  
            return false;
        }else if(code === 13 && currentHref == "register"){
            goRegister();//要触发的方法 
            return false;
        } else if (code === 13 && currentHref === 'nickName') {
            goNickName();
            return false;
        } else if (code === 13 && currentHref === 'resetPW'){
            goFogetPassword();
            return false;
        } else if (code === 13 && currentHref === 'weixin') {
            goBind();
            return false;
        }
        return true;  
    } 
})();