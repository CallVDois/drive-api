package com.callv2.drive.domain.acl;

import java.util.Optional;

public interface AclGateway {

    Optional<Acl> findById(AclID id);

    Acl create(Acl acl);

    Acl update(Acl acl);

    Optional<Acl> findByResource(Resource<?> resource);

}
