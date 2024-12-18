package com.shop.config;

import org.springframework.beans.factory.annotation.Value; // @Value 어노테이션을 import하여 프로퍼티의 값을 불러오기 위해 사용
import org.springframework.context.annotation.Configuration; // @Configuration 어노테이션을 import하여 스프링의 설정 클래스로 인식하게 함
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry; // 리소스 핸들러를 등록하는 데 필요한 클래스 import
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer; // WebMvcConfigurer 인터페이스를 import하여 MVC 설정을 커스터마이징할 수 있도록 함

/**
 * WebMvcConfig 클래스는 스프링 MVC의 리소스 핸들러(Resource Handler)를 설정하는 클래스입니다.
 * @Configuration 어노테이션이 붙어 있어, 스프링 부트 애플리케이션 실행 시
 * 이 클래스를 설정 파일로 인식하고 자동으로 Bean으로 등록합니다.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer { // WebMvcConfigurer 인터페이스를 구현하여 MVC 관련 설정을 재정의합니다.

    // application.properties 또는 application.yml에 정의된 "uploadPath" 값을 불러와 변수에 주입합니다.
    @Value("${uploadPath}") // @Value 어노테이션을 사용하여 프로퍼티 파일의 uploadPath 값을 주입
            String uploadPath; // 업로드된 파일이 저장될 경로를 저장하는 변수

    /**
     * addResourceHandlers 메서드는 리소스 핸들러를 추가하는 역할을 합니다.
     *
     * @param registry - 리소스 핸들러를 등록할 때 사용하는 객체
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){

        /**
         * 1. registry.addResourceHandler("/images/**")
         *    - 클라이언트가 브라우저에서 접근할 URL 경로 패턴을 정의합니다.
         *    - "/images/**"는 images 디렉터리 하위의 모든 파일에 접근할 수 있도록 설정합니다.
         *    - 예: http://localhost:8080/images/파일명 형식으로 접근할 수 있습니다.
         *
         * 2. addResourceLocations(uploadPath)
         *    - 실제 서버의 파일 시스템 경로를 매핑합니다.
         *    - uploadPath는 application.properties 또는 application.yml에 정의된 업로드 경로입니다.
         *    - 예를 들어, uploadPath가 "file:///C:/shoppingmall_project/workspace/shop/" 로 설정되었다면
         *      클라이언트는 /images/ 경로로 접근할 수 있지만, 실제 파일은 "C:/shoppingmall_project/workspace/shop/" 경로에 저장됩니다.
         *    - 즉, URL과 서버의 물리적 경로를 매핑하여 클라이언트가 직접 파일 경로를 알지 못해도
         *      외부에서 파일에 접근할 수 있도록 해줍니다.
         */
        registry.addResourceHandler("/images/**") // 클라이언트가 URL로 접근할 경로 (/images/ 하위 경로의 모든 파일에 접근)
                .addResourceLocations(uploadPath); // 실제 서버의 물리 경로를 매핑 (예: file:///C:/shoppingmall_project/workspace/shop/)
    }
}
