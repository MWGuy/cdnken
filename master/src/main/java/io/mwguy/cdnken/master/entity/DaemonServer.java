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
@RequiredArgsConstructor
@Table(name = "daemon_server")
public class DaemonServer implements Serializable {
    private static final long serialVersionUID = 1348649116634429812L;

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "endpoint", nullable = false, unique = true)
    private String endpoint;

    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "mirror", nullable = false)
    private boolean mirror;

    @Column(name = "template", nullable = false)
    private String template; // https://node1.cdnken.ru/%s

    @ManyToMany(mappedBy = "servers")
    private List<Object> objects = new ArrayList<>();

    @GeneratedValue
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    public String computeUrl(Object object) {
        return String.format(template, object.getName());
    }
}
