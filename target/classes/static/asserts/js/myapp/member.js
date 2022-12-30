//alert彈出框
function swal(icon,title){
    Swal.fire({
        position: 'center',
        icon: icon,
        title: title,
        showConfirmButton: false,
        timer: 1500
    })
}
function swalConfirm() {
    Swal.fire({
        title: '確認要刪除嗎',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '刪除',
        cancelButtonText: '取消',
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire(
                '您已刪除此商品',

            )
        }
    })
}
//確認圖片
function check(img){
    let headImg  = img.val();
    headImg = headImg.replace("","");
    let flag = true;
    if (headImg == ""){
        flag = false
        swal('warning','請選擇圖片');
    }else {
        let size = img[0].files[0].size;
        console.log(size)
        if ((size/1000>100)){
            flag = false;
            swal('warning','圖片大小不能超過100KB')
        }
    }
    return flag
}
//顯示圖片
function showLoadImg(path) {
    let headerimg = $("#headerImg").get(0).files[0]
    let reder = new FileReader();

    reder.readAsDataURL(headerimg);
    console.log('toString=' + headerimg.toString())
    reder.onload = function (e) {
        //刪除其子元素後再創建
        $("#showImg").empty().html('<img src="/books/'+path+'" class="card-img-top" alt="">')

    }
}
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
//手機認證
function validPhone(){
    $("#phone1").addClass("hidden");
    $("#phone2").addClass("hidden");
    let phone = $("#phoneText").val();
    let regPhone = /^09[0-9]{8}$/;
    if(!regPhone.test(phone)){
        returnMsg("#phone1","invalid")
        return false;
    }else {
        returnMsg("#phone2","valid")
    }
}


let urlStr = location.href;
let url = new URL(urlStr);

window.onload = function () {
        let vm = new Vue({
            el: "#member",
            data: {
                cartItem:"",
                order:"",
                orderDetail:"",
                password:"",
                confirmPassword:"",
                msg:"",
                headerImg: "",
                operate : "profile"
            }, methods: {
                //獲得分頁操作的operate
                getOperate:function (){
                  let operate = url.searchParams.get("operate");
                  vm.operate = operate;
                },
                profile:function (){
                    vm.operate = "profile"
                },
                updatePwd:function (){
                    vm.operate = "updatePwd"
                }
                ,
                getCart:function (){
                    vm.operate ="cart"
                    vm.cart();
                }
                ,getOrder:function (){
                    vm.operate = "order"
                    vm.Order();
                }
                ,getOrderDetail:function (orderNo){
                    vm.operate = "orderDetail"
                    vm.orderDetailList(orderNo)
                }
                ,
                //確認頭像
                checkImg: function () {
                    check($("#headerImg"))
                }
                //上傳頭像
                , imgUpload: function () {
                    let formdata = new FormData();
                    formdata.append('headerImg', $("#headerImg").get(0).files[0]);
                    axios({
                        async: false,
                        method: "POST",
                        url: "/books/uploadImg",
                        dataType: 'json',
                        data: formdata,
                        contentType: false,
                        processData: false
                    }).then(function (value) {
                        if (value.data.code == 200) {
                            swal("success","上傳成功")
                            showLoadImg(value.data.data.path)
                        } else {
                            swal("warning","上傳成功")
                        }
                    }).catch(function (reason) {
                        console.log('resp=' + reason)
                    })
                }
                //更改個人檔案
                ,onsubmit:function () {
                    axios({
                        method: "POST",
                        url:"/books/updateProfile",
                        params:{
                            username: $("#emailText").val(),
                            name: $("#nameText").val(),
                            phone:$("#phoneText").val(),
                            sex: $("input:checked").val()
                        }
                    }).then(function (value){
                        if (value.data.code == 200){
                            swal("success",value.data.data.msg)
                        }else {
                            swal("warning",value.data.data.msg)
                        }
                    }).catch(function (reason){
                        console.log(reason)
                    })

                }
                //更改密碼
                ,onsubmitPwd:function (){
                    axios({
                        method: "POST",
                        url:"/books/updatePwd",
                        params:{
                            password:vm.password,
                            confirmPassword:vm.confirmPassword,
                        }
                    }).then(function (value){
                        if (value.data.code == 200){
                            swal("success","修改成功")
                        }else {
                            let msg = value.data.data.msg;
                            vm.msg = msg
                        }
                    }).catch(function (reason){
                        console.log(reason)
                    })
                },
                //獲得購物車信息
                cart:function (){
                    axios({
                        method:"POST",
                        url:"/books/cartInMember",
                    }).then(function (value){
                        vm.cartItem = value.data.data.cart;
                        console.log(vm.cartItem);
                    }).catch(function (reason){
                        console.log(reason)
                    })
                },
                //獲得訂單信息
                Order:function (){
                    axios({
                        method:"POST",
                        url:"/books/getOrder"
                    }).then(function (value){
                        vm.order = value.data.data.orderList
                    }).catch(function (reason){
                        console.log(reason)
                    })
                },
                orderDetailList:function (orderNo){
                  axios({
                      method:"POST",
                      url:"/books/getOrderItem",
                      params:{
                          orderNo: orderNo
                      }
                  }).then(function (value){
                      vm.orderDetail = value.data.data.orderItemList
                      console.log(value.data.data.orderItemList)
                  }).catch(function (reason){
                      console.log(reason)
                  })
                },
                //加減按鈕操作
                editCart:function (cartItemId,buyCount){
                    axios({
                        method:"POST",
                        url:"/books/editCart",
                        params:{
                           cartItemId :cartItemId,
                           buyCount :buyCount
                        }
                    }).then(function (){
                      vm.cart();
                    }).catch(function (reason){
                        console.log(reason)
                    })
                },
                //刪除單筆操作
                deleteCart:function (cartItemId) {
                    Swal.fire({
                        title: '確認要刪除嗎',
                        icon: 'question',
                        showCancelButton: true,
                        confirmButtonColor: '#3085d6',
                        cancelButtonColor: '#d33',
                        confirmButtonText: '刪除',
                        cancelButtonText: '取消',
                    }).then((result) => {
                        if (result.isConfirmed) {
                            axios({
                                method:"DELETE",
                                url: "/books/deleteCart/"+cartItemId
                            }).then(function (value){
                                Swal.fire(
                                    '刪除成功',
                                    '您已刪除此商品',
                                    'success'
                                )
                                vm.cart();
                            }).catch(function (reason){
                                console.log(reason)
                            })
                        }
                    })
                },
                //刪除整筆操作
                deleteAll:function (){
                    Swal.fire({
                        title: '確認要整筆刪除嗎',
                        icon: 'question',
                        showCancelButton: true,
                        confirmButtonColor: '#3085d6',
                        cancelButtonColor: '#d33',
                        confirmButtonText: '刪除',
                        cancelButtonText: '取消',
                    }).then((result) => {
                        if (result.isConfirmed) {
                            axios({
                                method:"DELETE",
                                url: "/books/deleteCart"
                            }).then(function (value){
                                Swal.fire(
                                    '刪除成功',
                                    '您已刪除整筆紀錄',
                                    'success'
                                )
                                vm.cart();
                            }).catch(function (reason){
                                console.log(reason)
                            })
                        }
                    })
                },
                //結帳操作
                checkout:function (){
                    Swal.fire({
                        title: '確認要結帳嗎',
                        icon: 'question',
                        showCancelButton: true,
                        confirmButtonColor: '#3085d6',
                        cancelButtonColor: '#d33',
                        confirmButtonText: '結帳',
                        cancelButtonText: '取消',
                    }).then((result) => {
                        if (result.isConfirmed) {
                            axios({
                                method:"POST",
                                url: "/books/checkout"
                            }).then(function (value){
                                Swal.fire(
                                    '購買成功',
                                    '可由訂單詳情查看',
                                    'success'
                                )
                                vm.cart();
                            }).catch(function (reason){
                                console.log(reason)
                            })
                        }
                    })
                }
            }
            ,mounted:function (){
                this.cart()
                //在頁面加載完成渲染頁面結束後調用方法, this.$nextTick()沒加上這個會報錯
                this.$nextTick(()=>{
                   this.getOperate();
                   if (vm.operate == 'cart'){
                       this.getCart();
                   }else {
                       this.profile();
                   }
                })

                 }
        })
    }
