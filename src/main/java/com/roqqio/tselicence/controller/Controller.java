package com.roqqio.tselicence.controller;

import com.roqqio.tselicence.core.interfaces.TaskRunner;
import org.springframework.http.ResponseEntity;

import java.util.List;

abstract class Controller implements TaskRunner {
    <T> ResponseEntity<T> statusUnauthorized(T item) {
        return ResponseEntity.status(401).body(item);
    }

    <T> ResponseEntity<List<T>> statusUnauthorized(List<T> items) {
        return ResponseEntity.status(401).body(items);
    }

    <T> ResponseEntity<T> status404NoEntityFound(T item) {
        return ResponseEntity.status(404).body(item);
    }

    ResponseEntity<Void> status404NoEntityFound() {
        return ResponseEntity.status(404).build();
    }

    <T> ResponseEntity<T> statusOk(T item) {
        return ResponseEntity.ok().body(item);
    }

    <T> ResponseEntity<List<T>> statusOk(List<T> items) {
        return ResponseEntity.ok().body(items);
    }

    ResponseEntity<Void> statusUnauthorized() {
        return ResponseEntity.status(401).build();
    }

    ResponseEntity<Void> statusDeleteOk() {
        return ResponseEntity.status(204).build();
    }

    ResponseEntity<Void> statusOk() {
        return ResponseEntity.ok().build();
    }
}
