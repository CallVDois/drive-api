package com.callv2.drive.infrastructure.file;

import java.io.InputStream;

import org.springframework.stereotype.Component;

import com.callv2.drive.domain.file.FileContentGateway;
import com.callv2.drive.infrastructure.storage.StorageService;

@Component
public class DefaultFileContentGateway implements FileContentGateway {

    private final StorageService storageService;

    public DefaultFileContentGateway(final StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public String store(final String contentName, final InputStream inputStream) {
        return storageService.store(contentName, inputStream);
    }

    @Override
    public void delete(String contentLocation) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    // @Override
    // public Content load(final File file) {
    // return storageService.load(file.getContentLocation());
    // }

    // public FileSystemResource load2(final File file) {

    // Content originalContent = storageService.load(file.getContent());

    // try {

    // // Comprime o conteúdo usando GZIP
    // ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    // GZIPOutputStream gzipStream = new GZIPOutputStream(byteStream);

    // byte[] buffer = new byte[8192];
    // int bytesRead;
    // while ((bytesRead = originalContent.inputStream().read(buffer)) != -1) {
    // gzipStream.write(buffer, 0, bytesRead);
    // }

    // gzipStream.close();
    // byte[] compressedData = byteStream.toByteArray();

    // return Content.of(new ByteArrayInputStream(compressedData),
    // compressedData.length);
    // } catch (Exception e) {
    // throw new RuntimeException("Erro ao comprimir conteúdo", e);
    // }
    // }

}
