package dev.codeunlu.blog.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

@Data
@MappedSuperclass
public class BaseModel {

  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime updatedAt;
  @CreatedBy
  private String createdBy;
  @LastModifiedBy
  private String updatedBy;
  @Column(name = "record_status")
  private boolean recordStatus = Boolean.TRUE;
}
