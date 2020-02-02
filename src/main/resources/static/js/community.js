
$(function () {
    $('.j-comment-btn').click(function () {

        var data ={
            parentId: $('.j-questionId').val(),
            type: 1,
            comment: $('.j-comment').val()
        };
        if(!data.comment){
            alert("评论不能为空！");
            return;
        }
        data = JSON.stringify(data);
        $.ajax({

            contentType: "application/JSON",
            url:"/comment",
            type: "POST",
            data: data,
            success:function (rtn) {
                if(rtn.code == 200){
                    $('.j-comment-box').hide();
                    console.log(rtn);
                }else{
                    if(rtn.code == 2003){
                        var isConfirm = confirm(rtn.message);
                        if(isConfirm){
                            window.open("https://github.com/login/oauth/authorize?client_id=46292375f7531e51a887&redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                            window.localStorage.setItem("Closable","true");
                        }
                    }else{
                        alert(rtn.message);
                    }
                }
            },
            error:function (rtn) {
                console.log(rtn);
            }

        });

    })
})