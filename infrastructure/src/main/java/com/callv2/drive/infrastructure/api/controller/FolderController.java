package com.callv2.drive.infrastructure.api.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.callv2.drive.application.folder.create.CreateFolderUseCase;
import com.callv2.drive.infrastructure.api.FolderAPI;
import com.callv2.drive.infrastructure.folder.adapter.FolderAdapter;
import com.callv2.drive.infrastructure.folder.model.CreateFolderRequest;
import com.callv2.drive.infrastructure.folder.model.CreateFolderResponse;
import com.callv2.drive.infrastructure.folder.presenter.FolderPresenter;

@RestController
public class FolderController implements FolderAPI {

    private final CreateFolderUseCase createFolderUseCase;

    public FolderController(final CreateFolderUseCase createFolderUseCase) {
        this.createFolderUseCase = createFolderUseCase;
    }

    @Override
    public ResponseEntity<CreateFolderResponse> create(CreateFolderRequest request) {
        final var response = FolderPresenter.present(createFolderUseCase.execute(FolderAdapter.adapt(request)));

        return ResponseEntity
                .created(URI.create("/folders/" + response.id()))
                .body(response);
    }

}
