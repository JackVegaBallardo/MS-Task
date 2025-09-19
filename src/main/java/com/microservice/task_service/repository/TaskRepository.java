package com.microservice.task_service.repository;

import com.microservice.task_service.model.entity.Task;
import com.microservice.task_service.model.entity.TaskVisibility;

import feign.Param;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByCreatedBy(Long createdBy);

   @Query("""
   SELECT t
   FROM Task t
   WHERE t.visibility = :visibility
     AND t.createdBy IN (:friendIds)
     AND t.createdBy <> :myId
     AND NOT EXISTS (
        SELECT 1 FROM TaskParticipant tp
        WHERE tp.task = t AND tp.userId = :myId
     )
   ORDER BY t.updatedAt DESC
""")
  List<Task> findJoinablePublicTasksFromFriends(@Param("myId") Long myId,
                                                @Param("friendIds") Collection<Long> friendIds, @Param("visibility") TaskVisibility visibility);
}
