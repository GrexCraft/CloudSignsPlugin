//package net.grexcraft.cloud.signs.model;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.GenericGenerator;
//
//@Entity
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Table(name = "sign")
//public class Sign {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
//    @GenericGenerator(name = "native", strategy = "native")
//    Long id;
//
//    @Column
//    String servername;
//
//    @JoinColumn(name = "fk_location_id")
//    @OneToOne
//    SignLocation location;
//
//    @Column
//    boolean enabled;
//
//    @Column
//    int maxPlayers;
//
//    @Column
//    int currentPlayers;
//}
