package com.ace.mochis.entity;

import com.ace.mochis.base.BaseTimeStampedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="stubs")
public class Stub extends BaseTimeStampedEntity {
    private int batchId;
    private int x;
    private int y;
    private boolean prize;
    private boolean open;
}
