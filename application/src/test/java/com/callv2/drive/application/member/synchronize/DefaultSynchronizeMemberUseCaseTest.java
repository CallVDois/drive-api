package com.callv2.drive.application.member.synchronize;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.member.Nickname;
import com.callv2.drive.domain.member.Quota;
import com.callv2.drive.domain.member.QuotaUnit;
import com.callv2.drive.domain.member.Username;

@ExtendWith(MockitoExtension.class)
public class DefaultSynchronizeMemberUseCaseTest {

    @InjectMocks
    DefaultSynchronizeMemberUseCase useCase;

    @Mock
    MemberGateway memberGateway;

    @Test
    void givenAnValidInput_whenCallsExecute_thenShouldSynchonizeMember() {

        final var expectedIdVAlue = "123";
        final var expectedUsernameValue = "username";
        final var expectedNicknameValue = "nickname";
        final var expectedCreatedAt = java.time.Instant.now();
        final var expectedUpdatedAt = expectedCreatedAt.plusSeconds(100);
        final var expectedSynchronizedVersion = 1L;

        final var expectedMemberId = MemberID.of(expectedIdVAlue);
        final var expectedUsername = Username.of(expectedUsernameValue);
        final var expectedNickname = Nickname.of(expectedNicknameValue);
        final var expectedQuota = Quota.of(10L, QuotaUnit.GIGABYTE);

        final var oldMember = Member.with(
                expectedMemberId,
                expectedUsername,
                expectedNickname,
                expectedQuota,
                null,
                expectedCreatedAt,
                expectedCreatedAt,
                0L);

        when(memberGateway.findById(expectedMemberId))
                .thenReturn(Optional.of(oldMember));

        when(memberGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var input = SynchronizeMemberInput.from(
                expectedIdVAlue,
                expectedUsernameValue,
                expectedNicknameValue,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedSynchronizedVersion);

        assertDoesNotThrow(() -> useCase.execute(input));

        verify(memberGateway, times(1)).findById(eq(expectedMemberId));
        verify(memberGateway, times(1)).findById(any());

        verify(memberGateway).update(argThat(member -> {
            assertEquals(expectedMemberId, member.getId());
            assertEquals(expectedUsername, member.getUsername());
            assertEquals(expectedNickname, member.getNickname());
            assertEquals(expectedQuota, member.getQuota());
            assertEquals(expectedCreatedAt, member.getCreatedAt());
            assertEquals(expectedUpdatedAt, member.getUpdatedAt());
            assertEquals(expectedSynchronizedVersion, member.getSynchronizedVersion());
            return true;
        }));
        verify(memberGateway, times(1)).update(any());

        verify(memberGateway, times(0)).create(any());
    }

    @Test
    void givenAnValidInputWhitNonExistentMember_whenCallsExecute_thenShouldCreateMember() {

        final var expectedIdVAlue = "123";
        final var expectedUsernameValue = "username";
        final var expectedNicknameValue = "nickname";
        final var expectedCreatedAt = java.time.Instant.now();
        final var expectedUpdatedAt = expectedCreatedAt.plusSeconds(100);
        final var expectedSynchronizedVersion = 1L;

        final var expectedMemberId = MemberID.of(expectedIdVAlue);
        final var expectedUsername = Username.of(expectedUsernameValue);
        final var expectedNickname = Nickname.of(expectedNicknameValue);
        final var expectedQuota = Quota.of(0L, QuotaUnit.BYTE);

        when(memberGateway.findById(expectedMemberId))
                .thenReturn(Optional.empty());

        when(memberGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var input = SynchronizeMemberInput.from(
                expectedIdVAlue,
                expectedUsernameValue,
                expectedNicknameValue,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedSynchronizedVersion);

        assertDoesNotThrow(() -> useCase.execute(input));

        verify(memberGateway, times(1)).findById(eq(expectedMemberId));
        verify(memberGateway, times(1)).findById(any());

        verify(memberGateway, times(0)).update(any());

        verify(memberGateway, times(1)).create(argThat(member -> {
            assertEquals(expectedMemberId, member.getId());
            assertEquals(expectedUsername, member.getUsername());
            assertEquals(expectedNickname, member.getNickname());
            assertEquals(expectedQuota, member.getQuota());
            assertEquals(expectedCreatedAt, member.getCreatedAt());
            assertEquals(expectedUpdatedAt, member.getUpdatedAt());
            assertEquals(expectedSynchronizedVersion, member.getSynchronizedVersion());
            return true;
        }));
        verify(memberGateway, times(1)).create(any());
    }

}
