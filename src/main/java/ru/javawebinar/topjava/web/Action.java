package ru.javawebinar.topjava.web;

public enum Action {
    GET, CREATE, UPDATE, DELETE;

    public boolean isCreate() {
        return this == CREATE;
    }

    public boolean isUpdate() {
        return this == UPDATE;
    }

    public boolean isDelete() {
        return this == DELETE;
    }
}
