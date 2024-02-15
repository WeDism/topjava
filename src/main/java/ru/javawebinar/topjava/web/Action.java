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

    @Override
    public String toString() {
        String name = this.name();
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
}
