
//顯示校驗結果的提示信息
function returnMsg(ele,status){
    if ("invalid" == status){
        $(ele).removeClass("hidden");
    }else if ("valid" == status){
        $(ele).removeClass("hidden");
    }
}
//姓名驗證
function validName(){
    $("#name1").addClass("hidden");
    $("#name2").addClass("hidden");
    let name = $("#nameText").val();
    let regName = /(^[a-zA-Z0-9_-]{6,16}$)|(^[\u2E80-\u9FFF]{2,5})/;
    if(!regName.test(name)){
       returnMsg("#name1","invalid")
        return false;
    }else {
        returnMsg("#name2","valid")
    }
}
//密碼驗證
function validPassword(){
    $("#pwd1").addClass("hidden");
    $("#pwd2").addClass("hidden");
    let password = $("#passwordText").val();
    let regPassword = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9a-zA-Z]{8,16}$/;
    if (!regPassword.test(password)){
        returnMsg("#pwd1","invalid")
        return false;
    }else {
        returnMsg("#pwd2","valid")
    }
}
//確認密碼驗證
function validCheckPassword() {
    $("#ckpwd1").addClass("hidden");
    $("#ckpwd2").addClass("hidden");
    let password = $("#passwordText").val();
    let checkPassword = $("#pwdCheckText").val();
    if (password != checkPassword) {
        returnMsg("#ckpwd1", "invalid")
        return false;
    } else {
        returnMsg("#ckpwd2", "valid")
    }
}

//從url上獲取參數
let urlStr = location.href;
let url = new URL(urlStr);

function countDown(){
        const Toast = Swal.mixin({
            position: 'center',
            timer: 3000,
            timerProgressBar: true,
        })
        Toast.fire({
            icon: 'success',
            title: '註冊成功'
        }).then(function () {
            location.href="/books/index";
        })
    }

    window.onload = function () {
        let vm = new Vue({
            el: "#register",
            data: {
                name: "",
                password: "",
                checkPassword: "",
                verifyCode: "",
                msg: "",
                kaptMsg:"",
                token:url.searchParams.get("token")
            }, methods: {
                onsubmit: function () {
                    axios({
                        method: "POST",
                        url: "/books/registerInfo",
                        params: {
                            name: vm.name,
                            password: vm.password,
                            checkPassword: vm.checkPassword,
                            verifyCode: vm.verifyCode,
                            token: vm.token
                        }
                    }).then(function (value) {
                        if (value.data.code == 200){
                             countDown();
                        }else {
                            $("#msg").removeClass("color-green color-danger").next("i").remove();
                            let msg = value.data.data.msg
                            vm.msg = msg;
                            let icon = $("<i></i>").addClass("color-danger fa-solid fa-circle-exclamation")
                            $("#msg").addClass("color-danger").after(icon)
                        }
                    }).catch(function (reason) {

                    })
                },
                kaptcha:function (){
                    axios({
                        method: "POST",
                        url: "/books/kaptchaValid",
                        params: {
                            verifyCode: vm.verifyCode
                        }
                    }).then(function (value){
                        if (value.data.code == 200){
                            $("#kaptcha").removeClass("color-green color-danger").next("i").remove();
                            let kaptcha = value.data.data.kaptMsg
                            vm.kaptMsg = kaptcha;
                            let icon = $("<i></i>").addClass("color-green fa-solid fa-check")
                            $("#kaptcha").addClass("color-green ").after(icon)
                        }else {
                            $("#kaptcha").removeClass("color-green color-danger").next("i").remove();
                            let kaptcha = value.data.data.kaptMsg
                            vm.kaptMsg = kaptcha;
                            let icon = $("<i></i>").addClass("color-danger fa-solid fa-circle-exclamation")
                            $("#kaptcha").addClass("color-danger").after(icon)
                        }
                    }).catch(function (reason){})
                }
            }
        })
    }
