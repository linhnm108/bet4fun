package com.fafc.bet4fun.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="match")
public class Match implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name="match_id")
    private UUID matchId;

    @Column(name="home_name")
    private String homeName;

    @Column(name="away_name")
    private String awayName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "schedule_date")
    private Date scheduleDate;

    @OneToOne(mappedBy="match")
    private Result result;

    @ManyToMany(cascade = { CascadeType.DETACH }, fetch = FetchType.EAGER)
    @JoinTable(
            name="match_handicap_link",
            joinColumns = { @JoinColumn(name="match_id") },
            inverseJoinColumns = { @JoinColumn(name="handicap_id") })
    private List<Handicap> handicaps;
}
