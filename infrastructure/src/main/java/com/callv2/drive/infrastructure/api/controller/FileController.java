package com.callv2.drive.infrastructure.api.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.callv2.drive.application.file.create.CreateFileUseCase;
import com.callv2.drive.infrastructure.api.FileAPI;
import com.callv2.drive.infrastructure.file.adapter.FileAdapter;
import com.callv2.drive.infrastructure.file.model.FileResponse;
import com.callv2.drive.infrastructure.file.presenter.FilePresenter;

@RestController
public class FileController implements FileAPI {

    private final CreateFileUseCase createFileUseCase;

    public FileController(final CreateFileUseCase createFileUseCase) {
        this.createFileUseCase = createFileUseCase;
    }

    @Override
    public ResponseEntity<FileResponse> create(MultipartFile file) {

        final var response = FilePresenter.present(createFileUseCase.execute(FileAdapter.adaptCreateFileInput(file)));

        return ResponseEntity
                .created(URI.create("/files/" + response.id()))
                .body(response);
    }

}
