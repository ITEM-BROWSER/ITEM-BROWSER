package com.psj.itembrowser.product.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import com.psj.itembrowser.product.persistence.ProductPersistence;
import com.psj.itembrowser.product.service.FileService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class LocalFileServiceImplTest {

    @Mock
    private ProductPersistence productPersistence;

    @Mock
    private FileService fileService;

    @InjectMocks
    private LocalFileServiceImpl localFileService;

    private MockMultipartFile file;

    private static final String UPLOAD_DIR = "./src/main/resources/static/uploads/";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(localFileService, "uploadDir", UPLOAD_DIR);
        file = new MockMultipartFile("newImage", "newTest.jpg", "image/jpeg",
            "New Test Image Content".getBytes());
    }

    @AfterEach
    void cleanup() throws IOException {
        // 테스트에서 생성된 파일들을 삭제
        Path uploadDirPath = Paths.get(UPLOAD_DIR);
        Files.isRegularFile(uploadDirPath);
        if (Files.exists(uploadDirPath)) {
            // 지정된 디렉토리 내의 모든 파일을 탐색 하여 Path 반환
            Files.walk(uploadDirPath)
                .map(Path::toFile)
                .forEach(File::delete);
        }
    }

    @Nested
    class storeFile {

        @Mock
        private MockMultipartFile failFile;

        @Test
        @DisplayName("파일 저장 성공")
        void storeFileSuccess() throws Exception {
            // when
            Path savedFile = localFileService.storeFile(file);

            // then
            assertThat(savedFile).exists();
            assertThat(Files.exists(savedFile)).isTrue();
        }

        @Test
        @DisplayName("파일 저장 실패")
        void storeFileFailure() throws IOException {
            // given
            doThrow(IOException.class).when(failFile).transferTo(any(Path.class));

            // when & then
            assertThrows(RuntimeException.class, () -> localFileService.storeFile(failFile),
                "Failed to store file null");
        }
    }

    @Test
    @DisplayName("파일 삭제 성공")
    void deleteFilesInStorageSuccess() throws Exception {
        // given
        Path savedFile = localFileService.storeFile(file);
        String filePath = savedFile.toString();

        // when
        localFileService.deleteFilesInStorage(filePath);

        // then
        assertFalse(Files.exists(savedFile));
    }

    @Test
    @DisplayName("파일 교체 성공")
    void replaceFileInStorageSuccess() throws Exception {
        // given
        MultipartFile originalFile = new MockMultipartFile(
            "file",
            "original.txt",
            "text/plain",
            "Original content".getBytes()
        );
        MultipartFile newFile = new MockMultipartFile(
            "file",
            "new.txt",
            "text/plain",
            "New content".getBytes()
        );

        Path originalFilePath = localFileService.storeFile(originalFile);
        String originalFilePathString = originalFilePath.toString();

        // when
        Path newFilePath = localFileService.replaceFileInStorage(newFile, originalFilePathString);

        // then
        assertThat(Files.exists(
            originalFilePath)).isFalse();
        assertThat(Files.exists(newFilePath)).isTrue();

        String newFileContent = Files.readString(newFilePath);
        assertThat(newFileContent).isEqualTo("New content");
    }

}