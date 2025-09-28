package com.callv2.drive.domain.access;

import java.util.Optional;

public interface AclGateway {

    Acl create(Acl acl);

    Optional<Acl> findByResource(Resource<?> resource);

}
