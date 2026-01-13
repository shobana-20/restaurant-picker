package com.govtech.restaurantpicker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "session_users")
@IdClass(SessionUserId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionUser {

    @Id
    private Long sessionId;

    @Id
    private Long userId;
}
