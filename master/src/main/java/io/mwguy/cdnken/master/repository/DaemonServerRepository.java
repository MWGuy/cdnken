package io.mwguy.cdnken.master.repository;

import io.mwguy.cdnken.master.entity.DaemonServer;
import io.mwguy.cdnken.master.entity.Object;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface DaemonServerRepository extends JpaRepository<DaemonServer, UUID> {
    @Query("select s from DaemonServer s join s.objects o where o = ?1 and s.enabled = true")
    List<DaemonServer> findAllByObject(Object object);
}