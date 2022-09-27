package com.ace.mochis.controller;

import com.ace.mochis.entity.Stub;
import com.ace.mochis.exception.ErrorMessages;
import com.ace.mochis.exception.ServiceException;
import com.ace.mochis.repository.StubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/stubs")
public class StubController {

    @Autowired
    StubRepository stubRepository;

    // GET STUBS -> OPEN
    @GetMapping
    public ResponseEntity<List<Stub>> getStubs(@RequestParam(value = "batch_id") Integer batchId) {
        return ResponseEntity.ok(stubRepository.findByBatchIdOrderById(batchId));
    }

    // GET STUB
    @GetMapping("/{stub_id}")
    public ResponseEntity<Stub> getStub(@PathVariable("stub_id") Integer stubId) {
        Optional<Stub> stub = stubRepository.findById(stubId);
        if (!stub.isPresent()) {
            throw new ServiceException(ErrorMessages.STUB_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(stub.get());
    }

    // OPEN STUB -> COMPLETE BATCH IF ALL STUBS ARE OPEN
    @PostMapping("/open/{stub_id}")
    public ResponseEntity<Stub> openStub(@PathVariable("stub_id") Integer stubId) {
        Optional<Stub> stub = stubRepository.findById(stubId);
        if (!stub.isPresent()) {
            throw new ServiceException(ErrorMessages.STUB_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        if(stub.get().isOpen()) {
            throw new ServiceException(ErrorMessages.STUB_ALREADY_OPENED, HttpStatus.BAD_REQUEST);
        }

        stub.get().setOpen(true);
        return ResponseEntity.ok(stubRepository.save(stub.get()));
    }



}
