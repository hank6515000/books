window.onload = function (){
    let vm = new Vue({
        el:"#contact",
        data:{
            name:"",
            email:"",
            message:""
        },
        methods:{
            getUserInfo:function(){
                axios({
                    method:"POST",
                    url:"/books/getUserInfo",
                }).then(function (value){
                    vm.name = value.data.data.user.name
                    vm.email = value.data.data.user.username
                }).catch(function (reason){
                    console.log(reason)
                })

            },
            contact:function (){
                axios({
                    method:"POST",
                    url:"/books/getContact",
                    params:{
                        name : vm.name,
                        email : vm.email,
                        message : vm.message
                    }
                }).then(function (){
                        Swal.fire({
                            position: 'center',
                            icon:'success',
                            title:"發送成功 ",
                            text:"感謝您的寶貴意見",
                            width: 500,
                            color: 'rgba(70,70,70,0.93)',
                            backdrop: `
                            rgba(70,70,70,0.93)
                            url("asserts/image/9LT14G0BMH2_cNUgfB77.gif")
                            top
                            no-repeat
                            `
                        })

                }).catch(function (reason){
                    console.log(reason)
                })
            }
        },mounted:function (){
            this.getUserInfo();
        }
    })
}