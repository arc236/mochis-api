package com.ace.mochis.controller;

import com.ace.mochis.dto.BatchStatus;
import com.ace.mochis.entity.Batch;
import com.ace.mochis.entity.Stub;
import com.ace.mochis.exception.ErrorMessages;
import com.ace.mochis.exception.ServiceException;
import com.ace.mochis.repository.BatchRepository;
import com.ace.mochis.repository.StubRepository;
import com.ace.mochis.service.StubService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/batches")
public class BatchController {

    private static final Logger logger = LogManager.getLogger(BatchController.class);

    @Autowired
    BatchRepository batchRepository;

    @Autowired
    StubRepository stubRepository;

    @Autowired
    StubService stubService;

    private static final int DEFAULT_WINNERS = 30;
    private static final int DEFAULT_STUBS = 99;

    // create or select new batch of mochi
    @PostMapping("/batch")
    public ResponseEntity<Batch> getOrCreateBatch(@RequestBody Batch batch) {
        if (batch != null) {
            if (batch.getBatchSerial() == null || batch.getBatchSerial().equals("")) {
                throw new ServiceException(ErrorMessages.INVALID_SERIAL, HttpStatus.BAD_REQUEST);
            }

            // check if batch already exists
            Batch existingBatch = batchRepository.findByBatchSerial(batch.getBatchSerial().trim());
            if (existingBatch != null) {
                logger.info("Batch " + batch.getBatchSerial() + " already exists");
                return ResponseEntity.ok(existingBatch);
            }

            logger.info("Attempting to create new batch with serial: " + batch.getBatchSerial());

            if (batch.getCompleted() != null && Boolean.TRUE.equals(batch.getCompleted())) {
                logger.info("Cannot set batch to completed during creation");
                batch.setCompleted(false);
            }
            if (batch.getWinners() <= DEFAULT_WINNERS) {
                logger.info("Defaulting # of winners to: " + DEFAULT_WINNERS);
                batch.setWinners(DEFAULT_WINNERS);
            }
            if (batch.getStubs() <= DEFAULT_STUBS) {
                logger.info("Defaulting # of stubs to: " + DEFAULT_STUBS);
                batch.setStubs(DEFAULT_STUBS);
            }
            return ResponseEntity.ok(stubService.generateStubsFromBatch(batch));
        } else {
            throw new ServiceException(ErrorMessages.INVALID_BATCH_DETAILS, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create/{serial}")
    public ResponseEntity<Batch> getOrCreateBatch(@PathVariable("serial") String serial) {
        if (serial != null) {
            // check if batch already exists
            Batch existingBatch = batchRepository.findByBatchSerial(serial.trim());
            if (existingBatch != null) {
                logger.info("Batch " + serial + " already exists");
                return ResponseEntity.ok(existingBatch);
            }

            logger.info("Attempting to create new batch with serial: " + serial);
            Batch batch = new Batch();
            batch.setBatchSerial(serial);
            batch.setCompleted(false);
            batch.setWinners(DEFAULT_WINNERS);
            batch.setStubs(DEFAULT_STUBS);
            return ResponseEntity.ok(stubService.generateStubsFromBatch(batch));
        } else {
            throw new ServiceException(ErrorMessages.INVALID_BATCH_DETAILS, HttpStatus.BAD_REQUEST);
        }
    }

    // get all batches or specific batch + filter completed
    @GetMapping
    public ResponseEntity<List<Batch>> getBatches(@RequestParam(value="serial", required = false) String serial,
                                                  @RequestParam(value="completed", required = false) Boolean completed) {
        List<Batch> batches = new ArrayList<>();
        if (serial != null) {
            Batch batch = batchRepository.findByBatchSerial(serial.trim());
            if (batch != null) {
                batches.add(batch);
            }
            return ResponseEntity.ok(batches);
        }
        if (completed != null) {
            batches.addAll(batchRepository.findByCompleted(completed));
            return ResponseEntity.ok(batches);
        }
        batches.addAll(batchRepository.findAll());
        return ResponseEntity.ok(batches);
    }

    @PostMapping("/close/{serial}")
    public ResponseEntity<Batch> closeBatchBySerial(@PathVariable("serial") String serial) {
        Batch batch = batchRepository.findByBatchSerial(serial.trim());
        if (batch == null) {
            throw new ServiceException(ErrorMessages.INVALID_SERIAL, HttpStatus.BAD_REQUEST);
        }
        if (batch.getCompleted()) {
            throw new ServiceException(ErrorMessages.BATCH_ALREADY_CLOSED, HttpStatus.BAD_REQUEST);
        }
        batch.setCompleted(true);
        return ResponseEntity.ok(batchRepository.save(batch));
    }

    @PostMapping("/open/{serial}")
    public ResponseEntity<Batch> openBatchBySerial(@PathVariable("serial") String serial) {
        Batch batch = batchRepository.findByBatchSerial(serial.trim());
        if (batch == null) {
            throw new ServiceException(ErrorMessages.INVALID_SERIAL, HttpStatus.BAD_REQUEST);
        }
        if (!batch.getCompleted()) {
            throw new ServiceException(ErrorMessages.BATCH_STILL_OPEN, HttpStatus.BAD_REQUEST);
        }
        batch.setCompleted(false);
        return ResponseEntity.ok(batchRepository.save(batch));
    }

    @GetMapping("/status/")
    public ResponseEntity<BatchStatus> getBatchStatus() {
        return geStatus(null);
    }

    @GetMapping("/status/{serial}")
    public ResponseEntity<BatchStatus> getBatchStatus(@PathVariable("serial") String serial) {
        return geStatus(serial);
    }

    private ResponseEntity<BatchStatus> geStatus(String serial) {
        Batch batch;
        if (serial == null) {
            batch = batchRepository.findLatestBatch();
        } else {
            batch = batchRepository.findByBatchSerial(serial.trim());
        }
        if (batch == null) {
            throw new ServiceException(ErrorMessages.INVALID_SERIAL, HttpStatus.BAD_REQUEST);
        }
        BatchStatus batchStatus = new BatchStatus();
        batchStatus.setSerial(batch.getBatchSerial());
        batchStatus.setBatchId(batch.getId());
        List<Stub> stubs = stubRepository.findByBatchIdOrderById(batch.getId());
        if (stubs == null || stubs.isEmpty()) {
            batchStatus.setStatus("EMPTY");
            return ResponseEntity.ok(batchStatus);
        }

        stubs.forEach(stub -> {
            if (stub.isOpen()) {
                batchStatus.setOpened(batchStatus.getOpened()+1);
                if (stub.isPrize()) {
                    batchStatus.setOpenedPrizes(batchStatus.getOpenedPrizes()+1);
                }
            } else {
                batchStatus.setClosed(batchStatus.getClosed()+1);
                if (stub.isPrize()) {
                    batchStatus.setRemainingPrizes(batchStatus.getRemainingPrizes()+1);
                }
            }
        });

        if (batchStatus.getClosed() == 0) {
            batchStatus.setStatus("COMPLETED");
            if (!batch.getCompleted()) {
                batch.setCompleted(true);
                batchRepository.save(batch);
            }
        } else {
            batchStatus.setStatus("OPEN");
        }
        return ResponseEntity.ok(batchStatus);
    }

}
