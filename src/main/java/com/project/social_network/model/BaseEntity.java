package com.project.social_network.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class BaseEntity implements Serializable {

  private static final long serialVersionUID = -863164858986274318L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "createddate")
  @CreatedDate
  private Date createdDate;

  @Column(name = "createdby")
  @CreatedBy
  private String createdBy;

  @Column(name = "modifieddate")
  @LastModifiedDate
  private Date modifiedDate;

  @Column(name = "modifiedby")
  @LastModifiedBy
  private String modifiedBy;

}