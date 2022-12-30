//alert彈出框
function swal(icon, title){
    Swal.fire({
        position: 'center',
        icon: icon,
        title: title,
        showConfirmButton: false,
        timer: 1500
    })
}

let urlStr = location.href;
let url = new URL(urlStr);

window.onload=function () {
    let vm = new Vue({
        el: "#detail",
        data: {
            buyCount: 1,
            bid: url.searchParams.get("bid"),
            starNum:"",
            showStar:"",
            reply:"",
            replyList:""
        },
        methods: {
            buyCart:function (){
                axios({
                    method: "POST",
                    url: "/books/addCartItem",
                    params: {
                        bid: vm.bid,
                        buyCount: vm.buyCount
                    }
                }).then(function (value) {
                    if (value.data.code == 200) {
                        window.location.href='/books/member?operate=cart'
                    } else {
                        swal("warning", value.data.data.msg)
                    }
                }).catch(function (reason) {
                    console.log(reason)
                })
            },
            addCart: function () {
                axios({
                    method: "POST",
                    url: "/books/addCartItem",
                    params: {
                        bid: vm.bid,
                        buyCount: vm.buyCount
                    }
                }).then(function (value) {
                    if (value.data.code == 200) {
                        swal("success", value.data.data.msg)
                    } else {
                        swal("warning", value.data.data.msg)
                    }
                }).catch(function (reason) {

                })
            },
            minus: function () {
                if (vm.buyCount <= 1) {
                    vm.buyCount = 1;
                } else {
                    vm.buyCount--;
                }
            },
            plus: function () {
                axios({
                    method:"GET",
                    url:"/books/getBookCount",
                    params:{
                        bid:url.searchParams.get("bid")
                    }
                }).then(function (value){
                    if (vm.buyCount >= value.data.data.bookCount) {
                        vm.buyCount = value.data.data.bookCount;
                    } else {
                        vm.buyCount++;
                    }
                }).catch(function (reason){
                    console.log(reason)
                })
            },
            starScore:function (){
                $(function () {
                    var wjxs = "<i class=\"fa-solid fa-star\"></i>";
                    var wjxk = "<i class=\"fa-regular fa-star\"></i>";
                    //鼠标进入事件
                    $(".comment>li").on("mouseenter", function () {
                        $(this).html(wjxs).prevAll().html(wjxs);
                        $(this).nextAll().html(wjxk);
                    });
                    //离开事件，所有的li变成空心
                    $(".comment").on("mouseleave", function () {
                        $(this).children().html(wjxk);
                        // 找到current，让current以及current前面的变成实心
                        $("li.current").html(wjxs).prevAll().html(wjxs);
                    });

                    //给li注册点击事件，获取当前元素的位置current
                    $(".comment>li").on("click", function () {
                        $(this).addClass("current").siblings().removeClass("current");
                        let star = $(this).attr("class")
                        vm.starNum = star.split(" ")[0]
                    });
                });
            },addReply:function (){
                axios({
                    method:"POST",
                    url:"/books/addReply",
                    params:{
                        bid:vm.bid,
                        reply: vm.reply,
                        starNum: vm.starNum
                    }
                }).then(function (){
                    swal("success","已成功添加回覆")
                    vm.reply = "";
                    vm.starNum ="";
                    vm.getReply();
                }).catch(function (reason){
                    console.log(reason)
                })
            }
            ,getReply:function (){
                axios({
                    method:"POST",
                    url:"/books/getReply",
                    params:{
                        bid:vm.bid
                    }
                }).then(function (value){
                    vm.replyList = value.data.data.replyList;

                    console.log(value.data.data.replyList)
                }).catch(function (reason){
                    console.log(reason)
                })
            }

        },mounted:function (){
            this.$nextTick(()=>{
            this.starScore();
            this.getReply();
        })
        }
    })
}