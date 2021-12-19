package io.mwguy.cdnken.master.repository;

import io.mwguy.cdnken.master.entity.DaemonServer;
import io.mwguy.cdnken.master.entity.Object;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ObjectRepository extends JpaRepository<Object, UUID> {
    @Query("select o from Object o where upper(o.name) = upper(?1)")
    Optional<Object> findByName(String name);

    @Query("select sum(o.size) from Object o join o.servers s where s = ?1")
    long totalSizeForServer(DaemonServer daemonServer);
}
