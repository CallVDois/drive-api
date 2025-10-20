package com.callv2.drive.infrastructure.api.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.callv2.drive.application.file.usecase.content.get.GetFileContentInput;
import com.callv2.drive.application.file.usecase.content.get.GetFileContentOutput;
import com.callv2.drive.application.file.usecase.content.get.GetFileContentUseCase;
import com.callv2.drive.application.file.usecase.create.CreateFileUseCase;
import com.callv2.drive.application.file.usecase.delete.DeleteFileInput;
import com.callv2.drive.application.file.usecase.delete.DeleteFileUseCase;
import com.callv2.drive.application.file.usecase.retrieve.get.GetFileInput;
import com.callv2.drive.application.file.usecase.retrieve.get.GetFileUseCase;
import com.callv2.drive.application.file.usecase.retrieve.list.FileListInput;
import com.callv2.drive.application.file.usecase.retrieve.list.ListFilesUseCase;
import com.callv2.drive.application.file.usecase.sharing.create.CreateFileSharingUseCase;
import com.callv2.drive.application.file.usecase.sharing.remove.RemoveFileSharingInput;
import com.callv2.drive.application.file.usecase.sharing.remove.RemoveFileSharingUseCase;
import com.callv2.drive.application.file.usecase.sharing.retrieve.list.ListFileSharingInput;
import com.callv2.drive.application.file.usecase.sharing.retrieve.list.ListFileSharingUseCase;
import com.callv2.drive.domain.pagination.Filter;
import com.callv2.drive.domain.pagination.Page;
import com.callv2.drive.domain.pagination.Pagination;
import com.callv2.drive.domain.pagination.SearchQuery;
import com.callv2.drive.infrastructure.api.FileAPI;
import com.callv2.drive.infrastructure.file.adapter.FileAdapter;
import com.callv2.drive.infrastructure.file.filter.FileField;
import com.callv2.drive.infrastructure.file.model.CreateFileResponse;
import com.callv2.drive.infrastructure.file.model.FileListResponse;
import com.callv2.drive.infrastructure.file.model.FileSharingListResponse;
import com.callv2.drive.infrastructure.file.model.GetFileResponse;
import com.callv2.drive.infrastructure.file.model.ShareFileRequest;
import com.callv2.drive.infrastructure.file.presenter.FilePresenter;
import com.callv2.drive.infrastructure.filter.adapter.QueryAdapter;
import com.callv2.drive.infrastructure.security.SecurityContext;

@RestController
public class FileController implements FileAPI {

    private final CreateFileUseCase createFileUseCase;
    private final DeleteFileUseCase deleteFileUseCase;
    private final GetFileUseCase getFileUseCase;
    private final GetFileContentUseCase getFileContentUseCase;
    private final ListFilesUseCase listFilesUseCase;
    private final CreateFileSharingUseCase createFileSharingUseCase;
    private final RemoveFileSharingUseCase removeFileSharingUseCase;
    private final ListFileSharingUseCase listFileSharingUseCase;

    public FileController(
            final CreateFileUseCase createFileUseCase,
            final DeleteFileUseCase deleteFileUseCase,
            final GetFileUseCase getFileUseCase,
            final GetFileContentUseCase getFileContentUseCase,
            final ListFilesUseCase listFilesUseCase,
            final CreateFileSharingUseCase createFileSharingUseCase,
            final RemoveFileSharingUseCase removeFileSharingUseCase,
            final ListFileSharingUseCase listFileSharingUseCase) {
        this.createFileUseCase = createFileUseCase;
        this.deleteFileUseCase = deleteFileUseCase;
        this.getFileUseCase = getFileUseCase;
        this.getFileContentUseCase = getFileContentUseCase;
        this.listFilesUseCase = listFilesUseCase;
        this.createFileSharingUseCase = createFileSharingUseCase;
        this.removeFileSharingUseCase = removeFileSharingUseCase;
        this.listFileSharingUseCase = listFileSharingUseCase;
    }

    @Override
    public ResponseEntity<CreateFileResponse> create(UUID folderId, MultipartFile file) {

        final var ownerId = SecurityContext.getAuthenticatedUserId();

        final var response = FilePresenter
                .present(createFileUseCase.execute(FileAdapter.adapt(ownerId, folderId, file)));

        return ResponseEntity
                .created(URI.create("/files/" + response.id()))
                .body(response);
    }

    @Override
    public ResponseEntity<Void> delete(UUID id) {
        final var deleterId = SecurityContext.getAuthenticatedUserId();

        DeleteFileInput deleteFileInput = DeleteFileInput.of(deleterId, id);

        deleteFileUseCase.execute(deleteFileInput);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<GetFileResponse> getById(UUID id) {

        final var actorId = SecurityContext.getAuthenticatedUserId();

        return ResponseEntity.ok(FilePresenter.present(getFileUseCase.execute(GetFileInput.from(id, actorId))));
    }

    @Override
    @Async
    public ResponseEntity<Resource> download(UUID id) {

        final var actorId = SecurityContext.getAuthenticatedUserId();

        final GetFileContentOutput output = getFileContentUseCase
                .execute(GetFileContentInput.with(id, actorId));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + output.name() + "\"")
                .contentLength(output.size())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(output.inputStream()));
    }

    @Override
    public ResponseEntity<Page<FileListResponse>> list(
            final int page,
            final int perPage,
            final FileField orderField,
            final Pagination.Order.Direction orderDirection,
            final Filter.Operator filterOperator,
            final List<String> filterGroups) {

        final List<Filter.Group> searchFilterGroups = filterGroups == null ? List.of()
                : filterGroups
                        .stream()
                        .map(source -> QueryAdapter.of(
                                source,
                                List.of(FileField.values())))
                        .toList();

        final SearchQuery query = SearchQuery.of(
                Pagination.of(page, perPage, Pagination.Order.of(orderField, orderDirection)),
                filterOperator,
                searchFilterGroups);

        final var actorId = SecurityContext.getAuthenticatedUserId();

        return ResponseEntity
                .ok(listFilesUseCase.execute(new FileListInput(actorId, query))
                        .map(FilePresenter::present));

    }

    @Override
    public ResponseEntity<Void> shareFile(
            final UUID id,
            final ShareFileRequest request) {

        final var granterId = SecurityContext.getAuthenticatedUserId();

        createFileSharingUseCase.execute(FileAdapter.adapt(id, granterId, request));

        return ResponseEntity.noContent().build();

    }

    @Override
    public ResponseEntity<Void> unshareFile(UUID id, UUID sharingId) {

        final var revokerId = SecurityContext.getAuthenticatedUserId();

        this.removeFileSharingUseCase.execute(new RemoveFileSharingInput(id, sharingId, revokerId));

        return ResponseEntity.noContent().build();

    }

    @Override
    public ResponseEntity<List<FileSharingListResponse>> listSharings(final UUID id) {

        final var actorId = SecurityContext.getAuthenticatedUserId();

        final var response = listFileSharingUseCase.execute(new ListFileSharingInput(id, actorId))
                .stream()
                .map(FilePresenter::present)
                .toList();

        return ResponseEntity.ok(response);

    }

}
