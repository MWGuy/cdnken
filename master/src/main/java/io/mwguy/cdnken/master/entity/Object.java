package io.mwguy.cdnken.master.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@Table(name = "object")
@RequiredArgsConstructor
public class Object implements Serializable {
    private static final long serialVersionUID = 8404225492375446667L;

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "size", nullable = false)
    private long size;

    @ManyToMany
    @ToString.Exclude
    @JoinTable(name = "object_daemon_servers",
            joinColumns = @JoinColumn(name = "object_id"),
            inverseJoinColumns = @JoinColumn(name = "daemon_server_id"))
    private List<DaemonServer> servers = new ArrayList<>();

    @GeneratedValue
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;
}
