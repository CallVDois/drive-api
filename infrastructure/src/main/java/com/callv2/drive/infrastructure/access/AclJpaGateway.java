package com.callv2.drive.infrastructure.access;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.callv2.drive.domain.access.Acl;
import com.callv2.drive.domain.access.AclGateway;
import com.callv2.drive.domain.access.Resource;

@Component
public class AclJpaGateway implements AclGateway {

    @Override
    public Acl create(Acl acl) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public Optional<Acl> findByResource(Resource<?> resource) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByResource'");
    }

}
