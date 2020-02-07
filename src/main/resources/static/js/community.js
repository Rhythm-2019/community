
$(function () {
    //提问
    $('.j-comment-btn').click(function () {
        console.log("到这了")
        var data ={
            parentId: $('.j-questionId').val(),
            type: 1,
            comment: $('.j-comment').val()
        };
        if(!data.comment){
            alert("评论不能为空！");
            return;
        }
        var isOk = addCommentOrReply(data);
        if (isOk == true){
            $('.j-comment-box').hide();
        }
    })
    //默认展示第一个标签的内容
    selectTag($('.tag-label')[0]);

})

//增加回复/评论的接口方法
function addCommentOrReply (data) {

    data = JSON.stringify(data);
    $.ajax({

        contentType: "application/JSON",
        url:"/comment",
        type: "POST",
        data: data,
        success:function (rtn) {
            if(rtn.code == 200){
                window.location.reload();
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
}
//获取二级评论的接口
function getReply (id) {

    $.ajax({
        contentType: "application/JSON",
        url:"/comment/" +id,
        type: "GET",
        success:function (rtn) {
            if(rtn.code == 200){

               if(rtn.data.length != 0){
                   for (var i in rtn.data) {
                       console.log(rtn.data[i]);
                       var liHtml = "<div class='media f-m20'><div class='media-left'><span><img class='media-object img-rounded" +
                           " u-img-logo' src="+ rtn.data[i].user.avatarUrl + "></span> </div> <div class='media-body'>" +
                           " <h4 class='media-heading'> <p> <span class='f-fs15 f-cb'>" + rtn.data[i].user.name + " </span> <span " +
                           "class='f-fs15 f-fr f-cg'>" + getTime(rtn.data[i].gmtCreate) + "</span></p> <p class='f-fs15 f-wbwa u-content'>" +
                           rtn.data[i].comment + "</p> </h4> </div> </div>";
                       $('#reply-list-' + id).append(liHtml);
                   }
               }else{
                   var liHtml = "<div class=' f-tac'><sapn class='f-fs15 f-cg'>暂时没有评论哦</span></div>";
                   $('#reply-list-' + id).append(liHtml);
               }
            }
        },
        error:function (rtn) {
            console.log(rtn);
        }

    });
}
//控制二级评论的开关
function openCollapse(e) {
    var on = $(e).attr("data-status");
    var id = $(e).attr("data-id");
    if (on == "on"){
        //已经开启了
        $('#commentbox-' + id).removeClass("in");
        $(e).attr("data-status","off");


    }else{
        //还没打开

        $('#commentbox-' + id).addClass("in");
        $(e).attr("data-status","on");
     //   if($('#reply-list-' + id).children().length <){}
        getReply(id);


    }
}
function submitReply(e) {
    var id = $(e).attr("data-id");
    var data ={
        parentId: id,
        type: 2,
        comment: $('#reply-' + id).val()
    };
    if(!data.comment){
        alert("回复不能为空");
        return
    }
    addCommentOrReply(data);
}
function getTime(srcTime) {
    var tartime = new Date(srcTime);
    return tartime.getFullYear() + "-" + (tartime.getMonth()+1) + "-" + tartime.getDate() + ' ' +
            tartime.getHours() + ":" + tartime.getMinutes();
    
}

//打开标签栏
function openTagBox() {
    $("#tag-box").show();

//关闭标签栏
}
function closeTagBox() {
    $("#tag-box").hide();
}

//添加到输入框内
function addTag(e) {
    var oldTag = $("#tag-input").val();
    var tag = $(e).text();
    if(oldTag.indexOf(tag) != -1){

        return
    }else{
        if(oldTag==""){
            $("#tag-input").val(tag);
        }else {
            $("#tag-input").val(oldTag + "," + tag);
        }
    }

}

function selectTag(e) {
    var type = $(e).attr("data-type");

    //选中标签类别
    $('.tag-label').removeClass("active");
    $(e).addClass("active");
    console.log($(e));
    //展示标签
    $(".tag-all").hide();
    $('.tag-' + type).show();
}
