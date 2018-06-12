package com.fafc.bet4fun.entities;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="bet")
public class Bet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name="bet_id")
    private UUID betId;

    @Column(name="one_x_two")
    private int oneXTwo;

    @Column(name="stake")
    private int stake;

    @Column(name="home_rate")
    private float homeRate;

    @Column(name="home_money_rate")
    private float homeMoneyRate;

    @Column(name="away_rate")
    private float awayRate;

    @Column(name="away_money_rate")
    private float awayMoneyRate;

    @ManyToOne
    @JoinColumn(name="client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name="handicap_id")
    private Handicap handicap;
}
