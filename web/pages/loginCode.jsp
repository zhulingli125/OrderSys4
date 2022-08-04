<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>${ORDER_SYS_NAME}</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!--
        <link rel="stylesheet" type="text/css" href="styles.css">
        -->
    <!-- Bootstrap -->
    <link href="bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="bootstrap/css/main.css" rel="stylesheet">

    <script type="text/javascript">

        function showTimeOut(){
            let _this=this
            console.log(_this.expireTime)
            let timer = setInterval(()=>{
                if(_this.expireTime<0){
                    clearInterval(timer)
                }else{
                    _this.expireTime--
                    _this.showTimeOut()
                }
            },1000)
        }

    function SendSms() {
            var phoneNumber = $("#phoneNumber").val();
            var param={"phoneNumber":phoneNumber}
            $.post("send.order",param,function (data) {
                if (data.state == 200){
                    alert(data.msg);
                }else {
                    alert(data.msg);
                }
            })
       }

    </script>

 <style type="text/css">
     .buttonn{
         color: red;
         display: none;
     }
     .code-overtime{
         color: red;
         display: none;
     }
 </style>
</head>

<body class="login-body">

<div class="panel login-panel">

    <form role="form" action="" method="post">

        <h2 class="text-center">点餐系统登录</h2>
        <c:if test="${not empty ERROR_MSG}">
            <div class="alert alert-danger alert-dismissible" role="alert">
                <button type="button" class="close" data-dismiss="alert"
                        aria-label="close">
                    <span aria-hidden="true">&times;</span>
                </button>
                    ${ERROR_MSG}
            </div>
        </c:if>
        <div id="userID" class="form-group login-text" style="text-align: left;margin-bottom: 10px">
            <label>手机号:</label>
            <input type="text" style="float: left" class="form-control" name="phoneNumber" id="phoneNumber" placeholder="请输入您的手机号" required autofocus >
            <button type="button"  style="float: right;border-radius: 5px;border: none;background-color:#337ab7;color:#f1f1f1 ;font-size: 10px;margin-top: 3px" id="btn">获取验证码</button>

        </div>
        <div id="passwordID"  class="form-group login-text" style="text-align: left;">
            <label>验证码:</label>
            <input type="text"  class="form-control" name="smscode" id="rCode" placeholder="输入验证码" required >

            <div  class="buttonn" id="tel-error" >请输入正确手机号</div>
            <div  class="code-overtime" id="code-overtime" >验证码已失效</div>
        </div>

        <button class="btn btn-lg btn-primary btn-block login-text" id="login-btn">登录</button>
        <a href="logout.order"><button class="btn btn-lg btn-primary btn-block login-text"
                   id="codeBtu" type="button" >账号登录</button></a>
    </form>
</div>
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="bootstrap/js/jquery-1.11.1.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="bootstrap/js/bootstrap.js"></script>
<script type="text/javascript">
    let btn = document.getElementById("btn"),
        tel = document.getElementById("phoneNumber"),
        timer = 0,
        code_overtime = document.getElementById("code-overtime"),
        num = 10;
        telError = document.getElementById("tel-error");
    let telReg = /^1[3456789]\d{9}$/;
    let login = document.getElementById("login-btn");
    //login//////////////////////////////////
    login.onclick = function(){
        if (timer>=10){
            code_overtime.style.display = 'block';
        }
        else {
            code_overtime.style.display = 'none';
            alert(timer)
           // ajax异步
            var xhr = new XMLHttpRequest();
            xhr.open("post","verify.order?phoneNumber="+document.getElementById("phoneNumber").value+"&rCode="+document.getElementById("rCode").value,true);
            xhr.onreadystatechange=function(){
                if(xhr.readyState==4&&xhr.readyState==20){
                    alert(xhr.responseText);
                }
            }
            xhr.send(null);
        }
    }
    //send////////////////////////////////////
    btn.onclick = function () {
        if (telReg.test(tel.value)){

            //ajax异步请求
            var xhr=new XMLHttpRequest();
            xhr.open("post","send.order?phoneNumber="+document.getElementById("phoneNumber").value,true);
            xhr.onreadystatechange=function(){
                if(xhr.readyState==4&&xhr.readyState==20){
                    alert(xhr.responseText);
                }
            }
            xhr.send(null);
            telError.style.display='none';
            btn.style.background = 'gray';
            settime(btn);
            timer=0;
            times();
        }else {
            telError.style.display='block';
        }
    }
    function settime(el) {
        if (num==0){
            el.removeAttribute("disabled");
            el.style.background = '#337ab7';
            el.innerHTML = '获取验证码';
            num =10;
            return ;
        }else {
            el.setAttribute("disabled",true);
            el.innerHTML = num+"s后再试";
            num--;
        }
        setTimeout(function () {
            settime(el);
        },1000);
    }

    function times() {
        if (timer<=150){
            timer++;
        }else {
            return ;
        }

        setTimeout(function () {
            times();
        },1000);
    }

</script>
</body>
</html>
