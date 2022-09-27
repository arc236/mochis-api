package com.ace.mochis.entity;

import com.ace.mochis.base.BaseTimeStampedEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="batches")
public class Batch extends BaseTimeStampedEntity {

    @JsonProperty("batch_serial")
    private String batchSerial;

    @JsonProperty("completed")
    private Boolean completed;

    @JsonProperty("winners")
    private int winners;

    @JsonProperty("stubs")
    private int stubs;
}
