package com.callv2.drive.infrastructure.api.controller;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.callv2.drive.application.file.content.get.GetFileContentInput;
import com.callv2.drive.application.file.content.get.GetFileContentOutput;
import com.callv2.drive.application.file.content.get.GetFileContentUseCase;
import com.callv2.drive.application.file.create.CreateFileUseCase;
import com.callv2.drive.application.file.retrieve.get.GetFileInput;
import com.callv2.drive.application.file.retrieve.get.GetFileUseCase;
import com.callv2.drive.infrastructure.api.FileAPI;
import com.callv2.drive.infrastructure.file.adapter.FileAdapter;
import com.callv2.drive.infrastructure.file.model.CreateFileResponse;
import com.callv2.drive.infrastructure.file.model.GetFileResponse;
import com.callv2.drive.infrastructure.file.presenter.FilePresenter;

@RestController
public class FileController implements FileAPI {

    private final CreateFileUseCase createFileUseCase;
    private final GetFileUseCase getFileUseCase;
    private final GetFileContentUseCase getFileContentUseCase;

    public FileController(
            final CreateFileUseCase createFileUseCase,
            final GetFileUseCase getFileUseCase,
            final GetFileContentUseCase getFileContentUseCase) {
        this.createFileUseCase = createFileUseCase;
        this.getFileUseCase = getFileUseCase;
        this.getFileContentUseCase = getFileContentUseCase;
    }

    @Override
    public ResponseEntity<CreateFileResponse> create(MultipartFile file) {

        final var response = FilePresenter.present(createFileUseCase.execute(FileAdapter.adaptCreateFileInput(file)));

        return ResponseEntity
                .created(URI.create("/files/" + response.id()))
                .body(response);
    }

    @Override
    public ResponseEntity<GetFileResponse> get(String id) {
        return ResponseEntity.ok(FilePresenter.presenter(getFileUseCase.execute(GetFileInput.from(id))));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<StreamingResponseBody> download(String id) {

        final GetFileContentOutput output = getFileContentUseCase.execute(GetFileContentInput.with(id));

        final StreamingResponseBody responseBody = outputStream -> {
            output.content().transferTo(outputStream);
        };

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + output.name() + "\"")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(output.size()))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(responseBody);
    }

}
