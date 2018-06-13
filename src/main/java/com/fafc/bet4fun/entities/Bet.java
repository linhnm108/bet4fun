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

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import com.fafc.bet4fun.common.Constants;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="bet")
@NoArgsConstructor
public class Bet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name="bet_id")
    private UUID betId;

    @Column(name = "punter_choice")
    private String punterChoice;

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

    @Column(name="punter_revenue")
    private double punterRevenue;

    public Bet(Handicap handicap) {
        this.homeRate = handicap.getHomeRate();
        this.homeMoneyRate = handicap.getHomeMoneyRate();
        this.awayMoneyRate = handicap.getAwayMoneyRate();
        this.awayRate = handicap.getAwayRate();
        this.stake = 50;
    }

    public void calculatePunterRevenue(int numberGoalHome, int numberGoalAway) {
        float totalHomeGoals = numberGoalHome + this.homeRate;
        float totalAwayGoals = numberGoalAway + this.awayRate;
        float goalDifference = totalHomeGoals - totalAwayGoals;

        if (goalDifference == 0) {
            this.punterRevenue = 0;
        } else if (goalDifference == 0.25) {
            if (StringUtils.equals(this.punterChoice, Constants.HANDICAP_HOME_WIN)){
                this.punterRevenue = this.stake * this.homeMoneyRate * 0.5;
            } else {
                this.punterRevenue = -1 * this.stake * 0.5;
            }
        } else if (goalDifference > 0.25) {
            if (StringUtils.equals(this.punterChoice, Constants.HANDICAP_HOME_WIN)) {
                this.punterRevenue = this.stake * this.homeMoneyRate;
            } else {
                this.punterRevenue = -1 * this.stake;
            }
        } else if (goalDifference == -0.25) {
            if (StringUtils.equals(this.punterChoice, Constants.HANDICAP_HOME_WIN)) {
                this.punterRevenue = -1 * this.stake * 0.5;
            } else {
                this.punterRevenue = this.stake * this.awayMoneyRate * 0.5;
            }
        } else {
            if (StringUtils.equals(this.punterChoice, Constants.HANDICAP_HOME_WIN)) {
                this.punterRevenue = -1 * this.stake;
            } else {
                this.punterRevenue = this.stake * this.awayMoneyRate;
            }
        }
    }
}
