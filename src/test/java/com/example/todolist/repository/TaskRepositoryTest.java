package com.example.todolist.repository;

import com.example.todolist.model.Priority;
import com.example.todolist.model.Task;
import com.example.todolist.model.TaskAttachment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.todolist.config.JpaConfig;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaConfig.class)
@ActiveProfiles("test")
class TaskRepositoryTest {

  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private TaskAttachmentRepository attachmentRepository;

  @Autowired
  private TestEntityManager entityManager;

  @Test
  void findByCompletedAndPriority_returnsMatchingTasks() {
    Task task = new Task("Title", "Description");
    task.setCompleted(false);
    task.setPriority(Priority.HIGH);
    task.setCreatedAt(LocalDateTime.now());
    taskRepository.save(task);

    assertThat(taskRepository.findByCompletedAndPriority(false, Priority.HIGH))
        .hasSize(1)
        .extracting(Task::getTitle)
        .containsExactly("Title");
  }

  @Test
  void findTasksDueWithinDays_returnsTasksInRange() {
    Task dueSoon = new Task("Due soon", "Desc");
    dueSoon.setDueDate(LocalDate.now().plusDays(3));
    dueSoon.setCreatedAt(LocalDateTime.now());
    taskRepository.save(dueSoon);

    Task dueLater = new Task("Due later", "Desc");
    dueLater.setDueDate(LocalDate.now().plusDays(30));
    dueLater.setCreatedAt(LocalDateTime.now());
    taskRepository.save(dueLater);

    LocalDate endDate = LocalDate.now().plusDays(7);
    assertThat(taskRepository.findTasksDueWithinDays(endDate))
        .extracting(Task::getTitle)
        .containsExactly("Due soon");
  }

  @Test
  void saveTaskWithAttachment_persistsRelationship() {
    Task task = new Task("With attachment", "Desc");
    task.setCreatedAt(LocalDateTime.now());
    task.setTags(Set.of("work"));
    Task savedTask = taskRepository.save(task);

    TaskAttachment attachment = new TaskAttachment();
    attachment.setTask(savedTask);
    attachment.setFileName("doc.txt");
    attachment.setStoredFileName("uuid_doc.txt");
    attachment.setContentType("text/plain");
    attachment.setSize(10L);
    attachment.setUploadedAt(LocalDateTime.now());
    attachmentRepository.save(attachment);

    assertThat(attachmentRepository.findByTask_Id(savedTask.getId())).hasSize(1);
    assertThat(taskRepository.findAllWithAttachments()).hasSize(1);
  }

  @Test
  void deleteTask_cascadesToAttachments() {
    Task task = new Task("Cascade", "Desc");
    task.setCreatedAt(LocalDateTime.now());
    Task savedTask = taskRepository.save(task);

    TaskAttachment attachment = new TaskAttachment();
    attachment.setTask(savedTask);
    attachment.setFileName("file.txt");
    attachment.setStoredFileName("stored.txt");
    attachment.setSize(1L);
    attachment.setUploadedAt(LocalDateTime.now());
    attachmentRepository.save(attachment);

    taskRepository.deleteById(savedTask.getId());
    entityManager.flush();
    entityManager.clear();

    assertThat(taskRepository.findById(savedTask.getId())).isEmpty();
    assertThat(attachmentRepository.findAll()).isEmpty();
  }
}
