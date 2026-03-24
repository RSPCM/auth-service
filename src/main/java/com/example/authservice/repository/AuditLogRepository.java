package com.example.authservice.repository;

import com.example.authservice.entity.AuditLog;
import com.example.authservice.projection.AuditLogProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {

    @Query(value = """
            select al.id               AS auditLogId,
                   al.request_id       AS requestId,
                   al.service_name     AS serviceName,
                   al.entity_type      AS entityType,
                   al.entity_id        AS entityId,
                   al.action           AS action,
                   al.diff_json        AS diffJson,
                   al.datetime_created AS auditTime,

                   al.user_id          AS userId,
                   al.username         AS username

            from audit_log al
            where (cast(:from as timestamp) is null or al.datetime_created >= cast(:from as timestamp))
              and (cast(:to as timestamp) is null or al.datetime_created <= cast(:to as timestamp))
              and (:query is null or :query = ''
                or al.username ilike concat('%', :query, '%')
                or cast(al.user_id as varchar) ilike concat('%', :query, '%')
                or al.action ilike concat('%', :query, '%')
                or al.service_name ilike concat('%', :query, '%')
                or al.entity_type ilike concat('%', :query, '%')
                or al.entity_id ilike concat('%', :query, '%')
                or al.request_id ilike concat('%', :query, '%'))
            order by al.datetime_created desc
            """,
            countQuery = """
                    select count(*)
                    from audit_log al
                    where (cast(:from as timestamp) is null or al.datetime_created >= cast(:from as timestamp))
                      and (cast(:to as timestamp) is null or al.datetime_created <= cast(:to as timestamp))
                      and (:query is null or :query = ''
                        or al.username ilike concat('%', :query, '%')
                        or cast(al.user_id as varchar) ilike concat('%', :query, '%')
                        or al.action ilike concat('%', :query, '%')
                        or al.service_name ilike concat('%', :query, '%')
                        or al.entity_type ilike concat('%', :query, '%')
                        or al.entity_id ilike concat('%', :query, '%')
                        or al.request_id ilike concat('%', :query, '%'))
                    """,
            nativeQuery = true)
    Page<AuditLogProjection> getAllLogWithFilter(
            @Param(value = "query") String query,
            @Param(value = "from") LocalDateTime from,
            @Param(value = "to") LocalDateTime to,
            Pageable pageable
    );
}
