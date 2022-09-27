package com.ace.mochis.impl;

import com.ace.mochis.entity.Batch;
import com.ace.mochis.entity.Stub;
import com.ace.mochis.exception.ErrorMessages;
import com.ace.mochis.exception.ServiceException;
import com.ace.mochis.repository.BatchRepository;
import com.ace.mochis.repository.StubRepository;
import com.ace.mochis.service.StubService;
import org.flywaydb.core.internal.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StubServiceImpl implements StubService {

    public static Map<Integer,Pair<Integer,Integer>> LAYOUT_CONFIG_MAP = Map.of(99, Pair.of(9,11), 100, Pair.of(10,10));

    @Autowired
    StubRepository stubRepository;

    @Autowired
    BatchRepository batchRepository;

    @Override
    public Batch generateStubsFromBatch(Batch batch) {
        if (!LAYOUT_CONFIG_MAP.containsKey(batch.getStubs())) {
            throw new ServiceException(ErrorMessages.STUBS_VALUE_UNSUPPORTED, HttpStatus.BAD_REQUEST);
        }

        Batch newBatch = batchRepository.save(batch);

        Pair<Integer,Integer> dimension = LAYOUT_CONFIG_MAP.get(newBatch.getStubs());
        generateSubsByDimension(dimension, newBatch.getWinners(), newBatch.getId());

        return newBatch;

    }

    private void generateSubsByDimension(Pair<Integer,Integer> dimension, Integer prizesCount, Integer batchId) {
        int stubsLength = dimension.getLeft() * dimension.getRight();
        Stub[] stubs = new Stub[stubsLength];
        Set<Integer> prizeSet = new HashSet<>();
        Random r = new Random();
        while(prizeSet.size() < prizesCount) {
            prizeSet.add(r.nextInt(stubsLength));
        }
        int i = 0;
        for (int x = 0; x < dimension.getLeft(); x++) {
            for (int y = 0; y < dimension.getRight(); y++) {
                Stub stub = new Stub();
                stub.setBatchId(batchId);
                stub.setOpen(false);
                stub.setPrize(prizeSet.contains(i));
                stub.setX(x);
                stub.setY(y);
                stubRepository.save(stub);
                i++;
            }
        }
    }

}
