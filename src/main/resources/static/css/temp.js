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
//function addCart(){
//            var token = $("meta[name='_csrf']").attr("content");
//            var header = $("meta[name='_csrf_header']").attr("content");
//
//            var url = "/cart";
//            var paramData = {
//                itemId : $("#itemId").val(),
//                count : $("#count").val()
//            };
//
//            var param = JSON.stringify(paramData);
//
//            $.ajax({
//                url      : url,
//                type     : "POST",
//                contentType : "application/json",
//                data     : param,
//                beforeSend : function(xhr){
//                    /* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
//                    xhr.setRequestHeader(header, token);
//                },
//                dataType : "json",
//                cache   : false,
//                success  : function(result, status){
//                    alert("상품을 장바구니에 담았습니다.");
//                    location.href='/';
//                },
//                error : function(jqXHR, status, error){
//
//                    if(jqXHR.status == '401'){
//                        alert('로그인 후 이용해주세요');
//                        location.href='/members/login';
//                    } else{
//                        alert(jqXHR.responseText);
//                    }
//
//                }
//            });
//        }

//function addCart(){
//    // CSRF 토큰 및 헤더 가져오기
//    const token = document.querySelector("meta[name='_csrf']").getAttribute("content");
//    const header = document.querySelector("meta='_csrf_header'").getAttribute("content");
//
//    const url="/cart";
//
//    const paramData = {
//        itemId: document.querySelector("#itemId").value,
//        count: document.querySelector("#count").value
//    };
//
//    const param = JSON.stringify(paramData);
//
//    // XMLHttpRequest 객체 생성
//    const xhr = new XMLHttpRequest();
//    xhr.open("POST",url,true); // POST 요청 설정
//    xhr.setRequestHeader("Content-Type","application/json")// Content-Type 설정
//    xhr.setRequestHeader(header, token); // CSRF 헤더 설정
//
//    // 요청이 성공했을 때 실행되는 함수
//    xhr.onload = function(){
//        if(xhr.status === 200 ){ // HTTP 상태 코드 200일 때 성공 처리
//            alert("상품을 장바구니에 담았습니다.");
//            window.location.href = "/"; // 홈 화면으로 리다이렉트
//        }
//        else if(xhr.status === 401){
//            alert("로그인 뒤 이용해주세요."); // 로그인 필요 상태 코드
//            window.location.href = '/members/login'; // 로그인 페이지로 이동
//        }
//        else{// 기타 오류 처리
//            alert(xhr.responseText);
//        }
//    };
//
//    // 요청 전송
//    xhr.onerror = function(){
//        alert("요청 중 오류가 발생했습니다.");
//    }
//
//    xhr.send(param); // 데이터 전송
//
//}