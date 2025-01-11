package com.callv2.drive.infrastructure.file;

import org.springframework.stereotype.Component;

import com.callv2.drive.domain.file.BinaryContent;
import com.callv2.drive.domain.file.ContentGateway;

@Component
public class DefaultContentGateway implements ContentGateway {

    @Override
    public void store(BinaryContent content) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'store'");
    }

}
