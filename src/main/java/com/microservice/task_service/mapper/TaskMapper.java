package com.microservice.task_service.mapper;

import com.microservice.task_service.model.dto.TaskDto;
import com.microservice.task_service.model.dto.TaskSaveRequestDto;
import com.microservice.task_service.model.dto.TaskUpdateRequestDto;
import com.microservice.task_service.model.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TaskMapper {

    TaskMapper TASK_MAPPER = Mappers.getMapper(TaskMapper.class);

    Task toEntity (TaskSaveRequestDto taskSaveRequestDto);
    TaskDto map(Task task);
    Task map(TaskUpdateRequestDto taskUpdateRequestDto);
    List<TaskDto> map(List<Task> tasks);
}
