package com.shop.service;

import lombok.extern.java.Log; // lombok의 @Log 어노테이션을 import하여 Logger 객체를 자동으로 생성
import org.springframework.stereotype.Service; // 스프링의 @Service 어노테이션을 import하여 서비스 클래스임을 명시

import java.io.File; // 파일을 생성, 삭제, 존재 여부 확인하는 데 사용하는 클래스
import java.io.FileOutputStream; // 파일에 바이트 데이터를 기록하는 데 사용하는 클래스
import java.util.UUID; // 고유한 파일 이름을 생성하기 위해 사용하는 UUID 클래스

/**
 * 📘 **FileService 클래스**
 *
 * 이 클래스는 파일 업로드와 파일 삭제 기능을 담당하는 **파일 서비스 클래스**입니다.
 * @Service 어노테이션이 붙어 있어 **스프링 컨테이너에 서비스로 등록**됩니다.
 * 파일 업로드와 삭제는 보통 **상품 이미지 파일 관리**에 사용됩니다.
 */
@Service
@Log // lombok의 @Log 어노테이션으로, log.info() 메서드를 사용할 수 있습니다.
public class FileService {

    /**
     * 📘 **파일 업로드 메서드**
     *
     * @param uploadPath 업로드할 파일의 경로
     * @param originalFileName 업로드할 파일의 원래 파일명
     * @param fileData 업로드할 파일의 바이트 데이터
     * @return 업로드된 파일의 고유한 이름 (UUID + 확장자)
     * @throws Exception 입출력 예외가 발생할 수 있습니다.
     *
     * 1️⃣ **UUID로 파일 이름 생성**
     *  - 파일 이름의 중복을 방지하기 위해 **UUID**를 사용합니다.
     *  - 예: "example.jpg" → "f81d4fae-7dec-11d0-a765-00a0c91e6bf6.jpg"
     *
     * 2️⃣ **파일을 지정된 경로에 저장**
     *  - `FileOutputStream`을 사용하여 **파일의 바이트 데이터를 실제 파일로 저장**합니다.
     */
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {

        //  **경로가 없으면 디렉터리 생성**
        File directory = new File(uploadPath);
        if (!directory.exists()) {
            directory.mkdirs(); // **디렉터리 생성**
        }

        // 1️⃣ **파일 이름 생성**: UUID를 통해 고유한 파일 이름을 만듭니다.
        UUID uuid = UUID.randomUUID(); // 랜덤한 UUID 생성 (예: f81d4fae-7dec-11d0-a765-00a0c91e6bf6)

        // 2️⃣ **파일 확장자 추출**: 원래 파일의 확장자를 추출합니다.
        // 예: originalFileName이 "example.jpg"라면 extension = ".jpg"
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

        // 3️⃣ **최종 파일 이름 생성**: UUID + 확장자 조합으로 파일 이름을 생성합니다.
        // 예: "f81d4fae-7dec-11d0-a765-00a0c91e6bf6.jpg"
        String savedFileName = uuid.toString() + extension;

        // 4️⃣ **파일 전체 경로 생성**: 업로드 경로에 최종 파일 이름을 추가하여 전체 경로를 생성합니다.
        // 예: "C:/shoppingmall_project/workspace/shop/item/f81d4fae-7dec-11d0-a765-00a0c91e6bf6.jpg"
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;

        // 5️⃣ **파일에 바이트 데이터 쓰기**: FileOutputStream을 사용하여 실제 파일을 생성하고 데이터를 기록합니다.
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl); // 파일 경로에 대한 출력 스트림 생성
        fos.write(fileData); // 바이트 데이터를 파일에 씀
        fos.close(); // 스트림 닫기 (리소스 반환)

        // 6️⃣ **저장된 파일 이름 반환**: 업로드된 파일의 고유 이름을 반환합니다.
        // 예: "f81d4fae-7dec-11d0-a765-00a0c91e6bf6.jpg"
        return savedFileName;
    }

    /**
     * 📘 **파일 삭제 메서드**
     *
     * @param filePath 삭제할 파일의 전체 경로
     * @throws Exception 파일 삭제 중 예외가 발생할 수 있습니다.
     *
     * 1️⃣ **파일 경로로 파일 객체 생성**
     *  - 파일 경로를 통해 **File 객체를 생성**합니다.
     *
     * 2️⃣ **파일 존재 여부 확인 후 삭제**
     *  - 파일이 존재하면 `deleteFile.delete()`로 파일을 삭제합니다.
     *  - 파일 삭제 후, `log.info()`로 "파일을 지웠습니다." 메시지를 출력합니다.
     *  - 파일이 존재하지 않으면 **파일이 없다는 메시지를 출력**합니다.
     */
    public void deleteFile(String filePath) throws Exception {

        // 1️⃣ **파일 경로로 파일 객체 생성**: filePath로 File 객체를 생성합니다.
        File deleteFile = new File(filePath);

        // 2️⃣ **파일 존재 여부 확인**
        if(deleteFile.exists()) { // 파일이 존재할 경우
            deleteFile.delete(); // 파일 삭제
            log.info("파일을 지웠습니다."); // 로그에 파일 삭제 메시지를 기록
        } else {
            log.info("파일이 없습니다."); // 파일이 없다는 메시지를 로그에 기록
        }
    }
}
