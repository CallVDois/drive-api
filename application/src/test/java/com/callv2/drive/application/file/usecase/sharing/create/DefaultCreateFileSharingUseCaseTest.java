package com.callv2.drive.application.file.usecase.sharing.create;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.callv2.drive.domain.acl.AccessPermission;
import com.callv2.drive.domain.acl.Acl;
import com.callv2.drive.domain.acl.AclGateway;
import com.callv2.drive.domain.acl.Resource;
import com.callv2.drive.domain.event.EventDispatcher;
import com.callv2.drive.domain.event.EventSource;
import com.callv2.drive.domain.file.Content;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileID;
import com.callv2.drive.domain.file.FileName;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.entity.Folder;
import com.callv2.drive.domain.folder.valueobject.FolderName;
import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.member.Nickname;
import com.callv2.drive.domain.member.Quota;
import com.callv2.drive.domain.member.QuotaUnit;
import com.callv2.drive.domain.member.Username;

@ExtendWith(MockitoExtension.class)
public class DefaultCreateFileSharingUseCaseTest {

    @InjectMocks
    DefaultCreateFileSharingUseCase useCase;

    @Mock
    EventDispatcher eventDispatcher;

    @Mock
    MemberGateway memberGateway;

    @Mock
    AclGateway aclGateway;

    @Mock
    FileGateway fileGateway;

    @Mock
    FolderGateway folderGateway;

    @Test
    void givenAnValidInputWithReadPermission_whenCallsExecute_thenShouldShareFileOnlyRead() {

        final var expectedFileId = FileID.unique();
        final var expectedGranterId = MemberID.of(UUID.randomUUID());
        final var expectedGranteeId = MemberID.of(UUID.randomUUID());
        final var expectedAccessPermission = AccessPermission.READ;

        final var expectedGranterMember = Member.with(
                expectedGranterId,
                Username.of("granterUserName"),
                Nickname.of("granterNickname"),
                Quota.of(1, QuotaUnit.BYTE),
                null,
                true,
                Instant.now(),
                Instant.now(),
                0L);

        final var expectedGranteeMember = Member.with(
                expectedGranteeId,
                Username.of("granteeUserName"),
                Nickname.of("granteeNickname"),
                Quota.of(1, QuotaUnit.BYTE),
                null,
                true,
                Instant.now(),
                Instant.now(),
                1L);

        final var expectedRootFolder = Folder.createRoot(expectedGranteeId);
        final var expectedInboxFolder = Folder.createInbox(
                expectedGranteeId,
                expectedRootFolder.getId(),
                FolderName.of("Shared"));
        final var expectedOriginalFileFolder = expectedRootFolder;

        final var expectedFile = File.create(
                expectedGranterId,
                expectedGranterId,
                expectedOriginalFileFolder.getId(),
                FileName.of("image.png"),
                Content.of(UUID.randomUUID().toString(), "image/png", 1024L));

        final var expectedResource = Resource.file(expectedFile);
        final var expectedFileAcl = Acl.create(expectedResource, expectedGranterId);

        when(memberGateway.findById(expectedGranterId))
                .thenReturn(Optional.of(expectedGranterMember));

        when(memberGateway.findById(expectedGranteeId))
                .thenReturn(Optional.of(expectedGranteeMember));

        when(fileGateway.findByIdWithMemberAccess(expectedFileId, expectedGranterId))
                .thenReturn(Optional.of(expectedFile));

        when(aclGateway.findByResource(expectedResource))
                .thenReturn(Optional.of(expectedFileAcl));

        when(folderGateway.findDefaultMemberSharedInbox(expectedGranteeId))
                .thenReturn(Optional.of(expectedInboxFolder));

        when(fileGateway.update(expectedFile))
                .thenAnswer(returnsFirstArg());

        when(aclGateway.update(expectedFileAcl))
                .thenAnswer(returnsFirstArg());

        doNothing()
                .when(eventDispatcher).notify(expectedFile);

        doNothing()
                .when(eventDispatcher).notify(expectedFileAcl);

        final var input = CreateFileSharingInput.with(
                expectedFileId.getValue(),
                expectedGranterId.getValue(),
                expectedGranteeId.getValue(),
                expectedAccessPermission);

        assertDoesNotThrow(() -> useCase.execute(input));

        verify(memberGateway, times(1)).findById(expectedGranteeId);
        verify(memberGateway, times(1)).findById(expectedGranterId);
        verify(memberGateway, times(2)).findById(any());

        verify(fileGateway, times(1)).findByIdWithMemberAccess(expectedFileId, expectedGranterId);
        verify(fileGateway, times(1)).findByIdWithMemberAccess(any(), any());

        verify(fileGateway, times(1)).update(expectedFile);
        verify(fileGateway, times(1)).update(any());

        verify(folderGateway, times(1)).findDefaultMemberSharedInbox(expectedGranteeId);
        verify(folderGateway, times(1)).findDefaultMemberSharedInbox(any());

        verify(aclGateway, times(1)).findByResource(expectedResource);
        verify(aclGateway, times(1)).findByResource(any());

        verify(aclGateway, times(1)).update(expectedFileAcl);
        verify(aclGateway, times(1)).update(any());

        verify(eventDispatcher, times(1)).notify(expectedFile);
        verify(eventDispatcher, times(1)).notify(expectedFileAcl);
        verify(eventDispatcher, times(2)).notify(any(EventSource.class));

    }

}
