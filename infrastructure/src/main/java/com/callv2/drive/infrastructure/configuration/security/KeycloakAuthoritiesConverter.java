package com.callv2.drive.infrastructure.configuration.security;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;

public class KeycloakAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String REALM_ACCESS = "realm_access";
    private static final String ROLES = "roles";
    private static final String RESOURCE_ACCESS = "resource_access";
    private static final String SEPARATOR = "_";
    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public Collection<GrantedAuthority> convert(final Jwt jwt) {
        final var realmRoles = extractRealmRoles(jwt);
        final var resourceRoles = extractResourceRoles(jwt);

        return Stream.concat(realmRoles, resourceRoles)
                .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role.toUpperCase()))
                .collect(Collectors.toSet());
    }

    private Stream<String> extractResourceRoles(final Jwt jwt) {

        final Function<Map.Entry<String, Object>, Stream<String>> mapResource = resource -> {
            final var key = resource.getKey();
            @SuppressWarnings("rawtypes")
            final LinkedTreeMap value = (LinkedTreeMap) resource.getValue();
            @SuppressWarnings("unchecked")
            final var roles = (Collection<String>) value.get(ROLES);
            return roles.stream().map(role -> key.concat(SEPARATOR).concat(role));
        };

        final Function<Set<Map.Entry<String, Object>>, Collection<String>> mapResources = resources -> resources
                .stream()
                .flatMap(mapResource)
                .toList();

        return Optional.ofNullable(jwt.getClaimAsMap(RESOURCE_ACCESS))
                .map(resources -> resources.entrySet())
                .map(mapResources)
                .orElse(Collections.emptyList())
                .stream();
    }

    @SuppressWarnings("unchecked")
    private Stream<String> extractRealmRoles(final Jwt jwt) {
        return Optional.ofNullable(jwt.getClaimAsMap(REALM_ACCESS))
                .map(resource -> (Collection<String>) resource.get(ROLES))
                .orElse(Collections.emptyList())
                .stream();
    }
}
