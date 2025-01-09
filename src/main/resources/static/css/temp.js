//$(document),ready(function({
//    $("#searchBtn").on("click",function(e){
//        e.preventDefault();
//        page(0);
//    })
//})

//function cancelOrder(orderId){
//    var token = $("meta[name='_csrf']").attr("content");
//    var header = $("meta[name='_csrf_header']").attr("content");
//
//    var url = "/order/"+orderId+"/cancel";
//    var paramData = {
//        orderId : orderId,
//    }
//
//    var param = JSON.stringify(paramData);
//
//    $.ajax({
//       url : url,
//       type : "POST",
//       contentType : "application/json",
//       data : param,
//       beforeSend : function(xhr){
//            /* 데이터에 전송하기 전에 헤더에 csrf 값을 설정 */
//            xhr.setRequestHeader(header,token);
//       },
//       dataType : "json",
//       cache : false,
//       success : function(result, status){
//            alert("주문이 취소되었습니다.");
//            location.href='/orders/'+[[${page}]];
//       },
//       error : function(jqXHR, status, error){
//            if(jqXHR.status == '401'){
//                alert('로그인 뒤 이용해주세요');
//                location.href='/members/login';
//            }
//            else{
//                alert(jqXHR.responseText);
//            }
//       }
//    });
//}