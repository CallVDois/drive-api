package com.callv2.drive.infrastructure.api.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    public FileController(
            final CreateFileUseCase createFileUseCase,
            final GetFileUseCase getFileUseCase) {
        this.createFileUseCase = createFileUseCase;
        this.getFileUseCase = getFileUseCase;
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

}
