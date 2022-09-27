package com.ace.mochis.repository;

import com.ace.mochis.base.BaseRepository;
import com.ace.mochis.entity.Stub;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StubRepository extends BaseRepository<Stub,Integer> {
    List<Stub> findByBatchIdOrderById(Integer batchId);
}
