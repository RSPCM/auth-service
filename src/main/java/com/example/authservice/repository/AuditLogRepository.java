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
            select rl.id               AS req_log_id,
                   rl.request_id,
                   rl.http_method,
                   rl.request_uri,
                   rl.query_string,
                   rl.remote_address,
                   rl.user_agent,
                   rl.response_status,
                   rl.duration_ms,
                   rl.datetime_created AS request_time,
            
                   al.id               AS audit_log_id,
                   al.entity_type,
                   al.entity_id,
                   al.action,
                   al.diff_json,
                   al.datetime_created AS audit_time,
            
                   al.user_id,
                   al.username
            
            from request_audit_log rl
                     inner join audit_log al on rl.request_id = al.request_id
            where (cast(:from as timestamp) is null or rl.datetime_created >= cast(:from as timestamp))
              and (cast(:to as timestamp) is null or rl.datetime_created <= cast(:to as timestamp))
              and (:query is null or :query = ''
                or al.username ilike concat('%', :query, '%')
                or cast(al.user_id as varchar) ilike concat('%', :query, '%')
                or al.action ilike concat('%', :query, '%')
                or al.entity_type ilike concat('%', :query, '%')
                or rl.http_method ilike concat('%', :query, '%')
                or rl.request_uri ilike concat('%', :query, '%')
                or rl.query_string ilike concat('%', :query, '%')
                or rl.remote_address ilike concat('%', :query, '%')
                or rl.user_agent ilike concat('%', :query, '%')
                or cast(rl.response_status as varchar) ilike concat('%', :query, '%')
                or cast(rl.duration_ms as varchar) ilike concat('%', :query, '%'))
            order by rl.datetime_created desc
            """,
            countQuery = """
                    select count(rl.*)
                    from request_audit_log rl
                             inner join audit_log al on rl.request_id = al.request_id
                    where (cast(:from as timestamp) is null or rl.datetime_created >= cast(:from as timestamp))
                      and (cast(:to as timestamp) is null or rl.datetime_created <= cast(:to as timestamp))
                      and (:query is null or :query = ''
                        or al.username ilike concat('%', :query, '%')
                        or cast(al.user_id as varchar) ilike concat('%', :query, '%')
                        or al.action ilike concat('%', :query, '%')
                        or al.entity_type ilike concat('%', :query, '%')
                        or rl.http_method ilike concat('%', :query, '%')
                        or rl.request_uri ilike concat('%', :query, '%')
                        or rl.query_string ilike concat('%', :query, '%')
                        or rl.remote_address ilike concat('%', :query, '%')
                        or rl.user_agent ilike concat('%', :query, '%')
                        or cast(rl.response_status as varchar) ilike concat('%', :query, '%')
                        or cast(rl.duration_ms as varchar) ilike concat('%', :query, '%'))
                    """,
            nativeQuery = true)
    Page<AuditLogProjection> getAllLogWithFilter(
            @Param(value = "query") String query,
            @Param(value = "from") LocalDateTime from,
            @Param(value = "to") LocalDateTime to,
            Pageable pageable
    );
}
