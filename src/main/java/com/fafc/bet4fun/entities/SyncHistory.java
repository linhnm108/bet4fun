package com.fafc.bet4fun.entities;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="sync_history")
public class SyncHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name="sync_id")
    private UUID syncId;

    @Column(name="refresh_token")
    private String refreshToken;

    @Column(name = "from_date")
    private String fromDate ;

    @Column(name = "to_date")
    private String toDate ;
}
