package com.callv2.drive.infrastructure.api.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import com.callv2.drive.application.folder.usecase.create.CreateFolderUseCase;
import com.callv2.drive.application.folder.usecase.delete.DeleteFolderInput;
import com.callv2.drive.application.folder.usecase.delete.DeleteFolderUseCase;
import com.callv2.drive.application.folder.usecase.move.MoveFolderInput;
import com.callv2.drive.application.folder.usecase.move.MoveFolderUseCase;
import com.callv2.drive.application.folder.usecase.retrieve.get.GetFolderUseCase;
import com.callv2.drive.application.folder.usecase.retrieve.get.root.GetRootFolderInput;
import com.callv2.drive.application.folder.usecase.retrieve.get.root.GetRootFolderUseCase;
import com.callv2.drive.application.folder.usecase.retrieve.list.FolderListInput;
import com.callv2.drive.application.folder.usecase.retrieve.list.ListFoldersUseCase;
import com.callv2.drive.application.folder.usecase.sharing.create.CreateFolderSharingInput;
import com.callv2.drive.application.folder.usecase.sharing.create.CreateFolderSharingUseCase;
import com.callv2.drive.application.folder.usecase.update.name.UpdateFolderNameInput;
import com.callv2.drive.application.folder.usecase.update.name.UpdateFolderNameUseCase;
import com.callv2.drive.domain.pagination.Filter;
import com.callv2.drive.domain.pagination.Page;
import com.callv2.drive.domain.pagination.Pagination;
import com.callv2.drive.domain.pagination.SearchQuery;
import com.callv2.drive.infrastructure.api.FolderAPI;
import com.callv2.drive.infrastructure.filter.adapter.QueryAdapter;
import com.callv2.drive.infrastructure.folder.adapter.FolderAdapter;
import com.callv2.drive.infrastructure.folder.filter.FolderField;
import com.callv2.drive.infrastructure.folder.model.CreateFolderRequest;
import com.callv2.drive.infrastructure.folder.model.CreateFolderResponse;
import com.callv2.drive.infrastructure.folder.model.FolderListResponse;
import com.callv2.drive.infrastructure.folder.model.GetFolderResponse;
import com.callv2.drive.infrastructure.folder.model.MoveFolderRequest;
import com.callv2.drive.infrastructure.folder.model.ShareFolderRequest;
import com.callv2.drive.infrastructure.folder.presenter.FolderPresenter;
import com.callv2.drive.infrastructure.security.SecurityContext;

@RestController
public class FolderController implements FolderAPI {

    private final GetRootFolderUseCase getRootFolderUseCase;
    private final CreateFolderUseCase createFolderUseCase;
    private final GetFolderUseCase getFolderUseCase;
    private final MoveFolderUseCase moveFolderUseCase;
    private final ListFoldersUseCase listFoldersUseCase;
    private final UpdateFolderNameUseCase updateFolderNameUseCase;
    private final DeleteFolderUseCase deleteFolderUseCase;
    private final CreateFolderSharingUseCase createFolderSharingUseCase;

    public FolderController(
            final GetRootFolderUseCase getRootFolderUseCase,
            final CreateFolderUseCase createFolderUseCase,
            final GetFolderUseCase getFolderUseCase,
            final MoveFolderUseCase moveFolderUseCase,
            final ListFoldersUseCase listFoldersUseCase,
            final UpdateFolderNameUseCase updateFolderNameUseCase,
            final DeleteFolderUseCase deleteFolderUseCase,
            final CreateFolderSharingUseCase createFolderSharingUseCase) {
        this.getRootFolderUseCase = getRootFolderUseCase;
        this.createFolderUseCase = createFolderUseCase;
        this.getFolderUseCase = getFolderUseCase;
        this.moveFolderUseCase = moveFolderUseCase;
        this.listFoldersUseCase = listFoldersUseCase;
        this.updateFolderNameUseCase = updateFolderNameUseCase;
        this.deleteFolderUseCase = deleteFolderUseCase;
        this.createFolderSharingUseCase = createFolderSharingUseCase;
    }

    @Override
    public ResponseEntity<GetFolderResponse> getRoot() {
        return ResponseEntity.ok(FolderPresenter.present(
                getRootFolderUseCase.execute(GetRootFolderInput.from(SecurityContext.getAuthenticatedUserId()))));
    }

    @Override
    public ResponseEntity<CreateFolderResponse> create(final CreateFolderRequest request) {
        final UUID ownerId = SecurityContext.getAuthenticatedUserId();
        final var response = FolderPresenter
                .present(createFolderUseCase.execute(FolderAdapter.adapt(request, ownerId)));

        return ResponseEntity
                .created(URI.create("/folders/" + response.id()))
                .body(response);
    }

    @Override
    public ResponseEntity<GetFolderResponse> getById(final UUID id) {

        final var actorId = SecurityContext.getAuthenticatedUserId();

        return ResponseEntity
                .ok(FolderPresenter.present(getFolderUseCase.execute(FolderAdapter.adapt(id, actorId))));
    }

    @Override
    public ResponseEntity<Void> move(final UUID id, final MoveFolderRequest request) {
        final var actorId = SecurityContext.getAuthenticatedUserId();
        moveFolderUseCase.execute(MoveFolderInput.with(id, request.newParentId(), actorId));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Page<FolderListResponse>> list(
            final int page,
            final int perPage,
            final FolderField orderField,
            final Pagination.Order.Direction orderDirection,
            final Filter.Operator filterOperator,
            final List<String> filterGroups) {

        final List<Filter.Group> searchFilterGroups = filterGroups == null ? List.of()
                : filterGroups
                        .stream()
                        .map(source -> QueryAdapter.of(
                                source,
                                List.of(FolderField.values())))
                        .toList();

        final SearchQuery query = SearchQuery.of(
                Pagination.of(page, perPage, Pagination.Order.of(orderField, orderDirection)),
                filterOperator,
                searchFilterGroups);

        final var actorId = SecurityContext.getAuthenticatedUserId();

        return ResponseEntity
                .ok(listFoldersUseCase.execute(new FolderListInput(actorId, query)).map(FolderPresenter::present));
    }

    @Override
    public ResponseEntity<Void> changeName(UUID id, String newName) {

        final var actorId = SecurityContext.getAuthenticatedUserId();

        this.updateFolderNameUseCase.execute(new UpdateFolderNameInput(id, newName, actorId));

        return ResponseEntity.noContent().build();
    }

    @Transactional
    @Override
    public ResponseEntity<Void> delete(UUID id) {

        final var memberId = SecurityContext.getAuthenticatedUserId();

        this.deleteFolderUseCase.execute(new DeleteFolderInput(id, memberId));

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> shareFolder(UUID id, ShareFolderRequest request) {

        final var actorId = SecurityContext.getAuthenticatedUserId();

        this.createFolderSharingUseCase.execute(
                CreateFolderSharingInput.with(
                        id,
                        actorId,
                        request.grantee(),
                        request.accessPermission()));

        return ResponseEntity.noContent().build();

    }

}
