package com.callv2.drive.infrastructure.folder.model;

import java.util.List;
import java.util.UUID;

public record FolderPath(List<Segment> segments) {

    public record Segment(UUID id, String name) {
    }

}
