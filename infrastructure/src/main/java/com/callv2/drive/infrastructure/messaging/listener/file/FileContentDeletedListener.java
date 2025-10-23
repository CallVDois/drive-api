package com.callv2.drive.infrastructure.messaging.listener.file;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.callv2.drive.application.file.usecase.content.delete.DeleteFileContentUseCase;
import com.callv2.drive.infrastructure.file.adapter.FileAdapter;
import com.callv2.drive.infrastructure.file.model.DeleteFileContentMessage;
import com.callv2.drive.infrastructure.messaging.listener.Listener;

@Component
public class FileContentDeletedListener implements Listener<DeleteFileContentMessage> {

    private final DeleteFileContentUseCase deleteFileContentUseCase;

    public FileContentDeletedListener(final DeleteFileContentUseCase deleteFileContentUseCase) {
        this.deleteFileContentUseCase = deleteFileContentUseCase;
    }

    @Override
    @RabbitListener(queues = "drive.file.deleted.queue")
    public void handle(final DeleteFileContentMessage message) {
        this.deleteFileContentUseCase.execute(FileAdapter.adapt(message));
    }

}