package com.psj.itembrowser.security.common.exception;

import lombok.NonNull;

public class StorageException extends CustomRuntimeException {

    public StorageException(@NonNull ErrorCode e) {
        super(e);
    }
}
