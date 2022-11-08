package com.babas.models;

import com.babas.utilities.Babas;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;

public class Problem extends Babas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Boolean hasDependency=false;

    @ManyToOne
    private Problem problem;

    @OneToMany
    private List<Problem> sons=new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getHasDependency() {
        return hasDependency;
    }

    public void setHasDependency(Boolean hasDependency) {
        this.hasDependency = hasDependency;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public List<Problem> getSons() {
        return sons;
    }
}
