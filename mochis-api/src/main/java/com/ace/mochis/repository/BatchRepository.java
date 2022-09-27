package com.ace.mochis.repository;

import com.ace.mochis.base.BaseRepository;
import com.ace.mochis.entity.Batch;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchRepository extends BaseRepository<Batch,Integer> {

    Batch findByBatchSerial(String serial);

    List<Batch> findByCompleted(Boolean completed);

    @Query(
            value="select * from batches order by created desc limit 1",
            nativeQuery = true
    )
    Batch findLatestBatch();

}
