package com.callv2.drive.infrastructure.access;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.callv2.drive.domain.access.Acl;
import com.callv2.drive.domain.access.AclGateway;
import com.callv2.drive.domain.access.Resource;
import com.callv2.drive.infrastructure.access.persistence.AclJpaEntity;
import com.callv2.drive.infrastructure.access.persistence.AclJpaRepository;

@Component
public class AclJpaGateway implements AclGateway {

    private final AclJpaRepository aclJpaRepository;

    public AclJpaGateway(final AclJpaRepository aclJpaRepository) {
        this.aclJpaRepository = aclJpaRepository;
    }

    @Override
    public Acl create(final Acl acl) {
        return aclJpaRepository.save(AclJpaEntity.fromDomain(acl)).toDomain();
    }

    @Override
    public Optional<Acl> findByResource(Resource<?> resource) {

        return aclJpaRepository.findAll().stream()
                .map(AclJpaEntity::toDomain)
                .filter(acl -> acl.getResource().equals(resource))
                .findFirst();

    }

}
