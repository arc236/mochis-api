package com.ace.mochis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchStatus {

    @JsonProperty("id")
    Integer batchId;

    @JsonProperty("serial")
    String serial;

    @JsonProperty("status")
    String status;

    @JsonProperty("opened")
    Integer opened = 0;

    @JsonProperty("closed")
    Integer closed = 0;

    @JsonProperty("opened_prizes")
    Integer openedPrizes = 0;

    @JsonProperty("remaining_prizes")
    Integer remainingPrizes = 0;
}
