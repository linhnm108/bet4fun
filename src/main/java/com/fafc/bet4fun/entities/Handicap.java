package com.fafc.bet4fun.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fafc.bet4fun.common.DateTimeUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="handicap")
@NoArgsConstructor
public class Handicap implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name="handicap_id")
    private UUID handicapId;

    @Column(name="home_rate")
    private float homeRate;

    @Column(name="home_money_rate")
    private float homeMoneyRate;

    @Column(name="away_rate")
    private float awayRate;

    @Column(name="away_money_rate")
    private float awayMoneyRate;

    @Column(name="max_bet")
    private int maxBet;

    @Column(name="result")
    private String result;

    @Transient
    private String strExpiredDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expired_date")
    private Date expiredDate ;

    @Column(name = "bookie_choice")
    private String bookieChoice;

    @ManyToMany(mappedBy="handicaps")
    private List<Match> matches;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="client_id")
    private Client client;

    @OneToMany(mappedBy="handicap")
    private List<Bet> bets;

    public Handicap(Date scheduleDate) {
        this.homeMoneyRate = 2;
        this.awayMoneyRate = 2;
        this.maxBet = 50;

        Calendar cal = Calendar.getInstance();
        cal.setTime(scheduleDate);
        cal.add(Calendar.MINUTE, -10);

        this.expiredDate = cal.getTime();
        this.strExpiredDate = this.getLocalStrExpiredDate();
    }

    public String getLocalStrExpiredDate() {
        Date localDate = DateTimeUtils.convertUTCDateToLocal(this.expiredDate);
        return DateTimeUtils.convertDateToString(localDate);
    }

    public void convertLocalExpiredDateToUTC() {
        Date localDate = DateTimeUtils.convertStringToDate(this.strExpiredDate);
        this.expiredDate = DateTimeUtils.convertLocalDateToUTC(localDate);
    }
}
