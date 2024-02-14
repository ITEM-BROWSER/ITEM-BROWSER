package com.psj.itembrowser.product.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.psj.itembrowser.product.domain.dto.request.ProductUpdateDTO;
import com.psj.itembrowser.product.domain.vo.ProductImage;
import com.psj.itembrowser.product.service.FileStoreService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.psj.itembrowser.product.persistence.ProductPersistence;

@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {

    @Mock
    private ProductPersistence productPersistence;

    @Mock
    private FileStoreService fileStoreService;

    @Mock
    private FileUtil fileUtil;

    @InjectMocks
    private FileServiceImpl fileServiceImpl;

    private MockMultipartFile file;

    private static final String UPLOAD_DIR = "./src/main/resources/static/uploads/";
    List<MultipartFile> files;
    MockedStatic<FileUtil> mockedFileUtil;

    @BeforeEach
    void setUp() {
        file = new MockMultipartFile("newImage", "newTest.jpg", "image/jpeg",
            "New Test Image Content".getBytes());
        files = List.of(mock(MultipartFile.class), mock(MultipartFile.class),
            mock(MultipartFile.class));
        // Mockito.mockStatic을 사용하여 정적 메서드를 모킹할 수 있다
        mockedFileUtil = Mockito.mockStatic(FileUtil.class);
        mockedFileUtil.when(() -> FileUtil.validateImageFile(any(MultipartFile.class)))
            .thenAnswer(invocation -> null);
        mockedFileUtil.when(() -> FileUtil.validateNumberOfImageFiles(anyList()))
            .thenAnswer(invocation -> null);
    }

    @AfterEach
    void cleanup() throws IOException {
        // Mockito에서는 한 번에 하나의 MockedStatic 인스턴스만 활성화할 수 있기 때문에 MockedStatic.close()를 호출하여 닫아주어야 한다.
        mockedFileUtil.close();
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
    class CreateProduct {

        @Test
        @DisplayName("상품 이미지 생성 성공")
        void createProductImagesSuccess1() {
            // given
            Long productId = 1L;
            Path savePath = Paths.get("saved/image/path");
            when(fileStoreService.storeFile(any(MultipartFile.class))).thenReturn(savePath);

            // when
            fileServiceImpl.createProductImages(files, productId);

            // then
            verify(fileStoreService, times(files.size())).storeFile(any(MultipartFile.class));
            verify(productPersistence, times(1)).createProductImages(anyList());
        }

        @Test
        @DisplayName("데이터 베이스 저장 실패")
        void createProductImagesWhenDatabaseSaveFails() {
            // given
            Long productId = 1L;
            Path savePath = Paths.get("saved/image/path");
            when(fileStoreService.storeFile(any(MultipartFile.class))).thenReturn(savePath);

            doThrow(RuntimeException.class).when(productPersistence).createProductImages(anyList());

            // when & then
            assertThrows(RuntimeException.class,
                () -> fileServiceImpl.createProductImages(files, productId));

            verify(productPersistence, times(1)).createProductImages(anyList());
        }
    }

    @Nested
    class UpdateProductImages {

        @Test
        @DisplayName("상품 이미지 업데이트 성공")
        void updateProductImagesSuccess() {
            // given
            Long productId = 1L;
            Path savePath = Paths.get("updated/image/path");
            when(fileStoreService.storeFile(any(MultipartFile.class))).thenReturn(savePath);
            when(productPersistence.findProductImagesByImageIds(anyList())).thenReturn(List.of());

            ProductUpdateDTO productUpdateDTO = new ProductUpdateDTO();
            productUpdateDTO.setMultipartFiles(files);
            productUpdateDTO.setDeleteImageIds(List.of(1L, 2L));

            // when
            fileServiceImpl.updateProductImages(productUpdateDTO, productId);

            // then
            verify(fileStoreService, times(files.size())).storeFile(any(MultipartFile.class));
            verify(productPersistence).createProductImages(anyList());
            verify(productPersistence).deleteProductImages(anyList());
        }

        @Test
        @DisplayName("상품 이미지 업데이트 실패 - 파일 저장 중 예외 발생")
        void updateProductImagesFailureDueToFileStoreException() {
            // given
            Long productId = 1L;
            when(fileStoreService.storeFile(any(MultipartFile.class))).thenThrow(
                RuntimeException.class);
            ProductUpdateDTO productUpdateDTO = new ProductUpdateDTO();
            productUpdateDTO.setMultipartFiles(files);
            productUpdateDTO.setDeleteImageIds(List.of(1L, 2L));

            // when & then
            assertThrows(RuntimeException.class,
                () -> fileServiceImpl.updateProductImages(productUpdateDTO, productId));

            verify(productPersistence, never()).createProductImages(anyList());
            verify(productPersistence, never()).deleteProductImages(anyList());
        }
    }

    @Nested
    class DeleteProductImages {

        @Test
        @DisplayName("상품 이미지 삭제 성공")
        void deleteProductImagesSuccess() {
            // given
            Long productId = 1L;
            List<ProductImage> productImages = List.of(mock(ProductImage.class));
            when(productPersistence.findProductImagesByProductId(productId)).thenReturn(
                productImages);

            // when
            fileServiceImpl.deleteProductImages(productId);

            // then
            verify(fileStoreService, times(productImages.size())).deleteFilesInStorage(any());
            verify(productPersistence).deleteProductImages(anyList());
        }

        @Test
        @DisplayName("상품 이미지 삭제 실패 - 이미지 조회 중 예외 발생")
        void deleteProductImagesFailureDueToPersistenceException() {
            // given
            Long productId = 1L;
            when(productPersistence.findProductImagesByProductId(productId)).thenThrow(
                RuntimeException.class);

            // when & then
            assertThrows(RuntimeException.class,
                () -> fileServiceImpl.deleteProductImages(productId));

            verify(fileStoreService, never()).deleteFilesInStorage(anyString());
            verify(productPersistence, never()).deleteProductImages(anyList());
        }
    }
}