package com.callv2.drive.application;

public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN input);

}
