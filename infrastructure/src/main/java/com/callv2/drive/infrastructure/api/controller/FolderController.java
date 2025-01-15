package com.callv2.drive.infrastructure.api.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.callv2.drive.application.folder.create.CreateFolderUseCase;
import com.callv2.drive.application.folder.move.MoveFolderInput;
import com.callv2.drive.application.folder.move.MoveFolderUseCase;
import com.callv2.drive.application.folder.retrieve.get.GetFolderUseCase;
import com.callv2.drive.infrastructure.api.FolderAPI;
import com.callv2.drive.infrastructure.folder.adapter.FolderAdapter;
import com.callv2.drive.infrastructure.folder.model.CreateFolderRequest;
import com.callv2.drive.infrastructure.folder.model.CreateFolderResponse;
import com.callv2.drive.infrastructure.folder.model.GetFolderResponse;
import com.callv2.drive.infrastructure.folder.model.MoveFolderRequest;
import com.callv2.drive.infrastructure.folder.presenter.FolderPresenter;

@RestController
public class FolderController implements FolderAPI {

    private final CreateFolderUseCase createFolderUseCase;
    private final GetFolderUseCase getFolderUseCase;
    private final MoveFolderUseCase moveFolderUseCase;

    public FolderController(
            final CreateFolderUseCase createFolderUseCase,
            final GetFolderUseCase getFolderUseCase,
            final MoveFolderUseCase moveFolderUseCase) {
        this.createFolderUseCase = createFolderUseCase;
        this.getFolderUseCase = getFolderUseCase;
        this.moveFolderUseCase = moveFolderUseCase;
    }

    @Override
    public ResponseEntity<CreateFolderResponse> create(CreateFolderRequest request) {
        final var response = FolderPresenter.present(createFolderUseCase.execute(FolderAdapter.adapt(request)));

        return ResponseEntity
                .created(URI.create("/folders/" + response.id()))
                .body(response);
    }

    @Override
    public ResponseEntity<GetFolderResponse> getById(UUID id) {
        return ResponseEntity
                .ok(FolderPresenter.present(getFolderUseCase.execute(FolderAdapter.adapt(id))));
    }

    @Override
    public ResponseEntity<Void> move(UUID id, MoveFolderRequest request) {
        moveFolderUseCase.execute(MoveFolderInput.with(id, request.newParentId()));
        return ResponseEntity.noContent().build();
    }

}
