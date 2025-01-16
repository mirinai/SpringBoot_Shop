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


//function updateCartItemCount(cartItemId, count){
//            // CSRF 보호를 위한 토큰 및 헤더 값 가져오기
//            var token = $("meta[name='_csrf']").attr("content"); // CSRF 토큰 값
//            var header = $("meta[name='_csrf_header']").attr("content"); // CSRF 헤더 이름
//
//           // 요청할 URL을 생성합니다. cartItemId와 count 값을 쿼리 파라미터로 전달합니다.
//            var url = "/cartItem/" + cartItemId + "?count=" + count;
//
//            // Ajax 요청을 통해 서버에 PATCH 메서드를 사용하여 수량을 업데이트합니다.
//            $.ajax({
//                url : url, // 수량 업데이트 API URL
//                type : "PATCH", // HTTP PATCH 메서드를 사용하여 데이터 일부를 수정합니다.
//                beforeSend : function(xhr){
//                    /* 데이터를 전송하기 전에 CSRF 보호를 위해 헤더에 CSRF 값을 설정합니다. */
//                    xhr.setRequestHeader(header, token); // 요청 헤더에 CSRF 토큰 값을 추가합니다.
//                },
//                dataType : "json", // 서버로부터 반환받을 데이터 타입을 JSON으로 설정합니다.
//                cache : false, // 캐시를 사용하지 않도록 설정합니다.
//                success : function(result, status){
//                    // 요청이 성공하면 로그 메시지를 출력합니다.
//                    console.log("cartItem count update success");
//                },
//                error : function(jqXHR, status, error){
//                    // 요청이 실패하면 HTTP 상태 코드를 확인하여 처리합니다.
//
//                    if(jqXHR.status == '401'){ // 401 상태: 인증되지 않은 사용자
//                        alert('로그인 뒤에 이용해주세요'); // 로그인 필요 메시지 출력
//                        location.href = '/member/login'; // 로그인 페이지로 리디렉션합니다.
//                    }
//                    else{
//                        // 그 외의 에러일 경우 서버에서 반환된 응답 메시지를 출력합니다.
//                        alert(jqXHR.responseText);
//                    }
//                }
//            });
//        }

//function deleteCartItem(obj){
//
//    var cartItemId = obj.dataset.id;
//    var token = $("meta[name='_csrf']").attr("content");
//    var header = $("meta[name='_csrf_header']");
//
//    var url = "/cartItem/"+cartItemId;
//
//    $.ajax({
//        url : url,
//        type : "DELETE",
//        beforeSend : function(xhr){
//            /* 데이터를 보내기 전에 헤더에 csrf 값을 설정 */
//            xhr.setRequestHeader(header, token);
//        },
//        dataType : "json",
//        cache : false,
//        success : function(result, status){
//            location.href='/cart';
//        },
//        error : function(jqXHR, status, error){
//            if(jqXHR.status = '401'){
//                alert('로그인 한 뒤에 이용해주세요');
//                location.href='/members/login';
//            }
//            else{
//                alert(jqXHR.responseJSON.message);
//            }
//        }
//    });
//}