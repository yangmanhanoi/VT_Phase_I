package com.namduong.viettel.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "acd")
public class Acd {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne(mappedBy = "acd", cascade = CascadeType.ALL)
    private Agent agent;
    @Column(name = "current_convs")
    private Long currentConvs;
    @Column(name = "max_convs")
    private Long maxConvs;
    public void addConv(){
        if(this.currentConvs < this.maxConvs){
            this.currentConvs += 1;
        }

    }
    public void removeConv()
    {
        if(this.currentConvs > 0)
        {
            this.currentConvs -= 1;
        }
    }

    @Override
    public String toString() {
        return "Acd{" +
                "id=" + id +
                ", currentConvs=" + currentConvs +
                ", maxConvs=" + maxConvs +
                '}';
    }
}
